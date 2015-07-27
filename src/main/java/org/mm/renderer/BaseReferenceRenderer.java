package org.mm.renderer;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.rendering.OWLLiteralRendering;
import org.mm.rendering.ReferenceRendering;
import org.mm.rendering.Rendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Contains common functionality for rendering a mapping master reference that may be used by renderer implementations.
 */
public abstract class BaseReferenceRenderer
		implements ReferenceRenderer, OWLLiteralRenderer, MappingMasterParserConstants
{
	// Configuration options
	public int defaultEmptyLocationDirective = MM_PROCESS_IF_EMPTY_LOCATION;
	public int defaultEmptyRDFIDDirective = MM_PROCESS_IF_EMPTY_ID;
	public int defaultEmptyRDFSLabelDirective = MM_PROCESS_IF_EMPTY_LABEL;
	public int defaultIfOWLEntityExistsDirective = MM_RESOLVE_IF_OWL_ENTITY_EXISTS;
	public int defaultIfOWLEntityDoesNotExistDirective = MM_CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST;

	// Configuration options
	public int defaultValueEncoding = RDFS_LABEL;
	public int defaultReferenceType = OWL_CLASS;
	public int defaultOWLPropertyType = OWL_OBJECT_PROPERTY;
	public int defaultOWLPropertyAssertionObjectType = XSD_STRING;
	public int defaultOWLDataPropertyValueType = XSD_STRING;

	private String defaultNamespace = "";
	private String defaultLanguage = "";

	private SpreadSheetDataSource dataSource;

	public BaseReferenceRenderer(SpreadSheetDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override public void setDataSource(SpreadSheetDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override public int getDefaultValueEncoding()
	{
		return this.defaultValueEncoding;
	}

	@Override public int getDefaultReferenceType()
	{
		return this.defaultReferenceType;
	}

	@Override public int getDefaultOWLPropertyType()
	{
		return this.defaultOWLPropertyType;
	}

	@Override public int getDefaultOWLPropertyAssertionObjectType()
	{
		return this.defaultOWLPropertyAssertionObjectType;
	}

	@Override public int getDefaultOWLDataPropertyValueType()
	{
		return this.defaultOWLDataPropertyValueType;
	}

	@Override public void setDefaultValueEncoding(int defaultValueEncoding)
	{
		this.defaultValueEncoding = defaultValueEncoding;
	}

	@Override public void setDefaultReferenceType(int defaultReferenceType)
	{
		this.defaultReferenceType = defaultReferenceType;
	}

	@Override public void setDefaultOWLPropertyType(int defaultOWLPropertyType)
	{
		this.defaultOWLDataPropertyValueType = defaultOWLPropertyType;
	}

	@Override public void setDefaultOWLPropertyAssertionObjectType(int defaultOWLPropertyAssertionObjectType)
	{
		this.defaultOWLPropertyAssertionObjectType = defaultOWLPropertyAssertionObjectType;
	}

	@Override public void setDefaultOWLDataPropertyValueType(int defaultOWLDataPropertyValueType)
	{
		this.defaultOWLDataPropertyValueType = defaultOWLDataPropertyValueType;
	}

	protected SpreadsheetLocation resolveLocation(SourceSpecificationNode sourceSpecificationNode)
			throws RendererException
	{
		return this.dataSource.resolveLocation(sourceSpecificationNode);
	}

	protected String getReferenceLanguage(ReferenceNode referenceNode) throws RendererException
	{
		if (referenceNode.hasExplicitlySpecifiedLanguage())
			return referenceNode.getActualLanguage();
		else
			return getDefaultLanguage(); // Which might be null or empty
	}

	protected String getDefaultNamespace()
	{
		return this.defaultNamespace;
	}

	protected String getDefaultLanguage()
	{
		return this.defaultLanguage;
	}

	protected boolean hasDefaultNamespace()
	{
		return !this.defaultNamespace.isEmpty();
	}

	protected String resolveReferenceValue(SpreadsheetLocation location, ReferenceNode referenceNode)
			throws RendererException
	{
		SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
		String referenceValue;

		if (sourceSpecificationNode.hasLiteral()) {
			// Reference is a literal, e.g., @"Person", @"http://a.com#Person"
			referenceValue = sourceSpecificationNode.getLiteral();
		} else { // Reference to data source location
			String rawLocationValue = this.dataSource.getLocationValue(location, referenceNode); // Deals with shifting

			if (rawLocationValue == null || rawLocationValue.isEmpty())
				referenceValue = referenceNode.getActualDefaultLocationValue();
			else
				referenceValue = rawLocationValue;

			if (referenceValue.isEmpty() && referenceNode.getActualEmptyLocationDirective() == MM_ERROR_IF_EMPTY_LOCATION)
				throw new RendererException("empty location " + location + " in reference " + referenceNode);

			if (referenceValue.isEmpty() && referenceNode.getActualEmptyLocationDirective() == MM_WARNING_IF_EMPTY_LOCATION) {
			}
		}
		return referenceValue;
	}

	protected String processOWLLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
			ReferenceNode referenceNode) throws RendererException
	{
		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
		String sourceValue = rawLocationValue.replace("\"", "\\\"");
		String processedReferenceValue;

		if (referenceNode.hasLiteralValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
				processedReferenceValue = generateReferenceValue(sourceValue, referenceNode.getLiteralValueEncodingNode(),
						referenceNode);
			else if (referenceNode.hasValueExtractionFunction()) {
				ValueExtractionFunctionNode valueExtractionFunctionNode = referenceNode.getValueExtractionFunctionNode();
				processedReferenceValue = generateReferenceValue(sourceValue, valueExtractionFunctionNode);
			} else
				processedReferenceValue = sourceValue;
		} else
			processedReferenceValue = "";

		if (processedReferenceValue.isEmpty() && !referenceNode.getActualDefaultLiteral().isEmpty())
			processedReferenceValue = referenceNode.getActualDefaultLiteral();

		if (processedReferenceValue.isEmpty()
				&& referenceNode.getActualEmptyLiteralDirective() == MM_ERROR_IF_EMPTY_LITERAL)
			throw new RendererException("empty literal in reference " + referenceNode + " at location " + location);

		return processedReferenceValue;
	}

	protected String getReferenceRDFID(String sourceValue, ReferenceNode referenceNode) throws RendererException
	{
		String rdfIDValue;

		if (referenceNode.hasRDFIDValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding())
				rdfIDValue = generateReferenceValue(sourceValue, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
			else if (referenceNode.hasValueExtractionFunction())
				rdfIDValue = generateReferenceValue(sourceValue, referenceNode.getValueExtractionFunctionNode());
			else
				rdfIDValue = sourceValue;
		} else
			rdfIDValue = "";

		if (rdfIDValue.isEmpty() && !referenceNode.getActualDefaultRDFID().isEmpty())
			rdfIDValue = referenceNode.getActualDefaultRDFID();

		if (rdfIDValue.isEmpty() && referenceNode.getActualEmptyRDFIDDirective() == MM_ERROR_IF_EMPTY_ID)
			throw new RendererException("empty RDF ID in reference " + referenceNode);

		return rdfIDValue;
	}

	protected String getReferenceRDFSLabel(String sourceValue, ReferenceNode referenceNode) throws RendererException
	{
		String rdfsLabelText;

		if (referenceNode.hasRDFSLabelValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding())
				rdfsLabelText = generateReferenceValue(sourceValue, referenceNode.getRDFSLabelValueEncodingNode(),
						referenceNode);
			else if (referenceNode.hasValueExtractionFunction())
				rdfsLabelText = generateReferenceValue(sourceValue, referenceNode.getValueExtractionFunctionNode());
			else
				rdfsLabelText = sourceValue;
		} else
			rdfsLabelText = "";

		if (rdfsLabelText.isEmpty() && !referenceNode.getActualDefaultRDFSLabel().isEmpty())
			rdfsLabelText = referenceNode.getActualDefaultRDFSLabel();

		if (rdfsLabelText.isEmpty() && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_LABEL)
			throw new RendererException("empty RDFS label in reference " + referenceNode);

		return rdfsLabelText;
	}

	protected String generateReferenceValue(String sourceValue, ValueEncodingNode valueEncodingNode,
			ReferenceNode referenceNode) throws RendererException
	{
		if (valueEncodingNode != null) {
			if (valueEncodingNode.hasValueSpecificationNode())
				return generateReferenceValue(sourceValue, valueEncodingNode.getValueSpecificationNode(), referenceNode);
			else
				return sourceValue;
		} else
			return sourceValue;
	}

	protected String generateReferenceValue(String sourceValue, ValueSpecificationNode valueSpecificationNode,
			ReferenceNode referenceNode) throws RendererException
	{
		String processedReferenceValue = "";

		for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
				.getValueSpecificationItemNodes()) {
			if (valueSpecificationItemNode.hasStringLiteral())
				processedReferenceValue += valueSpecificationItemNode.getStringLiteral();
			else if (valueSpecificationItemNode.hasReferenceNode()) {
				ReferenceNode valueSpecificationItemReferenceNode = valueSpecificationItemNode.getReferenceNode();
				valueSpecificationItemReferenceNode.setDefaultShiftSetting(referenceNode.getActualShiftDirective());
				Optional<? extends ReferenceRendering> referenceRendering = renderReference(
						valueSpecificationItemReferenceNode);
				if (referenceRendering.isPresent()) {
					if (referenceRendering.get().isOWLLiteral()) {
						processedReferenceValue += referenceRendering.get().getRawValue();
					} else
						throw new RendererException(
								"expecting OWL literal for value specification, got " + referenceRendering.get());
				}
			} else if (valueSpecificationItemNode.hasValueExtractionFunctionNode()) {
				ValueExtractionFunctionNode valueExtractionFunction = valueSpecificationItemNode
						.getValueExtractionFunctionNode();
				processedReferenceValue += generateReferenceValue(sourceValue, valueExtractionFunction);
			} else if (valueSpecificationItemNode.hasCapturingExpression() && sourceValue != null) {
				String capturingExpression = valueSpecificationItemNode.getCapturingExpression();
				processedReferenceValue += processCapturingExpression(sourceValue, capturingExpression);
			}
		}
		return processedReferenceValue;
	}

	/**
	 * Arguments to value extraction functions cannot be dropped if the reference resolves to nothing.
	 */
	private String generateValueExtractionFunctionArgument(
			ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
	{
		if (valueExtractionFunctionArgumentNode.isOWLLiteralNode()) {
			Optional<? extends OWLLiteralRendering> literalRendering = renderOWLLiteral(
					valueExtractionFunctionArgumentNode.getOWLLiteralNode());
			if (literalRendering.isPresent()) {
				return literalRendering.get().getRawValue();
			} else
				throw new RendererException("empty literal for value extraction function argument");
		} else if (valueExtractionFunctionArgumentNode.isReferenceNode()) {
			ReferenceNode referenceNode = valueExtractionFunctionArgumentNode.getReferenceNode();
			Optional<? extends ReferenceRendering> referenceRendering = renderReference(referenceNode);
			if (referenceRendering.isPresent()) {
				if (referenceRendering.get().isOWLLiteral()) {
					return referenceRendering.get().getRawValue();
				} else
					throw new RendererException("expecting literal reference for value extraction function argument, got "
							+ valueExtractionFunctionArgumentNode);
			} else
				throw new RendererException("empty reference " + referenceNode + " for value extraction function argument");
		} else
			throw new InternalRendererException(
					"unknown child for node " + valueExtractionFunctionArgumentNode.getNodeName());
	}

	// Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

	protected String generateReferenceValue(String sourceValue, ValueExtractionFunctionNode valueExtractionFunctionNode)
			throws RendererException
	{
		List<String> arguments = new ArrayList<>();
		String functionName = valueExtractionFunctionNode.getFunctionName();
		boolean hasExplicitArguments = valueExtractionFunctionNode.hasArguments();
		String processedReferenceValue;

		if (valueExtractionFunctionNode.hasArguments()) {
			for (ValueExtractionFunctionArgumentNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
				String argumentValue = generateValueExtractionFunctionArgument(argumentNode);
				arguments.add(argumentValue);
			}
		}

		switch (valueExtractionFunctionNode.getFunctionID()) {
		case MM_TO_UPPER_CASE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException("function " + functionName + " expecting one argument, got " + arguments.size());
				processedReferenceValue = arguments.get(0).toUpperCase();
			} else
				processedReferenceValue = sourceValue.toUpperCase();
			break;
		case MM_TO_LOWER_CASE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedReferenceValue = arguments.get(0).toLowerCase();
			} else
				processedReferenceValue = sourceValue.toLowerCase();
			break;
		case MM_TRIM:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedReferenceValue = arguments.get(0).trim();
			} else
				processedReferenceValue = sourceValue.trim();
			break;
		case MM_REVERSE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedReferenceValue = reverse(arguments.get(0));
			} else
				processedReferenceValue = reverse(sourceValue);
			break;
		case MM_CAPTURING:
			if (arguments.size() == 1) {
				processedReferenceValue = processCapturingExpression(sourceValue, arguments.get(0));
			} else if (arguments.size() == 2) {
				processedReferenceValue = processCapturingExpression(arguments.get(0), arguments.get(1));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_PREPEND:
			if (arguments.size() == 1) {
				processedReferenceValue = arguments.get(0) + sourceValue;
			} else if (arguments.size() == 2) {
				processedReferenceValue = arguments.get(0) + arguments.get(1);
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_APPEND:
			if (arguments.size() == 1) {
				processedReferenceValue = sourceValue + arguments.get(0);
			} else if (arguments.size() == 2) {
				processedReferenceValue = arguments.get(0) + arguments.get(1);
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE:
			if (arguments.size() == 2) {
				processedReferenceValue = sourceValue.replace(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedReferenceValue = arguments.get(0).replace(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE_ALL:
			if (arguments.size() == 2) {
				processedReferenceValue = sourceValue.replaceAll(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedReferenceValue = arguments.get(0).replaceAll(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE_FIRST:
			if (arguments.size() == 2) {
				processedReferenceValue = sourceValue.replaceFirst(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedReferenceValue = arguments.get(0).replaceFirst(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		default:
			throw new RendererException("unknown mapping function " + valueExtractionFunctionNode.getFunctionName());
		}
		return processedReferenceValue;
	}

	private String processCapturingExpression(String locationValue, String capturingExpression) throws RendererException
	{
		try {
			Pattern p = Pattern.compile(capturingExpression); // Pull the value out of the location
			Matcher m = p.matcher(locationValue);
			boolean matchFound = m.find();
			String result = "";
			if (matchFound) {
				for (int groupIndex = 1; groupIndex <= m.groupCount(); groupIndex++)
					result += m.group(groupIndex);
			}
			return result;
		} catch (PatternSyntaxException e) {
			throw new RendererException("invalid capturing expression " + capturingExpression + ": " + e.getMessage());
		}
	}

	private String reverse(String source)
	{
		int i, len = source.length();
		StringBuilder dest = new StringBuilder(len);

		for (i = len - 1; i >= 0; i--)
			dest.append(source.charAt(i));

		return dest.toString();
	}
}

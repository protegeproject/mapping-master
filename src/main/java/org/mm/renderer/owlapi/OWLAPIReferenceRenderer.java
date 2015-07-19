package org.mm.renderer.owlapi;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.TypesNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.RendererException;
import org.mm.renderer.Rendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class OWLAPIReferenceRenderer implements ReferenceRenderer, MappingMasterParserConstants
{
	// Configuration options
	public int defaultEmptyLocationDirective = MM_PROCESS_IF_EMPTY_LOCATION;
	public int defaultEmptyRDFIDDirective = MM_PROCESS_IF_EMPTY_ID;
	public int defaultEmptyRDFSLabelDirective = MM_PROCESS_IF_EMPTY_LABEL;
	public int defaultIfExistsDirective = MM_RESOLVE_IF_EXISTS;
	public int defaultIfNotExistsDirective = MM_CREATE_IF_NOT_EXISTS;

	// Configuration options
	public int defaultValueEncoding = RDFS_LABEL;
	public int defaultReferenceType = OWL_CLASS;
	public int defaultOWLPropertyType = OWL_OBJECT_PROPERTY;
	public int defaultOWLPropertyAssertionObjectType = XSD_STRING;
	public int defaultOWLDataPropertyValueType = XSD_STRING;

	private String defaultNamespace = "";
	private String defaultLanguage = "";

	private final OWLOntology ontology;
	private final OWLDataFactory owlDataFactory;
	private final OWLAPIObjectHandler owlObjectHandler;
	private final OWLAPIEntityRenderer entityRenderer;
	private SpreadSheetDataSource dataSource;

	public OWLAPIReferenceRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource,
			OWLAPIEntityRenderer entityRenderer)
	{
		this.ontology = ontology;
		this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		this.owlObjectHandler = new OWLAPIObjectHandler(ontology);
		this.entityRenderer = entityRenderer;
		this.dataSource = dataSource;
	}

	public void reset()
	{
		owlObjectHandler.reset();
	}

	public void setDataSource(SpreadSheetDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	// TODO Too long. Clean up.
	@Override public Optional<OWLAPIReferenceRendering> renderReference(ReferenceNode referenceNode)
			throws RendererException
	{
		SpreadsheetLocation location = getLocation(referenceNode.getSourceSpecificationNode());
		String defaultNamespace = getReferenceNamespace(referenceNode);
		String language = getReferenceLanguage(referenceNode);
		OWLAPIReferenceRendering referenceRendering = new OWLAPIReferenceRendering();

		referenceRendering.logLine("<<<<<<<<<<<<<<<<<<<< Rendering reference [" + referenceNode + "] <<<<<<<<<<<<<<<<<<<<");

		String locationValue = processLocationValue(location, referenceNode, referenceRendering);

		if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION)
			return Optional.empty();

		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

		if (referenceType.isUntyped())
			throw new RendererException("untyped reference " + referenceNode);

		if (referenceType.isOWLLiteral()) { // OWL literal
			String processedLiteralValue = processOWLLiteral(location, locationValue, referenceType, referenceNode,
					referenceRendering);
			OWLLiteral literal = this.owlDataFactory.getOWLLiteral(processedLiteralValue); // TODO Literal type

			if (processedLiteralValue.equals("")
					&& referenceNode.getActualEmptyDataValueDirective() == MM_SKIP_IF_EMPTY_DATA_VALUE) {
				return Optional.empty();
			}

			referenceRendering.logLine(
					">>>>>>>>>>>>>>>>>>>> Reference [" + referenceNode.toString() + "] rendered as " + referenceNode
							.getReferenceTypeNode() + " " + referenceRendering.toString() + " >>>>>>>>>>>>>>>>>>>>");

			referenceRendering.setOWLLiteral(literal);
		} else if (referenceType.isOWLEntity()) { // OWL entity
			String rdfID = processRDFIDValue(locationValue, referenceNode, referenceRendering);
			String rdfsLabelText = processRDFSLabelText(locationValue, referenceNode, referenceRendering);
			OWLEntity owlEntity = this.owlObjectHandler
					.createOrResolveOWLEntity(location, locationValue, referenceType, rdfID, rdfsLabelText, defaultNamespace,
							language, referenceNode.getReferenceDirectives());
			Set<OWLAxiom> axioms = addDefiningTypesFromReference(owlEntity, referenceNode);
			referenceRendering.addOWLAxioms(axioms);

			referenceRendering.logLine(
					">>>>>>>>>>>>>>>>>>>> Reference [" + referenceNode.toString() + "] rendered as " + referenceNode
							.getReferenceTypeNode() + " " + referenceRendering.toString() + " >>>>>>>>>>>>>>>>>>>>");

			referenceRendering.setOWLEntity(owlEntity);
		} else
			throw new RendererException(
					"internal error: unknown reference type " + referenceType + " for reference " + referenceNode.toString());

		return Optional.of(referenceRendering);
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

	public Set<OWLAxiom> processTypesClause(OWLAPIRendering individualDeclarationRendering,
			OWLNamedIndividual declaredIndividual, List<TypeNode> typeNodes) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		for (TypeNode typeNode : typeNodes) {
			Optional<OWLEntity> typeRendering = renderType(typeNode);

			if (!typeRendering.isPresent()) {
				individualDeclarationRendering.logLine(
						"processReference: skipping OWL type declaration clause [" + typeNode + "] for individual "
								+ individualDeclarationRendering + " because of missing type");
				continue;
			}

			OWLEntity entity = typeRendering.get();

			if (entity.isOWLClass()) {

				OWLClass cls = entity.asOWLClass();
				OWLClassAssertionAxiom axiom = this.owlDataFactory.getOWLClassAssertionAxiom(cls, declaredIndividual);

				axioms.add(axiom);
			} else
				throw new RendererException(
						"expecting OWL class as type for individual " + declaredIndividual.getIRI() + ", got " + entity.getIRI());
		}
		return axioms;
	}

	private SpreadsheetLocation getLocation(SourceSpecificationNode sourceSpecificationNode) throws RendererException
	{
		if (sourceSpecificationNode.hasLiteral())
			return null;
		else
			return dataSource.resolveLocation(sourceSpecificationNode);
	}

	private String processRDFIDValue(String locationValue, ReferenceNode referenceNode, Rendering rendering)
			throws RendererException
	{
		String rdfIDValue;

		if (referenceNode.hasRDFIDValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding())
				rdfIDValue = processValueEncoding(locationValue, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
			else if (referenceNode.hasValueExtractionFunction())
				rdfIDValue = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(), locationValue);
			else
				rdfIDValue = locationValue;
		} else
			rdfIDValue = "";

		if (rdfIDValue.equals("") && !referenceNode.getActualDefaultRDFID().equals(""))
			rdfIDValue = referenceNode.getActualDefaultRDFID();

		if (rdfIDValue.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_ID)
			throw new RendererException("empty RDF ID in reference " + referenceNode);

		if (rdfIDValue.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_ID)
			rendering.logLine("processReference: WARNING: empty RDF ID in reference");

		return rdfIDValue;
	}

	private String processRDFSLabelText(String locationValue, ReferenceNode referenceNode, Rendering rendering)
			throws RendererException
	{
		String rdfsLabelText;

		if (referenceNode.hasRDFSLabelValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding())
				rdfsLabelText = processValueEncoding(locationValue, referenceNode.getRDFSLabelValueEncodingNode(),
						referenceNode);
			else if (referenceNode.hasValueExtractionFunction())
				rdfsLabelText = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(), locationValue);
			else
				rdfsLabelText = locationValue;
		} else
			rdfsLabelText = "";

		if (rdfsLabelText.equals("") && !referenceNode.getActualDefaultRDFSLabel().equals(""))
			rdfsLabelText = referenceNode.getActualDefaultRDFSLabel();

		if (rdfsLabelText.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_LABEL)
			throw new RendererException("empty RDFS label in reference " + referenceNode);

		if (rdfsLabelText.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_LABEL)
			rendering.logLine("processReference: WARNING: empty RDFS label in reference");

		return rdfsLabelText;
	}

	private String processValueEncoding(String value, ValueEncodingNode valueEncodingNode, ReferenceNode referenceNode)
			throws RendererException
	{
		if (valueEncodingNode != null) {
			if (valueEncodingNode.hasValueSpecification())
				return processValueSpecification(value, valueEncodingNode.getValueSpecification(), referenceNode);
			else
				return value;
		} else
			return value;
	}

	private String processValueSpecification(String value, ValueSpecificationNode valueSpecificationNode,
			ReferenceNode referenceNode) throws RendererException
	{
		String processedValue = "";

		for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
				.getValueSpecificationItemNodes()) {
			if (valueSpecificationItemNode.hasStringLiteral())
				processedValue += valueSpecificationItemNode.getStringLiteral();
			else if (valueSpecificationItemNode.hasReferenceNode()) {
				ReferenceNode valueSpecificationItemReferenceNode = valueSpecificationItemNode.getReferenceNode();
				valueSpecificationItemReferenceNode.setDefaultShiftSetting(referenceNode.getActualShiftDirective());
				Optional<OWLAPIReferenceRendering> referenceRendering = renderReference(valueSpecificationItemReferenceNode);
				if (referenceRendering.isPresent()) {
					if (referenceRendering.get().isOWLLiteral()) {
						OWLLiteral literal = referenceRendering.get().getOWLLiteral().get();
						processedValue += literal.getLiteral();
					} else
						throw new RendererException(
								"expecting OWL literal for value specification, got " + referenceRendering.get());
				}
			} else if (valueSpecificationItemNode.hasValueExtractionFunctionNode()) {
				ValueExtractionFunctionNode valueExtractionFunction = valueSpecificationItemNode
						.getValueExtractionFunctionNode();
				processedValue += processValueExtractionFunction(valueExtractionFunction, value);
			} else if (valueSpecificationItemNode.hasCapturingExpression() && value != null) {
				String capturingExpression = valueSpecificationItemNode.getCapturingExpression();
				processedValue += processCapturingExpression(value, capturingExpression);
			}
		}
		return processedValue;
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
					result += (m.group(groupIndex));
			}
			return result;
		} catch (PatternSyntaxException e) {
			throw new RendererException("invalid capturing expression: " + capturingExpression + ": " + e.getMessage());
		}
	}

	private Optional<OWLLiteralRendering> renderValueExtractionFunctionArgument(
			ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
	{
		return Optional.empty(); // TODO Deal with reference and literal
	}

	private Optional<? extends Rendering> renderValueSpecificationItem(
			ValueSpecificationItemNode valueSpecificationItemNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	// TODO Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

	private String processValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode, String value)
			throws RendererException
	{
		List<String> arguments = new ArrayList<>();
		String functionName = valueExtractionFunctionNode.getFunctionName();
		boolean hasExplicitArguments = valueExtractionFunctionNode.hasArguments();
		String processedValue;

		if (valueExtractionFunctionNode.hasArguments()) {
			for (ValueExtractionFunctionArgumentNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
				Optional<OWLLiteralRendering> argumentRendering = renderValueExtractionFunctionArgument(argumentNode);
				if (argumentRendering.isPresent()) {
					OWLLiteral literal = argumentRendering.get().getOWLLiteral();
					arguments.add(literal.getLiteral());
				}
			}
		}

		switch (valueExtractionFunctionNode.getFunctionID()) {
		case MM_TO_UPPER_CASE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException("function " + functionName + " expecting one argument, got " + arguments.size());
				processedValue = arguments.get(0).toUpperCase();
			} else
				processedValue = value.toUpperCase();
			break;
		case MM_TO_LOWER_CASE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedValue = arguments.get(0).toLowerCase();
			} else
				processedValue = value.toLowerCase();
			break;
		case MM_TRIM:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedValue = arguments.get(0).trim();
			} else
				processedValue = value.trim();
			break;
		case MM_REVERSE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedValue = reverse(arguments.get(0));
			} else
				processedValue = reverse(value);
			break;
		case MM_CAPTURING:
			if (arguments.size() == 1) {
				processedValue = processCapturingExpression(value, arguments.get(0));
			} else if (arguments.size() == 2) {
				processedValue = processCapturingExpression(arguments.get(0), arguments.get(1));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_PREPEND:
			if (arguments.size() == 1) {
				processedValue = arguments.get(0) + value;
			} else if (arguments.size() == 2) {
				processedValue = arguments.get(0) + arguments.get(1);
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_APPEND:
			if (arguments.size() == 1) {
				processedValue = value + arguments.get(0);
			} else if (arguments.size() == 2) {
				processedValue = arguments.get(0) + arguments.get(1);
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE:
			if (arguments.size() == 2) {
				processedValue = value.replace(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedValue = arguments.get(0).replace(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE_ALL:
			if (arguments.size() == 2) {
				processedValue = value.replaceAll(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedValue = arguments.get(0).replaceAll(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE_FIRST:
			if (arguments.size() == 2) {
				processedValue = value.replaceFirst(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedValue = arguments.get(0).replaceFirst(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		default:
			throw new RendererException("unknown mapping function " + valueExtractionFunctionNode.getFunctionName() + ")");
		}
		return processedValue;
	}

	private String processLocationValue(SpreadsheetLocation location, ReferenceNode referenceNode, Rendering rendering)
			throws RendererException
	{
		String locationValue;
		SourceSpecificationNode sourceSpecification = referenceNode.getSourceSpecificationNode();

		if (sourceSpecification.hasLiteral()) {
			// Reference is a literal, e.g., @"Person", @"http://a.com#Person"
			locationValue = sourceSpecification.getLiteral();
			rendering.log("processReference: literal");
		} else { // Reference to data source location

			rendering.log("--processReference: specified location " + location);
			locationValue = dataSource.getLocationValue(location, referenceNode); // Deals with shifting

			if ((locationValue == null || locationValue.equals("")))
				locationValue = referenceNode.getActualDefaultLocationValue();

			if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_ERROR_IF_EMPTY_LOCATION)
				throw new RendererException("empty location " + location + " in reference " + referenceNode);

			if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_WARNING_IF_EMPTY_LOCATION)
				rendering.logLine("processReference: WARNING: empty location " + location + " in reference " + referenceNode);

			rendering.log(", location value [" + locationValue + "], entity type " + referenceNode.getReferenceTypeNode());
		}

		if (!referenceNode.getReferenceTypeNode().getReferenceType().isOWLLiteral()) {
			rendering.log(", namespace " + getReferenceNamespace(referenceNode));
			String language = getReferenceLanguage(referenceNode);
			displayLanguage(language, rendering);
			rendering.log(", valueEncoding");
			for (ValueEncodingNode valueEncoding : referenceNode.getValueEncodingNodes())
				rendering.log(" " + valueEncoding);
		}

		if (!sourceSpecification.hasLiteral()) { // Determine if originally specified location has been shifted
			if (referenceNode.getActualShiftDirective() != MM_NO_SHIFT && referenceNode.hasShiftedLocation()) {
				location = referenceNode.getShiftedLocation();
				rendering.log(", shifted location " + referenceNode.getShiftedLocation());
			}
		}
		rendering.logLine("");

		return locationValue;
	}

	private String processOWLLiteral(SpreadsheetLocation location, String locationValue, ReferenceType referenceType,
			ReferenceNode referenceNode, Rendering rendering) throws RendererException
	{
		String processedLocationValue = locationValue.replace("\"", "\\\"");
		String dataValue;

		if (referenceNode.hasLiteralValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
				dataValue = processValueEncoding(processedLocationValue, referenceNode.getLiteralValueEncodingNode(),
						referenceNode);
			else if (referenceNode.hasValueExtractionFunction())
				dataValue = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(),
						processedLocationValue);
			else
				dataValue = processedLocationValue;
		} else
			dataValue = "";

		if (dataValue.equals("") && !referenceNode.getActualDefaultDataValue().equals(""))
			dataValue = referenceNode.getActualDefaultDataValue();

		if (dataValue.equals("") && referenceNode.getActualEmptyDataValueDirective() == MM_ERROR_IF_EMPTY_DATA_VALUE)
			throw new RendererException("empty data value in reference " + referenceNode + " at location " + location);

		if (dataValue.equals("") && referenceNode.getActualEmptyDataValueDirective() == MM_WARNING_IF_EMPTY_DATA_VALUE)
			rendering.logLine(
					"processReference: WARNING: empty data value in reference " + referenceNode + " at location " + location);

		if (referenceType.isQuotedOWLDataValue())
			dataValue = "\"" + dataValue + "\"";

		return dataValue;
	}

	private void displayLanguage(String language, Rendering rendering)
	{
		String display = "";

		if (language != null) {
			display += ", xml:lang";
			if (language.equals(""))
				display += "=mm:null";
			else if (!language.equals("*"))
				display += "=*";
			else if (language.equals("+"))
				display += "!=mm:null";
			else
				display += language;
		}
		rendering.log(display);
	}

	private Set<OWLAxiom> addDefiningTypesFromReference(OWLEntity entity, ReferenceNode referenceNode)
			throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();
		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

		if (referenceNode.hasExplicitlySpecifiedTypes()) {
			for (TypeNode typeNode : referenceNode.getTypesNode().getTypeNodes()) {
				Optional<OWLEntity> definingType = renderType(typeNode);
				if (!definingType.isPresent()) {
					if (referenceType.isOWLClass()) {
						if (!entity.isOWLClass())
							throw new RendererException(
									"expecting class for type in reference " + referenceNode + " for " + entity + ", got " + entity
											.getClass().getCanonicalName());

						OWLClass cls = definingType.get().asOWLClass();
						OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(cls, entity.asOWLClass());
						axioms.add(axiom);
					} else if (referenceType.isOWLNamedIndividual()) {
						if (!entity.isOWLNamedIndividual())
							throw new RendererException(
									"expecting individual for type in reference " + referenceNode + " for " + entity + ", got " + entity
											.getClass().getCanonicalName());

						OWLClass cls = definingType.get().asOWLClass();
						OWLClassAssertionAxiom axiom = this.owlDataFactory
								.getOWLClassAssertionAxiom(cls, entity.asOWLNamedIndividual());
						axioms.add(axiom);
					} else if (referenceType.isOWLObjectProperty()) {
						if (!entity.isOWLObjectProperty())
							throw new RendererException(
									"expecting object property for type in reference " + referenceNode + " for " + entity);

						OWLObjectProperty property = definingType.get().asOWLObjectProperty();
						OWLSubObjectPropertyOfAxiom axiom = owlDataFactory
								.getOWLSubObjectPropertyOfAxiom(property, entity.asOWLObjectProperty());
						axioms.add(axiom);
					} else if (referenceType.isOWLDataProperty()) {
						if (!entity.isOWLDataProperty())
							throw new RendererException(
									"expecting data property for type in reference " + referenceNode + " for " + entity);

						OWLDataProperty property = definingType.get().asOWLDataProperty();

						OWLSubDataPropertyOfAxiom axiom = owlDataFactory
								.getOWLSubDataPropertyOfAxiom(property, entity.asOWLDataProperty());
						axioms.add(axiom);
					} else
						throw new RendererException("invalid entity type " + referenceType);
				}
			}
		}
		return axioms;
	}

	private Optional<OWLEntity> renderType(TypeNode typeNode) throws RendererException
	{
		if (typeNode.isOWLClassNode()) {
			Optional<OWLClassRendering> classRendering = entityRenderer.renderOWLClass((OWLClassNode)typeNode);
			if (classRendering.isPresent()) {
				return Optional.of(classRendering.get().getOWLClass());
			} else
				return Optional.empty();
		} else if (typeNode.isOWLPropertyNode()) {
			Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty((OWLPropertyNode)typeNode);
			if (propertyRendering.isPresent()) {
				return Optional.of(propertyRendering.get().getOWLProperty());
			} else
				return Optional.empty();
		} else if (typeNode.isReferenceNode()) {
			Optional<OWLAPIReferenceRendering> referenceRendering = renderReference((ReferenceNode)typeNode);
			if (referenceRendering.isPresent()) {
				if (referenceRendering.get().isOWLEntity()) {
					OWLEntity entity = referenceRendering.get().getOWLEntity().get();
					return Optional.of(entity);
				} else
					throw new RendererException("expecting OWL entity for node " + typeNode.getNodeName());
			} else
				return Optional.empty();
		} else
			throw new RendererException("internal error: unknown type " + typeNode + " for node " + typeNode.getNodeName());
	}

	private Optional<? extends Rendering> renderTypes(TypesNode typesNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	private Optional<? extends Rendering> renderValueExtractionFunction(
			ValueExtractionFunctionNode valueExtractionFunctionNode) throws RendererException
	{
		return Optional.empty();
	}

	private Optional<? extends Rendering> renderValueEncoding(ValueEncodingNode valueEncodingNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	private String getReferenceNamespace(ReferenceNode referenceNode) throws RendererException
	{
		// A reference will not have both a prefix and a namespace specified
		if (referenceNode.hasExplicitlySpecifiedPrefix()) {
			String prefix = referenceNode.getPrefixNode().getPrefix();
			String namespace = getNamespaceForPrefix(prefix);
			if (namespace == null)
				throw new RendererException("unknown prefix " + prefix + " specified in reference " + referenceNode);
			return namespace;
		} else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
			return referenceNode.getNamespaceNode().getNamespace();
		} else {
			if (!hasDefaultNamespace())
				throw new RendererException(
						"ontology has no default namespace and no namespace specified by reference " + referenceNode);

			return getDefaultNamespace();
		}
	}

	private String getReferenceLanguage(ReferenceNode referenceNode) throws RendererException
	{
		if (referenceNode.hasExplicitlySpecifiedLanguage())
			return referenceNode.getActualLanguage();
		else
			return getDefaultLanguage(); // Which might be null or empty
	}

	private String getDefaultNamespace()
	{
		return this.defaultNamespace;
	}

	private String getDefaultLanguage()
	{
		return this.defaultLanguage;
	}

	private String getNamespaceForPrefix(String prefix)
	{
		return null; // TODO
	}

	private boolean hasDefaultNamespace()
	{
		return this.defaultNamespace.length() != 0;
	}

	private String reverse(String source)
	{
		int i, len = source.length();
		StringBuffer dest = new StringBuffer(len);

		for (i = (len - 1); i >= 0; i--)
			dest.append(source.charAt(i));

		return dest.toString();
	}
}

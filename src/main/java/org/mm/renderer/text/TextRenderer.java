package org.mm.renderer.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLDataAllValuesFromNode;
import org.mm.parser.node.OWLDataSomeValuesFromNode;
import org.mm.parser.node.OWLDifferentFromNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLExactCardinalityNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLObjectAllValuesFromNode;
import org.mm.parser.node.OWLObjectOneOfNode;
import org.mm.parser.node.OWLObjectSomeValuesFromNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSameAsNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.TypesNode;
import org.mm.parser.node.ValueEncodingDirectiveNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLClassExpressionRenderer;
import org.mm.renderer.OWLCoreRenderer;
import org.mm.renderer.OWLEntityRenderer;
import org.mm.renderer.OWLLiteralRenderer;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.ReferenceRendererConfiguration;
import org.mm.renderer.ReferenceUtil;
import org.mm.renderer.Renderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.OWLLiteralRendering;
import org.mm.rendering.ReferenceRendering;
import org.mm.rendering.text.TextLiteralRendering;
import org.mm.rendering.text.TextReferenceRendering;
import org.mm.rendering.text.TextRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;

// TODO Refactor - too long. Look at the OWLAPI renderer for example of decomposition.

/**
 * This renderer produces a text rendering of a Mapping Master expression with reference values
 * substituted inline.
 */
public class TextRenderer extends ReferenceRendererConfiguration
		implements Renderer, ReferenceRenderer, OWLCoreRenderer, OWLEntityRenderer, OWLLiteralRenderer,
		OWLClassExpressionRenderer, MappingMasterParserConstants
{
	private SpreadSheetDataSource dataSource;

	public TextRenderer(SpreadSheetDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public void changeDataSource(SpreadSheetDataSource dataSource)
	{
		// Logging data source has been updated
		this.dataSource = dataSource;
	}

	@Override
	public ReferenceRendererConfiguration getReferenceRendererConfiguration()
	{
		return this;
	}

	@Override public Optional<? extends TextRendering> renderExpression(ExpressionNode expressionNode)
			throws RendererException
	{
		if (expressionNode.hasMMExpression())
			return renderMMExpression(expressionNode.getMMExpressionNode());
		else
			throw new InternalRendererException("unknown child for node " + expressionNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderMMExpression(MMExpressionNode mmExpressionNode)
			throws RendererException
	{
		if (mmExpressionNode.hasOWLClassDeclaration())
			return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
		else if (mmExpressionNode.hasOWLIndividualDeclaration())
			return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
		else
			throw new InternalRendererException("unknown child for node " + mmExpressionNode.getNodeName());
	}

	// TODO Refactor - too long
	@Override public Optional<TextReferenceRendering> renderReference(ReferenceNode referenceNode)
			throws RendererException
	{
		SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

		if (sourceSpecificationNode.hasLiteral()) {
			String literalValue = sourceSpecificationNode.getLiteral();
			return Optional.of(new TextReferenceRendering(literalValue, referenceType));
		} else {
			SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
			String resolvedReferenceValue = ReferenceUtil.resolveReferenceValue(dataSource, referenceNode);

			if (referenceType.isUntyped())
				throw new RendererException("untyped reference " + referenceNode);

			if (resolvedReferenceValue.isEmpty()
					&& referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION)
				return Optional.empty();

			if (resolvedReferenceValue.isEmpty()
					&& referenceNode.getActualEmptyLocationDirective() == MM_WARNING_IF_EMPTY_LOCATION) {
				// TODO Warn in log files
				return Optional.empty();
			}

			if (referenceType.isOWLLiteral()) { // Reference is an OWL literal
				String literalReferenceValue = processOWLLiteralReferenceValue(location, resolvedReferenceValue, referenceNode);

				if (literalReferenceValue.isEmpty()
						&& referenceNode.getActualEmptyLiteralDirective() == MM_SKIP_IF_EMPTY_LITERAL)
					return Optional.empty();

				if (literalReferenceValue.isEmpty()
						&& referenceNode.getActualEmptyLiteralDirective() == MM_WARNING_IF_EMPTY_LITERAL) {
					// TODO Warn in log file
					return Optional.empty();
				}

				return Optional.of(new TextReferenceRendering(literalReferenceValue, referenceType));
			} else if (referenceType.isOWLEntity()) { // Reference is an OWL entity
				// TODO If the rendering uses the ID then we should use it
				String rdfID = getReferenceRDFID(resolvedReferenceValue, referenceNode);
				String rdfsLabel = getReferenceRDFSLabel(resolvedReferenceValue, referenceNode);

				if (rdfID.isEmpty() && referenceNode.getActualEmptyRDFIDDirective() == MM_SKIP_IF_EMPTY_ID)
					return Optional.empty();

				if (rdfsLabel.isEmpty() && referenceNode.getActualEmptyRDFSLabelDirective() == MM_SKIP_IF_EMPTY_LABEL)
					return Optional.empty();

				if (rdfID.isEmpty() && referenceNode.getActualEmptyRDFIDDirective() == MM_WARNING_IF_EMPTY_ID) {
					// TODO Warn in log file
					return Optional.empty();
				}

				if (rdfsLabel.isEmpty() && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_LABEL) {
					// TODO Warn in log file
					return Optional.empty();
				}

				return Optional.of(new TextReferenceRendering(rdfsLabel, referenceType));
			} else
				throw new InternalRendererException(
						"unknown reference type " + referenceType + " for reference " + referenceNode);
		}
	}

	private String processOWLLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
			ReferenceNode referenceNode) throws RendererException
	{
		String sourceValue = rawLocationValue.replace("\"", "\\\"");
		String processedReferenceValue;
		
		if (referenceNode.hasLiteralValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
				processedReferenceValue = generateReferenceValue(sourceValue, referenceNode.getLiteralValueEncodingNode(),
						referenceNode);
			else if (referenceNode.hasValueExtractionFunctionNode()) {
				ValueExtractionFunctionNode valueExtractionFunctionNode = referenceNode.getValueExtractionFunctionNode();
				processedReferenceValue = generateReferenceValue(sourceValue, valueExtractionFunctionNode);
			} else
				processedReferenceValue = sourceValue;
		} else
			processedReferenceValue = "";
		
		if (processedReferenceValue.isEmpty() && !referenceNode.getActualDefaultLiteral().isEmpty())
			processedReferenceValue = referenceNode.getActualDefaultLiteral();
		
		if (processedReferenceValue.isEmpty() && referenceNode.getActualEmptyLiteralDirective() == MM_ERROR_IF_EMPTY_LITERAL)
			throw new RendererException("empty literal in reference " + referenceNode + " at location " + location);
		
		return processedReferenceValue;
	}

	private String generateReferenceValue(String sourceValue, ValueEncodingDirectiveNode valueEncodingDirectiveNode,
			ReferenceNode referenceNode) throws RendererException
	{
		if (valueEncodingDirectiveNode != null) {
			if (valueEncodingDirectiveNode.hasValueSpecificationNode())
				return generateReferenceValue(sourceValue, valueEncodingDirectiveNode.getValueSpecificationNode(),
						referenceNode);
			else
				return sourceValue;
		} else
			return sourceValue;
	}

	private String generateReferenceValue(String sourceValue, ValueSpecificationNode valueSpecificationNode,
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
				processedReferenceValue += ReferenceUtil.capture(sourceValue, capturingExpression);
			}
		}
		return processedReferenceValue;
	}

	// Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

	private String generateReferenceValue(String sourceValue, ValueExtractionFunctionNode valueExtractionFunctionNode)
			throws RendererException
	{
		List<String> arguments = new ArrayList<>();
		if (valueExtractionFunctionNode.hasArguments()) {
			for (ValueExtractionFunctionArgumentNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
				String argumentValue = generateValueExtractionFunctionArgument(argumentNode);
				arguments.add(argumentValue);
			}
		}
		return ReferenceUtil.evaluateReferenceValue(
				valueExtractionFunctionNode.getFunctionName(),
				valueExtractionFunctionNode.getFunctionID(),
				arguments,
				sourceValue,
				valueExtractionFunctionNode.hasArguments());
	}

	private String getReferenceRDFID(String sourceValue, ReferenceNode referenceNode) throws RendererException
	{
		String rdfIDValue;

		if (referenceNode.hasRDFIDValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding())
				rdfIDValue = generateReferenceValue(sourceValue, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
			else if (referenceNode.hasValueExtractionFunctionNode())
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

	private String getReferenceRDFSLabel(String sourceValue, ReferenceNode referenceNode) throws RendererException
	{
		String rdfsLabelText;

		if (referenceNode.hasRDFSLabelValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding())
				rdfsLabelText = generateReferenceValue(sourceValue, referenceNode.getRDFSLabelValueEncodingNode(),
						referenceNode);
			else if (referenceNode.hasValueExtractionFunctionNode())
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

	// TODO Refactor - too long
	@Override public Optional<? extends TextRendering> renderOWLClassDeclaration(
			OWLClassDeclarationNode classDeclarationNode) throws RendererException
	{
		OWLClassNode declaredClassNode = classDeclarationNode.getOWLClassNode();
		Optional<? extends TextRendering> declaredClassRendering = renderOWLClass(declaredClassNode);

		if (!declaredClassRendering.isPresent())
			return Optional.empty();

		String declaredClassName = declaredClassRendering.get().getRendering();
		StringBuilder textRepresentation = new StringBuilder("Class: " + declaredClassName);
		boolean isFirst = true;

		if (classDeclarationNode.hasOWLSubclassOfNodes()) {
			for (OWLSubclassOfNode subClassOfNode : classDeclarationNode.getOWLSubclassOfNodes()) {
				Optional<? extends TextRendering> subClassOfRendering = renderOWLSubClassOf(
						classDeclarationNode.getOWLClassNode(), subClassOfNode);

				if (!subClassOfRendering.isPresent())
					continue;

				textRepresentation.append(subClassOfRendering.get().getRendering());
				isFirst = false;
			}
		}

		isFirst = true;
		if (classDeclarationNode.hasOWLEquivalentClassesNode()) {
			for (OWLEquivalentClassesNode equivalentToNode : classDeclarationNode.getOWLEquivalentClassesNodes()) {
				Optional<? extends TextRendering> equivalentToRendering = renderOWLEquivalentClasses(
						classDeclarationNode.getOWLClassNode(), equivalentToNode);

				if (!equivalentToRendering.isPresent())
					continue;

				textRepresentation.append(equivalentToRendering.get().getRendering());
				isFirst = false;
			}
		}

		isFirst = true;
		if (classDeclarationNode.hasAnnotationFactNodes()) {
			for (AnnotationFactNode annotationFactNode : classDeclarationNode.getAnnotationFactNodes()) {
				Optional<? extends TextRendering> annotationFactRendering = renderAnnotationFact(annotationFactNode);

				if (!annotationFactRendering.isPresent())
					continue;

				if (isFirst)
					textRepresentation.append(" Annotations: ");
				else
					textRepresentation.append(", ");
				textRepresentation.append(annotationFactRendering.get().getRendering());
				isFirst = false;
			}
		}

		return textRepresentation.length() == 0 ?
				Optional.empty() :
				Optional.of(new TextRendering(textRepresentation.toString()));
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

	// TODO Refactor - too long
	@Override public Optional<? extends TextRendering> renderOWLIndividualDeclaration(
			OWLIndividualDeclarationNode individualDeclarationNode) throws RendererException
	{
		StringBuilder textRepresentation = new StringBuilder();
		boolean isFirst = true;
		Optional<? extends TextRendering> individualRendering = renderOWLNamedIndividual(
				individualDeclarationNode.getOWLIndividualNode());

		if (!individualRendering.isPresent())
			return Optional.empty();

		textRepresentation.append("Individual: ");
		textRepresentation.append(individualRendering.get().getRendering());

		if (individualDeclarationNode.hasFacts()) {

			for (FactNode factNode : individualDeclarationNode.getFactNodes()) {
				Optional<? extends TextRendering> factRendering = renderFact(factNode);

				if (!factRendering.isPresent())
					continue;

				if (isFirst)
					textRepresentation.append(" Facts: ");
				else
					textRepresentation.append(", ");

				textRepresentation.append(factRendering.get().getRendering());
				isFirst = false;
			}
		}

		if (individualDeclarationNode.hasTypes()) {
			Optional<? extends TextRendering> typesRendering = renderTypes(individualDeclarationNode.getTypesNode());

			if (typesRendering.isPresent())
				textRepresentation.append(" Types: " + typesRendering.get().getRendering());
		}

		isFirst = true;
		if (individualDeclarationNode.hasAnnotations()) {

			for (AnnotationFactNode factNode : individualDeclarationNode.getAnnotationNodes()) {
				Optional<? extends TextRendering> factRendering = renderAnnotationFact(factNode);

				if (!factRendering.isPresent())
					continue;

				if (isFirst)
					textRepresentation.append(" Annotations: ");
				else
					textRepresentation.append(", ");
				textRepresentation.append(factRendering.get().getRendering());
				isFirst = false;
			}
		}

		if (individualDeclarationNode.hasSameAs()) {
			Optional<? extends TextRendering> sameAsRendering = renderOWLSameAs(individualDeclarationNode.getOWLSameAsNode());
			if (sameAsRendering.isPresent())
				textRepresentation.append(sameAsRendering.get().getRendering());
		}

		if (individualDeclarationNode.hasDifferentFrom()) {
			Optional<? extends TextRendering> differentFromRendering = renderOWLDifferentFrom(
					individualDeclarationNode.getOWLDifferentFromNode());
			if (differentFromRendering.isPresent())
				textRepresentation.append(differentFromRendering.get().getRendering());
		}

		return Optional.of(new TextRendering(textRepresentation.toString()));
	}

	@Override public Optional<? extends TextRendering> renderOWLClassExpression(
			OWLClassExpressionNode classExpressionNode) throws RendererException
	{
		StringBuilder textRepresentation = new StringBuilder();

		if (classExpressionNode.hasOWLObjectOneOfNode()) {
			Optional<? extends TextRendering> objectOneOfRendering = renderOWLObjectOneOf(
					classExpressionNode.getOWLObjectOneOfNode());
			if (objectOneOfRendering.isPresent())
				textRepresentation.append(objectOneOfRendering.get().getRendering());
		} else if (classExpressionNode.hasOWLUnionClassNode()) {
			Optional<? extends TextRendering> unionClassRendering = renderOWLUnionClass(
					classExpressionNode.getOWLUnionClassNode());
			if (unionClassRendering.isPresent())
				textRepresentation.append(unionClassRendering.get().getRendering());
		} else if (classExpressionNode.hasOWLRestrictionNode()) {
			Optional<? extends TextRendering> restrictionRendering = renderOWLRestriction(
					classExpressionNode.getOWLRestrictionNode());
			if (restrictionRendering.isPresent())
				textRepresentation.append(restrictionRendering.get().getRendering());
		} else if (classExpressionNode.hasOWLClassNode()) {
			Optional<? extends TextRendering> classRendering = renderOWLClass(classExpressionNode.getOWLClassNode());
			if (classRendering.isPresent())
				textRepresentation.append(classRendering.get().getRendering());
		} else
			throw new RendererException("unexpected child for node " + classExpressionNode.getNodeName());

		if (textRepresentation.length() != 0 && classExpressionNode.getIsNegated())
			textRepresentation.insert(0, "NOT ");

		return textRepresentation.length() == 0 ?
				Optional.empty() :
				Optional.of(new TextRendering(textRepresentation.toString()));
	}

	@Override public Optional<? extends TextRendering> renderOWLIntersectionClass(
			OWLIntersectionClassNode intersectionClassNode) throws RendererException
	{
		if (intersectionClassNode.getOWLClassExpressionNodes().size() == 1) {
			Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(
					intersectionClassNode.getOWLClassExpressionNodes().get(0));

			return classExpressionRendering;
		} else {
			StringBuilder textRepresentation = new StringBuilder();
			boolean isFirst = true;

			for (OWLClassExpressionNode classExpressionNode : intersectionClassNode.getOWLClassExpressionNodes()) {
				Optional<? extends TextRendering> classesExpressionRendering = renderOWLClassExpression(classExpressionNode);

				if (classesExpressionRendering.isPresent()) {
					if (isFirst)
						textRepresentation.append("(");
					else
						textRepresentation.append(" AND ");
					textRepresentation.append(classesExpressionRendering.get().getRendering());
					isFirst = false;
				}
			}
			if (textRepresentation.length() != 0)
				textRepresentation.append(")");

			return textRepresentation.length() == 0 ?
					Optional.empty() :
					Optional.of(new TextRendering(textRepresentation.toString()));
		}
	}

	@Override public Optional<? extends TextRendering> renderOWLEquivalentClasses(OWLClassNode declaredClassNode,
			OWLEquivalentClassesNode equivalentClassesNode) throws RendererException
	{
		StringBuilder textRepresentation = new StringBuilder();

		if (equivalentClassesNode.getClassExpressionNodes().size() == 1) {
			Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(
					equivalentClassesNode.getClassExpressionNodes().get(0));
			if (!classExpressionRendering.isPresent())
				return Optional.empty();
			else
				textRepresentation.append(" EquivalentTo: " + classExpressionRendering.get().getRendering());
		} else {
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassExpressionNode : equivalentClassesNode.getClassExpressionNodes()) {
				Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
				if (!classExpressionRendering.isPresent())
					continue; // Any empty class expression will generate an empty rendering
				if (isFirst)
					textRepresentation.append(" EquivalentTo: ");
				else
					textRepresentation.append(", ");
				textRepresentation.append(classExpressionRendering.get().getRendering());
				isFirst = false;
			}
		}
		return textRepresentation.length() == 0 ?
				Optional.empty() :
				Optional.of(new TextRendering(textRepresentation.toString()));
	}

	@Override public Optional<? extends TextRendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode)
			throws RendererException
	{
		if (unionClassNode.getOWLIntersectionClassNodes().size() == 1) {
			Optional<? extends TextRendering> intersectionRendering = renderOWLIntersectionClass(
					unionClassNode.getOWLIntersectionClassNodes().get(0));

			return intersectionRendering;
		} else {
			StringBuilder textRepresentation = new StringBuilder();
			boolean isFirst = true;

			for (OWLIntersectionClassNode intersectionClassNode : unionClassNode.getOWLIntersectionClassNodes()) {
				Optional<? extends TextRendering> intersectionRendering = renderOWLIntersectionClass(intersectionClassNode);

				if (intersectionRendering.isPresent()) {
					if (isFirst)
						textRepresentation.append("(");
					else
						textRepresentation.append(" OR ");
					textRepresentation.append(intersectionRendering.get().getRendering());
					isFirst = false;
				}
			}
			if (textRepresentation.length() != 0)
				textRepresentation.append(")");

			return textRepresentation.length() == 0 ?
					Optional.empty() :
					Optional.of(new TextRendering(textRepresentation.toString()));
		}
	}

	@Override public Optional<? extends TextRendering> renderOWLProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		if (propertyNode.hasReferenceNode())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.hasNameNode())
			return renderName(propertyNode.getNameNode());
		else
			throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		if (propertyNode.hasReferenceNode())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.hasNameNode())
			return renderName(propertyNode.getNameNode());
		else
			throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLRestriction(OWLRestrictionNode restrictionNode)
			throws RendererException
	{
		Optional<? extends TextRendering> propertyRendering = renderOWLProperty(restrictionNode.getOWLPropertyNode());
		Optional<? extends TextRendering> restrictionRendering;

		if (restrictionNode.isOWLMinCardinality())
			restrictionRendering = renderOWLMinCardinality(restrictionNode.getOWLPropertyNode(),
					restrictionNode.getOWLMinCardinalityNode());
		else if (restrictionNode.isOWLMaxCardinality())
			restrictionRendering = renderOWLMaxCardinality(restrictionNode.getOWLPropertyNode(),
					restrictionNode.getOWLMaxCardinalityNode());
		else if (restrictionNode.isOWLExactCardinality())
			restrictionRendering = renderOWLExactCardinality(restrictionNode.getOWLPropertyNode(),
					restrictionNode.getOWLExactCardinalityNode());
		else if (restrictionNode.isOWLHasValue())
			restrictionRendering = renderOWLHasValue(restrictionNode.getOWLPropertyNode(),
					restrictionNode.getOWLHasValueNode());
		else if (restrictionNode.isOWLAllValuesFrom())
			restrictionRendering = renderOWLAllValuesFrom(restrictionNode.getOWLPropertyNode(),
					restrictionNode.getOWLAllValuesFromNode());
		else if (restrictionNode.isOWLSomeValuesFrom())
			restrictionRendering = renderOWLSomeValuesFrom(restrictionNode.getOWLPropertyNode(),
					restrictionNode.getOWLSomeValuesFromNode());
		else
			throw new InternalRendererException("unknown child for node " + restrictionNode.getNodeName());

		if (propertyRendering.isPresent() && restrictionRendering.isPresent())
			return Optional.of(new TextRendering(
					"(" + propertyRendering.get().getRendering() + " " + restrictionRendering.get().getRendering()
							+ ")"));
		else
			return Optional.empty();
	}

	@Override public Optional<? extends TextRendering> renderOWLObjectHasValue(OWLPropertyNode propertyNode,
			OWLHasValueNode hasValueNode) throws RendererException
	{
		if (hasValueNode.hasReferenceNode())
			return renderReference(hasValueNode.getReferenceNode());
		else if (hasValueNode.hasNameNone())
			return renderName(hasValueNode.getNameNode());
		else
			throw new InternalRendererException("unknown child for node " + hasValueNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLDataHasValue(OWLPropertyNode propertyNode,
			OWLHasValueNode dataHasValueNode) throws RendererException
	{
		if (dataHasValueNode.hasReferenceNode())
			return renderReference(dataHasValueNode.getReferenceNode());
		else if (dataHasValueNode.hasLiteralNode())
			return renderOWLLiteral(dataHasValueNode.getOWLLiteralNode());
		else
			throw new InternalRendererException("unknown child for node " + dataHasValueNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
			OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException
	{
		String datatypeName = dataAllValuesFromNode.getDatatypeName();

		if (!datatypeName.isEmpty())
			return Optional.of(new TextRendering("ONLY " + datatypeName));
		else
			return Optional.empty();
	}

	@Override public Optional<? extends TextRendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectSomeValuesFromNode objectSomeValuesFromNode) throws RendererException
	{
		if (objectSomeValuesFromNode.hasOWLClassNode())
			return renderOWLClass(objectSomeValuesFromNode.getOWLClassNode());
		else if (objectSomeValuesFromNode.hasOWLClassExpressionNode())
			return renderOWLClassExpression(objectSomeValuesFromNode.getOWLClassExpressionNode());
		else
			throw new InternalRendererException("unknown child for node " + objectSomeValuesFromNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLDataSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLDataSomeValuesFromNode dataSomeValuesFromNode) throws RendererException
	{
		String datatypeName = dataSomeValuesFromNode.getDatatypeName();

		if (!datatypeName.isEmpty())
			return Optional.of(new TextRendering(datatypeName));
		else
			return Optional.empty();
	}

	private Optional<? extends TextRendering> renderOWLSubClassOf(OWLClassNode declaredClassNode,
			OWLSubclassOfNode subClassOfNode) throws RendererException
	{
		StringBuilder subClassesRepresentation = new StringBuilder();

		if (subClassOfNode.getClassExpressionNodes().size() == 1) {
			Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(
					subClassOfNode.getClassExpressionNodes().get(0));
			if (!classExpressionRendering.isPresent())
				return Optional.empty();
			else
				subClassesRepresentation.append(classExpressionRendering.get().getRendering());
		} else {
			boolean isFirst = true;

			for (OWLClassExpressionNode classExpressionNode : subClassOfNode.getClassExpressionNodes()) {
				Optional<? extends TextRendering> classExpressionRendering = renderOWLClassExpression(classExpressionNode);
				if (!classExpressionRendering.isPresent())
					continue; // Any empty class expression will generate an empty rendering
				if (!isFirst)
					subClassesRepresentation.append(", ");
				subClassesRepresentation.append(classExpressionRendering.get().getRendering());
				isFirst = false;
			}
		}

		if (subClassesRepresentation.length() != 0)
			return Optional.of(new TextRendering(" SubClassOf: " + subClassesRepresentation));
		else
			return Optional.empty();
	}

	private Optional<? extends TextRendering> renderOWLSameAs(OWLSameAsNode OWLSameAsNode)
			throws RendererException
	{
		StringBuilder textRepresentation = new StringBuilder();
		boolean isFirst = true;

		for (OWLNamedIndividualNode owlNamedIndividualNode : OWLSameAsNode.getIndividualNodes()) {
			Optional<? extends TextRendering> namedIndividualRendering = renderOWLNamedIndividual(owlNamedIndividualNode);

			if (namedIndividualRendering.isPresent()) {
				if (!isFirst)
					textRepresentation.append(", ");
				textRepresentation.append(namedIndividualRendering.get().getRendering());
				isFirst = false;
			}
		}
		return textRepresentation.length() == 0 ?
				Optional.empty() :
				Optional.of(new TextRendering(" SameAs: " + textRepresentation));
	}

	private Optional<? extends TextRendering> renderOWLDifferentFrom(OWLDifferentFromNode differentFromNode)
			throws RendererException
	{
		StringBuilder textRepresentation = new StringBuilder();
		boolean isFirst = true;

		for (OWLNamedIndividualNode namedIndividualNode : differentFromNode.getNamedIndividualNodes()) {
			Optional<? extends TextRendering> individualRendering = renderOWLNamedIndividual(namedIndividualNode);

			if (individualRendering.isPresent()) {
				if (!isFirst)
					textRepresentation.append(", ");
				textRepresentation.append(individualRendering.get().getRendering());
				isFirst = false;
			}
		}
		return textRepresentation.length() == 0 ?
				Optional.empty() :
				Optional.of(new TextRendering(" DifferentFrom: " + textRepresentation));
	}

	@Override public Optional<? extends TextRendering> renderOWLPropertyAssertionObject(
			OWLPropertyAssertionObjectNode propertyAssertionObjectNode) throws RendererException
	{
		if (propertyAssertionObjectNode.isReference()) {
			ReferenceNode referenceNode = propertyAssertionObjectNode.getReferenceNode();
			ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
			Optional<? extends TextReferenceRendering> referenceRendering = renderReference(referenceNode);
			if (referenceRendering.isPresent()) {
				if (referenceType.isQuotedOWLLiteral())
					return Optional.of(new TextRendering(addQuotes(referenceRendering.get().getRawValue())));
				else
					return referenceRendering;
			} else
				return Optional.empty();
		} else if (propertyAssertionObjectNode.isName())
			return renderName(propertyAssertionObjectNode.getNameNode());
		else if (propertyAssertionObjectNode.isLiteral()) {
			Optional<? extends TextLiteralRendering> literalRendering = renderOWLLiteral(
					propertyAssertionObjectNode.getOWLLiteralNode());
			if (literalRendering.isPresent()) {
				if (literalRendering.get().getOWLLiteralType().isQuotedOWLLiteral())
					return Optional.of(new TextRendering(addQuotes(literalRendering.get().getRawValue())));
				else
					return literalRendering;
			} else
				return Optional.empty();
		} else
			throw new InternalRendererException("unknown child for node " + propertyAssertionObjectNode.getNodeName());
	}

	private Optional<? extends TextRendering> renderAnnotationFact(AnnotationFactNode annotationFactNode)
			throws RendererException
	{
		Optional<? extends TextRendering> propertyRendering = renderOWLProperty(annotationFactNode.getOWLPropertyNode());
		Optional<? extends TextRendering> annotationValueRendering = renderOWLAnnotationValue(
				annotationFactNode.getOWLAnnotationValueNode());

		if (propertyRendering.isPresent() && annotationValueRendering.isPresent()) {
			String textRepresentation =
					propertyRendering.get().getRendering() + " " + annotationValueRendering.get().getRendering();
			return Optional.of(new TextRendering(textRepresentation));
		} else
			return Optional.empty();
	}

	@Override public Optional<? extends TextRendering> renderOWLObjectOneOf(OWLObjectOneOfNode objectOneOfNode)
			throws RendererException
	{
		StringBuilder textRepresentation = new StringBuilder();

		if (objectOneOfNode.getOWLNamedIndividualNodes().size() == 1) {
			Optional<? extends TextRendering> individualRendering = renderOWLNamedIndividual(
					objectOneOfNode.getOWLNamedIndividualNodes().get(0));

			if (!individualRendering.isPresent())
				return Optional.empty();
			else
				textRepresentation.append(individualRendering.get().getRendering());
		} else {
			boolean isFirst = true;

			for (OWLNamedIndividualNode owlNamedIndividualNode : objectOneOfNode.getOWLNamedIndividualNodes()) {
				if (isFirst)
					textRepresentation.append("({"); // TODO These parenthesis should not be required. See comment in grammar.
				else
					textRepresentation.append(", ");

				Optional<? extends TextRendering> individualRendering = renderOWLNamedIndividual(owlNamedIndividualNode);

				if (!individualRendering.isPresent())
					return Optional.empty();
				else {
					textRepresentation.append(individualRendering.get().getRendering());
					isFirst = false;
				}
			}
			if (textRepresentation.length() != 0)
				textRepresentation.append("})"); // TODO These parenthesis should not be required. See comment in grammar.
		}
		return textRepresentation.length() == 0 ?
				Optional.empty() :
				Optional.of(new TextRendering(textRepresentation.toString()));
	}

	private Optional<? extends TextRendering> renderFact(FactNode factNode) throws RendererException
	{
		Optional<? extends TextRendering> propertyRendering = renderOWLProperty(factNode.getOWLPropertyNode());
		Optional<? extends TextRendering> propertyValueRendering = renderOWLPropertyAssertionObject(
				factNode.getOWLPropertyAssertionObjectNode());

		if (propertyRendering.isPresent() && propertyValueRendering.isPresent()) {
			String textRepresentation =
					propertyRendering.get().getRendering() + " " + propertyValueRendering.get().getRendering();
			return Optional.of(new TextRendering(textRepresentation));
		} else
			return Optional.empty();
	}

	@Override public Optional<? extends TextRendering> renderOWLAnnotationValue(
			OWLAnnotationValueNode annotationValueNode) throws RendererException
	{
		if (annotationValueNode.isReference()) {
			ReferenceNode referenceNode = annotationValueNode.getReferenceNode();
			ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
			Optional<? extends TextReferenceRendering> referenceRendering = renderReference(referenceNode);
			if (referenceRendering.isPresent()) {
				if (referenceType.isQuotedOWLLiteral())
					return Optional.of(new TextRendering(addQuotes(referenceRendering.get().getRawValue())));
				else
					return referenceRendering;
			} else
				return Optional.empty();
		} else if (annotationValueNode.isName())
			return renderName(annotationValueNode.getNameNode());
		else if (annotationValueNode.isLiteral()) {
			Optional<? extends TextLiteralRendering> literalRendering = renderOWLLiteral(
					annotationValueNode.getOWLLiteralNode());
			if (literalRendering.isPresent()) {
				if (literalRendering.get().getOWLLiteralType().isQuotedOWLLiteral())
					return Optional.of(new TextRendering(addQuotes(literalRendering.get().getRawValue())));
				else
					return literalRendering;
			} else
				return Optional.empty();
		} else
			throw new InternalRendererException("unknown child for node " + annotationValueNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLObjectExactCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode exactCardinalityNode) throws RendererException
	{
		return renderOWLExactCardinality(propertyNode, exactCardinalityNode);
	}

	@Override public Optional<? extends TextRendering> renderOWLDataExactCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode exactCardinalityNode) throws RendererException
	{
		return renderOWLExactCardinality(propertyNode, exactCardinalityNode);
	}

	@Override public Optional<? extends TextRendering> renderOWLObjectMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		return renderOWLMaxCardinality(propertyNode, maxCardinalityNode);
	}

	@Override public Optional<? extends TextRendering> renderOWLDataMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		return renderOWLMaxCardinality(propertyNode, maxCardinalityNode);
	}

	@Override public Optional<? extends TextRendering> renderOWLObjectMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		return renderOWLMinCardinality(propertyNode, minCardinalityNode);
	}

	@Override public Optional<? extends TextRendering> renderOWLDataMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		return renderOWLMinCardinality(propertyNode, minCardinalityNode);
	}

	@Override public Optional<? extends TextRendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectAllValuesFromNode objectAllValuesFromNode) throws RendererException
	{
		Optional<? extends TextRendering> classRendering;

		if (objectAllValuesFromNode.hasOWLClass())
			classRendering = renderOWLClass(objectAllValuesFromNode.getOWLClassNode());
		else if (objectAllValuesFromNode.hasOWLClassExpression())
			classRendering = renderOWLClassExpression(objectAllValuesFromNode.getOWLClassExpressionNode());
		else
			throw new InternalRendererException("unknown child for node " + objectAllValuesFromNode.getNodeName());

		if (classRendering.isPresent())
			return Optional.of(new TextRendering("ONLY " + classRendering.get().getRendering()));
		else
			return Optional.empty();
	}

	@Override public Optional<? extends TextRendering> renderOWLClass(OWLClassNode classNode) throws RendererException
	{
		if (classNode.hasReferenceNode())
			return renderReference(classNode.getReferenceNode());
		else if (classNode.hasNameNode())
			return renderName(classNode.getNameNode());
		else
			throw new InternalRendererException("unknown child for node " + classNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		if (propertyNode.hasReferenceNode())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.hasNameNode())
			return renderName(propertyNode.getNameNode());
		else
			throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLDataProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		if (propertyNode.hasReferenceNode())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.hasNameNode())
			return renderName(propertyNode.getNameNode());
		else
			throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
	}

	@Override public Optional<? extends TextRendering> renderOWLNamedIndividual(
			OWLNamedIndividualNode namedIndividualNode) throws RendererException
	{
		if (namedIndividualNode.hasReferenceNode())
			return renderReference(namedIndividualNode.getReferenceNode());
		else if (namedIndividualNode.hasNameNode())
			return renderName(namedIndividualNode.getNameNode());
		else
			throw new InternalRendererException("unknown child for node " + namedIndividualNode.getNodeName());
	}

	@Override public Optional<? extends TextLiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode)
			throws RendererException
	{
		if (literalNode.isInt())
			return Optional.of(new TextLiteralRendering(literalNode.getIntLiteralNode().getValue()));
		else if (literalNode.isFloat())
			return Optional.of(new TextLiteralRendering(literalNode.getFloatLiteralNode().getValue()));
		else if (literalNode.isString())
			return Optional.of(new TextLiteralRendering(literalNode.getStringLiteralNode().getValue()));
		else if (literalNode.isBoolean())
			return Optional.of(new TextLiteralRendering(literalNode.getBooleanLiteralNode().getValue()));
		else
			throw new InternalRendererException("unknown child for node " + literalNode.getNodeName());
	}

	private Optional<? extends TextRendering> renderOWLAllValuesFrom(OWLPropertyNode propertyNode,
			OWLAllValuesFromNode allValuesFromNode) throws RendererException
	{
		if (allValuesFromNode.hasOWLDataAllValuesFromNode())
			return renderOWLDataAllValuesFrom(propertyNode, allValuesFromNode.getOWLDataAllValuesFromNode());
		else if (allValuesFromNode.hasOWLObjectAllValuesFromNode())
			return renderOWLObjectAllValuesFrom(propertyNode, allValuesFromNode.getObjectOWLAllValuesFromNode());
		else
			throw new InternalRendererException("unknown child for node " + allValuesFromNode.getNodeName());
	}

	private Optional<? extends TextRendering> renderOWLSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLSomeValuesFromNode someValuesFromNode) throws RendererException
	{
		Optional<? extends TextRendering> valueRendering;

		if (someValuesFromNode.hasOWLDataSomeValuesFromNode())
			valueRendering = renderOWLDataSomeValuesFrom(propertyNode, someValuesFromNode.getOWLDataSomeValuesFromNode());
		else if (someValuesFromNode.hasOWLObjectSomeValuesFrom())
			valueRendering = renderOWLObjectSomeValuesFrom(propertyNode, someValuesFromNode.getOWLObjectSomeValuesFromNode());
		else
			throw new InternalRendererException("unknown child for node " + someValuesFromNode.getNodeName());

		if (valueRendering.isPresent()) {
			return Optional.of(new TextRendering("SOME " + valueRendering.get().getRendering()));
		} else
			return Optional.empty();
	}

	private Optional<? extends TextRendering> renderOWLHasValue(OWLPropertyNode propertyNode,
			OWLHasValueNode hasValueNode) throws RendererException
	{
		Optional<? extends TextRendering> valueRendering;

		if (hasValueNode.hasReferenceNode()) {
			ReferenceNode referenceNode = hasValueNode.getReferenceNode();
			ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
			Optional<? extends TextReferenceRendering> referenceRendering = renderReference(referenceNode);
			if (referenceRendering.isPresent()) {
				if (referenceType.isQuotedOWLLiteral())
					return Optional.of(new TextRendering(addQuotes(referenceRendering.get().getRawValue())));
				else
					return Optional.of(new TextRendering(referenceRendering.get().getRawValue()));
			} else
				return Optional.empty();
		} else if (hasValueNode.hasNameNone())
			valueRendering = renderName(hasValueNode.getNameNode());
		else if (hasValueNode.hasLiteralNode()) {
			Optional<? extends TextLiteralRendering> literalRendering = renderOWLLiteral(hasValueNode.getOWLLiteralNode());
			if (literalRendering.isPresent()) {
				if (literalRendering.get().getOWLLiteralType().isQuotedOWLLiteral())
					valueRendering = Optional.of(new TextLiteralRendering(addQuotes(literalRendering.get().getRendering())));
				else
					valueRendering = literalRendering;
			} else
				return Optional.empty();
		} else
			throw new InternalRendererException("unknown child for node " + hasValueNode.getNodeName());

		if (valueRendering.isPresent()) {
			return Optional.of(new TextRendering("VALUE " + valueRendering.get().getRendering()));
		} else
			return Optional.empty();
	}

	private Optional<? extends TextRendering> renderOWLExactCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode owlExactCardinalityNode) throws RendererException
	{
		String textRepresentation = "" + owlExactCardinalityNode.getCardinality();

		if (!textRepresentation.isEmpty())
			textRepresentation = "EXACTLY " + textRepresentation;

		return textRepresentation.isEmpty() ? Optional.empty() : Optional.of(new TextRendering(textRepresentation));
	}

	private Optional<? extends TextRendering> renderOWLMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		String textRepresentation = "" + maxCardinalityNode.getCardinality();

		if (!textRepresentation.isEmpty())
			textRepresentation = "MAX " + textRepresentation;

		return textRepresentation.isEmpty() ? Optional.empty() : Optional.of(new TextRendering(textRepresentation));
	}

	private Optional<? extends TextRendering> renderOWLMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		String textRepresentation = "" + minCardinalityNode.getCardinality();

		if (!textRepresentation.isEmpty())
			textRepresentation = "MIN " + textRepresentation;

		return textRepresentation.isEmpty() ? Optional.empty() : Optional.of(new TextRendering(textRepresentation));
	}

	private Optional<? extends TextRendering> renderName(NameNode nameNode) throws RendererException
	{
		String name = nameNode.isQuoted() ? "'" + nameNode.getName() + "'" : nameNode.getName();

		return name.isEmpty() ? Optional.empty() : Optional.of(new TextRendering(name));
	}

	private Optional<? extends TextRendering> renderType(TypeNode typeNode) throws RendererException
	{
		if (typeNode instanceof ReferenceNode)
			return renderReference((ReferenceNode)typeNode);
		else if (typeNode instanceof OWLClassExpressionNode)
			return renderOWLClassExpression((OWLClassExpressionNode)typeNode);
		else if (typeNode instanceof OWLClassNode)
			return renderOWLClass((OWLClassNode)typeNode);
		else
			throw new InternalRendererException("do not know how to render type node " + typeNode.getNodeName());
	}

	private Optional<? extends TextRendering> renderTypes(TypesNode types) throws RendererException
	{
		StringBuilder textRepresentation = new StringBuilder();
		boolean isFirst = true;

		for (TypeNode typeNode : types.getTypeNodes()) {
			Optional<? extends TextRendering> typeRendering = renderType(typeNode);
			if (typeRendering.isPresent()) {
				if (!isFirst)
					textRepresentation.append(", ");
				textRepresentation.append(typeRendering.get().getRendering());
				isFirst = false;
			}
		}
		return textRepresentation.length() == 0 ?
				Optional.empty() :
				Optional.of(new TextRendering(textRepresentation.toString()));
	}

	private String addQuotes(String s) { return "\"" + s + "\""; }
}

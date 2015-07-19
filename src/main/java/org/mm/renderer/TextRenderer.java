package org.mm.renderer;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.DefaultDataValueNode;
import org.mm.parser.node.DefaultIDNode;
import org.mm.parser.node.DefaultLabelNode;
import org.mm.parser.node.DefaultLocationValueNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLDataAllValuesFromNode;
import org.mm.parser.node.OWLDataSomeValuesFromNode;
import org.mm.parser.node.OWLDifferentFromNode;
import org.mm.parser.node.OWLEnumeratedClassNode;
import org.mm.parser.node.OWLExactCardinalityNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLObjectAllValuesFromNode;
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
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;

import java.sql.Ref;
import java.util.Optional;

/**
 * This renderer simply produces the standard presentation syntax rendering of the supplied entities. Subclasses will specialize and perform custom actions for
 * individual entities.
 */
public class TextRenderer
		implements CoreRenderer, OWLEntityRenderer, OWLLiteralRenderer, ReferenceRenderer, OWLClassExpressionRenderer,
		MappingMasterParserConstants
{
	public Optional<TextRendering> renderExpression(ExpressionNode expressionNode) throws RendererException
	{
		if (expressionNode.hasMMExpression())
			return renderMMExpression(expressionNode.getMMExpressionNode());
		else
			throw new RendererException("unknown expression type " + expressionNode);
	}

	public Optional<TextRendering> renderMMExpression(MMExpressionNode mmExpressionNode) throws RendererException
	{
		if (mmExpressionNode.hasOWLClassDeclaration())
			return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
		else if (mmExpressionNode.hasOWLIndividualDeclaration())
			return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
		else
			throw new RendererException("unknown expression: " + mmExpressionNode);
	}

	public Optional<TextRendering> renderOWLIndividualDeclaration(
			OWLIndividualDeclarationNode owlIndividualDeclarationNode) throws RendererException
	{
		TextRendering rendering = new TextRendering();
		boolean isFirst = true;
		Optional<TextRendering> individualRendering = renderOWLNamedIndividual(
				owlIndividualDeclarationNode.getOWLIndividualNode());

		if (!individualRendering.isPresent())
			return Optional.empty();

		rendering.addText("Individual: ");

		rendering.addText(individualRendering.get().getTextRendering());

		if (owlIndividualDeclarationNode.hasFacts()) {
			rendering.addText(" Facts: ");

			for (FactNode fact : owlIndividualDeclarationNode.getFactNodes()) {
				Optional<TextRendering> factRendering = renderFact(fact);

				if (!factRendering.isPresent())
					continue;

				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(factRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		if (owlIndividualDeclarationNode.hasTypes()) {
			Optional<TextRendering> typesRendering = renderTypes(owlIndividualDeclarationNode.getTypesNode());

			if (typesRendering.isPresent())
				rendering.addText(" Types: " + typesRendering.get().getTextRendering());
		}

		isFirst = true;
		if (owlIndividualDeclarationNode.hasAnnotations()) {
			rendering.addText(" Annotations:");

			for (AnnotationFactNode factNode : owlIndividualDeclarationNode.getAnnotationNodes()) {
				Optional<TextRendering> factRendering = renderAnnotationFact(factNode);

				if (!factRendering.isPresent())
					continue;

				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(factRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		if (owlIndividualDeclarationNode.hasSameAs()) {
			Optional<TextRendering> sameAsRendering = renderOWLSameAs(owlIndividualDeclarationNode.getOWLSameAsNode());
			if (sameAsRendering.isPresent())
				rendering.addText(sameAsRendering.get().getTextRendering());
		}

		if (owlIndividualDeclarationNode.hasDifferentFrom()) {
			Optional<TextRendering> differentFromRendering = renderDifferentFrom(
					owlIndividualDeclarationNode.getOWLDifferentFromNode());
			if (differentFromRendering.isPresent())
				rendering.addText(differentFromRendering.get().getTextRendering());
		}

		return Optional.of(rendering);
	}

	public Optional<TextRendering> renderFact(FactNode factNode) throws RendererException
	{
		Optional<TextRendering> propertyRendering = renderOWLProperty(factNode.getOWLPropertyNode());
		Optional<TextRendering> propertyValueRendering = renderOWLPropertyAssertionObject(
				factNode.getOWLPropertyAssertionObjectNode());

		if (propertyRendering.isPresent() && propertyValueRendering.isPresent()) {
			String textRendering =
					propertyRendering.get().getTextRendering() + " " + propertyValueRendering.get().getTextRendering();
			return Optional.of(new TextRendering(textRendering));
		} else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLPropertyAssertionObject(
			OWLPropertyAssertionObjectNode owlPropertyAssertionObjectNode) throws RendererException
	{
		if (owlPropertyAssertionObjectNode.isReference())
			return renderReference(owlPropertyAssertionObjectNode.getReferenceNode());
		else if (owlPropertyAssertionObjectNode.isName())
			return renderName(owlPropertyAssertionObjectNode.getNameNode());
		else if (owlPropertyAssertionObjectNode.isLiteral())
			return renderOWLLiteral(owlPropertyAssertionObjectNode.getOWLLiteralNode());
		else
			throw new RendererException("unknown property value node " + owlPropertyAssertionObjectNode);
	}

	public Optional<TextRendering> renderOWLUnionClass(OWLUnionClassNode owlUnionClassNode) throws RendererException
	{
		if (owlUnionClassNode.getOWLIntersectionClassNodes().size() == 1) {
			Optional<TextRendering> intersectionRendering = renderOWLIntersectionClass(
					owlUnionClassNode.getOWLIntersectionClassNodes().get(0));

			return intersectionRendering;
		} else {
			TextRendering rendering = new TextRendering();
			boolean isFirst = true;

			for (OWLIntersectionClassNode owlIntersectionClassNode : owlUnionClassNode.getOWLIntersectionClassNodes()) {
				Optional<TextRendering> intersectionRendering = renderOWLIntersectionClass(owlIntersectionClassNode);

				if (intersectionRendering.isPresent()) {
					if (isFirst)
						rendering.addText("(");
					else
						rendering.addText(" OR ");
					rendering.addText(intersectionRendering.get().getTextRendering());
					isFirst = false;
				}
			}
			if (!rendering.nothingRendered())
				rendering.addText(")");

			return Optional.of(rendering);
		}
	}

	public Optional<TextRendering> renderOWLProperty(OWLPropertyNode propertyNode) throws RendererException
	{
		if (propertyNode.hasReferenceNode())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.hasNameNode())
			return renderName(propertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + propertyNode);
	}

	public Optional<TextRendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode) throws RendererException
	{
		if (propertyNode.hasReferenceNode())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.hasNameNode())
			return renderName(propertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + propertyNode);
	}

	public Optional<TextRendering> renderOWLRestriction(OWLRestrictionNode restrictionNode) throws RendererException
	{
		Optional<TextRendering> propertyRendering = renderOWLProperty(restrictionNode.getOWLPropertyNode());
		Optional<TextRendering> restrictionRendering;

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
			throw new RendererException("unknown OWLRestriction " + restrictionNode);

		if (propertyRendering.isPresent() && restrictionRendering.isPresent())
			return Optional.of(new TextRendering(
					"(" + propertyRendering.get().getTextRendering() + " " + restrictionRendering.get().getTextRendering()
							+ ")"));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLExactCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode owlExactCardinalityNode) throws RendererException
	{
		String textRendering = "" + owlExactCardinalityNode.getCardinality();

		if (textRendering.length() != 0)
			textRendering += "EXACTLY " + textRendering;

		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering));
	}

	private Optional<TextRendering> renderOWLHasValue(OWLPropertyNode propertyNode, OWLHasValueNode hasValueNode)
			throws RendererException
	{
		if (hasValueNode.isReference())
			return renderReference(hasValueNode.getReferenceNode());
		else if (hasValueNode.isName())
			return renderName(hasValueNode.getNameNode());
		else if (hasValueNode.isLiteral())
			return renderOWLLiteral(hasValueNode.getOWLLiteralNode());
		else
			throw new RendererException("unknown property value node " + hasValueNode + " for object has value restriction");
	}

	@Override public Optional<TextRendering> renderOWLObjectHasValue(OWLPropertyNode propertyNode,
			OWLHasValueNode hasValueNode) throws RendererException
	{
		if (hasValueNode.isReference())
			return renderReference(hasValueNode.getReferenceNode());
		else if (hasValueNode.isName())
			return renderName(hasValueNode.getNameNode());
		else
			throw new RendererException("unknown property value node " + hasValueNode + " for object has value restriction");
	}

	@Override public Optional<TextRendering> renderOWLDataHasValue(OWLPropertyNode propertyNode,
			OWLHasValueNode hasValueNode) throws RendererException
	{
		if (hasValueNode.isReference())
			return renderReference(hasValueNode.getReferenceNode());
		else if (hasValueNode.isLiteral())
			return renderOWLLiteral(hasValueNode.getOWLLiteralNode());
		else
			throw new RendererException("unknown property value node " + hasValueNode + " for data has value restriction");
	}

	public Optional<TextRendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
			OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException
	{
		String datatypeName = dataAllValuesFromNode.getDatatypeName();

		if (!datatypeName.equals(""))
			return Optional.of(new TextRendering("ONLY " + datatypeName));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLDataSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLDataSomeValuesFromNode dataSomeValuesFromNode) throws RendererException
	{
		String datatypeName = dataSomeValuesFromNode.getDatatypeName();

		if (!datatypeName.equals(""))
			return Optional.of(new TextRendering("SOME " + datatypeName));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectSomeValuesFromNode objectSomeValuesFromNode) throws RendererException
	{
		Optional<TextRendering> classRendering;

		if (objectSomeValuesFromNode.hasOWLClass())
			classRendering = renderOWLClass(objectSomeValuesFromNode.getOWLClassNode());
		else if (objectSomeValuesFromNode.hasOWLClassExpression())
			classRendering = renderOWLClassExpression(objectSomeValuesFromNode.getOWLClassExpressionNode());
		else
			throw new RendererException("unknown OWLObjectSomeValuesFrom node " + objectSomeValuesFromNode);

		if (classRendering.isPresent())
			return Optional.of(new TextRendering("SOME " + classRendering.get().getTextRendering()));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLSubclassOf(OWLSubclassOfNode subclassOfNode) throws RendererException
	{
		TextRendering rendering = new TextRendering();
		rendering.addText(" SubClassOf: ");

		if (subclassOfNode.getClassExpressionNodes().size() == 1) {
			Optional<TextRendering> classExpressionRendering = renderOWLClassExpression(
					subclassOfNode.getClassExpressionNodes().get(0));
			if (!classExpressionRendering.isPresent())
				return Optional.empty();
			else
				rendering.addText(classExpressionRendering.get().getTextRendering());
		} else {
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassExpressionNode : subclassOfNode.getClassExpressionNodes()) {
				Optional<TextRendering> classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
				if (!classExpressionRendering.isPresent())
					continue; // Any empty class expression will generate an empty rendering
				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(classExpressionRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		return Optional.of(rendering);
	}

	public Optional<TextRendering> renderReference(ReferenceNode referenceNode) throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();
		boolean hasExplicitOptions = referenceNode.hasExplicitOptions();
		boolean atLeastOneOptionProcessed = false;

		textRendering.append(referenceNode.getSourceSpecificationNode().toString());

		if (hasExplicitOptions)
			textRendering.append("(");

		if (referenceNode.hasExplicitlySpecifiedReferenceType()) {
			textRendering.append(referenceNode.getReferenceTypeNode().getReferenceType().getTypeName());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedPrefix()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			else
				atLeastOneOptionProcessed = true;
			textRendering.append(referenceNode.getPrefixNode().getPrefix());
		}

		if (referenceNode.hasExplicitlySpecifiedNamespace()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			else
				atLeastOneOptionProcessed = true;
			textRendering.append(referenceNode.getNamespaceNode().getNamespace());
		}

		if (referenceNode.hasValueExtractionFunction()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			else
				atLeastOneOptionProcessed = true;
			textRendering.append(referenceNode.getValueExtractionFunctionNode().getFunctionName());
		}

		if (referenceNode.hasExplicitlySpecifiedValueEncodings()) {
			boolean isFirst = true;
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			for (ValueEncodingNode valueEncodingNode : referenceNode.getValueEncodingNodes()) {
				if (!isFirst)
					textRendering.append(" ");
				textRendering.append(valueEncodingNode.getEncodingTypeName());
				isFirst = false;
			}
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultLocationValue()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getDefaultLocationValueNode().getDefaultLocationValue());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultDataValue()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getDefaultDataValueNode().getDefaultDataValue());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultRDFID()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getDefaultRDFIDNode().getDefaultRDFID());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultRDFSLabel()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getDefaultRDFSLabelNode().getDefaultRDFSLabel());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedLanguage()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getLanguageNode().getLanguage());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedPrefix()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getPrefixNode().getPrefix());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedNamespace()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getNamespaceNode().getNamespace());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyLocationDirective()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getEmptyLocationDirectiveNode().toString());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyDataValueDirective()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getEmptyDataValueDirectiveNode().toString());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyRDFIDDirective()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getEmptyRDFIDDirectiveNode().toString());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyRDFSLabelDirective()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getEmptyRDFSLabelDirectiveNode().getEmptyRDFSLabelSettingName());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedShiftDirective()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getShiftDirectiveNode().getShiftSettingName());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedTypes()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getTypesNode().toString());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedIfExistsDirective()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getIfExistsDirectiveNode().getIfExistsSettingName());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedIfNotExistsDirective()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			textRendering.append(referenceNode.getIfNotExistsDirectiveNode().toString());
			atLeastOneOptionProcessed = true;
		}

		if (hasExplicitOptions)
			textRendering.append(")");

		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
	}

	public Optional<TextRendering> renderValueEncoding(ValueEncodingNode valueEncodingNode) throws RendererException
	{
		TextRendering rendering = new TextRendering(valueEncodingNode.getEncodingTypeName());

		if (valueEncodingNode.hasValueSpecification()) {
			Optional<TextRendering> valueSpecificationRendering = renderValueSpecification(
					valueEncodingNode.getValueSpecification());
			if (valueSpecificationRendering.isPresent())
				rendering.addText(valueSpecificationRendering.get().getTextRendering());
		}
		return Optional.of(rendering);
	}

	public Optional<TextRendering> renderTypes(TypesNode types) throws RendererException
	{
		TextRendering rendering = new TextRendering();
		boolean isFirst = true;

		for (TypeNode typeNode : types.getTypeNodes()) {
			Optional<TextRendering> typeRendering = renderType(typeNode);
			if (typeRendering.isPresent()) {
				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(typeRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		return Optional.of(rendering);
	}

	public Optional<TextRendering> renderType(TypeNode typeNode) throws RendererException
	{
		if (typeNode instanceof ReferenceNode)
			return renderReference((ReferenceNode)typeNode);
		else if (typeNode instanceof OWLClassExpressionNode)
			return renderOWLClassExpression((OWLClassExpressionNode)typeNode);
		else
			throw new RendererException("do not know how to render type node " + typeNode.getNodeName());
	}

	public Optional<TextRendering> renderOWLSameAs(OWLSameAsNode OWLSameAsNode) throws RendererException
	{
		TextRendering rendering = new TextRendering(" SameAs: ");
		boolean isFirst = true;

		for (OWLNamedIndividualNode owlNamedIndividualNode : OWLSameAsNode.getIndividualNodes()) {
			Optional<TextRendering> individualRendering = renderOWLNamedIndividual(owlNamedIndividualNode);

			if (individualRendering.isPresent()) {
				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(individualRendering.get().getTextRendering());
				isFirst = false;
			}
		}
		return Optional.of(rendering);
	}

	public Optional<TextRendering> renderValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode)
			throws RendererException
	{
		TextRendering rendering = new TextRendering(valueExtractionFunctionNode.getFunctionName());

		if (valueExtractionFunctionNode.hasArguments()) {
			boolean isFirst = true;
			rendering.addText("(");

			for (ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode : valueExtractionFunctionNode
					.getArgumentNodes()) {
				Optional<TextRendering> valueExtractionFunctionArgumentRendering = renderValueExtractionFunctionArgument(
						valueExtractionFunctionArgumentNode);
				if (valueExtractionFunctionArgumentRendering.isPresent()) {
					if (!isFirst)
						rendering.addText(" ");
					rendering.addText(valueExtractionFunctionArgumentRendering.get().getTextRendering());
					isFirst = false;
				}
			}
			rendering.addText(")");
		}
		return Optional.of(rendering);
	}

	public Optional<TextRendering> renderSourceSpecification(SourceSpecificationNode sourceSpecificationNode)
			throws RendererException
	{
		TextRendering rendering = new TextRendering("@");

		if (sourceSpecificationNode.hasSource())
			rendering.addText("'" + sourceSpecificationNode.getSource() + "'!");

		if (sourceSpecificationNode.hasLocation())
			rendering.addText(sourceSpecificationNode.getLocation());
		else
			rendering.addText("\"" + sourceSpecificationNode.getLiteral() + "\""); // A literal

		return Optional.of(rendering);
	}

	public Optional<TextRendering> renderValueExtractionFunctionArgument(
			ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
	{
		if (valueExtractionFunctionArgumentNode.isOWLLiteralNode())
			return renderOWLLiteral(valueExtractionFunctionArgumentNode.getOWLLiteralNode());
		else if (valueExtractionFunctionArgumentNode.isReferenceNode())
			return renderReference(valueExtractionFunctionArgumentNode.getReferenceNode());
		else
			throw new RendererException(
					"unknown ValueExtractionFunctionArgument node " + valueExtractionFunctionArgumentNode);
	}

	public Optional<TextRendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode)
			throws RendererException
	{
		TextRendering rendering = new TextRendering("Class: " + owlClassDeclarationNode.getOWLClassNode().toString());
		boolean isFirst = true;

		if (owlClassDeclarationNode.hasSubclassOf()) {
			rendering.addText(" SubclassOf: ");
			for (OWLSubclassOfNode subclassOf : owlClassDeclarationNode.getSubclassOfNodes()) {
				Optional<TextRendering> subclassOfRendering = renderOWLSubclassOf(subclassOf);
				if (!subclassOfRendering.isPresent())
					continue;

				if (!isFirst)
					rendering.addText(", ");

				rendering.addText(subclassOfRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		isFirst = true;
		if (owlClassDeclarationNode.hasEquivalentTo()) {
			rendering.addText(" EquivalentTo: ");
			for (OWLClassEquivalentToNode equivalentTo : owlClassDeclarationNode.getEquivalentToNodes()) {
				Optional<TextRendering> equivalentToRendering = renderOWLClassEquivalentTo(equivalentTo);
				if (!equivalentToRendering.isPresent())
					continue;

				if (!isFirst)
					rendering.addText(", ");

				rendering.addText(equivalentToRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		isFirst = true;
		if (owlClassDeclarationNode.hasAnnotations()) {
			rendering.addText(" Annotations: ");
			for (AnnotationFactNode annotationFactNode : owlClassDeclarationNode.getAnnotationFactNodes()) {
				Optional<TextRendering> factRendering = renderAnnotationFact(annotationFactNode);

				if (factRendering.isPresent())
					continue;

				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(factRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		return Optional.of(rendering);
	}

	@Override public Optional<TextRendering> renderOWLAnnotationValue(OWLAnnotationValueNode annotationValueNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	public Optional<TextRendering> renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException
	{
		Optional<TextRendering> propertyRendering = renderOWLProperty(annotationFactNode.getOWLPropertyNode());
		Optional<TextRendering> annotationValueRendering = renderOWLAnnotationValue(
				annotationFactNode.getOWLAnnotationValueNode());

		if (propertyRendering.isPresent() && annotationValueRendering.isPresent()) {
			String textRendering =
					propertyRendering.get().getTextRendering() + " " + annotationValueRendering.get().getTextRendering();
			return Optional.of(new TextRendering(textRendering));
		} else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLIntersectionClass(OWLIntersectionClassNode owlIntersectionClassNode)
			throws RendererException
	{
		if (owlIntersectionClassNode.getOWLClassExpressionNodes().size() == 1) {
			Optional<TextRendering> classesOrRestrictionsRendering = renderOWLClassExpression(
					owlIntersectionClassNode.getOWLClassExpressionNodes().get(0));

			return classesOrRestrictionsRendering;
		} else {
			TextRendering rendering = new TextRendering();
			boolean isFirst = true;

			for (OWLClassExpressionNode classExpressionNode : owlIntersectionClassNode.getOWLClassExpressionNodes()) {
				Optional<TextRendering> classesOrRestrictionsRendering = renderOWLClassExpression(classExpressionNode);

				if (classesOrRestrictionsRendering.isPresent()) {
					if (isFirst)
						rendering.addText("(");
					else
						rendering.addText(" AND ");
					rendering.addText(classesOrRestrictionsRendering.get().getTextRendering());
					isFirst = false;
				}
			}
			if (!rendering.nothingRendered())
				rendering.addText(")");
			return Optional.of(rendering);
		}
	}

	public Optional<TextRendering> renderOWLClassExpression(OWLClassExpressionNode classExpressionNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		if (classExpressionNode.hasOWLEnumeratedClassNode()) {
			Optional<TextRendering> enumeratedClassRendering = renderOWLEnumeratedClass(
					classExpressionNode.getOWLEnumeratedClassNode());
			if (enumeratedClassRendering.isPresent())
				textRendering.append(enumeratedClassRendering.get().getTextRendering());
		} else if (classExpressionNode.hasOWLUnionClassNode()) {
			Optional<TextRendering> unionClassRendering = renderOWLUnionClass(classExpressionNode.getOWLUnionClassNode());
			if (unionClassRendering.isPresent())
				textRendering.append(unionClassRendering.get().getTextRendering());
		} else if (classExpressionNode.hasOWLRestrictionNode()) {
			Optional<TextRendering> restrictionRendering = renderOWLRestriction(classExpressionNode.getOWLRestrictionNode());
			if (restrictionRendering.isPresent())
				textRendering.append(restrictionRendering.get().getTextRendering());
		} else if (classExpressionNode.hasOWLClassNode()) {
			Optional<TextRendering> classRendering = renderOWLClass(classExpressionNode.getOWLClassNode());
			if (classRendering.isPresent())
				textRendering.append(classRendering.get().getTextRendering());
		} else
			throw new RendererException("unexpected OWLClassExpression node " + classExpressionNode);

		if (textRendering.length() == 0)
			if (classExpressionNode.getIsNegated())
				textRendering.append("NOT " + textRendering);

		return textRendering.length() != 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
	}

	public Optional<TextRendering> renderOWLEnumeratedClass(OWLEnumeratedClassNode enumeratedClassNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		if (enumeratedClassNode.getOWLNamedIndividualNodes().size() == 1) {
			Optional<TextRendering> individualRendering = renderOWLNamedIndividual(
					enumeratedClassNode.getOWLNamedIndividualNodes().get(0));

			if (!individualRendering.isPresent())
				return Optional.empty();
			else
				textRendering.append(individualRendering.get().getTextRendering());
		} else {
			boolean isFirst = true;

			textRendering.append("{");
			for (OWLNamedIndividualNode owlNamedIndividualNode : enumeratedClassNode.getOWLNamedIndividualNodes()) {
				if (!isFirst)
					textRendering.append(" ");
				Optional<TextRendering> individualRendering = renderOWLNamedIndividual(owlNamedIndividualNode);

				if (!individualRendering.isPresent())
					return Optional.empty();
				else {
					textRendering.append(individualRendering.get().getTextRendering());
					isFirst = false;
				}
			}
			textRendering.append("}");
		}
		return textRendering.length() != 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
	}

	public Optional<TextRendering> renderOWLMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		String textRendering = "" + maxCardinalityNode.getCardinality();

		if (textRendering.length() != 0)
			return Optional.of(new TextRendering("MAX " + textRendering));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		String textRendering = "" + minCardinalityNode.getCardinality();

		if (textRendering.length() != 0)
			return Optional.of(new TextRendering("MIN " + textRendering));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLAllValuesFrom(OWLPropertyNode propertyNode,
			OWLAllValuesFromNode allValuesFromNode) throws RendererException
	{
		if (allValuesFromNode.hasOWLDataAllValuesFromNode())
			return renderOWLDataAllValuesFrom(propertyNode, allValuesFromNode.getOWLDataAllValuesFromNode());
		else if (allValuesFromNode.hasOWLObjectAllValuesFromNode())
			return renderOWLObjectAllValuesFrom(propertyNode, allValuesFromNode.getObjectOWLAllValuesFromNode());
		else
			throw new RendererException("unknown child for node " + allValuesFromNode.getNodeName());
	}

	public Optional<TextRendering> renderOWLSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLSomeValuesFromNode someValuesFromNode) throws RendererException
	{
		if (someValuesFromNode.hasOWLDataSomeValuesFromNode())
			return renderOWLDataSomeValuesFrom(propertyNode, someValuesFromNode.getOWLDataSomeValuesFromNode());
		else if (someValuesFromNode.hasOWLObjectSomeValuesFrom())
			return renderOWLObjectSomeValuesFrom(propertyNode, someValuesFromNode.getOWLObjectSomeValuesFromNode());
		else
			throw new RendererException("unknown child for node " + someValuesFromNode.getNodeName());
	}

	public Optional<TextRendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectAllValuesFromNode objectAllValuesFromNode) throws RendererException
	{
		Optional<TextRendering> classRendering;

		if (objectAllValuesFromNode.hasOWLClass())
			classRendering = renderOWLClass(objectAllValuesFromNode.getOWLClassNode());
		else if (objectAllValuesFromNode.hasOWLClassExpression())
			classRendering = renderOWLClassExpression(objectAllValuesFromNode.getOWLClassExpressionNode());
		else
			throw new RendererException("unknown child for node " + objectAllValuesFromNode.getNodeName());

		if (classRendering.isPresent())
			return Optional.of(new TextRendering("ONLY " + classRendering.get().getTextRendering()));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLClass(OWLClassNode classNode) throws RendererException
	{
		if (classNode.hasReferenceNode())
			return renderReference(classNode.getReferenceNode());
		else if (classNode.hasNameNode())
			return renderName(classNode.getNameNode());
		else
			throw new RendererException("unknown OWLClass node " + classNode);
	}

	public Optional<TextRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode) throws RendererException
	{
		if (propertyNode.hasReferenceNode())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.hasNameNode())
			return renderName(propertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + propertyNode);
	}

	public Optional<TextRendering> renderOWLDataProperty(OWLPropertyNode propertyNode) throws RendererException
	{
		if (propertyNode.hasReferenceNode())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.hasNameNode())
			return renderName(propertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + propertyNode);
	}

	public Optional<TextRendering> renderOWLNamedIndividual(OWLNamedIndividualNode namedIndividualNode)
			throws RendererException
	{
		if (namedIndividualNode.hasReferenceNode())
			return renderReference(namedIndividualNode.getReferenceNode());
		else if (namedIndividualNode.hasNameNode())
			return renderName(namedIndividualNode.getNameNode());
		else
			throw new RendererException("unknown OWLIndividual node " + namedIndividualNode);
	}

	public Optional<TextRendering> renderName(NameNode nameNode) throws RendererException
	{
		String name = nameNode.isQuoted() ? "'" + nameNode.getName() + "'" : nameNode.getName();

		return name.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(name));
	}

	public Optional<TextRendering> renderOWLClassEquivalentTo(OWLClassEquivalentToNode classEquivalentToNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		textRendering.append(" EquivalentTo: ");

		if (classEquivalentToNode.getClassExpressionNodes().size() == 1) {
			Optional<TextRendering> classExpressionRendering = renderOWLClassExpression(
					classEquivalentToNode.getClassExpressionNodes().get(0));
			if (!classExpressionRendering.isPresent())
				return classExpressionRendering;
			else
				textRendering.append(classExpressionRendering.get().getTextRendering());
		} else {
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassExpressionNode : classEquivalentToNode.getClassExpressionNodes()) {
				Optional<TextRendering> classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
				if (!classExpressionRendering.isPresent())
					continue; // Any empty class expression will generate an empty rendering
				if (!isFirst)
					textRendering.append(", ");
				textRendering.append(classExpressionRendering.get().getTextRendering());
				isFirst = false;
			}
		}
		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
	}

	public Optional<TextRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
	{
		if (literalNode.isInteger())
			return Optional.of(new TextRendering(literalNode.getIntegerLiteralNode().toString()));
		else if (literalNode.isFloat())
			return Optional.of(new TextRendering(literalNode.getFloatLiteralNode().toString()));
		else if (literalNode.isString())
			return Optional.of(new TextRendering(literalNode.toString()));
		else if (literalNode.isBoolean())
			return Optional.of(new TextRendering(literalNode.getBooleanLiteralNode().toString()));
		else
			throw new RendererException("unknown Literal node " + literalNode);
	}

	public Optional<TextRendering> renderValueSpecification(ValueSpecificationNode valueSpecificationNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();
		boolean isFirst = true;

		if (valueSpecificationNode.getNumberOfValueSpecificationItems() > 1)
			textRendering.append("(");

		for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
				.getValueSpecificationItemNodes()) {
			Optional<TextRendering> valueSpecificationItemRendering = renderValueSpecificationItem(
					valueSpecificationItemNode);

			if (valueSpecificationItemRendering.isPresent()) {
				if (isFirst)
					textRendering.append("=");
				else
					textRendering.append(", ");
				textRendering.append(valueSpecificationItemRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		if (valueSpecificationNode.getNumberOfValueSpecificationItems() > 1)
			textRendering.append(")");

		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
	}

	public Optional<TextRendering> renderDefaultLocationValue(DefaultLocationValueNode defaultLocationValueNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(defaultLocationValueNode.toString()));
	}

	public Optional<TextRendering> renderDefaultDataValue(DefaultDataValueNode defaultDataValueNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(defaultDataValueNode.toString()));
	}

	public Optional<TextRendering> renderDefaultID(DefaultIDNode defaultIDNode) throws RendererException
	{
		return Optional.of(new TextRendering(defaultIDNode.toString()));
	}

	public Optional<TextRendering> renderDefaultLabel(DefaultLabelNode defaultLabelNode) throws RendererException
	{
		return Optional.of(new TextRendering(defaultLabelNode.toString()));
	}

	public Optional<TextRendering> renderValueSpecificationItem(ValueSpecificationItemNode valueSpecificationItemNode)
			throws RendererException
	{
		if (valueSpecificationItemNode.hasStringLiteral())
			return Optional.of(new TextRendering("\"" + valueSpecificationItemNode.getStringLiteral() + "\""));
		else if (valueSpecificationItemNode.hasReferenceNode())
			return renderReference(valueSpecificationItemNode.getReferenceNode());
		else if (valueSpecificationItemNode.hasValueExtractionFunctionNode())
			return renderValueExtractionFunction(valueSpecificationItemNode.getValueExtractionFunctionNode());
		else if (valueSpecificationItemNode.hasCapturingExpression())
			return Optional.of(new TextRendering("[\"" + valueSpecificationItemNode.getCapturingExpression() + "\"]"));
		else
			throw new RendererException("unknown ValueSpecificationItem node " + valueSpecificationItemNode);
	}

	public Optional<TextRendering> renderDifferentFrom(OWLDifferentFromNode OWLDifferentFromNode) throws RendererException
	{
		TextRendering rendering = new TextRendering(" DifferentFrom: ");
		boolean isFirst = true;

		for (OWLNamedIndividualNode owlNamedIndividualNode : OWLDifferentFromNode.getNamedIndividualNodes()) {
			Optional<TextRendering> individualRendering = renderOWLNamedIndividual(owlNamedIndividualNode);

			if (individualRendering.isPresent()) {
				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(individualRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		return Optional.of(rendering);
	}
}

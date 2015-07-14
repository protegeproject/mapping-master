package org.mm.renderer;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.DefaultDataValueNode;
import org.mm.parser.node.DefaultIDNode;
import org.mm.parser.node.DefaultLabelNode;
import org.mm.parser.node.DefaultLocationValueNode;
import org.mm.parser.node.DifferentFromNode;
import org.mm.parser.node.EmptyDataValueSettingNode;
import org.mm.parser.node.EmptyLocationSettingNode;
import org.mm.parser.node.EmptyRDFIDSettingNode;
import org.mm.parser.node.EmptyRDFSLabelSettingNode;
import org.mm.parser.node.ReferenceTypeNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.IfExistsDirectiveNode;
import org.mm.parser.node.IfNotExistsDirectiveNode;
import org.mm.parser.node.LanguageNode;
import org.mm.parser.node.LiteralNode;
import org.mm.parser.node.MMDefaultDatatypePropertyValueTypeNode;
import org.mm.parser.node.MMDefaultReferenceTypeNode;
import org.mm.parser.node.MMDefaultPropertyTypeNode;
import org.mm.parser.node.MMDefaultPropertyValueTypeNode;
import org.mm.parser.node.MMDefaultValueEncodingNode;
import org.mm.parser.node.MMDirectiveNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.NamespaceNode;
import org.mm.parser.node.OWLAllValuesFromClassNode;
import org.mm.parser.node.OWLAllValuesFromDataTypeNode;
import org.mm.parser.node.OWLAllValuesFromRestrictionNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLExactCardinalityRestrictionNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLEnumeratedClassNode;
import org.mm.parser.node.OWLHasValueRestrictionNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIndividualNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLMaxCardinalityRestrictionNode;
import org.mm.parser.node.OWLMinCardinalityRestrictionNode;
import org.mm.parser.node.OWLNamedClassNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSomeValuesFromClassNode;
import org.mm.parser.node.OWLSomeValuesFromDataTypeNode;
import org.mm.parser.node.OWLSomeValuesFromRestrictionNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.PrefixNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SameAsNode;
import org.mm.parser.node.ShiftSettingNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.StringLiteralNode;
import org.mm.parser.node.StringOrReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.TypesNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;

import java.util.Optional;

/**
 * This renderer simply produces the standard presentation syntax rendering of the supplied entities. Subclasses will specialize and perform custom actions for
 * individual entities.
 */
public class TextRenderer implements Renderer, MappingMasterParserConstants
{
	public Optional<TextRendering> renderExpression(ExpressionNode expressionNode) throws RendererException
	{
		if (expressionNode.hasMMDirective())
			return renderMMDirective(expressionNode.getMMDirectiveNode());
		else if (expressionNode.hasMMExpression())
			return renderMMExpression(expressionNode.getMMExpressionNode());
		else
			throw new RendererException("unknown expression type " + expressionNode);
	}

	public Optional<TextRendering> renderMMDirective(MMDirectiveNode mmDirectiveNode) throws RendererException
	{
		if (mmDirectiveNode.hasDefaultValueEncoding())
			return renderMMDefaultValueEncoding(mmDirectiveNode.getDefaultValueEncodingNode());
		else if (mmDirectiveNode.hasDefaultReferenceType())
			return renderMMDefaultReferenceType(mmDirectiveNode.getDefaultReferenceTypeNode());
		else if (mmDirectiveNode.hasDefaultPropertyValueType())
			return renderMMDefaultPropertyValueType(mmDirectiveNode.getDefaultPropertyValueTypeNode());
		else
			throw new RendererException("unknown directive " + mmDirectiveNode);
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

	public Optional<TextRendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode)
			throws RendererException
	{
		TextRendering rendering = new TextRendering("Class: " + owlClassDeclarationNode.getOWLNamedClassNode().toString());
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
				Optional<TextRendering> equivalentToRendering = renderOWLEquivalentTo(equivalentTo);
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
			Optional<TextRendering> typesRendering = renderTypes(owlIndividualDeclarationNode.getTypeNodes());

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
			Optional<TextRendering> sameAsRendering = renderSameAs(owlIndividualDeclarationNode.getSameAsNode());
			if (sameAsRendering.isPresent())
				rendering.addText(sameAsRendering.get().getTextRendering());
		}

		if (owlIndividualDeclarationNode.hasDifferentFrom()) {
			Optional<TextRendering> differentFromRendering = renderDifferentFrom(
					owlIndividualDeclarationNode.getDifferentFromNode());
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

	public Optional<TextRendering> renderOWLPropertyAssertionObject(
			OWLPropertyAssertionObjectNode owlPropertyAssertionObjectNode) throws RendererException
	{
		if (owlPropertyAssertionObjectNode.isReference())
			return renderReference(owlPropertyAssertionObjectNode.getReferenceNode());
		else if (owlPropertyAssertionObjectNode.isName())
			return renderName(owlPropertyAssertionObjectNode.getNameNode());
		else if (owlPropertyAssertionObjectNode.isLiteral())
			return renderLiteral(owlPropertyAssertionObjectNode.getLiteralNode());
		else
			throw new RendererException("unknown property value node " + owlPropertyAssertionObjectNode);
	}

	public Optional<TextRendering> renderOWLClassExpression(OWLClassExpressionNode owlClassExpressionNode)
			throws RendererException
	{
		return renderOWLUnionClass(owlClassExpressionNode.getOWLUnionClassNode());
	}

	public Optional<TextRendering> renderOWLUnionClass(OWLUnionClassNode owlUnionClassNode) throws RendererException
	{
		if (owlUnionClassNode.getOWLIntersectionClasseNodes().size() == 1) {
			Optional<TextRendering> intersectionRendering = renderOWLIntersectionClass(
					owlUnionClassNode.getOWLIntersectionClasseNodes().get(0));

			return intersectionRendering;
		} else {
			TextRendering rendering = new TextRendering();
			boolean isFirst = true;

			for (OWLIntersectionClassNode owlIntersectionClassNode : owlUnionClassNode.getOWLIntersectionClasseNodes()) {
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

	public Optional<TextRendering> renderOWLIntersectionClass(OWLIntersectionClassNode owlIntersectionClassNode)
			throws RendererException
	{
		if (owlIntersectionClassNode.getOWLClassExpressionNodes().size() == 1) {
			Optional<TextRendering> classesOrRestrictionsRendering = renderOWLClassOrRestriction(
					owlIntersectionClassNode.getOWLClassExpressionNodes().get(0));

			return classesOrRestrictionsRendering;
		} else {
			TextRendering rendering = new TextRendering();
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassOrRestriction : owlIntersectionClassNode.getOWLClassExpressionNodes()) {
				Optional<TextRendering> classesOrRestrictionsRendering = renderOWLClassOrRestriction(owlClassOrRestriction);

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

	public Optional<TextRendering> renderOWLClassOrRestriction(OWLClassExpressionNode classOrRestrictionNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		if (classOrRestrictionNode.hasOWLEnumeratedClass()) {
			Optional<TextRendering> enumeratedClassRendering = renderOWLEnumeratedClass(
					classOrRestrictionNode.getOWLEnumeratedClassNode());
			if (enumeratedClassRendering.isPresent())
				textRendering.append(enumeratedClassRendering.get().getTextRendering());
		} else if (classOrRestrictionNode.hasOWLUnionClass()) {
			Optional<TextRendering> unionClassRendering = renderOWLUnionClass(classOrRestrictionNode.getOWLUnionClassNode());
			if (unionClassRendering.isPresent())
				textRendering.append(unionClassRendering.get().getTextRendering());
		} else if (classOrRestrictionNode.hasOWLRestriction()) {
			Optional<TextRendering> restrictionRendering = renderOWLRestriction(
					classOrRestrictionNode.getOWLRestrictionNode());
			if (restrictionRendering.isPresent())
				textRendering.append(restrictionRendering.get().getTextRendering());
		} else if (classOrRestrictionNode.hasOWLNamedClass()) {
			Optional<TextRendering> namedClassRendering = renderOWLClass(classOrRestrictionNode.getOWLNamedClassNode());
			if (namedClassRendering.isPresent())
				textRendering.append(namedClassRendering.get().getTextRendering());
		} else
			throw new RendererException("unexpected OWLClassOrRestriction node " + classOrRestrictionNode);

		if (textRendering.length() == 0)
			if (classOrRestrictionNode.getIsNegated())
				textRendering.append("NOT " + textRendering);

		return textRendering.length() != 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
	}

	public Optional<TextRendering> renderOWLEnumeratedClass(OWLEnumeratedClassNode owlEnumeratedClassNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		if (owlEnumeratedClassNode.getOWLIndividualNodes().size() == 1) {
			Optional<TextRendering> individualRendering = renderOWLNamedIndividual(
					owlEnumeratedClassNode.getOWLIndividualNodes().get(0));

			if (!individualRendering.isPresent())
				return Optional.empty();
			else
				textRendering.append(individualRendering.get().getTextRendering());
		} else {
			boolean isFirst = true;

			textRendering.append("{");
			for (OWLIndividualNode owlIndividualNode : owlEnumeratedClassNode.getOWLIndividualNodes()) {
				if (!isFirst)
					textRendering.append(" ");
				Optional<TextRendering> individualRendering = renderOWLNamedIndividual(owlIndividualNode);

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

	public Optional<TextRendering> renderOWLRestriction(OWLRestrictionNode owlRestrictionNode) throws RendererException
	{
		Optional<TextRendering> propertyRendering = renderOWLProperty(owlRestrictionNode.getOWLPropertyNode());
		Optional<TextRendering> restrictionRendering;

		if (owlRestrictionNode.hasOWLMinCardinality())
			restrictionRendering = renderOWLMinCardinality(owlRestrictionNode.getOWLMinCardinalityNode());
		else if (owlRestrictionNode.hasOWLMaxCardinality())
			restrictionRendering = renderOWLMaxCardinality(owlRestrictionNode.getOWLMaxCardinalityNode());
		else if (owlRestrictionNode.hasOWLCardinality())
			restrictionRendering = renderOWLCardinality(owlRestrictionNode.getOWLCardinalityNode());
		else if (owlRestrictionNode.hasOWLHasValue())
			restrictionRendering = renderOWLHasValue(owlRestrictionNode.getOWLHasValueNode());
		else if (owlRestrictionNode.hasOWLAllValuesFrom())
			restrictionRendering = renderOWLAllValuesFrom(owlRestrictionNode.getOWLAllValuesFromNode());
		else if (owlRestrictionNode.hasOWLSomeValuesFrom())
			restrictionRendering = renderOWLSomeValuesFrom(owlRestrictionNode.getOWLSomeValuesFromNode());
		else
			throw new RendererException("unkown OWLRestriction " + owlRestrictionNode);

		if (propertyRendering.isPresent() && restrictionRendering.isPresent())
			return Optional.of(new TextRendering(
					"(" + propertyRendering.get().getTextRendering() + " " + restrictionRendering.get().getTextRendering()
							+ ")"));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLMaxCardinality(OWLMaxCardinalityRestrictionNode owlMaxCardinalityRestrictionNode)
			throws RendererException
	{
		String textRendering = "" + owlMaxCardinalityRestrictionNode.getCardinality();

		if (textRendering.length() != 0)
			return Optional.of(new TextRendering("MAX " + textRendering));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLMinCardinality(OWLMinCardinalityRestrictionNode owlMinCardinalityRestrictionNode)
			throws RendererException
	{
		String textRendering = "" + owlMinCardinalityRestrictionNode.getCardinality();

		if (textRendering.length() != 0)
			return Optional.of(new TextRendering("MIN " + textRendering));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLCardinality(OWLExactCardinalityRestrictionNode owlExactCardinalityRestrictionNode) throws RendererException
	{
		String textRendering = "" + owlExactCardinalityRestrictionNode.getCardinality();

		if (textRendering.length() != 0)
			textRendering += "EXACTLY " + textRendering;

		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering));
	}

	public Optional<TextRendering> renderOWLHasValue(OWLHasValueRestrictionNode owlHasValueRestrictionNode) throws RendererException
	{
		Optional<TextRendering> propertyValueRendering = renderOWLPropertyAssertionObject(
				owlHasValueRestrictionNode.getOWLPropertyAssertionObjectNode());

		if (propertyValueRendering.isPresent())
			return Optional.of(new TextRendering("VALUE " + propertyValueRendering.get().getTextRendering()));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLAllValuesFrom(OWLAllValuesFromRestrictionNode owlAllValuesFromRestrictionNode)
			throws RendererException
	{
		if (owlAllValuesFromRestrictionNode.hasOWLAllValuesFromDataType())
			return renderOWLAllValuesFromDataType(owlAllValuesFromRestrictionNode.getOWLAllValuesFromDataTypeNode());
		else if (owlAllValuesFromRestrictionNode.hasOWLAllValuesFromClass())
			return renderOWLAllValuesFromClass(owlAllValuesFromRestrictionNode.getOWLAllValuesFromClassNode());
		else
			throw new RendererException("unknown OWLAllValuesFrom node " + owlAllValuesFromRestrictionNode);
	}

	public Optional<TextRendering> renderOWLSomeValuesFrom(OWLSomeValuesFromRestrictionNode owlSomeValuesFromRestrictionNode)
			throws RendererException
	{
		if (owlSomeValuesFromRestrictionNode.hasOWLSomeValuesFromDataType())
			return renderOWLSomeValuesFromDataType(owlSomeValuesFromRestrictionNode.getOWLSomeValuesFromDataTypeNode());
		else if (owlSomeValuesFromRestrictionNode.hasOWLSomeValuesFromClass())
			return renderOWLSomeValuesFromClass(owlSomeValuesFromRestrictionNode.getOWLSomeValuesFromClassNode());
		else
			throw new RendererException("unknown OWLSomeValuesFrom node " + owlSomeValuesFromRestrictionNode);
	}

	public Optional<TextRendering> renderOWLClass(OWLNamedClassNode owlClassNode) throws RendererException
	{
		if (owlClassNode.isReference())
			return renderReference(owlClassNode.getReferenceNode());
		else if (owlClassNode.isName())
			return renderName(owlClassNode.getNameNode());
		else
			throw new RendererException("unknown OWLClass node " + owlClassNode);
	}

	public Optional<TextRendering> renderOWLProperty(OWLPropertyNode propertyNode) throws RendererException
	{
		if (propertyNode.isReference())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.isName())
			return renderName(propertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + propertyNode);
	}

	public Optional<TextRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode) throws RendererException
	{
		if (propertyNode.isReference())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.isName())
			return renderName(propertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + propertyNode);
	}

	public Optional<TextRendering> renderOWLDataProperty(OWLPropertyNode propertyNode) throws RendererException
	{
		if (propertyNode.isReference())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.isName())
			return renderName(propertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + propertyNode);
	}

	public Optional<TextRendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode) throws RendererException
	{
		if (propertyNode.isReference())
			return renderReference(propertyNode.getReferenceNode());
		else if (propertyNode.isName())
			return renderName(propertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + propertyNode);
	}

	public Optional<TextRendering> renderOWLNamedIndividual(OWLIndividualNode namedIndividualNode)
			throws RendererException
	{
		if (namedIndividualNode.isReference())
			return renderReference(namedIndividualNode.getReferenceNode());
		else if (namedIndividualNode.isName())
			return renderName(namedIndividualNode.getNameNode());
		else
			throw new RendererException("unknown OWLIndividual node " + namedIndividualNode);
	}

	public Optional<TextRendering> renderName(NameNode nameNode) throws RendererException
	{
		String name = nameNode.isQuoted() ? "'" + nameNode.getName() + "'" : nameNode.getName();

		return name.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(name));
	}

	public Optional<TextRendering> renderPrefix(PrefixNode prefixNode) throws RendererException
	{
		String prefix = prefixNode.toString();

		return prefix.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(prefix));
	}

	public Optional<TextRendering> renderLanguage(LanguageNode languageNode) throws RendererException
	{
		String language = languageNode.toString();

		return language.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(language));
	}

	public Optional<TextRendering> renderNamespace(NamespaceNode namespaceNode) throws RendererException
	{
		return Optional
				.of(new TextRendering(ParserUtil.getTokenName(MM_NAMESPACE) + "=\"" + namespaceNode.getNamespace() + "\""));
	}

	public Optional<TextRendering> renderOWLAllValuesFromDataType(
			OWLAllValuesFromDataTypeNode owlAllValuesFromDataTypeNode) throws RendererException
	{
		String datatypeName = owlAllValuesFromDataTypeNode.getDataTypeName();

		if (!datatypeName.equals(""))
			return Optional.of(new TextRendering("ONLY " + datatypeName));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLAllValuesFromClass(OWLAllValuesFromClassNode owlAllValuesFromClassNode)
			throws RendererException
	{
		Optional<TextRendering> classRendering;

		if (owlAllValuesFromClassNode.hasOWLNamedClass())
			classRendering = renderOWLClass(owlAllValuesFromClassNode.getOWLNamedClassNode());
		else if (owlAllValuesFromClassNode.hasOWLClassExpression())
			classRendering = renderOWLClassExpression(owlAllValuesFromClassNode.getOWLClassExpressionNode());
		else
			throw new RendererException("unknown OWLAllValuesFromClass node " + owlAllValuesFromClassNode);

		if (classRendering.isPresent())
			return Optional.of(new TextRendering("ONLY " + classRendering.get().getTextRendering()));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLSomeValuesFromDataType(
			OWLSomeValuesFromDataTypeNode owlSomeValuesFromDataTypeNode) throws RendererException
	{
		String datatypeName = owlSomeValuesFromDataTypeNode.getDataTypeName();

		if (!datatypeName.equals(""))
			return Optional.of(new TextRendering("SOME " + datatypeName));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLSomeValuesFromClass(OWLSomeValuesFromClassNode owlSomeValuesFromClassNode)
			throws RendererException
	{
		Optional<TextRendering> classRendering;

		if (owlSomeValuesFromClassNode.hasOWLNamedClass())
			classRendering = renderOWLClass(owlSomeValuesFromClassNode.getOWLNamedClassNode());
		else if (owlSomeValuesFromClassNode.hasOWLClassExpression())
			classRendering = renderOWLClassExpression(owlSomeValuesFromClassNode.getOWLClassExpressionNode());
		else
			throw new RendererException("unknown OWLSomeValuesFromClass node " + owlSomeValuesFromClassNode);

		if (classRendering.isPresent())
			return Optional.of(new TextRendering("SOME " + classRendering.get().getTextRendering()));
		else
			return Optional.empty();
	}

	public Optional<TextRendering> renderOWLEquivalentTo(OWLClassEquivalentToNode owlClassEquivalentToNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		textRendering.append(" EquivalentTo: ");

		if (owlClassEquivalentToNode.getClassExpressionNodes().size() == 1) {
			Optional<TextRendering> classExpressionRendering = renderOWLClassExpression(
					owlClassEquivalentToNode.getClassExpressionNodes().get(0));
			if (!classExpressionRendering.isPresent())
				return classExpressionRendering;
			else
				textRendering.append(classExpressionRendering.get().getTextRendering());
		} else {
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassExpressionNode : owlClassEquivalentToNode.getClassExpressionNodes()) {
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

	public Optional<TextRendering> renderOWLSubclassOf(OWLSubclassOfNode owlSubclassOfNode) throws RendererException
	{
		TextRendering rendering = new TextRendering();
		rendering.addText(" SubClassOf: ");

		if (owlSubclassOfNode.getClassExpressionNodes().size() == 1) {
			Optional<TextRendering> classExpressionRendering = renderOWLClassExpression(
					owlSubclassOfNode.getClassExpressionNodes().get(0));
			if (!classExpressionRendering.isPresent())
				return Optional.empty();
			else
				rendering.addText(classExpressionRendering.get().getTextRendering());
		} else {
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassExpressionNode : owlSubclassOfNode.getClassExpressionNodes()) {
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

	public Optional<TextRendering> renderLiteral(LiteralNode literalNode) throws RendererException
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

	public Optional<TextRendering> renderReference(ReferenceNode referenceNode) throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();
		boolean hasExplicitOptions = referenceNode.hasExplicitOptions();
		boolean atLeastOneOptionProcessed = false;

		textRendering.append(referenceNode.getSourceSpecificationNode().toString());

		if (hasExplicitOptions)
			textRendering.append("(");

		if (referenceNode.hasExplicitlySpecifiedReferenceType()) {
			textRendering.append(referenceNode.getReferenceTypeNode().toString());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedPrefix()) {
			Optional<TextRendering> prefixRendering = renderPrefix(referenceNode.getPrefixNode());
			if (prefixRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				else
					atLeastOneOptionProcessed = true;
				textRendering.append(prefixRendering.get().getTextRendering());
			}
		}

		if (referenceNode.hasExplicitlySpecifiedNamespace()) {
			Optional<TextRendering> namespaceRendering = renderNamespace(referenceNode.getNamespaceNode());
			if (namespaceRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				else
					atLeastOneOptionProcessed = true;
				textRendering.append(namespaceRendering.get().getTextRendering());
			}
		}

		if (referenceNode.hasValueExtractionFunction()) {
			Optional<TextRendering> valueExtractionFunctionRendering = renderValueExtractionFunction(
					referenceNode.getValueExtractionFunctionNode());
			if (valueExtractionFunctionRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				else
					atLeastOneOptionProcessed = true;
				textRendering.append(valueExtractionFunctionRendering.get().getTextRendering());
			}
		}

		if (referenceNode.hasExplicitlySpecifiedValueEncodings()) {
			boolean isFirst = true;
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			for (ValueEncodingNode valueEncodingNode : referenceNode.getValueEncodingNodes()) {
				Optional<TextRendering> valueEncodingRendering = renderValueEncoding(valueEncodingNode);
				if (valueEncodingRendering.isPresent()) {
					if (!isFirst)
						textRendering.append(" ");
					textRendering.append(valueEncodingRendering.get().getTextRendering());
					isFirst = false;
				}
			}
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultLocationValue()) {
			Optional<TextRendering> defaultLocationValueRendering = renderDefaultLocationValue(
					referenceNode.getDefaultLocationValueNode());
			if (defaultLocationValueRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(defaultLocationValueRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultDataValue()) {
			Optional<TextRendering> defaultDataValueRendering = renderDefaultDataValue(
					referenceNode.getDefaultDataValueNode());
			if (defaultDataValueRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(defaultDataValueRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultID()) {
			Optional<TextRendering> defaultIDRendering = renderDefaultID(referenceNode.getDefaultRDFIDNode());
			if (defaultIDRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(defaultIDRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultLabel()) {
			Optional<TextRendering> defaultLabelRendering = renderDefaultLabel(referenceNode.getDefaultRDFSLabelNode());
			if (defaultLabelRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(defaultLabelRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedLanguage()) {
			Optional<TextRendering> languageRendering = renderLanguage(referenceNode.getLanguageNode());
			if (languageRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(languageRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedPrefix()) {
			if (atLeastOneOptionProcessed)
				textRendering.append(" ");
			Optional<TextRendering> prefixNodeRendering = renderPrefix(referenceNode.getPrefixNode());
			if (prefixNodeRendering.isPresent()) {
				textRendering.append(prefixNodeRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedNamespace()) {
			Optional<TextRendering> namespaceRendering = renderNamespace(referenceNode.getNamespaceNode());
			if (namespaceRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(namespaceRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyLocationDirective()) {
			Optional<TextRendering> emptyLocationSettingRendering = renderEmptyLocationSetting(
					referenceNode.getEmptyLocationSettingNode());
			if (emptyLocationSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(emptyLocationSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyDataValueDirective()) {
			Optional<TextRendering> emptyDataValueSettingRendering = renderEmptyDataValueSetting(
					referenceNode.getEmptyDataValueSettingNode());
			if (emptyDataValueSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(emptyDataValueSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyRDFIDDirective()) {
			Optional<TextRendering> emptyRDFIFSettingRendering = renderEmptyRDFIDSetting(
					referenceNode.getEmptyRDFIDSettingNode());
			if (emptyRDFIFSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(emptyRDFIFSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyRDFSLabelDirective()) {
			Optional<TextRendering> emptyRDFSLabelSettingRendering = renderEmptyRDFSLabelSetting(
					referenceNode.getEmptyRDFSLabelSettingNode());
			if (emptyRDFSLabelSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(emptyRDFSLabelSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedShiftDirective()) {
			Optional<TextRendering> shiftSettingRendering = renderShiftSetting(referenceNode.getShiftSettingNode());
			if (shiftSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(shiftSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedTypes()) {
			Optional<TextRendering> typesRendering = renderTypes(referenceNode.getTypesNode());
			if (typesRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(typesRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedIfExistsDirective()) {
			Optional<TextRendering> ifExistsRendering = renderIfExistsDirective(referenceNode.getIfExistsDirectiveNode());
			if (ifExistsRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(ifExistsRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedIfNotExistsDirective()) {
			Optional<TextRendering> ifExistsRendering = renderIfNotExistsDirective(
					referenceNode.getIfNotExistsDirectiveNode());
			if (ifExistsRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(ifExistsRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (hasExplicitOptions)
			textRendering.append(")");

		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new TextRendering(textRendering.toString()));
	}

	public Optional<TextRendering> renderReferenceType(ReferenceTypeNode referenceTypeNode) throws RendererException
	{
		return Optional.of(new TextRendering(referenceTypeNode.getReferenceType().getTypeName()));
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
		else if (valueSpecificationItemNode.hasReference())
			return renderReference(valueSpecificationItemNode.getReferenceNode());
		else if (valueSpecificationItemNode.hasValueExtractionFunction())
			return renderValueExtractionFunction(valueSpecificationItemNode.getValueExtractionFunctionNode());
		else if (valueSpecificationItemNode.hasCapturingExpression())
			return Optional.of(new TextRendering("[\"" + valueSpecificationItemNode.getCapturingExpression() + "\"]"));
		else
			throw new RendererException("unknown ValueSpecificationItem node " + valueSpecificationItemNode);
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

	public Optional<TextRendering> renderSameAs(SameAsNode sameAsNode) throws RendererException
	{
		TextRendering rendering = new TextRendering(" SameAs: ");
		boolean isFirst = true;

		for (OWLIndividualNode owlIndividualNode : sameAsNode.getIndividualNodes()) {
			Optional<TextRendering> individualRendering = renderOWLNamedIndividual(owlIndividualNode);

			if (individualRendering.isPresent()) {
				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(individualRendering.get().getTextRendering());
				isFirst = false;
			}
		}
		return Optional.of(rendering);
	}

	public Optional<TextRendering> renderDifferentFrom(DifferentFromNode differentFromNode) throws RendererException
	{
		TextRendering rendering = new TextRendering(" DifferentFrom: ");
		boolean isFirst = true;

		for (OWLIndividualNode owlIndividualNode : differentFromNode.getIndividualNodes()) {
			Optional<TextRendering> individualRendering = renderOWLNamedIndividual(owlIndividualNode);

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

			for (StringOrReferenceNode stringOrReferenceNode : valueExtractionFunctionNode.getArgumentNodes()) {
				Optional<TextRendering> stringOrReferenceRendering = renderStringOrReference(stringOrReferenceNode);
				if (stringOrReferenceRendering.isPresent()) {
					if (!isFirst)
						rendering.addText(" ");
					rendering.addText(stringOrReferenceRendering.get().getTextRendering());
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

	public Optional<TextRendering> renderStringOrReference(StringOrReferenceNode stringOrReferenceNode)
			throws RendererException
	{
		if (stringOrReferenceNode.isString())
			return renderStringLiteral(stringOrReferenceNode.getStringLiteralNode());
		else if (stringOrReferenceNode.isReference())
			return renderReference(stringOrReferenceNode.getReferenceNode());
		else
			throw new RendererException("unknown StringOrReference node " + stringOrReferenceNode);
	}

	public Optional<TextRendering> renderMMDefaultReferenceType(MMDefaultReferenceTypeNode mmDefaultReferenceTypeNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(mmDefaultReferenceTypeNode.toString()));
	}

	public Optional<TextRendering> renderMMDefaultValueEncoding(MMDefaultValueEncodingNode mmDefaultValueEncodingNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(mmDefaultValueEncodingNode.toString()));
	}

	public Optional<TextRendering> renderMMDefaultPropertyValueType(
			MMDefaultPropertyValueTypeNode mmDefaultPropertyValueTypeNode) throws RendererException
	{
		return Optional.of(new TextRendering(mmDefaultPropertyValueTypeNode.toString()));
	}

	public Optional<TextRendering> renderMMDefaultPropertyType(MMDefaultPropertyTypeNode mmDefaultPropertyTypeNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(mmDefaultPropertyTypeNode.toString()));
	}

	public Optional<TextRendering> renderMMDefaultDatatypePropertyValueType(
			MMDefaultDatatypePropertyValueTypeNode mmDefaultDatatypePropertyValueTypeNode) throws RendererException
	{
		return Optional.of(new TextRendering(mmDefaultDatatypePropertyValueTypeNode.toString()));
	}

	public Optional<TextRendering> renderStringLiteral(StringLiteralNode stringLiteralNode) throws RendererException
	{
		return Optional.of(new TextRendering(stringLiteralNode.toString()));
	}

	public Optional<TextRendering> renderShiftSetting(ShiftSettingNode shiftSettingNode) throws RendererException
	{
		return Optional.of(new TextRendering(shiftSettingNode.toString()));
	}

	public Optional<TextRendering> renderEmptyLocationSetting(EmptyLocationSettingNode emptyLocationSettingNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(emptyLocationSettingNode.toString()));
	}

	public Optional<TextRendering> renderEmptyDataValueSetting(EmptyDataValueSettingNode emptyDataValueSettingNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(emptyDataValueSettingNode.toString()));
	}

	public Optional<TextRendering> renderEmptyRDFIDSetting(EmptyRDFIDSettingNode emptyRDFIDSettingNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(emptyRDFIDSettingNode.toString()));
	}

	public Optional<TextRendering> renderEmptyRDFSLabelSetting(EmptyRDFSLabelSettingNode emptyRDFSLabelSettingNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(emptyRDFSLabelSettingNode.toString()));
	}

	public Optional<TextRendering> renderIfExistsDirective(IfExistsDirectiveNode ifExistsDirectiveNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(ifExistsDirectiveNode.toString()));
	}

	public Optional<TextRendering> renderIfNotExistsDirective(IfNotExistsDirectiveNode ifNotExistsDirectiveNode)
			throws RendererException
	{
		return Optional.of(new TextRendering(ifNotExistsDirectiveNode.toString()));
	}
}

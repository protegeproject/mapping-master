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
import org.mm.parser.node.EntityTypeNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.IfExistsDirectiveNode;
import org.mm.parser.node.IfNotExistsDirectiveNode;
import org.mm.parser.node.LanguageNode;
import org.mm.parser.node.LiteralNode;
import org.mm.parser.node.MMDefaultDatatypePropertyValueTypeNode;
import org.mm.parser.node.MMDefaultEntityTypeNode;
import org.mm.parser.node.MMDefaultPropertyTypeNode;
import org.mm.parser.node.MMDefaultPropertyValueTypeNode;
import org.mm.parser.node.MMDefaultValueEncodingNode;
import org.mm.parser.node.MMDirectiveNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.NamespaceNode;
import org.mm.parser.node.OWLAllValuesFromClassNode;
import org.mm.parser.node.OWLAllValuesFromDataTypeNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLCardinalityNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLEnumeratedClassNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIndividualNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedClassNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLPropertyValueNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSomeValuesFromClassNode;
import org.mm.parser.node.OWLSomeValuesFromDataTypeNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
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
public class DefaultRenderer implements Renderer, MappingMasterParserConstants
{
	public Optional<Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException
	{
		if (expressionNode.hasMMDirective())
			return renderMMDirective(expressionNode.getMMDirectiveNode());
		else if (expressionNode.hasMMExpression())
			return renderMMExpression(expressionNode.getMMExpressionNode());
		else
			throw new RendererException("unknown expression type " + expressionNode);
	}

	public Optional<Rendering> renderMMDirective(MMDirectiveNode mmDirectiveNode) throws RendererException
	{
		if (mmDirectiveNode.hasDefaultValueEncoding())
			return renderMMDefaultValueEncoding(mmDirectiveNode.getDefaultValueEncodingNode());
		else if (mmDirectiveNode.hasDefaultEntityType())
			return renderMMDefaultEntityType(mmDirectiveNode.getDefaultEntityTypeNode());
		else if (mmDirectiveNode.hasDefaultPropertyValueType())
			return renderMMDefaultPropertyValueType(mmDirectiveNode.getDefaultPropertyValueTypeNode());
		else
			throw new RendererException("unknown directive " + mmDirectiveNode);
	}

	public Optional<Rendering> renderMMExpression(MMExpressionNode mmExpressionNode) throws RendererException
	{
		if (mmExpressionNode.hasOWLClassDeclaration())
			return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
		else if (mmExpressionNode.hasOWLIndividualDeclaration())
			return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
		else
			throw new RendererException("unknown expression: " + mmExpressionNode);
	}

	public Optional<Rendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode)
			throws RendererException
	{
		Rendering rendering = new Rendering("Class: " + owlClassDeclarationNode.getOWLNamedClassNode().toString());
		boolean isFirst = true;

		if (owlClassDeclarationNode.hasSubclassOf()) {
			rendering.addText(" SubclassOf: ");
			for (OWLSubclassOfNode subclassOf : owlClassDeclarationNode.getSubclassOfNodes()) {
				Optional<Rendering> subclassOfRendering = renderOWLSubclassOf(subclassOf);
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
				Optional<Rendering> equivalentToRendering = renderOWLEquivalentTo(equivalentTo);
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
				Optional<Rendering> factRendering = renderAnnotationFact(annotationFactNode);

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

	public Optional<Rendering> renderOWLIndividualDeclaration(OWLIndividualDeclarationNode owlIndividualDeclarationNode)
			throws RendererException
	{
		Rendering rendering = new Rendering();
		boolean isFirst = true;
		Optional<Rendering> individualRendering = renderOWLIndividual(owlIndividualDeclarationNode.getOWLIndividualNode());

		if (!individualRendering.isPresent())
			return Optional.empty();

		rendering.addText("Individual: ");

		rendering.addText(individualRendering.get().getTextRendering());

		if (owlIndividualDeclarationNode.hasFacts()) {
			rendering.addText(" Facts: ");

			for (FactNode fact : owlIndividualDeclarationNode.getFactNodes()) {
				Optional<Rendering> factRendering = renderFact(fact);

				if (!factRendering.isPresent())
					continue;

				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(factRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		if (owlIndividualDeclarationNode.hasTypes()) {
			Optional<Rendering> typesRendering = renderTypes(owlIndividualDeclarationNode.getTypeNodes());

			if (typesRendering.isPresent())
				rendering.addText(" Types: " + typesRendering.get().getTextRendering());
		}

		isFirst = true;
		if (owlIndividualDeclarationNode.hasAnnotations()) {
			rendering.addText(" Annotations:");

			for (AnnotationFactNode factNode : owlIndividualDeclarationNode.getAnnotationNodes()) {
				Optional<Rendering> factRendering = renderAnnotationFact(factNode);

				if (!factRendering.isPresent())
					continue;

				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(factRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		if (owlIndividualDeclarationNode.hasSameAs()) {
			Optional<Rendering> sameAsRendering = renderSameAs(owlIndividualDeclarationNode.getSameAsNode());
			if (sameAsRendering.isPresent())
				rendering.addText(sameAsRendering.get().getTextRendering());
		}

		if (owlIndividualDeclarationNode.hasDifferentFrom()) {
			Optional<Rendering> differentFromRendering = renderDifferentFrom(
					owlIndividualDeclarationNode.getDifferentFromNode());
			if (differentFromRendering.isPresent())
				rendering.addText(differentFromRendering.get().getTextRendering());
		}

		return Optional.of(rendering);
	}

	public Optional<Rendering> renderFact(FactNode factNode) throws RendererException
	{
		Optional<Rendering> propertyRendering = renderOWLProperty(factNode.getOWLPropertyNode());
		Optional<Rendering> propertyValueRendering = renderOWLPropertyValue(factNode.getOWLPropertyValueNode());

		if (propertyRendering.isPresent() && propertyValueRendering.isPresent()) {
			String textRendering =
					propertyRendering.get().getTextRendering() + " " + propertyValueRendering.get().getTextRendering();
			return Optional.of(new Rendering(textRendering));
		} else
			return Optional.empty();
	}

	public Optional<Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException
	{
		Optional<Rendering> propertyRendering = renderOWLProperty(annotationFactNode.getOWLPropertyNode());
		Optional<Rendering> propertyValueRendering = renderOWLPropertyValue(annotationFactNode.getOWLPropertyValueNode());

		if (propertyRendering.isPresent() && propertyValueRendering.isPresent()) {
			String textRendering =
					propertyRendering.get().getTextRendering() + " " + propertyValueRendering.get().getTextRendering();
			return Optional.of(new Rendering(textRendering));
		} else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLPropertyValue(OWLPropertyValueNode owlPropertyValueNode) throws RendererException
	{
		if (owlPropertyValueNode.isReference())
			return renderReference(owlPropertyValueNode.getReferenceNode());
		else if (owlPropertyValueNode.isName())
			return renderName(owlPropertyValueNode.getNameNode());
		else if (owlPropertyValueNode.isLiteral())
			return renderLiteral(owlPropertyValueNode.getLiteralNode());
		else
			throw new RendererException("unknown property value node " + owlPropertyValueNode);
	}

	public Optional<Rendering> renderOWLClassExpression(OWLClassExpressionNode owlClassExpressionNode)
			throws RendererException
	{
		return renderOWLUnionClass(owlClassExpressionNode.getOWLUnionClassNode());
	}

	public Optional<Rendering> renderOWLUnionClass(OWLUnionClassNode owlUnionClassNode) throws RendererException
	{
		if (owlUnionClassNode.getOWLIntersectionClasseNodes().size() == 1) {
			Optional<Rendering> intersectionRendering = renderOWLIntersectionClass(
					owlUnionClassNode.getOWLIntersectionClasseNodes().get(0));

			return intersectionRendering;
		} else {
			Rendering rendering = new Rendering();
			boolean isFirst = true;

			for (OWLIntersectionClassNode owlIntersectionClassNode : owlUnionClassNode.getOWLIntersectionClasseNodes()) {
				Optional<Rendering> intersectionRendering = renderOWLIntersectionClass(owlIntersectionClassNode);

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

	public Optional<Rendering> renderOWLIntersectionClass(OWLIntersectionClassNode owlIntersectionClassNode)
			throws RendererException
	{
		if (owlIntersectionClassNode.getOWLClassesOrRestrictionNodes().size() == 1) {
			Optional<Rendering> classesOrRestrictionsRendering = renderOWLClassOrRestriction(
					owlIntersectionClassNode.getOWLClassesOrRestrictionNodes().get(0));

			return classesOrRestrictionsRendering;
		} else {
			Rendering rendering = new Rendering();
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassOrRestriction : owlIntersectionClassNode.getOWLClassesOrRestrictionNodes()) {
				Optional<Rendering> classesOrRestrictionsRendering = renderOWLClassOrRestriction(owlClassOrRestriction);

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

	public Optional<Rendering> renderOWLClassOrRestriction(OWLClassExpressionNode classOrRestrictionNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		if (classOrRestrictionNode.hasOWLEnumeratedClass()) {
			Optional<Rendering> enumeratedClassRendering = renderOWLEnumeratedClass(
					classOrRestrictionNode.getOWLEnumeratedClassNode());
			if (enumeratedClassRendering.isPresent())
				textRendering.append(enumeratedClassRendering.get().getTextRendering());
		} else if (classOrRestrictionNode.hasOWLUnionClass()) {
			Optional<Rendering> unionClassRendering = renderOWLUnionClass(classOrRestrictionNode.getOWLUnionClassNode());
			if (unionClassRendering.isPresent())
				textRendering.append(unionClassRendering.get().getTextRendering());
		} else if (classOrRestrictionNode.hasOWLRestriction()) {
			Optional<Rendering> restrictionRendering = renderOWLRestriction(classOrRestrictionNode.getOWLRestrictionNode());
			if (restrictionRendering.isPresent())
				textRendering.append(restrictionRendering.get().getTextRendering());
		} else if (classOrRestrictionNode.hasOWLNamedClass()) {
			Optional<Rendering> namedClassRendering = renderOWLNamedClass(classOrRestrictionNode.getOWLNamedClassNode());
			if (namedClassRendering.isPresent())
				textRendering.append(namedClassRendering.get().getTextRendering());
		} else
			throw new RendererException("unexpected OWLClassOrRestriction node " + classOrRestrictionNode);

		if (textRendering.length() == 0)
			if (classOrRestrictionNode.getIsNegated())
				textRendering.append("NOT " + textRendering);

		return textRendering.length() != 0 ? Optional.empty() : Optional.of(new Rendering(textRendering.toString()));
	}

	public Optional<Rendering> renderOWLEnumeratedClass(OWLEnumeratedClassNode owlEnumeratedClassNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		if (owlEnumeratedClassNode.getOWLIndividualNodes().size() == 1) {
			Optional<Rendering> individualRendering = renderOWLIndividual(
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
				Optional<Rendering> individualRendering = renderOWLIndividual(owlIndividualNode);

				if (!individualRendering.isPresent())
					return Optional.empty();
				else {
					textRendering.append(individualRendering.get().getTextRendering());
					isFirst = false;
				}
			}
			textRendering.append("}");
		}
		return textRendering.length() != 0 ? Optional.empty() : Optional.of(new Rendering(textRendering.toString()));
	}

	public Optional<Rendering> renderOWLRestriction(OWLRestrictionNode owlRestrictionNode) throws RendererException
	{
		Optional<Rendering> propertyRendering = renderOWLProperty(owlRestrictionNode.getOWLPropertyNode());
		Optional<Rendering> restrictionRendering;

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
			return Optional.of(new Rendering(
					"(" + propertyRendering.get().getTextRendering() + " " + restrictionRendering.get().getTextRendering()
							+ ")"));
		else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLMaxCardinality(OWLMaxCardinalityNode owlMaxCardinalityNode)
			throws RendererException
	{
		String textRendering = "" + owlMaxCardinalityNode.getCardinality();

		if (textRendering.length() != 0)
			return Optional.of(new Rendering("MAX " + textRendering));
		else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLMinCardinality(OWLMinCardinalityNode owlMinCardinalityNode)
			throws RendererException
	{
		String textRendering = "" + owlMinCardinalityNode.getCardinality();

		if (textRendering.length() != 0)
			return Optional.of(new Rendering("MIN " + textRendering));
		else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLCardinality(OWLCardinalityNode owlCardinalityNode) throws RendererException
	{
		String textRendering = "" + owlCardinalityNode.getCardinality();

		if (textRendering.length() != 0)
			textRendering += "EXACTLY " + textRendering;

		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new Rendering(textRendering));
	}

	public Optional<Rendering> renderOWLHasValue(OWLHasValueNode owlHasValueNode) throws RendererException
	{
		Optional<Rendering> propertyValueRendering = renderOWLPropertyValue(owlHasValueNode.getOWLPropertyValueNode());

		if (propertyValueRendering.isPresent())
			return Optional.of(new Rendering("VALUE " + propertyValueRendering.get().getTextRendering()));
		else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLAllValuesFrom(OWLAllValuesFromNode owlAllValuesFromNode) throws RendererException
	{
		if (owlAllValuesFromNode.hasOWLAllValuesFromDataType())
			return renderOWLAllValuesFromDataType(owlAllValuesFromNode.getOWLAllValuesFromDataTypeNode());
		else if (owlAllValuesFromNode.hasOWLAllValuesFromClass())
			return renderOWLAllValuesFromClass(owlAllValuesFromNode.getOWLAllValuesFromClassNode());
		else
			throw new RendererException("unknown OWLAllValuesFrom node " + owlAllValuesFromNode);
	}

	public Optional<Rendering> renderOWLSomeValuesFrom(OWLSomeValuesFromNode owlSomeValuesFromNode)
			throws RendererException
	{
		if (owlSomeValuesFromNode.hasOWLSomeValuesFromDataType())
			return renderOWLSomeValuesFromDataType(owlSomeValuesFromNode.getOWLSomeValuesFromDataTypeNode());
		else if (owlSomeValuesFromNode.hasOWLSomeValuesFromClass())
			return renderOWLSomeValuesFromClass(owlSomeValuesFromNode.getOWLSomeValuesFromClassNode());
		else
			throw new RendererException("unknown OWLSomeValuesFrom node " + owlSomeValuesFromNode);
	}

	public Optional<Rendering> renderOWLNamedClass(OWLNamedClassNode owlNamedClassNode) throws RendererException
	{
		if (owlNamedClassNode.isReference())
			return renderReference(owlNamedClassNode.getReferenceNode());
		else if (owlNamedClassNode.isName())
			return renderName(owlNamedClassNode.getNameNode());
		else
			throw new RendererException("unknown OWLNamedClass node " + owlNamedClassNode);
	}

	public Optional<Rendering> renderOWLProperty(OWLPropertyNode owlPropertyNode) throws RendererException
	{
		if (owlPropertyNode.isReference())
			return renderReference(owlPropertyNode.getReferenceNode());
		else if (owlPropertyNode.isName())
			return renderName(owlPropertyNode.getNameNode());
		else
			throw new RendererException("unknown OWLProperty node " + owlPropertyNode);
	}

	public Optional<Rendering> renderOWLIndividual(OWLIndividualNode owlIndividualNode) throws RendererException
	{
		if (owlIndividualNode.isReference())
			return renderReference(owlIndividualNode.getReferenceNode());
		else if (owlIndividualNode.isName())
			return renderName(owlIndividualNode.getNameNode());
		else
			throw new RendererException("unknown OWLIndividual node " + owlIndividualNode);
	}

	public Optional<Rendering> renderName(NameNode nameNode) throws RendererException
	{
		String name = nameNode.isQuoted() ? "'" + nameNode.getName() + "'" : nameNode.getName();

		return name.length() == 0 ? Optional.empty() : Optional.of(new Rendering(name));
	}

	public Optional<Rendering> renderPrefix(PrefixNode prefixNode) throws RendererException
	{
		String prefix = prefixNode.toString();

		return prefix.length() == 0 ? Optional.empty() : Optional.of(new Rendering(prefix));
	}

	public Optional<Rendering> renderLanguage(LanguageNode languageNode) throws RendererException
	{
		String language = languageNode.toString();

		return language.length() == 0 ? Optional.empty() : Optional.of(new Rendering(language));
	}

	public Optional<Rendering> renderNamespace(NamespaceNode namespaceNode) throws RendererException
	{
		return Optional
				.of(new Rendering(ParserUtil.getTokenName(MM_NAMESPACE) + "=\"" + namespaceNode.getNamespace() + "\""));
	}

	public Optional<Rendering> renderOWLAllValuesFromDataType(OWLAllValuesFromDataTypeNode owlAllValuesFromDataTypeNode)
			throws RendererException
	{
		String datatypeName = owlAllValuesFromDataTypeNode.getDataTypeName();

		if (!datatypeName.equals(""))
			return Optional.of(new Rendering("ONLY " + datatypeName));
		else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLAllValuesFromClass(OWLAllValuesFromClassNode owlAllValuesFromClassNode)
			throws RendererException
	{
		Optional<Rendering> classRendering;

		if (owlAllValuesFromClassNode.hasOWLNamedClass())
			classRendering = renderOWLNamedClass(owlAllValuesFromClassNode.getOWLNamedClassNode());
		else if (owlAllValuesFromClassNode.hasOWLClassExpression())
			classRendering = renderOWLClassExpression(owlAllValuesFromClassNode.getOWLClassExpressionNode());
		else
			throw new RendererException("unknown OWLAllValuesFromClass node " + owlAllValuesFromClassNode);

		if (classRendering.isPresent())
			return Optional.of(new Rendering("ONLY " + classRendering.get().getTextRendering()));
		else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLSomeValuesFromDataType(
			OWLSomeValuesFromDataTypeNode owlSomeValuesFromDataTypeNode) throws RendererException
	{
		String datatypeName = owlSomeValuesFromDataTypeNode.getDataTypeName();

		if (!datatypeName.equals(""))
			return Optional.of(new Rendering("SOME " + datatypeName));
		else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLSomeValuesFromClass(OWLSomeValuesFromClassNode owlSomeValuesFromClassNode)
			throws RendererException
	{
		Optional<Rendering> classRendering;

		if (owlSomeValuesFromClassNode.hasOWLNamedClass())
			classRendering = renderOWLNamedClass(owlSomeValuesFromClassNode.getOWLNamedClassNode());
		else if (owlSomeValuesFromClassNode.hasOWLClassExpression())
			classRendering = renderOWLClassExpression(owlSomeValuesFromClassNode.getOWLClassExpressionNode());
		else
			throw new RendererException("unknown OWLSomeValuesFromClass node " + owlSomeValuesFromClassNode);

		if (classRendering.isPresent())
			return Optional.of(new Rendering("SOME " + classRendering.get().getTextRendering()));
		else
			return Optional.empty();
	}

	public Optional<Rendering> renderOWLEquivalentTo(OWLClassEquivalentToNode owlClassEquivalentToNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();

		textRendering.append(" EquivalentTo: ");

		if (owlClassEquivalentToNode.getClassExpressionNodes().size() == 1) {
			Optional<Rendering> classExpressionRendering = renderOWLClassExpression(
					owlClassEquivalentToNode.getClassExpressionNodes().get(0));
			if (!classExpressionRendering.isPresent())
				return classExpressionRendering;
			else
				textRendering.append(classExpressionRendering.get().getTextRendering());
		} else {
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassExpressionNode : owlClassEquivalentToNode.getClassExpressionNodes()) {
				Optional<Rendering> classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
				if (!classExpressionRendering.isPresent())
					continue; // Any empty class expression will generate an empty rendering
				if (!isFirst)
					textRendering.append(", ");
				textRendering.append(classExpressionRendering.get().getTextRendering());
				isFirst = false;
			}
		}
		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new Rendering(textRendering.toString()));
	}

	public Optional<Rendering> renderOWLSubclassOf(OWLSubclassOfNode owlSubclassOfNode) throws RendererException
	{
		Rendering rendering = new Rendering();
		rendering.addText(" SubClassOf: ");

		if (owlSubclassOfNode.getClassExpressionNodes().size() == 1) {
			Optional<Rendering> classExpressionRendering = renderOWLClassExpression(
					owlSubclassOfNode.getClassExpressionNodes().get(0));
			if (!classExpressionRendering.isPresent())
				return Optional.empty();
			else
				rendering.addText(classExpressionRendering.get().getTextRendering());
		} else {
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassExpressionNode : owlSubclassOfNode.getClassExpressionNodes()) {
				Optional<Rendering> classExpressionRendering = renderOWLClassExpression(owlClassExpressionNode);
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

	public Optional<Rendering> renderLiteral(LiteralNode literalNode) throws RendererException
	{
		if (literalNode.isInteger())
			return Optional.of(new Rendering(literalNode.getIntegerLiteralNode().toString()));
		else if (literalNode.isFloat())
			return Optional.of(new Rendering(literalNode.getFloatLiteralNode().toString()));
		else if (literalNode.isString())
			return Optional.of(new Rendering(literalNode.toString()));
		else if (literalNode.isBoolean())
			return Optional.of(new Rendering(literalNode.getBooleanLiteralNode().toString()));
		else
			throw new RendererException("unknown Literal node " + literalNode);
	}

	public Optional<Rendering> renderReference(ReferenceNode referenceNode) throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();
		boolean hasExplicitOptions = referenceNode.hasExplicitOptions();
		boolean atLeastOneOptionProcessed = false;

		textRendering.append(referenceNode.getSourceSpecificationNode().toString());

		if (hasExplicitOptions)
			textRendering.append("(");

		if (referenceNode.hasExplicitlySpecifiedEntityType()) {
			textRendering.append(referenceNode.getEntityTypeNode().toString());
			atLeastOneOptionProcessed = true;
		}

		if (referenceNode.hasExplicitlySpecifiedPrefix()) {
			Optional<Rendering> prefixRendering = renderPrefix(referenceNode.getPrefixNode());
			if (prefixRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				else
					atLeastOneOptionProcessed = true;
				textRendering.append(prefixRendering.get().getTextRendering());
			}
		}

		if (referenceNode.hasExplicitlySpecifiedNamespace()) {
			Optional<Rendering> namespaceRendering = renderNamespace(referenceNode.getNamespaceNode());
			if (namespaceRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				else
					atLeastOneOptionProcessed = true;
				textRendering.append(namespaceRendering.get().getTextRendering());
			}
		}

		if (referenceNode.hasValueExtractionFunction()) {
			Optional<Rendering> valueExtractionFunctionRendering = renderValueExtractionFunction(
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
				Optional<Rendering> valueEncodingRendering = renderValueEncoding(valueEncodingNode);
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
			Optional<Rendering> defaultLocationValueRendering = renderDefaultLocationValue(
					referenceNode.getDefaultLocationValueNode());
			if (defaultLocationValueRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(defaultLocationValueRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultDataValue()) {
			Optional<Rendering> defaultDataValueRendering = renderDefaultDataValue(referenceNode.getDefaultDataValueNode());
			if (defaultDataValueRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(defaultDataValueRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultID()) {
			Optional<Rendering> defaultIDRendering = renderDefaultID(referenceNode.getDefaultRDFIDNode());
			if (defaultIDRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(defaultIDRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedDefaultLabel()) {
			Optional<Rendering> defaultLabelRendering = renderDefaultLabel(referenceNode.getDefaultRDFSLabelNode());
			if (defaultLabelRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(defaultLabelRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedLanguage()) {
			Optional<Rendering> languageRendering = renderLanguage(referenceNode.getLanguageNode());
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
			Optional<Rendering> prefixNodeRendering = renderPrefix(referenceNode.getPrefixNode());
			if (prefixNodeRendering.isPresent()) {
				textRendering.append(prefixNodeRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedNamespace()) {
			Optional<Rendering> namespaceRendering = renderNamespace(referenceNode.getNamespaceNode());
			if (namespaceRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(namespaceRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyLocationDirective()) {
			Optional<Rendering> emptyLocationSettingRendering = renderEmptyLocationSetting(
					referenceNode.getEmptyLocationSettingNode());
			if (emptyLocationSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(emptyLocationSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyDataValueDirective()) {
			Optional<Rendering> emptyDataValueSettingRendering = renderEmptyDataValueSetting(
					referenceNode.getEmptyDataValueSettingNode());
			if (emptyDataValueSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(emptyDataValueSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyRDFIDDirective()) {
			Optional<Rendering> emptyRDFIFSettingRendering = renderEmptyRDFIDSetting(
					referenceNode.getEmptyRDFIDSettingNode());
			if (emptyRDFIFSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(emptyRDFIFSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedEmptyRDFSLabelDirective()) {
			Optional<Rendering> emptyRDFSLabelSettingRendering = renderEmptyRDFSLabelSetting(
					referenceNode.getEmptyRDFSLabelSettingNode());
			if (emptyRDFSLabelSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(emptyRDFSLabelSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedShiftDirective()) {
			Optional<Rendering> shiftSettingRendering = renderShiftSetting(referenceNode.getShiftSettingNode());
			if (shiftSettingRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(shiftSettingRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedTypes()) {
			Optional<Rendering> typesRendering = renderTypes(referenceNode.getTypesNode());
			if (typesRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(typesRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedIfExistsDirective()) {
			Optional<Rendering> ifExistsRendering = renderIfExistsDirective(referenceNode.getIfExistsDirectiveNode());
			if (ifExistsRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(ifExistsRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (referenceNode.hasExplicitlySpecifiedIfNotExistsDirective()) {
			Optional<Rendering> ifExistsRendering = renderIfNotExistsDirective(referenceNode.getIfNotExistsDirectiveNode());
			if (ifExistsRendering.isPresent()) {
				if (atLeastOneOptionProcessed)
					textRendering.append(" ");
				textRendering.append(ifExistsRendering.get().getTextRendering());
				atLeastOneOptionProcessed = true;
			}
		}

		if (hasExplicitOptions)
			textRendering.append(")");

		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new Rendering(textRendering.toString()));
	}

	public Optional<Rendering> renderEntityType(EntityTypeNode entityTypeNode) throws RendererException
	{
		return Optional.of(new Rendering(entityTypeNode.getEntityType().getTypeName()));
	}

	public Optional<Rendering> renderValueEncoding(ValueEncodingNode valueEncodingNode) throws RendererException
	{
		Rendering rendering = new Rendering(valueEncodingNode.getEncodingTypeName());

		if (valueEncodingNode.hasValueSpecification()) {
			Optional<Rendering> valueSpecificationRendering = renderValueSpecification(
					valueEncodingNode.getValueSpecification());
			if (valueSpecificationRendering.isPresent())
				rendering.addText(valueSpecificationRendering.get().getTextRendering());
		}
		return Optional.of(rendering);
	}

	public Optional<Rendering> renderValueSpecification(ValueSpecificationNode valueSpecificationNode)
			throws RendererException
	{
		StringBuffer textRendering = new StringBuffer();
		boolean isFirst = true;

		if (valueSpecificationNode.getNumberOfValueSpecificationItems() > 1)
			textRendering.append("(");

		for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
				.getValueSpecificationItemNodes()) {
			Optional<Rendering> valueSpecificationItemRendering = renderValueSpecificationItem(valueSpecificationItemNode);

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

		return textRendering.length() == 0 ? Optional.empty() : Optional.of(new Rendering(textRendering.toString()));
	}

	public Optional<Rendering> renderDefaultLocationValue(DefaultLocationValueNode defaultLocationValueNode)
			throws RendererException
	{
		return Optional.of(new Rendering(defaultLocationValueNode.toString()));
	}

	public Optional<Rendering> renderDefaultDataValue(DefaultDataValueNode defaultDataValueNode) throws RendererException
	{
		return Optional.of(new Rendering(defaultDataValueNode.toString()));
	}

	public Optional<Rendering> renderDefaultID(DefaultIDNode defaultIDNode) throws RendererException
	{
		return Optional.of(new Rendering(defaultIDNode.toString()));
	}

	public Optional<Rendering> renderDefaultLabel(DefaultLabelNode defaultLabelNode) throws RendererException
	{
		return Optional.of(new Rendering(defaultLabelNode.toString()));
	}

	public Optional<Rendering> renderValueSpecificationItem(ValueSpecificationItemNode valueSpecificationItemNode)
			throws RendererException
	{
		if (valueSpecificationItemNode.hasStringLiteral())
			return Optional.of(new Rendering("\"" + valueSpecificationItemNode.getStringLiteral() + "\""));
		else if (valueSpecificationItemNode.hasReference())
			return renderReference(valueSpecificationItemNode.getReferenceNode());
		else if (valueSpecificationItemNode.hasValueExtractionFunction())
			return renderValueExtractionFunction(valueSpecificationItemNode.getValueExtractionFunctionNode());
		else if (valueSpecificationItemNode.hasCapturingExpression())
			return Optional.of(new Rendering("[\"" + valueSpecificationItemNode.getCapturingExpression() + "\"]"));
		else
			throw new RendererException("unknown ValueSpecificationItem node " + valueSpecificationItemNode);
	}

	public Optional<Rendering> renderTypes(TypesNode types) throws RendererException
	{
		Rendering rendering = new Rendering();
		boolean isFirst = true;

		for (TypeNode typeNode : types.getTypeNodes()) {
			Optional<Rendering> typeRendering = renderType(typeNode);
			if (typeRendering.isPresent()) {
				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(typeRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		return Optional.of(rendering);
	}

	public Optional<Rendering> renderType(TypeNode typeNode) throws RendererException
	{
		if (typeNode instanceof ReferenceNode)
			return renderReference((ReferenceNode)typeNode);
		else if (typeNode instanceof OWLClassExpressionNode)
			return renderOWLClassExpression((OWLClassExpressionNode)typeNode);
		else
			throw new RendererException("do not know how to render type node " + typeNode.getNodeName());
	}

	public Optional<Rendering> renderSameAs(SameAsNode sameAsNode) throws RendererException
	{
		Rendering rendering = new Rendering(" SameAs: ");
		boolean isFirst = true;

		for (OWLIndividualNode owlIndividualNode : sameAsNode.getIndividualNodes()) {
			Optional<Rendering> individualRendering = renderOWLIndividual(owlIndividualNode);

			if (individualRendering.isPresent()) {
				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(individualRendering.get().getTextRendering());
				isFirst = false;
			}
		}
		return Optional.of(rendering);
	}

	public Optional<Rendering> renderDifferentFrom(DifferentFromNode differentFromNode) throws RendererException
	{
		Rendering rendering = new Rendering(" DifferentFrom: ");
		boolean isFirst = true;

		for (OWLIndividualNode owlIndividualNode : differentFromNode.getIndividualNodes()) {
			Optional<Rendering> individualRendering = renderOWLIndividual(owlIndividualNode);

			if (individualRendering.isPresent()) {
				if (!isFirst)
					rendering.addText(", ");
				rendering.addText(individualRendering.get().getTextRendering());
				isFirst = false;
			}
		}

		return Optional.of(rendering);
	}

	public Optional<Rendering> renderValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode)
			throws RendererException
	{
		Rendering rendering = new Rendering(valueExtractionFunctionNode.getFunctionName());

		if (valueExtractionFunctionNode.hasArguments()) {
			boolean isFirst = true;
			rendering.addText("(");

			for (StringOrReferenceNode stringOrReferenceNode : valueExtractionFunctionNode.getArgumentNodes()) {
				Optional<Rendering> stringOrReferenceRendering = renderStringOrReference(stringOrReferenceNode);
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

	public Optional<Rendering> renderSourceSpecification(SourceSpecificationNode sourceSpecificationNode)
			throws RendererException
	{
		Rendering rendering = new Rendering("@");

		if (sourceSpecificationNode.hasSource())
			rendering.addText("'" + sourceSpecificationNode.getSource() + "'!");

		if (sourceSpecificationNode.hasLocation())
			rendering.addText(sourceSpecificationNode.getLocation());
		else
			rendering.addText("\"" + sourceSpecificationNode.getLiteral() + "\""); // A literal

		return Optional.of(rendering);
	}

	public Optional<Rendering> renderStringOrReference(StringOrReferenceNode stringOrReferenceNode)
			throws RendererException
	{
		if (stringOrReferenceNode.isString())
			return renderStringLiteral(stringOrReferenceNode.getStringLiteralNode());
		else if (stringOrReferenceNode.isReference())
			return renderReference(stringOrReferenceNode.getReferenceNode());
		else
			throw new RendererException("unknown StringOrReference node " + stringOrReferenceNode);
	}

	public Optional<Rendering> renderMMDefaultEntityType(MMDefaultEntityTypeNode mmDefaultEntityTypeNode)
			throws RendererException
	{
		return Optional.of(new Rendering(mmDefaultEntityTypeNode.toString()));
	}

	public Optional<Rendering> renderMMDefaultValueEncoding(MMDefaultValueEncodingNode mmDefaultValueEncodingNode)
			throws RendererException
	{
		return Optional.of(new Rendering(mmDefaultValueEncodingNode.toString()));
	}

	public Optional<Rendering> renderMMDefaultPropertyValueType(
			MMDefaultPropertyValueTypeNode mmDefaultPropertyValueTypeNode) throws RendererException
	{
		return Optional.of(new Rendering(mmDefaultPropertyValueTypeNode.toString()));
	}

	public Optional<Rendering> renderMMDefaultPropertyType(MMDefaultPropertyTypeNode mmDefaultPropertyTypeNode)
			throws RendererException
	{
		return Optional.of(new Rendering(mmDefaultPropertyTypeNode.toString()));
	}

	public Optional<Rendering> renderMMDefaultDatatypePropertyValueType(
			MMDefaultDatatypePropertyValueTypeNode mmDefaultDatatypePropertyValueTypeNode) throws RendererException
	{
		return Optional.of(new Rendering(mmDefaultDatatypePropertyValueTypeNode.toString()));
	}

	public Optional<Rendering> renderStringLiteral(StringLiteralNode stringLiteralNode) throws RendererException
	{
		return Optional.of(new Rendering(stringLiteralNode.toString()));
	}

	public Optional<Rendering> renderShiftSetting(ShiftSettingNode shiftSettingNode) throws RendererException
	{
		return Optional.of(new Rendering(shiftSettingNode.toString()));
	}

	public Optional<Rendering> renderEmptyLocationSetting(EmptyLocationSettingNode emptyLocationSettingNode)
			throws RendererException
	{
		return Optional.of(new Rendering(emptyLocationSettingNode.toString()));
	}

	public Optional<Rendering> renderEmptyDataValueSetting(EmptyDataValueSettingNode emptyDataValueSettingNode)
			throws RendererException
	{
		return Optional.of(new Rendering(emptyDataValueSettingNode.toString()));
	}

	public Optional<Rendering> renderEmptyRDFIDSetting(EmptyRDFIDSettingNode emptyRDFIDSettingNode)
			throws RendererException
	{
		return Optional.of(new Rendering(emptyRDFIDSettingNode.toString()));
	}

	public Optional<Rendering> renderEmptyRDFSLabelSetting(EmptyRDFSLabelSettingNode emptyRDFSLabelSettingNode)
			throws RendererException
	{
		return Optional.of(new Rendering(emptyRDFSLabelSettingNode.toString()));
	}

	public Optional<Rendering> renderIfExistsDirective(IfExistsDirectiveNode ifExistsDirectiveNode)
			throws RendererException
	{
		return Optional.of(new Rendering(ifExistsDirectiveNode.toString()));
	}

	public Optional<Rendering> renderIfNotExistsDirective(IfNotExistsDirectiveNode ifNotExistsDirectiveNode)
			throws RendererException
	{
		return Optional.of(new Rendering(ifNotExistsDirectiveNode.toString()));
	}
}

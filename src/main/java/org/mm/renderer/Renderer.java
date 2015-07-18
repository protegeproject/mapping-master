package org.mm.renderer;

import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLDataAllValuesFromNode;
import org.mm.parser.node.OWLDataSomeValuesFromNode;
import org.mm.parser.node.OWLExactCardinalityNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLObjectAllValuesFromNode;
import org.mm.parser.node.OWLObjectSomeValuesFromNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.OWLSameAsNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.TypesNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;

import java.util.Optional;

public interface Renderer
{
	Optional<? extends Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException;

	Optional<? extends Rendering> renderMMExpression(MMExpressionNode MMExpressionNode) throws RendererException;

	Optional<? extends Rendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLIndividualDeclaration(
			OWLIndividualDeclarationNode owlIndividualDeclarationNode) throws RendererException;

	Optional<? extends Rendering> renderOWLClass(OWLClassNode classNode) throws RendererException;

	Optional<? extends Rendering> renderOWLProperty(OWLPropertyNode propertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLObjectProperty(OWLPropertyNode propertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLDataProperty(OWLPropertyNode propertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLNamedIndividual(OWLNamedIndividualNode namedIndividualNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLPropertyAssertionObject(
			OWLPropertyAssertionObjectNode propertyAssertionObjectNode) throws RendererException;

	Optional<? extends Rendering> renderFact(FactNode factNode) throws RendererException;

	Optional<? extends Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException;

	Optional<? extends Rendering> renderOWLAnnotationValue(OWLAnnotationValueNode annotationValueNode)
			throws RendererException;

	// OWL class expressions
	Optional<? extends Rendering> renderOWLClassExpression(OWLClassExpressionNode classExpressionNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode) throws RendererException;

	Optional<? extends Rendering> renderOWLIntersectionClass(OWLIntersectionClassNode intersectionClassNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLSubclassOf(OWLSubclassOfNode subclassOfNode) throws RendererException;

	Optional<? extends Rendering> renderOWLClassEquivalentTo(OWLClassEquivalentToNode classEquivalentToNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLSameAs(OWLSameAsNode sameAs) throws RendererException;

	Optional<? extends Rendering> renderOWLRestriction(OWLRestrictionNode restrictionNode) throws RendererException;

	Optional<? extends Rendering> renderOWLMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException;

	Optional<? extends Rendering> renderOWLMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException;

	Optional<? extends Rendering> renderOWLCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode cardinalityNode) throws RendererException;

	Optional<? extends Rendering> renderOWLHasValue(OWLPropertyNode propertyNode, OWLHasValueNode hasValueNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLAllValuesFrom(OWLPropertyNode propertyNode,
			OWLAllValuesFromNode allValuesFromNode) throws RendererException;

	Optional<? extends Rendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
			OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException;

	Optional<? extends Rendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectAllValuesFromNode objectAllValuesFromNode) throws RendererException;

	Optional<? extends Rendering> renderOWLDataSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLDataSomeValuesFromNode dataSomeValuesFromNode) throws RendererException;

	Optional<? extends Rendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectSomeValuesFromNode objectSomeValuesFromNode) throws RendererException;

	Optional<? extends Rendering> renderReference(ReferenceNode referenceNode) throws RendererException;

	Optional<? extends Rendering> renderValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode)
			throws RendererException;

	Optional<? extends Rendering> renderValueExtractionFunctionArgument(
			ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException;

	Optional<? extends Rendering> renderName(NameNode nameNode) throws RendererException;

	Optional<? extends Rendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException;

	Optional<? extends Rendering> renderValueEncoding(ValueEncodingNode valueEncodingNode) throws RendererException;

	Optional<? extends Rendering> renderValueSpecificationItem(ValueSpecificationItemNode valueSpecificationItemNode)
			throws RendererException;

	Optional<? extends Rendering> renderTypes(TypesNode typesNode) throws RendererException;
}

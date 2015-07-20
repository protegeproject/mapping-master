package org.mm.renderer;

import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLDataAllValuesFromNode;
import org.mm.parser.node.OWLDataSomeValuesFromNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLExactCardinalityNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLObjectAllValuesFromNode;
import org.mm.parser.node.OWLObjectOneOfNode;
import org.mm.parser.node.OWLObjectSomeValuesFromNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLUnionClassNode;

import java.util.Optional;

public interface OWLClassExpressionRenderer
{
  Optional<? extends Rendering> renderOWLClassExpression(OWLClassExpressionNode classExpressionNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode) throws RendererException;

  Optional<? extends Rendering> renderOWLIntersectionClass(OWLIntersectionClassNode intersectionClassNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLObjectOneOf(OWLObjectOneOfNode objectOneOfNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLEquivalentClasses(OWLClassNode declaredClassNode,
    OWLEquivalentClassesNode equivalentClassesNode) throws RendererException;

  Optional<? extends Rendering> renderOWLRestriction(OWLRestrictionNode restrictionNode) throws RendererException;

  Optional<? extends Rendering> renderOWLObjectExactCardinality(OWLPropertyNode propertyNode,
    OWLExactCardinalityNode cardinalityNode) throws RendererException;

  Optional<? extends Rendering> renderOWLDataExactCardinality(OWLPropertyNode propertyNode,
    OWLExactCardinalityNode cardinalityNode) throws RendererException;

  Optional<? extends Rendering> renderOWLObjectMaxCardinality(OWLPropertyNode propertyNode,
    OWLMaxCardinalityNode maxCardinalityNode) throws RendererException;

  Optional<? extends Rendering> renderOWLDataMaxCardinality(OWLPropertyNode propertyNode,
    OWLMaxCardinalityNode maxCardinalityNode) throws RendererException;

  Optional<? extends Rendering> renderOWLObjectMinCardinality(OWLPropertyNode propertyNode,
    OWLMinCardinalityNode minCardinalityNode) throws RendererException;

  Optional<? extends Rendering> renderOWLDataMinCardinality(OWLPropertyNode propertyNode,
    OWLMinCardinalityNode minCardinalityNode) throws RendererException;

  Optional<? extends Rendering> renderOWLObjectHasValue(OWLPropertyNode propertyNode, OWLHasValueNode hasValueNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLDataHasValue(OWLPropertyNode propertyNode, OWLHasValueNode hasValueNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
    OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException;

  Optional<? extends Rendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
    OWLObjectAllValuesFromNode objectAllValuesFromNode) throws RendererException;

  Optional<? extends Rendering> renderOWLDataSomeValuesFrom(OWLPropertyNode propertyNode,
    OWLDataSomeValuesFromNode dataSomeValuesFromNode) throws RendererException;

  Optional<? extends Rendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode propertyNode,
    OWLObjectSomeValuesFromNode objectSomeValuesFromNode) throws RendererException;
}

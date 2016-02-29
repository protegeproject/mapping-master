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
import org.mm.rendering.Rendering;

import java.util.Optional;

public interface ClassExpressionRenderer
{
   Optional<? extends Rendering> renderOWLClassExpression(OWLClassExpressionNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLUnionClass(OWLUnionClassNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLIntersectionClass(OWLIntersectionClassNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLObjectOneOf(OWLObjectOneOfNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLRestriction(OWLRestrictionNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLEquivalentClasses(OWLClassNode node1, OWLEquivalentClassesNode node2)
         throws RendererException;

   Optional<? extends Rendering> renderOWLObjectExactCardinality(OWLPropertyNode pnode, OWLExactCardinalityNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLDataExactCardinality(OWLPropertyNode pnode, OWLExactCardinalityNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLObjectMaxCardinality(OWLPropertyNode pnode, OWLMaxCardinalityNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLDataMaxCardinality(OWLPropertyNode pnode, OWLMaxCardinalityNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLObjectMinCardinality(OWLPropertyNode pnode, OWLMinCardinalityNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLDataMinCardinality(OWLPropertyNode pnode, OWLMinCardinalityNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLObjectHasValue(OWLPropertyNode pnode, OWLHasValueNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLDataHasValue(OWLPropertyNode pnode, OWLHasValueNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLDataAllValuesFrom(OWLPropertyNode pnode, OWLDataAllValuesFromNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLObjectAllValuesFrom(OWLPropertyNode pnode, OWLObjectAllValuesFromNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLDataSomeValuesFrom(OWLPropertyNode pnode, OWLDataSomeValuesFromNode target)
         throws RendererException;

   Optional<? extends Rendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode pnode, OWLObjectSomeValuesFromNode target)
         throws RendererException;
}

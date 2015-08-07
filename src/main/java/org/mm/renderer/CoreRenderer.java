package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLDifferentFromNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLSameAsNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.rendering.Rendering;

public interface CoreRenderer
{
  Optional<? extends Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException;

  Optional<? extends Rendering> renderMMExpression(MMExpressionNode MMExpressionNode) throws RendererException;

  Optional<? extends Rendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLSubClassOf(OWLClassNode declaredClassNode, OWLSubclassOfNode subclassOfNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLIndividualDeclaration(
    OWLIndividualDeclarationNode owlIndividualDeclarationNode) throws RendererException;

  Optional<? extends Rendering> renderOWLSameAs(OWLSameAsNode sameAs) throws RendererException;

  Optional<? extends Rendering> renderOWLDifferentFrom(OWLDifferentFromNode differentFromNode) throws RendererException;

  Optional<? extends Rendering> renderFact(FactNode factNode) throws RendererException;

  Optional<? extends Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException;
}

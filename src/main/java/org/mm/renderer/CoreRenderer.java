package org.mm.renderer;

import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLSameAsNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.ss.SpreadSheetDataSource;

import java.util.Optional;

public interface CoreRenderer
{
  void setDataSource(SpreadSheetDataSource dataSource);

  OWLEntityRenderer getOWLEntityRenderer();

  OWLClassExpressionRenderer getOWLClassExpressionRenderer();

  OWLLiteralRenderer getOWLLiteralRenderer();

  ReferenceRenderer getReferenceRenderer();

  Optional<? extends Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException;

  Optional<? extends Rendering> renderMMExpression(MMExpressionNode MMExpressionNode) throws RendererException;

  Optional<? extends Rendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLSubclassOf(OWLClassNode declaredClassNode, OWLSubclassOfNode subclassOfNode)
    throws RendererException;

  Optional<? extends Rendering> renderOWLIndividualDeclaration(
    OWLIndividualDeclarationNode owlIndividualDeclarationNode) throws RendererException;

  Optional<? extends Rendering> renderOWLSameAs(OWLSameAsNode sameAs) throws RendererException;

  Optional<? extends Rendering> renderOWLPropertyAssertionObject(
    OWLPropertyAssertionObjectNode propertyAssertionObjectNode) throws RendererException;

  Optional<? extends Rendering> renderOWLAnnotationValue(OWLAnnotationValueNode annotationValueNode)
    throws RendererException;

  Optional<? extends Rendering> renderFact(FactNode factNode) throws RendererException;

  Optional<? extends Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException;

  Optional<? extends Rendering> renderName(NameNode nameNode) throws RendererException;
}

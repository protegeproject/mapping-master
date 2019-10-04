package org.mm.renderer.owl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTClassExpressionFiller;
import org.mm.parser.node.ASTFiller;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTValue;
import org.mm.parser.node.Node;
import org.mm.renderer.CellCursor;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.Value;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class FillerNodeVisitor extends ClassExpressionNodeVisitor {

   private OWLClassExpression classExpression;

   public FillerNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      super(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }

   @Nullable
   public OWLClassExpression getClassExpression() {
      return classExpression;
   }

   @Override
   public void visit(ASTFiller node) {
      Node fillerNode = ParserUtils.getChild(node);
      fillerNode.accept(this);
   }

   @Override
   public void visit(ASTValue valueNode) {
      super.visit(valueNode);
      Value value = getValue();
      if (value != null) {
         String className = value.getString();
         classExpression = owlFactory.fetchOWLClass(className);
      }
   }

   @Override
   public void visit(ASTReference valueNode) {
      super.visit(valueNode);
      Value value = getValue();
      if (value != null) {
         String className = value.getString();
         classExpression = owlFactory.createOWLClass(className);
      }
   }

   @Override
   public void visit(ASTClassExpressionFiller node) {
      Node classExpressionNode = ParserUtils.getChild(node);
      classExpressionNode.accept(this);
      classExpression = super.getClassExpression();
   }
}

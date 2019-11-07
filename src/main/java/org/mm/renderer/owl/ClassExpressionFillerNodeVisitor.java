package org.mm.renderer.owl;

import static org.mm.parser.MappingMasterParserConstants.OWL_CLASS;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTClassExpressionFiller;
import org.mm.parser.node.ASTFiller;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTValue;
import org.mm.parser.node.Node;
import org.mm.renderer.CellCursor;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.ClassIri;
import org.mm.renderer.internal.ClassName;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.Value;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class ClassExpressionFillerNodeVisitor extends ClassExpressionNodeVisitor {

   private OWLClassExpression classExpression;

   public ClassExpressionFillerNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
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
      if (value instanceof ClassName) {
         classExpression = owlFactory.getOWLClass((ClassName) value);
      } else if (value instanceof ClassIri) {
         classExpression = owlFactory.getOWLClass((ClassIri) value);
      }
   }

   @Override
   public void visit(ASTReference valueNode) {
      changeEntityType(valueNode);
      super.visit(valueNode);
      Value value = getValue();
      if (value instanceof ClassName) {
         classExpression = owlFactory.getOWLClass((ClassName) value);
      } else if (value instanceof ClassIri) {
         classExpression = owlFactory.getOWLClass((ClassIri) value);
      }
   }

   private void changeEntityType(ASTReference valueNode) {
      ReferenceDirectives newDirectives = valueNode.getDirectives().setEntityType(OWL_CLASS);
      valueNode.referenceDirectives = newDirectives;
   }

   @Override
   public void visit(ASTClassExpressionFiller node) {
      Node classExpressionNode = ParserUtils.getChild(node);
      classExpressionNode.accept(this);
      classExpression = super.getClassExpression();
   }
}

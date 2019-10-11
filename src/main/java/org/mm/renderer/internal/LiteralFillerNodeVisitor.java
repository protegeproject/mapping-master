package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mm.parser.MappingMasterParserConstants.OWL_LITERAL;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnyValue;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTValue;
import org.mm.parser.node.Node;
import org.mm.renderer.CellCursor;
import org.mm.renderer.owl.OwlFactory;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class LiteralFillerNodeVisitor extends ValueNodeVisitor {

   private final OwlFactory owlFactory;

   private OWLLiteral literal;

   public LiteralFillerNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      super(referenceResolver, builtInFunctionHandler, cellCursor);
      this.owlFactory = checkNotNull(owlFactory);
   }

   @Nullable
   public OWLLiteral getLiteral() {
      return literal;
   }

   @Override
   public void visit(ASTAnyValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTValue valueNode) {
      super.visit(valueNode);
      Value value = getValue();
      if (value != null) {
         if (value instanceof LiteralValue) {
            literal = owlFactory.getOWLTypedLiteral((LiteralValue) value);
         } else if (value instanceof PlainLiteralValue) {
            literal = owlFactory.getOWLPlainLiteral((PlainLiteralValue) value);
         }
      }
   }

   @Override
   public void visit(ASTReference valueNode) {
      changeEntityType(valueNode);
      super.visit(valueNode);
      Value value = getValue();
      if (value != null) {
         if (value instanceof LiteralValue) {
            literal = owlFactory.getOWLTypedLiteral((LiteralValue) value);
         } else if (value instanceof PlainLiteralValue) {
            literal = owlFactory.getOWLPlainLiteral((PlainLiteralValue) value);
         }
      }
   }

   private void changeEntityType(ASTReference valueNode) {
      ReferenceDirectives newDirectives = valueNode.getDirectives().setEntityType(OWL_LITERAL);
      valueNode.referenceDirectives = newDirectives;
   }
}

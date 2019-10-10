package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mm.parser.MappingMasterParserConstants.OWL_DATATYPE;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTFiller;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTValue;
import org.mm.parser.node.Node;
import org.mm.renderer.CellCursor;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.Value;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLDatatype;

public class DataFillerNodeVisitor extends ValueNodeVisitor {

   private final OwlFactory owlFactory;

   private OWLDatatype datatype;

   public DataFillerNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      super(referenceResolver, builtInFunctionHandler, cellCursor);
      this.owlFactory = checkNotNull(owlFactory);
   }

   @Nullable
   public OWLDatatype getDatatype() {
      return datatype;
   }

   @Override
   public void visit(ASTFiller node) {
      if (node.getDatatype() != -1) {
         datatype = DatatypeUtils.getOWLDatatype(node.getDatatype());
      } else {
         Node fillerNode = ParserUtils.getChild(node);
         fillerNode.accept(this);
      }
   }

   @Override
   public void visit(ASTValue valueNode) {
      super.visit(valueNode);
      Value value = getValue();
      if (value != null) {
         datatype = owlFactory.createOWLDatatype(value.getString());
      }
   }

   @Override
   public void visit(ASTReference valueNode) {
      changeEntityType(valueNode);
      super.visit(valueNode);
      Value value = getValue();
      if (value != null) {
         datatype = owlFactory.createOWLDatatype(value.getString());
      }
   }

   private void changeEntityType(ASTReference valueNode) {
      ReferenceDirectives newDirectives = valueNode.getDirectives().setEntityType(OWL_DATATYPE);
      valueNode.referenceDirectives = newDirectives;
   }
}

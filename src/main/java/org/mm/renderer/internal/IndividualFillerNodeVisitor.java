package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mm.parser.MappingMasterParserConstants.OWL_NAMED_INDIVIDUAL;
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
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class IndividualFillerNodeVisitor extends ValueNodeVisitor {

   private final OwlFactory owlFactory;

   private OWLNamedIndividual individual;

   public IndividualFillerNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      super(referenceResolver, builtInFunctionHandler, cellCursor);
      this.owlFactory = checkNotNull(owlFactory);
   }

   @Nullable
   public OWLNamedIndividual getIndividual() {
      return individual;
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
         if (value instanceof IndividualName) {
            individual = owlFactory.getOWLNamedIndividual((IndividualName) value);
         } else if (value instanceof IndividualIri) {
            individual = owlFactory.getOWLNamedIndividual((IndividualIri) value);
         }
      }
   }

   @Override
   public void visit(ASTReference valueNode) {
      changeEntityType(valueNode);
      super.visit(valueNode);
      Value value = getValue();
      if (value != null) {
         if (value instanceof IndividualName) {
            individual = owlFactory.getOWLNamedIndividual((IndividualName) value);
         } else if (value instanceof IndividualIri) {
            individual = owlFactory.getOWLNamedIndividual((IndividualIri) value);
         }
      }
   }

   private void changeEntityType(ASTReference valueNode) {
      ReferenceDirectives newDirectives = valueNode.getDirectives().setEntityType(OWL_NAMED_INDIVIDUAL);
      valueNode.referenceDirectives = newDirectives;
   }
}

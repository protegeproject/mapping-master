package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.parser.NodeType;
import org.mm.parser.NodeVisitorAdapter;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationProperty;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.SimpleNode;
import org.mm.renderer.CellCursor;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.ClassIri;
import org.mm.renderer.internal.LiteralValue;
import org.mm.renderer.internal.PlainLiteralValue;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.UntypedIri;
import org.mm.renderer.internal.Value;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class AnnotationNodeVisitor extends NodeVisitorAdapter {

   private final ReferenceResolver referenceResolver;
   private final BuiltInFunctionHandler builtInFunctionHandler;
   private final OwlFactory owlFactory;
   private final CellCursor cellCursor;

   @Nullable private OWLAnnotationProperty property;
   @Nullable private OWLAnnotationValue annotationValue;

   private OWLAnnotation annotation;

   public AnnotationNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      this.referenceResolver = checkNotNull(referenceResolver);
      this.builtInFunctionHandler = checkNotNull(builtInFunctionHandler);
      this.owlFactory = checkNotNull(owlFactory);
      this.cellCursor = checkNotNull(cellCursor);
   }

   @Nullable
   public OWLAnnotation getAnnotation() {
      return annotation;
   }

   @Override
   public void visit(ASTAnnotation node) {
      visitAnnotationPropertyNode(node);
      visitAnnotationValueNode(node);
      if (property != null && annotationValue != null) {
         annotation = owlFactory.createOWLAnnotation(property, annotationValue);
      }
   }

   private void visitAnnotationPropertyNode(ASTAnnotation annotationNode) {
      ASTAnnotationProperty annotationPropertyNode = ParserUtils.getChild(
            annotationNode,
            NodeType.ANNOTATION_PROPERTY);
      EntityNodeVisitor visitor = new EntityNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
      visitor.visit(annotationPropertyNode);
      OWLEntity entity = visitor.getEntity();
      if (entity != null) {
         property = entity.asOWLAnnotationProperty();
      }
   }

   private void visitAnnotationValueNode(ASTAnnotation annotationNode) {
      Value value = getAnnotationValue(annotationNode);
      if (value instanceof ClassIri) {
         annotationValue = owlFactory.getOWLAnnotationValue((ClassIri) value);
      } else if (value instanceof UntypedIri) {
         annotationValue = owlFactory.getOWLAnnotationValue((UntypedIri) value);
      } else if (value instanceof LiteralValue) {
         annotationValue = owlFactory.getOWLAnnotationValue((LiteralValue) value);
      } else if (value instanceof PlainLiteralValue) {
         annotationValue = owlFactory.getOWLAnnotationValue((PlainLiteralValue) value);
      }
   }

   private Value getAnnotationValue(SimpleNode node) {
      ASTAnnotationValue valueNode = ParserUtils.getChild(node, NodeType.ANNOTATION_VALUE);
      ValueNodeVisitor visitor = new ValueNodeVisitor(referenceResolver, builtInFunctionHandler, cellCursor);
      visitor.visit(valueNode);
      return visitor.getValue();
   }
}

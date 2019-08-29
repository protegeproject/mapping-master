package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.parser.NodeType;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationProperty;
import org.mm.renderer.AbstractNodeVisitor;
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
public class AnnotationNodeVisitor extends AbstractNodeVisitor {

   private final OwlFactory owlObjectProvider;
   private final ValueNodeVisitor valueNodeVisitor;

   @Nullable private OWLAnnotationProperty property;
   @Nullable private OWLAnnotationValue annotationValue;

   private OWLAnnotation annotation;

   public AnnotationNodeVisitor(@Nonnull OwlFactory owlObjectProvider,
         @Nonnull ValueNodeVisitor valueNodeVisitor) {
      super(valueNodeVisitor);
      this.owlObjectProvider = checkNotNull(owlObjectProvider);
      this.valueNodeVisitor = checkNotNull(valueNodeVisitor);
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
         annotation = owlObjectProvider.createOWLAnnotation(property, annotationValue);
      }
   }

   private void visitAnnotationPropertyNode(ASTAnnotation annotationNode) {
      ASTAnnotationProperty annotationPropertyNode = ParserUtils.getChild(
            annotationNode,
            NodeType.ANNOTATION_PROPERTY);
      EntityNodeVisitor visitor = new EntityNodeVisitor(owlObjectProvider, valueNodeVisitor);
      visitor.visit(annotationPropertyNode);
      OWLEntity entity = visitor.getEntity();
      if (entity != null) {
         property = entity.asOWLAnnotationProperty();
      }
   }

   private void visitAnnotationValueNode(ASTAnnotation annotationNode) {
      Value value = getAnnotationValue(annotationNode);
      annotationValue = owlObjectProvider.getOWLAnnotationValue(value);
   }
}

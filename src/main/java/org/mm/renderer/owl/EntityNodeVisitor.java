package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.mm.parser.node.ASTAnnotationProperty;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTObjectProperty;
import org.mm.parser.node.ASTProperty;
import org.mm.renderer.AbstractNodeVisitor;
import org.mm.renderer.internal.Value;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class EntityNodeVisitor extends AbstractNodeVisitor {

   private final OwlFactory owlFactory;

   private OWLEntity entity;

   public EntityNodeVisitor(@Nonnull OwlFactory owlFactory,
         @Nonnull ValueNodeVisitor valueNodeVisitor) {
      super(valueNodeVisitor);
      this.owlFactory = checkNotNull(owlFactory);
   }

   public OWLEntity getEntity() {
      return entity;
   }

   @Override
   public void visit(ASTClass classNode) {
      Value classNameValue = getValue(classNode);
      entity = owlFactory.getOWLClass(classNameValue);
   }

   @Override
   public void visit(ASTProperty propertyNode) {
      Value propertyNameValue = getValue(propertyNode);
      entity = owlFactory.getOWLProperty(propertyNameValue);
   }

   @Override
   public void visit(ASTDataProperty propertyNode) {
      Value propertyNameValue = getValue(propertyNode);
      entity = owlFactory.getOWLDataProperty(propertyNameValue);
   }

   @Override
   public void visit(ASTObjectProperty propertyNode) {
      Value propertyNameValue = getValue(propertyNode);
      entity = owlFactory.getOWLObjectProperty(propertyNameValue);
   }

   @Override
   public void visit(ASTAnnotationProperty propertyNode) {
      Value propertyNameValue = getValue(propertyNode);
      entity = owlFactory.getOWLAnnotationProperty(propertyNameValue);
   }

   @Override
   public void visit(ASTNamedIndividual individualNode) {
      Value individualNameValue = getValue(individualNode);
      entity = owlFactory.getOWLNamedIndividual(individualNameValue);
   }
}

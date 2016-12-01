package org.mm.renderer.internal;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class AnnotationPropertyName extends ReferredEntityName {

   public AnnotationPropertyName(String prefixedName) {
      super(prefixedName);
   }

   @Override
   public boolean isClass() {
      return false;
   }

   @Override
   public boolean isDataProperty() {
      return false;
   }

   @Override
   public boolean isObjectProperty() {
      return false;
   }

   @Override
   public boolean isAnnotationProperty() {
      return true;
   }

   @Override
   public boolean isNamedIndividual() {
      return false;
   }
}

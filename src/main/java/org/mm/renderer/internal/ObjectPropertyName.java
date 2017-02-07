package org.mm.renderer.internal;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ObjectPropertyName extends PropertyName {

   public ObjectPropertyName(String prefixedName) {
      super(prefixedName);
   }

   @Override
   public boolean isDataProperty() {
      return false;
   }

   @Override
   public boolean isObjectProperty() {
      return true;
   }

   @Override
   public boolean isAnnotationProperty() {
      return false;
   }
}

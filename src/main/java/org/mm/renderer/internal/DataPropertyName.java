package org.mm.renderer.internal;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class DataPropertyName extends ReferredEntityName {

   public DataPropertyName(@Nonnull String prefixedName) {
      super(prefixedName);
   }

   @Override
   public boolean isClass() {
      return false;
   }

   @Override
   public boolean isDataProperty() {
      return true;
   }

   @Override
   public boolean isObjectProperty() {
      return false;
   }

   @Override
   public boolean isAnnotationProperty() {
      return false;
   }

   @Override
   public boolean isNamedIndividual() {
      return false;
   }
}

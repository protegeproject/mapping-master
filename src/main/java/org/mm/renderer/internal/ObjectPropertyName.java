package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ObjectPropertyName extends PropertyName {

   public ObjectPropertyName(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static ObjectPropertyName create(@Nonnull String value) {
      return new ObjectPropertyName(value, false);
   }

   @Override
   public ObjectPropertyName update(String newValue) {
      return new ObjectPropertyName(newValue, isFromWorkbook());
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

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof ObjectPropertyName)) {
         return false;
      }
      ObjectPropertyName other = (ObjectPropertyName) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}

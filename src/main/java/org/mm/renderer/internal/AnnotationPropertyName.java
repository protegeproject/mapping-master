package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class AnnotationPropertyName extends PropertyName {

   public AnnotationPropertyName(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static AnnotationPropertyName create(@Nonnull String value) {
      return new AnnotationPropertyName(value, false);
   }

   @Override
   public AnnotationPropertyName update(String newValue) {
      return new AnnotationPropertyName(newValue, isFromWorkbook());
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
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof AnnotationPropertyName)) {
         return false;
      }
      AnnotationPropertyName other = (AnnotationPropertyName) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}

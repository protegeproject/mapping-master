package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class AnnotationPropertyIri extends PropertyIri {

   public AnnotationPropertyIri(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static AnnotationPropertyIri create(@Nonnull String value) {
      return new AnnotationPropertyIri(value, false);
   }

   @Override
   public AnnotationPropertyIri update(String newValue) {
      return new AnnotationPropertyIri(newValue, isFromWorkbook());
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
      if (!(o instanceof AnnotationPropertyIri)) {
         return false;
      }
      AnnotationPropertyIri other = (AnnotationPropertyIri) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}

package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ObjectPropertyIri extends PropertyIri {

   public ObjectPropertyIri(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static ObjectPropertyIri create(@Nonnull String value) {
      return new ObjectPropertyIri(value, false);
   }

   @Override
   public ObjectPropertyIri update(String newValue) {
      return new ObjectPropertyIri(newValue, isFromWorkbook());
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
      if (!(o instanceof ObjectPropertyIri)) {
         return false;
      }
      ObjectPropertyIri other = (ObjectPropertyIri) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}

package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class DataPropertyIri extends PropertyIri {

   public DataPropertyIri(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static DataPropertyIri create(@Nonnull String value) {
      return new DataPropertyIri(value, false);
   }

   @Override
   public DataPropertyIri update(String newValue) {
      return new DataPropertyIri(newValue, isFromWorkbook());
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
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof DataPropertyIri)) {
         return false;
      }
      DataPropertyIri other = (DataPropertyIri) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}

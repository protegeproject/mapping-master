package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class DatatypeIri extends IriValue {

   public DatatypeIri(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static DatatypeIri create(@Nonnull String value) {
      return new DatatypeIri(value, false);
   }

   @Override
   public DatatypeIri update(String newValue) {
      return new DatatypeIri(newValue, isFromWorkbook());
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof DatatypeIri)) {
         return false;
      }
      DatatypeIri other = (DatatypeIri) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }

}

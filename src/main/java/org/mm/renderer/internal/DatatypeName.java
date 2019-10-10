package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class DatatypeName extends PrefixedValue {

   public DatatypeName(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static DatatypeName create(@Nonnull String value) {
      return new DatatypeName(value, false);
   }

   @Override
   public DatatypeName update(String newValue) {
      return new DatatypeName(newValue, isFromWorkbook());
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof DatatypeName)) {
         return false;
      }
      DatatypeName other = (DatatypeName) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}

package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class UntypedIri extends IriValue {

   public UntypedIri(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static UntypedIri create(@Nonnull String value) {
      return new UntypedIri(value, false);
   }

   @Override
   public UntypedIri update(String newValue) {
      return new UntypedIri(newValue, isFromWorkbook());
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof UntypedIri)) {
         return false;
      }
      UntypedIri other = (UntypedIri) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}

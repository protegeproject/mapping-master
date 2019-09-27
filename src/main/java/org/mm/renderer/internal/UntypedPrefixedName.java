package org.mm.renderer.internal;

import javax.annotation.Nonnull;
import com.google.common.base.Objects;

public class UntypedPrefixedName extends PrefixedValue {
   
   public UntypedPrefixedName(@Nonnull String value, boolean isFromWorkbook) {
      super(value, isFromWorkbook);
   }

   public static UntypedPrefixedName create(@Nonnull String value) {
      return new UntypedPrefixedName(value, false);
   }

   @Override
   public UntypedPrefixedName update(String newValue) {
      return new UntypedPrefixedName(newValue, isFromWorkbook());
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof UntypedPrefixedName)) {
         return false;
      }
      UntypedPrefixedName other = (UntypedPrefixedName) o;
      return Objects.equal(getString(), other.getString())
            && Objects.equal(isFromWorkbook(), other.isFromWorkbook());
   }
}

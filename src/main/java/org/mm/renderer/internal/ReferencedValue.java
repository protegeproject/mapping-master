package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;

public abstract class ReferencedValue implements Value {

   private final String prefixedName;

   public ReferencedValue(@Nonnull String prefixedName) {
      this.prefixedName = checkNotNull(prefixedName);
   }

   @Override
   public String getString() {
      return prefixedName;
   }
}

package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;

public abstract class ReferencedEntityName implements Value {

   private final String prefixedName;

   public ReferencedEntityName(@Nonnull String prefixedName) {
      this.prefixedName = checkNotNull(prefixedName);
   }

   @Override
   public String getString() {
      return prefixedName;
   }
}

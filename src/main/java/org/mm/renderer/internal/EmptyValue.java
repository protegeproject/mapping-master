package org.mm.renderer.internal;

import javax.annotation.Nullable;

public class EmptyValue implements Value {

   public static EmptyValue create() {
      return new EmptyValue();
   }

   @Override
   @Nullable
   public String getString() {
      return null;
   }

   @Override
   @Nullable
   public Value update(String newValue) {
      return null;
   }
}

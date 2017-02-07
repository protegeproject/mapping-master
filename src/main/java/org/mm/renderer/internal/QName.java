package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class QName implements Value<String> {

   private final String prefixedName;

   public QName(@Nonnull String prefixedName) {
      this.prefixedName = checkNotNull(prefixedName);
   }

   @Override
   public String getActualObject() {
      return prefixedName;
   }
}

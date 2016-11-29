package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferredEntityName implements Value<String> {

   private final String prefixedName;

   public ReferredEntityName(@Nonnull String prefixedName) {
      this.prefixedName = checkNotNull(prefixedName);
   }

   public Optional<String> getPrefixLabel() {
      String prefixLabel = null;
      if (prefixedName.contains(":")) {
         prefixLabel = prefixedName.split(":", 2)[0];
      }
      return Optional.ofNullable(prefixLabel);
   }

   public String getLocalName() {
      String localName = prefixedName;
      if (prefixedName.contains(":")) {
         localName = prefixedName.split(":", 2)[1];
      }
      return localName;
   }

   @Override
   public String getActualObject() {
      return prefixedName;
   }
}

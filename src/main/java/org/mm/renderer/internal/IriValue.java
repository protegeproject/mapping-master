package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class IriValue implements Value<IRI> {

   private final String iriString;

   public IriValue(@Nonnull String iriString) {
      this.iriString = checkNotNull(iriString);
   }

   @Override
   public IRI getActualObject() {
      return IRI.create(iriString);
   }
}

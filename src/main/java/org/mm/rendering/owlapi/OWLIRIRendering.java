package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.IRI;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OWLIRIRendering extends OWLRendering {

   private final IRI value;

   public OWLIRIRendering(@Nonnull IRI value) {
      super(value);
      this.value = checkNotNull(value);
   }

   @Override
   @Nonnull
   public IRI getOWLObject() {
      return value;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof OWLIRIRendering)) {
         return false;
      }
      OWLIRIRendering other = (OWLIRIRendering) obj;
      return value.equals(other.value);
   }
}

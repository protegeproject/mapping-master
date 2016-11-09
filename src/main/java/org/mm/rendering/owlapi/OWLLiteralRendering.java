package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OWLLiteralRendering extends OWLAnnotationValueRendering {

   private final OWLLiteral value;

   public OWLLiteralRendering(@Nonnull OWLLiteral value) {
      super(value);
      this.value = checkNotNull(value);
   }

   @Override
   @Nonnull
   public OWLLiteral getOWLObject() {
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
      if (!(obj instanceof OWLLiteralRendering)) {
         return false;
      }
      OWLLiteralRendering other = (OWLLiteralRendering) obj;
      return value.equals(other.value);
   }
}

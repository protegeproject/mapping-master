package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OWLNamedIndividualRendering extends OWLRendering {

   private final OWLNamedIndividual individual;

   public OWLNamedIndividualRendering(@Nonnull OWLNamedIndividual individual) {
      super(individual);
      this.individual = checkNotNull(individual);
   }

   @Override
   @Nonnull
   public OWLNamedIndividual getOWLObject() {
      return individual;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (obj == this) {
         return true;
     }
     if (!(obj instanceof OWLNamedIndividualRendering)) {
         return false;
     }
     OWLNamedIndividualRendering other = (OWLNamedIndividualRendering) obj;
     return individual.equals(other.individual);
   }
}

package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLRestriction;

public class OWLRestrictionRendering extends OWLObjectRendering implements OWLClassExpressionRendering {

   private final OWLRestriction restriction;

   public OWLRestrictionRendering(@Nonnull OWLRestriction restriction) {
      super(restriction);
      this.restriction = checkNotNull(restriction);
   }

   @Override
   @Nonnull
   public OWLRestriction getOWLObject() {
      return restriction;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (obj == this) {
         return true;
     }
     if (!(obj instanceof OWLRestrictionRendering)) {
         return false;
     }
     OWLRestrictionRendering other = (OWLRestrictionRendering) obj;
     return restriction.equals(other.restriction);
   }
}

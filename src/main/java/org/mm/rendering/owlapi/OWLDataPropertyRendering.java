package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLDataProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OWLDataPropertyRendering extends OWLRendering implements OWLPropertyRendering {

   private final OWLDataProperty property;

   public OWLDataPropertyRendering(@Nonnull OWLDataProperty property) {
      super(property);
      this.property = checkNotNull(property);
   }

   @Override
   @Nonnull
   public OWLDataProperty getOWLObject() {
      return property;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof OWLDataPropertyRendering)) {
         return false;
      }
      OWLDataPropertyRendering other = (OWLDataPropertyRendering) obj;
      return property.equals(other.property);
   }
}

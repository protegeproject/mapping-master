package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OWLObjectPropertyRendering extends OWLObjectRendering implements OWLPropertyRendering {

   private final OWLObjectProperty property;

   public OWLObjectPropertyRendering(@Nonnull OWLObjectProperty property) {
      super(property);
      this.property = checkNotNull(property);
   }

   @Override
   @Nonnull
   public OWLObjectProperty getOWLObject() {
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
      if (!(obj instanceof OWLObjectPropertyRendering)) {
         return false;
      }
      OWLObjectPropertyRendering other = (OWLObjectPropertyRendering) obj;
      return property.equals(other.property);
   }
}

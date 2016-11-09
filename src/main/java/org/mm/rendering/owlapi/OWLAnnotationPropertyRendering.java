package org.mm.rendering.owlapi;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OWLAnnotationPropertyRendering extends OWLPropertyRendering {

   private final OWLAnnotationProperty property;

   public OWLAnnotationPropertyRendering(@Nonnull OWLAnnotationProperty property) {
      super(property);
      this.property = checkNotNull(property);
   }

   @Override
   @Nonnull
   public OWLAnnotationProperty getOWLObject() {
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
      if (!(obj instanceof OWLAnnotationPropertyRendering)) {
         return false;
      }
      OWLAnnotationPropertyRendering other = (OWLAnnotationPropertyRendering) obj;
      return property.equals(other.property);
   }
}

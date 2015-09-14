package org.mm.rendering.owlapi;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;

public class OWLAnnotationPropertyRendering extends OWLAPIRendering
{
   private final OWLAnnotationProperty property;

   public OWLAnnotationPropertyRendering(OWLAnnotationProperty property)
   {
      super();
      this.property = property;
   }

   public OWLAnnotationProperty getOWLAnnotationProperty()
   {
      return this.property;
   }
}

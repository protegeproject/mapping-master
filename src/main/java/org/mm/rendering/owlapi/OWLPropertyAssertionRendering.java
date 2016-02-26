package org.mm.rendering.owlapi;

import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;

public class OWLPropertyAssertionRendering extends OWLAPIRendering
{
   private final OWLPropertyAssertionObject property;

   public OWLPropertyAssertionRendering(OWLPropertyAssertionObject propertyAssertionObject)
   {
      super();
      this.property = propertyAssertionObject;
   }

   public OWLPropertyAssertionObject getOWLPropertyAssertionObject()
   {
      return this.property;
   }
}

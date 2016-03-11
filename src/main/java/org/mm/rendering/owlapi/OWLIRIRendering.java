package org.mm.rendering.owlapi;

import org.semanticweb.owlapi.model.IRI;

public class OWLIRIRendering extends OWLAnnotationValueRendering
{
   private final IRI iri;

   public OWLIRIRendering(IRI iri)
   {
      super(iri);
      this.iri = iri;
   }

   public IRI getIRI()
   {
      return iri;
   }

   @Override
   public String getRawValue()
   {
      return iri.toString();
   }
}

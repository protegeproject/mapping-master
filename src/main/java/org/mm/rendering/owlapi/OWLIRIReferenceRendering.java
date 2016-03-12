package org.mm.rendering.owlapi;

import org.mm.core.ReferenceType;
import org.semanticweb.owlapi.model.IRI;

public class OWLIRIReferenceRendering extends OWLReferenceRendering
{
   private IRI iri;

   private final String rawRendering;

   public OWLIRIReferenceRendering(IRI iri, ReferenceType referenceType) {
      super(referenceType);
      this.iri = iri;
      this.rawRendering = iri.toString();
   }

   public IRI getIRI()
   {
      return iri;
   }

   @Override
   public String getRawValue()
   {
      return rawRendering;
   }

   @Override
   public boolean isOWLLiteral()
   {
      return false;
   }

   @Override
   public boolean isOWLEntity()
   {
      return false;
   }

   @Override
   public boolean isOWLClass()
   {
      return false;
   }

   @Override
   public boolean isOWLNamedIndividual()
   {
      return false;
   }

   @Override
   public boolean isOWLObjectProperty()
   {
      return false;
   }

   @Override
   public boolean isOWLDataProperty()
   {
      return false;
   }

   @Override
   public boolean isOWLAnnotationProperty()
   {
      return false;
   }

   @Override
   public boolean isOWLDatatype()
   {
      return false;
   }

   @Override
   public boolean isIRI()
   {
      return true;
   }
}

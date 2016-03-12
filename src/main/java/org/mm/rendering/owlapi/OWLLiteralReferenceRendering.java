package org.mm.rendering.owlapi;

import org.mm.core.ReferenceType;
import org.semanticweb.owlapi.model.OWLLiteral;

public class OWLLiteralReferenceRendering extends OWLReferenceRendering
{
   private OWLLiteral literal;

   private final String rawRendering;

   public OWLLiteralReferenceRendering(OWLLiteral literal, ReferenceType referenceType) {
      super(referenceType);
      this.literal = literal;
      this.rawRendering = literal.getLiteral();
   }

   public OWLLiteral getOWLLiteral()
   {
      return literal;
   }

   @Override
   public String getRawValue()
   {
      return rawRendering;
   }

   @Override
   public boolean isOWLLiteral()
   {
      return true;
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
      return false;
   }
}

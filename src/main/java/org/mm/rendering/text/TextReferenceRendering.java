package org.mm.rendering.text;

import org.mm.core.ReferenceType;
import org.mm.rendering.ReferenceRendering;

public class TextReferenceRendering extends TextRendering implements ReferenceRendering
{
   private final String rawValue;
   private final ReferenceType referenceType;

   public TextReferenceRendering(String rawValue, ReferenceType referenceType)
   {
      super(rawValue);
      this.rawValue = rawValue;
      this.referenceType = referenceType;
   }

   @Override
   public ReferenceType getReferenceType()
   {
      return this.referenceType;
   }

   @Override
   public String getRawValue()
   {
      return this.rawValue;
   }

   @Override
   public boolean isOWLLiteral()
   {
      return this.referenceType.isOWLLiteral();
   }

   @Override
   public boolean isOWLEntity()
   {
      return this.referenceType.isOWLEntity();
   }

   @Override
   public boolean isOWLClass()
   {
      return this.referenceType.isOWLClass();
   }

   @Override
   public boolean isOWLNamedIndividual()
   {
      return this.referenceType.isOWLNamedIndividual();
   }

   @Override
   public boolean isOWLObjectProperty()
   {
      return this.referenceType.isOWLObjectProperty();
   }

   @Override
   public boolean isOWLDataProperty()
   {
      return this.referenceType.isOWLDataProperty();
   }

   @Override
   public boolean isOWLAnnotationProperty()
   {
      return this.referenceType.isOWLAnnotationProperty();
   }

   @Override
   public boolean isOWLDatatype()
   {
      return this.referenceType.isOWLDatatype();
   }

   @Override
   public boolean isIRI()
   {
      return this.referenceType.isIRI();
   }
}

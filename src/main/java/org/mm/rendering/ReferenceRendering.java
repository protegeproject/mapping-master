package org.mm.rendering;

import org.mm.core.ReferenceType;

public interface ReferenceRendering extends Rendering
{
   ReferenceType getReferenceType();

   String getRawValue();

   boolean isOWLLiteral();

   boolean isOWLEntity();

   boolean isOWLClass();

   boolean isOWLNamedIndividual();

   boolean isOWLObjectProperty();

   boolean isOWLDataProperty();

   boolean isOWLAnnotationProperty();

   boolean isOWLDatatype();

   boolean isIRI();
}

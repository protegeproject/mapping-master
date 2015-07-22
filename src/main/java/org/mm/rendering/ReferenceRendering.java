package org.mm.rendering;

public interface ReferenceRendering extends Rendering
{
  String getRawValue();

  boolean isOWLLiteral();

  boolean isOWLEntity();

  boolean isOWLClass();

  boolean isOWLNamedIndividual();

  boolean isOWLObjectProperty();

  boolean isOWLDataProperty();

  boolean isOWLAnnotationProperty();

  boolean isOWLDatatype();
}

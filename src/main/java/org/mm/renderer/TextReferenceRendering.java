package org.mm.renderer;

public class TextReferenceRendering extends TextRendering implements ReferenceRendering
{
  private final String rawValue;

  public TextReferenceRendering(String rawValue)
  {
    super(rawValue);
    this.rawValue = rawValue;
  }

  @Override public String getRawValue()
  {
    return this.rawValue;
  }

  @Override public boolean isOWLLiteral()
  {
    return false; // TODO
  }

  @Override public boolean isOWLEntity()
  {
    return false; // TODO
  }

  @Override public boolean isOWLClass()
  {
    return false; // TODO
  }

  @Override public boolean isOWLNamedIndividual()
  {
    return false; // TODO
  }

  @Override public boolean isOWLObjectProperty()
  {
    return false; // TODO
  }

  @Override public boolean isOWLDataProperty()
  {
    return false; // TODO
  }

  @Override public boolean isOWLAnnotationProperty()
  {
    return false;// TODO
  }

  @Override public boolean isOWLDatatype()
  {
    return false;// TODO
  }
}

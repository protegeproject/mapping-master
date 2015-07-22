package org.mm.rendering.owlapi;

import org.mm.rendering.OWLLiteralRendering;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

public class OWLAPILiteralRendering extends OWLAPIRendering implements OWLLiteralRendering
{
  private final OWLLiteral literal;

  public OWLAPILiteralRendering(OWLLiteral literal)
  {
    super();
    this.literal = literal;
  }

  public OWLLiteral getOWLLiteral() { return this.literal; }

  @Override public String getRawValue()
  {
    return literal.getLiteral();
  }

  @Override public boolean isString()
  {
    return this.literal.getDatatype().isString();
  }

  @Override public boolean isBoolean()
  {
    return this.literal.getDatatype().isBoolean();
  }

  @Override public boolean isByte()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.BYTE.getIRI());
  }

  @Override public boolean isShort()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.SHORT.getIRI());
  }

  @Override public boolean isInt()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.INT.getIRI());
  }

  @Override public boolean isLong()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.LONG.getIRI());
  }

  @Override public boolean isFloat()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.FLOAT.getIRI());
  }

  @Override public boolean isDouble()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.DOUBLE.getIRI());
  }

  @Override public boolean isDate()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.DATE.getIRI());
  }

  @Override public boolean isTime()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.TIME.getIRI());
  }

  @Override public boolean isDateTime()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.DATE_TIME.getIRI());
  }

  @Override public boolean isDuration()
  {
    return this.literal.getDatatype().getIRI().equals(XSDVocabulary.DURATION.getIRI());
  }
}

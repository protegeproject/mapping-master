package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLLiteral;

public class OWLLiteralRendering extends OWLAPIRendering
{
  private final OWLLiteral literal;

  public OWLLiteralRendering(OWLLiteral literal)
  {
    super();
    this.literal = literal;
  }

  public OWLLiteral getOWLLiteral() { return this.literal; }
}

package org.mm.rendering.owlapi;

import org.semanticweb.owlapi.model.OWLClassExpression;

public class OWLClassExpressionRendering extends OWLAPIRendering
{
  private final OWLClassExpression classExpression;

  public OWLClassExpressionRendering(OWLClassExpression classExpression)
  {
    super();
    this.classExpression = classExpression;
  }

  public OWLClassExpression getOWLClassExpression() { return this.classExpression; }
}

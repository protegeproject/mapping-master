package org.mm.renderer.owlapi;

import org.mm.renderer.Rendering;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class OWLClassExpressionRendering extends Rendering
{
  private final OWLClassExpression classExpression;

  public OWLClassExpressionRendering(OWLClassExpression classExpression)
  {
    super();
    this.classExpression = classExpression;
  }

  public OWLClassExpression getOWLClassExpression() { return this.classExpression; }
}

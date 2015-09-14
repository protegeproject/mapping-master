package org.mm.rendering.owlapi;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

public class OWLClassExpressionRendering extends OWLAPIRendering
{
   private final OWLClassExpression classExpression;

   public OWLClassExpressionRendering(OWLClassExpression classExpression)
   {
      super();
      this.classExpression = classExpression;
   }

   public OWLClassExpressionRendering(OWLClassExpression classExpression, OWLAxiom axiom)
   {
      super(axiom);
      this.classExpression = classExpression;
   }

   public OWLClassExpressionRendering(OWLClassExpression classExpression, Set<OWLAxiom> axioms)
   {
      super(axioms);
      this.classExpression = classExpression;
   }

   public OWLClassExpression getOWLClassExpression()
   {
      return this.classExpression;
   }
}

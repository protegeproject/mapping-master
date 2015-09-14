package org.mm.rendering.owlapi;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLRestriction;

public class OWLRestrictionRendering extends OWLClassExpressionRendering
{
   private final OWLRestriction restriction;

   public OWLRestrictionRendering(OWLRestriction restriction)
   {
      super(restriction);
      this.restriction = restriction;
   }

   public OWLRestrictionRendering(OWLRestriction restriction, OWLAxiom axiom)
   {
      super(restriction, axiom);
      this.restriction = restriction;
   }

   public OWLRestrictionRendering(OWLRestriction restriction, Set<OWLAxiom> axioms)
   {
      super(restriction, axioms);
      this.restriction = restriction;
   }

   public OWLRestriction getOWLRestriction()
   {
      return this.restriction;
   }
}

package org.mm.rendering.owlapi;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class OWLNamedIndividualRendering extends OWLRendering
{
   private final OWLNamedIndividual individual;

   public OWLNamedIndividualRendering(OWLNamedIndividual individual)
   {
      super();
      this.individual = individual;
   }

   public OWLNamedIndividualRendering(OWLNamedIndividual individual, OWLAxiom axiom)
   {
      super(axiom);
      this.individual = individual;
   }

   public OWLNamedIndividualRendering(OWLNamedIndividual individual, Set<OWLAxiom> axioms)
   {
      super(axioms);
      this.individual = individual;
   }

   public OWLNamedIndividual getOWLNamedIndividual()
   {
      return this.individual;
   }
}

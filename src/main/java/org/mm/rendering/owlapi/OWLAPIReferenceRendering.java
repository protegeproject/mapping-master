package org.mm.rendering.owlapi;

import java.util.Set;

import org.mm.core.ReferenceType;
import org.mm.rendering.ReferenceRendering;
import org.semanticweb.owlapi.model.OWLAxiom;

public abstract class OWLAPIReferenceRendering extends OWLAPIRendering implements ReferenceRendering
{
   private ReferenceType type;

   public OWLAPIReferenceRendering(ReferenceType type) {
      super();
      this.type = type;
   }
   
   public OWLAPIReferenceRendering(Set<OWLAxiom> axioms, ReferenceType type) {
      super(axioms);
      this.type = type;
   }
   
   public OWLAPIReferenceRendering(OWLAxiom axiom, ReferenceType type) {
      super(axiom);
      this.type = type;
   }

   @Override
   public ReferenceType getReferenceType() {
      return type;
   }
}

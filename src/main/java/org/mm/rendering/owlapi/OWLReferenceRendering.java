package org.mm.rendering.owlapi;

import java.util.Set;

import org.mm.core.ReferenceType;
import org.mm.rendering.ReferenceRendering;
import org.semanticweb.owlapi.model.OWLAxiom;

public abstract class OWLReferenceRendering extends OWLRendering implements ReferenceRendering
{
   private ReferenceType type;

   public OWLReferenceRendering(ReferenceType type) {
      super();
      this.type = type;
   }
   
   public OWLReferenceRendering(Set<OWLAxiom> axioms, ReferenceType type) {
      super(axioms);
      this.type = type;
   }
   
   public OWLReferenceRendering(OWLAxiom axiom, ReferenceType type) {
      super(axiom);
      this.type = type;
   }

   @Override
   public ReferenceType getReferenceType() {
      return type;
   }
}

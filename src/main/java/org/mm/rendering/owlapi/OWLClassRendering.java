package org.mm.rendering.owlapi;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;

public class OWLClassRendering extends OWLClassExpressionRendering
{
   private final OWLClass cls;

   public OWLClassRendering(OWLClass cls)
   {
      super(cls);
      this.cls = cls;
   }

   public OWLClassRendering(OWLClass cls, OWLAxiom axiom)
   {
      super(cls, axiom);
      this.cls = cls;
   }

   public OWLClassRendering(OWLClass cls, Set<OWLAxiom> axioms)
   {
      super(cls, axioms);
      this.cls = cls;
   }

   public OWLClass getOWLClass()
   {
      return this.cls;
   }
}

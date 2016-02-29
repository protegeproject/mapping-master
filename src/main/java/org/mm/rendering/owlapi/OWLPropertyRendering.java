package org.mm.rendering.owlapi;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLProperty;

public abstract class OWLPropertyRendering extends OWLRendering
{
   private final OWLProperty property;

   public OWLPropertyRendering(OWLProperty property)
   {
      super();
      this.property = property;
   }

   public OWLPropertyRendering(OWLProperty property, OWLAxiom axiom)
   {
      super(axiom);
      this.property = property;
   }

   public OWLPropertyRendering(OWLProperty property, Set<OWLAxiom> axioms)
   {
      super(axioms);
      this.property = property;
   }

   public OWLProperty getOWLProperty()
   {
      return this.property;
   }
}

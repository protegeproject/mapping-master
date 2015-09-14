package org.mm.rendering.owlapi;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;

public class OWLDataPropertyRendering extends OWLPropertyRendering
{
   private final OWLDataProperty property;

   public OWLDataPropertyRendering(OWLDataProperty property)
   {
      super(property);
      this.property = property;
   }

   public OWLDataPropertyRendering(OWLDataProperty property, OWLAxiom axiom)
   {
      super(property, axiom);
      this.property = property;
   }

   public OWLDataPropertyRendering(OWLDataProperty property, Set<OWLAxiom> axioms)
   {
      super(property, axioms);
      this.property = property;
   }

   public OWLDataProperty getOWLDataProperty()
   {
      return this.property;
   }
}

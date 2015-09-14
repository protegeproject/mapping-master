package org.mm.rendering.owlapi;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class OWLObjectPropertyRendering extends OWLPropertyRendering
{
   private final OWLObjectProperty property;

   public OWLObjectPropertyRendering(OWLObjectProperty property)
   {
      super(property);
      this.property = property;
   }

   public OWLObjectPropertyRendering(OWLObjectProperty property, OWLAxiom axiom)
   {
      super(property, axiom);
      this.property = property;
   }

   public OWLObjectPropertyRendering(OWLObjectProperty property, Set<OWLAxiom> axioms)
   {
      super(property, axioms);
      this.property = property;
   }

   public OWLObjectProperty getOWLObjectProperty()
   {
      return this.property;
   }
}

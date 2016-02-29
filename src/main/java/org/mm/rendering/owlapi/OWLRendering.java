package org.mm.rendering.owlapi;

import org.mm.rendering.Rendering;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OWLRendering implements Rendering
{
   private final Set<OWLAxiom> axioms;

   public OWLRendering()
   {
      this.axioms = new HashSet<>();
   }

   public OWLRendering(Set<OWLAxiom> axioms)
   {
      this.axioms = new HashSet<>(axioms);
   }

   public OWLRendering(OWLAxiom axiom)
   {
      this.axioms = new HashSet<>();
      this.axioms.add(axiom);
   }

   @Override
   public String getRendering()
   {
      final StringBuffer sb = new StringBuffer();
      boolean newline = false;
      for (OWLAxiom axiom : getOWLAxioms()) {
         if (newline) {
            sb.append("\n");
         }
         sb.append(axiom.getAxiomType() + ": " + axiom.toString());
         newline = true;
      }
      return sb.toString();
   }

   public Set<OWLAxiom> getOWLAxioms()
   {
      return Collections.unmodifiableSet(this.axioms);
   }

   @Override
   public String toString()
   {
      return getRendering();
   }
}

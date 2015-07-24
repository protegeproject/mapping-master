package org.mm.rendering.owlapi;

import org.mm.rendering.Rendering;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OWLAPIRendering implements Rendering
{
  private final Set<OWLAxiom> axioms;

  public OWLAPIRendering()
  {
    this.axioms = new HashSet<>();
  }

  public OWLAPIRendering(Set<OWLAxiom> axioms)
  {
    this.axioms = new HashSet<>(axioms);
  }

  public OWLAPIRendering(OWLAxiom axiom)
  {
    this.axioms = new HashSet<>();
    this.axioms.add(axiom);
  }

  public Set<OWLAxiom> getOWLAxioms()
  {
    return Collections.unmodifiableSet(this.axioms);
  }

  @Override public String toString()
  {
    return "TODO"; // TODO Pretty print
  }
}

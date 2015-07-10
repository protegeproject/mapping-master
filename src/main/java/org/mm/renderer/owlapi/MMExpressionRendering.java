package org.mm.renderer.owlapi;

import org.mm.renderer.Rendering;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MMExpressionRendering extends Rendering
{
  private final Set<OWLAxiom> axioms;

  public MMExpressionRendering()
  {
    this.axioms = new HashSet<>();
  }

  public MMExpressionRendering(String initialTextRendering)
  {
    super(initialTextRendering);
    this.axioms = new HashSet<>();
  }

  public void addOWLAxiom(OWLAxiom axiom) { this.axioms.add(axiom); }

  public void addOWLAxioms(Set<OWLAxiom> axioms)
  {
    this.axioms.addAll(axioms);
  }

  public Set<OWLAxiom> getOWLAxioms()
  {
    return Collections.unmodifiableSet(this.axioms);
  }
}

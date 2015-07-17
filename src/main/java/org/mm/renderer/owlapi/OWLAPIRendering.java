package org.mm.renderer.owlapi;

import org.mm.renderer.Rendering;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OWLAPIRendering implements Rendering
{
  private final StringBuffer loggingText;
  private final Set<OWLAxiom> axioms;

  public OWLAPIRendering()
  {
    this.loggingText = new StringBuffer();
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

  // TODO Use real logger

  @Override public void log(String loggingText)
  {
    this.loggingText.append(loggingText);
    System.err.print(loggingText);
  }

  @Override public void logLine(String loggingText)
  {
    log(loggingText + "\n");
  }

  @Override public void addLoggingTextNewLine()
  {
    log("\n");
  }

  @Override public String getLoggingText()
  {
    return loggingText.toString();
  }

  @Override public String toString()
  {
    return "TODO"; // TODO
  }
}

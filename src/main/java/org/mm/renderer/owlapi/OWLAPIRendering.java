package org.mm.renderer.owlapi;

import org.mm.renderer.Rendering;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class OWLAPIRendering extends Rendering
{
	private final Set<OWLAxiom> axioms;

	public OWLAPIRendering()
	{
		this.axioms = new HashSet<>();
	}

	public OWLAPIRendering(String initialTextRendering)
	{
		super(initialTextRendering);
		this.axioms = new HashSet<>();
	}

	public void addAxiom(OWLAxiom axiom)
	{
		this.axioms.add(axiom);
	}

	public Set<OWLAxiom> getAxioms()
	{
		return Collections.unmodifiableSet(this.axioms);
	}
}

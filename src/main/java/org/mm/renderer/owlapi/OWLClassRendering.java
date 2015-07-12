package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLClass;

public class OWLClassRendering extends OWLAPIRendering
{
	private final OWLClass cls;

	public OWLClassRendering(OWLClass cls)
	{
		super();
		this.cls = cls;
	}

	public OWLClass getOWLClass() { return this.cls; }
}

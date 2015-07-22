package org.mm.rendering.owlapi;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class OWLNamedIndividualRendering extends OWLAPIRendering
{
	private final OWLNamedIndividual individual;

	public OWLNamedIndividualRendering(OWLNamedIndividual individual)
	{
		super();
		this.individual = individual;
	}

	public OWLNamedIndividual getOWLNamedIndividual() { return this.individual; }
}

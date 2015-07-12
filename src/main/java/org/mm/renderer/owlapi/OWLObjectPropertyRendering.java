package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLObjectProperty;

public class OWLObjectPropertyRendering extends OWLAPIRendering
{
	private final OWLObjectProperty property;

	public OWLObjectPropertyRendering(OWLObjectProperty property)
	{
		super();
		this.property = property;
	}

	public OWLObjectProperty getOWLObjectProperty() { return this.property; }
}

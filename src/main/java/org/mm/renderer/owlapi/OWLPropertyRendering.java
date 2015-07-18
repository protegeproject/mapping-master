package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLProperty;

public abstract class OWLPropertyRendering extends OWLAPIRendering
{
	private final OWLProperty property;

	public OWLPropertyRendering(OWLProperty property)
	{
		super();
		this.property = property;
	}

	public OWLProperty getOWLProperty() { return this.property; }
}

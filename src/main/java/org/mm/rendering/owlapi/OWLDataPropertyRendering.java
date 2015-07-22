package org.mm.rendering.owlapi;

import org.semanticweb.owlapi.model.OWLDataProperty;

public class OWLDataPropertyRendering extends OWLPropertyRendering
{
	private final OWLDataProperty property;

	public OWLDataPropertyRendering(OWLDataProperty property)
	{
		super(property);
		this.property = property;
	}

	public OWLDataProperty getOWLDataProperty() { return this.property; }
}

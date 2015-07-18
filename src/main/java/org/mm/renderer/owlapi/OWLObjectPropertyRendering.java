package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLObjectProperty;

public class OWLObjectPropertyRendering extends OWLPropertyRendering
{
	private final OWLObjectProperty property;

	public OWLObjectPropertyRendering(OWLObjectProperty property)
	{
		super(property);
		this.property = property;
	}

	public OWLObjectProperty getOWLObjectProperty() { return this.property; }
}

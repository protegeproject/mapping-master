package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;

public class OWLDataPropertyRendering extends OWLAPIRendering
{
	private final OWLDataProperty property;

	public OWLDataPropertyRendering(OWLDataProperty property)
	{
		super();
		this.property = property;
	}

	public OWLDataProperty getOWLDataProperty() { return this.property; }
}

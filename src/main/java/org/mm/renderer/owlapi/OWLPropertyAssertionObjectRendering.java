package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;

public class OWLPropertyAssertionObjectRendering extends OWLAPIRendering
{
	private final OWLPropertyAssertionObject property;

	public OWLPropertyAssertionObjectRendering(OWLPropertyAssertionObject propertyAssertionObject)
	{
		super();
		this.property = propertyAssertionObject;
	}

	public OWLPropertyAssertionObject getOWLPropertyAssertionObject() { return this.property; }
}

package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLAnnotationValue;

public class OWLAnnotationValueRendering extends OWLAPIRendering
{
	private final OWLAnnotationValue annotationValue;

	public OWLAnnotationValueRendering(OWLAnnotationValue annotationValue)
	{
		super();
		this.annotationValue = annotationValue;
	}

	public OWLAnnotationValue getOWLAnnotationValue() { return this.annotationValue; }
}

package org.mm.rendering.owlapi;

import org.semanticweb.owlapi.model.OWLClass;

public class OWLClassRendering extends OWLClassExpressionRendering
{
	private final OWLClass cls;

	public OWLClassRendering(OWLClass cls)
	{
		super(cls);
		this.cls = cls;
	}

	public OWLClass getOWLClass() { return this.cls; }
}

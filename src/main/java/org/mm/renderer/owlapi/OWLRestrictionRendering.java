package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLRestriction;

public class OWLRestrictionRendering extends OWLClassExpressionRendering
{
	private final OWLRestriction restriction;

	public OWLRestrictionRendering(OWLRestriction restriction)
	{
		super(restriction);
		this.restriction = restriction;
	}

	public OWLRestriction getOWLRestriction() { return this.restriction; }
}

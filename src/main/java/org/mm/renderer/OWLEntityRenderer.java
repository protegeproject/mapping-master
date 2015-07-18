package org.mm.renderer;

import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyNode;

import java.util.Optional;

public interface OWLEntityRenderer
{
	Optional<? extends Rendering> renderOWLClass(OWLClassNode classNode) throws RendererException;

	Optional<? extends Rendering> renderOWLNamedIndividual(OWLNamedIndividualNode namedIndividualNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLProperty(OWLPropertyNode propertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLObjectProperty(OWLPropertyNode propertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLDataProperty(OWLPropertyNode propertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode) throws RendererException;
}

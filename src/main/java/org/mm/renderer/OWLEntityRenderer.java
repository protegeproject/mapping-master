package org.mm.renderer;

import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.rendering.Rendering;

import java.util.Optional;

public interface OWLEntityRenderer
{
	Optional<? extends Rendering> renderOWLClass(OWLClassNode node) throws RendererException;

	Optional<? extends Rendering> renderOWLNamedIndividual(OWLNamedIndividualNode node) throws RendererException;

	Optional<? extends Rendering> renderOWLProperty(OWLPropertyNode node) throws RendererException;

	Optional<? extends Rendering> renderOWLObjectProperty(OWLPropertyNode node) throws RendererException;

	Optional<? extends Rendering> renderOWLDataProperty(OWLPropertyNode node) throws RendererException;

	Optional<? extends Rendering> renderOWLAnnotationProperty(OWLPropertyNode node) throws RendererException;

	Optional<? extends Rendering> renderOWLPropertyAssertionObject(OWLPropertyAssertionObjectNode node) throws RendererException;

	Optional<? extends Rendering> renderOWLAnnotationValue(OWLAnnotationValueNode node) throws RendererException;
}

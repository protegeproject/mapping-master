package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.OWLAnnotationPropertyNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyAssertionNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.rendering.Rendering;

public interface EntityRenderer
{
   Optional<? extends Rendering> renderOWLClass(OWLClassNode node, boolean isDeclaration) throws RendererException;

   Optional<? extends Rendering> renderOWLNamedIndividual(OWLNamedIndividualNode node, boolean isDeclaration) throws RendererException;

   Optional<? extends Rendering> renderOWLProperty(OWLPropertyNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLObjectProperty(OWLPropertyNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLDataProperty(OWLPropertyNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLAnnotationProperty(OWLAnnotationPropertyNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLPropertyAssertion(OWLPropertyAssertionNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLAnnotationValue(OWLAnnotationValueNode node) throws RendererException;
}

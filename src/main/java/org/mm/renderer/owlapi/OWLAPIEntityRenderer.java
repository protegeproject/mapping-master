package org.mm.renderer.owlapi;

import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLEntityRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.owlapi.OWLAnnotationPropertyRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLDataPropertyRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.mm.rendering.owlapi.OWLObjectPropertyRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Optional;

// TODO Implement methods

public class OWLAPIEntityRenderer implements OWLEntityRenderer
{
  @Override public Optional<OWLClassRendering> renderOWLClass(OWLClassNode classNode) throws RendererException
  {
    if (classNode.hasNameNode()) {
      OWLClass cls = null; // TODO Implement
      return Optional.of(new OWLClassRendering(cls));

    } else if (classNode.hasReferenceNode()) {
      OWLClass cls = null; // TODO Implement
      return Optional.of(new OWLClassRendering(cls));

    } else
      throw new InternalRendererException("unknown child for node " + classNode.getNodeName());
  }

  @Override public Optional<OWLNamedIndividualRendering> renderOWLNamedIndividual(
    OWLNamedIndividualNode namedIndividualNode) throws RendererException
  {
    if (namedIndividualNode.hasNameNode()) {
      OWLNamedIndividual individual = null; // TODO Implement

      return Optional.of(new OWLNamedIndividualRendering(individual));

    } else if (namedIndividualNode.hasReferenceNode()) {
      OWLNamedIndividual individual = null; // TODO Implement

      return Optional.of(new OWLNamedIndividualRendering(individual));
    } else
      throw new InternalRendererException("unknown child for node " + namedIndividualNode.getNodeName());
  }

  @Override public Optional<OWLPropertyRendering> renderOWLProperty(OWLPropertyNode propertyNode)
    throws RendererException
  {
    OWLObjectProperty property = null; // TODO Implement

    return Optional.of(new OWLObjectPropertyRendering(property));
  }

  @Override public Optional<OWLObjectPropertyRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode)
    throws RendererException
  {
    OWLObjectProperty objectProperty = null; // TODO Implement

    if (propertyNode.hasNameNode()) {

      return Optional.of(new OWLObjectPropertyRendering(objectProperty));

    } else if (propertyNode.hasReferenceNode()) {

      return Optional.of(new OWLObjectPropertyRendering(objectProperty));
    } else
      throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
  }

  @Override public Optional<OWLDataPropertyRendering> renderOWLDataProperty(OWLPropertyNode propertyNode)
    throws RendererException
  {
    OWLDataProperty dataProperty = null; // TODO Implement

    if (propertyNode.hasNameNode()) {

      return Optional.of(new OWLDataPropertyRendering(dataProperty));

    } else if (propertyNode.hasReferenceNode()) {

      return Optional.of(new OWLDataPropertyRendering(dataProperty));
    } else
      throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
  }

  @Override public Optional<OWLAnnotationPropertyRendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode)
    throws RendererException
  {
    OWLAnnotationProperty annotationProperty = null; // TODO Implement

    if (propertyNode.hasNameNode()) {

      return Optional.of(new OWLAnnotationPropertyRendering(annotationProperty));

    } else if (propertyNode.hasReferenceNode()) {

      return Optional.of(new OWLAnnotationPropertyRendering(annotationProperty));
    } else
      throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
  }
}
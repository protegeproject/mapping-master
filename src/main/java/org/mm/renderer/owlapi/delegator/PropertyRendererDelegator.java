package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAnnotationPropertyNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLObjectFactory;
import org.mm.renderer.owlapi.OWLReferenceRenderer;
import org.mm.rendering.owlapi.OWLEntityReferenceRendering;
import org.mm.rendering.owlapi.OWLLiteralReferenceRendering;
import org.mm.rendering.owlapi.OWLReferenceRendering;
import org.mm.rendering.owlapi.OWLAnnotationPropertyRendering;
import org.mm.rendering.owlapi.OWLDataPropertyRendering;
import org.mm.rendering.owlapi.OWLObjectPropertyRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLProperty;

public class PropertyRendererDelegator implements RendererDelegator<OWLPropertyRendering>
{
   private OWLReferenceRenderer referenceRenderer;

   public PropertyRendererDelegator(OWLReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
   }

   @Override
   public Optional<OWLPropertyRendering> render(TypeNode typeNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      if (typeNode instanceof OWLPropertyNode) {
         OWLPropertyNode propertyNode = (OWLPropertyNode) typeNode;
         if (propertyNode.hasNameNode()) {
            return renderNameNode(propertyNode.getNameNode(), typeNode, objectFactory);
         } else if (propertyNode.hasReferenceNode()) {
            return renderReferenceNode(propertyNode.getReferenceNode(), objectFactory);
         }
      }
      throw new RendererException("Node " + typeNode + " is not an OWL property");
   }

   private Optional<OWLPropertyRendering> renderNameNode(NameNode nameNode, TypeNode typeNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      OWLPropertyRendering propertyRendering = null;
      if (typeNode instanceof OWLAnnotationPropertyNode) {
         OWLProperty prop = objectFactory.getAndCheckOWLAnnotationProperty(nameNode.getName());
         propertyRendering = new OWLAnnotationPropertyRendering((OWLAnnotationProperty) prop);
      } else {
         OWLProperty prop = objectFactory.getAndCheckOWLProperty(nameNode.getName());
         if (prop instanceof OWLObjectProperty) {
            propertyRendering = new OWLObjectPropertyRendering((OWLObjectProperty) prop);
         } else if (prop instanceof OWLDataProperty) {
            propertyRendering = new OWLDataPropertyRendering((OWLDataProperty) prop);
         }
      }
      return Optional.ofNullable(propertyRendering);
   }

   private Optional<OWLPropertyRendering> renderReferenceNode(ReferenceNode referenceNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      OWLPropertyRendering propertyRendering = null;
      Optional<OWLReferenceRendering> rendering = referenceRenderer.renderReference(referenceNode);
      if (rendering.isPresent()) {
         OWLReferenceRendering referenceRendering = rendering.get();
         if (referenceRendering instanceof OWLLiteralReferenceRendering) {
            OWLLiteralReferenceRendering literalRendering = (OWLLiteralReferenceRendering) referenceRendering;
            if (literalRendering.isOWLObjectProperty()) {
               OWLObjectProperty op = objectFactory.getAndCheckOWLObjectProperty(literalRendering.getRawValue());
               propertyRendering = new OWLObjectPropertyRendering(op, literalRendering.getOWLAxioms());
            } else if (literalRendering.isOWLDataProperty()) {
               OWLDataProperty dp = objectFactory.getAndCheckOWLDataProperty(literalRendering.getRawValue());
               propertyRendering = new OWLDataPropertyRendering(dp, literalRendering.getOWLAxioms());
            } else if (literalRendering.isOWLAnnotationProperty()) {
               OWLAnnotationProperty ap = objectFactory.getAndCheckOWLAnnotationProperty(literalRendering.getRawValue());
               propertyRendering = new OWLAnnotationPropertyRendering(ap);
            } else {
               /*
                * By default, the property type for literal reference node is an object property.
                */
               OWLObjectProperty op = objectFactory.getAndCheckOWLObjectProperty(literalRendering.getRawValue());
               propertyRendering = new OWLObjectPropertyRendering(op, literalRendering.getOWLAxioms());
            }
         } else if (referenceRendering instanceof OWLEntityReferenceRendering) {
            OWLEntityReferenceRendering entityRendering = (OWLEntityReferenceRendering) referenceRendering;
            if (entityRendering.isOWLObjectProperty()) {
               OWLObjectProperty op = entityRendering.getOWLEntity().asOWLObjectProperty();
               propertyRendering = new OWLObjectPropertyRendering(op, entityRendering.getOWLAxioms());
            } else if (entityRendering.isOWLDataProperty()) {
               OWLDataProperty dp = entityRendering.getOWLEntity().asOWLDataProperty();
               propertyRendering = new OWLDataPropertyRendering(dp, entityRendering.getOWLAxioms());
            } else if (entityRendering.isOWLAnnotationProperty()) {
               OWLAnnotationProperty ap = entityRendering.getOWLEntity().asOWLAnnotationProperty();
               propertyRendering = new OWLAnnotationPropertyRendering(ap);
            } else {
               throw new RendererException("Reference node " + referenceNode + " is not an OWL property");
            }
         }
      }
      return Optional.ofNullable(propertyRendering);
   }
}

package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLAPIObjectHandler;
import org.mm.renderer.owlapi.OWLAPIReferenceRenderer;
import org.mm.rendering.owlapi.OWLAPIEntityReferenceRendering;
import org.mm.rendering.owlapi.OWLAPILiteralReferenceRendering;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
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
   private OWLAPIReferenceRenderer referenceRenderer;

   public PropertyRendererDelegator(OWLAPIReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
   }

   @Override
   public Optional<OWLPropertyRendering> render(TypeNode typeNode, OWLAPIObjectHandler objectFactory)
         throws RendererException
   {
      if (typeNode instanceof OWLPropertyNode) {
         OWLPropertyNode propertyNode = (OWLPropertyNode) typeNode;
         if (propertyNode.hasNameNode()) {
            return renderNameNode(propertyNode.getNameNode(), objectFactory);
         } else if (propertyNode.hasReferenceNode()) {
            return renderReferenceNode(propertyNode.getReferenceNode(), objectFactory);
         }
      }
      throw new RendererException("Node " + typeNode + " is not an OWL property");
   }

   private Optional<OWLPropertyRendering> renderNameNode(NameNode nameNode, OWLAPIObjectHandler objectFactory)
         throws RendererException
   {
      OWLPropertyRendering propertyRendering = null;
      OWLProperty prop = objectFactory.getAndCheckOWLProperty(nameNode.getName());
      if (prop instanceof OWLObjectProperty) {
         propertyRendering = new OWLObjectPropertyRendering((OWLObjectProperty) prop);
      } else if (prop instanceof OWLDataProperty) {
         propertyRendering = new OWLDataPropertyRendering((OWLDataProperty) prop);
      } else if (prop instanceof OWLAnnotationProperty) {
         propertyRendering = new OWLAnnotationPropertyRendering((OWLAnnotationProperty) prop);
      }
      return Optional.ofNullable(propertyRendering);
   }

   private Optional<OWLPropertyRendering> renderReferenceNode(ReferenceNode referenceNode, OWLAPIObjectHandler objectFactory)
         throws RendererException
   {
      OWLPropertyRendering propertyRendering = null;
      Optional<OWLAPIReferenceRendering> rendering = referenceRenderer.renderReference(referenceNode);
      if (rendering.isPresent()) {
         OWLAPIReferenceRendering referenceRendering = rendering.get();
         if (referenceRendering instanceof OWLAPILiteralReferenceRendering) {
            OWLAPILiteralReferenceRendering literalRendering = (OWLAPILiteralReferenceRendering) referenceRendering;
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
         } else if (referenceRendering instanceof OWLAPIEntityReferenceRendering) {
            OWLAPIEntityReferenceRendering entityRendering = (OWLAPIEntityReferenceRendering) referenceRendering;
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

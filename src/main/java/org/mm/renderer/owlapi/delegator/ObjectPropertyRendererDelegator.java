package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLObjectPropertyNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLObjectFactory;
import org.mm.renderer.owlapi.OWLReferenceRenderer;
import org.mm.rendering.owlapi.OWLEntityReferenceRendering;
import org.mm.rendering.owlapi.OWLLiteralReferenceRendering;
import org.mm.rendering.owlapi.OWLObjectPropertyRendering;
import org.mm.rendering.owlapi.OWLReferenceRendering;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class ObjectPropertyRendererDelegator implements RendererDelegator<OWLObjectPropertyRendering>
{
   private OWLReferenceRenderer referenceRenderer;

   public ObjectPropertyRendererDelegator(OWLReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
   }

   @Override
   public Optional<OWLObjectPropertyRendering> render(TypeNode typeNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      if (typeNode instanceof OWLObjectPropertyNode) {
         OWLObjectPropertyNode objectPropertyNode = (OWLObjectPropertyNode) typeNode;
         if (objectPropertyNode.hasNameNode()) {
            return renderNameNode(objectPropertyNode.getNameNode(), objectFactory);
         } else if (objectPropertyNode.hasReferenceNode()) {
            return renderReferenceNode(objectPropertyNode.getReferenceNode(), objectFactory);
         }
      }
      throw new RendererException("Node " + typeNode + " is not an OWL object property");
   }

   private Optional<OWLObjectPropertyRendering> renderNameNode(NameNode nameNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      OWLObjectProperty op = objectFactory.getAndCheckOWLObjectProperty(nameNode.getName());
      return Optional.of(new OWLObjectPropertyRendering(op));
   }

   private Optional<OWLObjectPropertyRendering> renderReferenceNode(ReferenceNode referenceNode,
         OWLObjectFactory objectFactory) throws RendererException
   {
      OWLObjectPropertyRendering objectPropertyRendering = null;
      Optional<? extends OWLReferenceRendering> rendering = referenceRenderer.renderReference(referenceNode);
      if (rendering.isPresent()) {
         OWLReferenceRendering referenceRendering = rendering.get();
         if (referenceRendering instanceof OWLLiteralReferenceRendering) {
            OWLLiteralReferenceRendering literalRendering = (OWLLiteralReferenceRendering) referenceRendering;
            OWLObjectProperty op = objectFactory.getAndCheckOWLObjectProperty(literalRendering.getRawValue());
            objectPropertyRendering = new OWLObjectPropertyRendering(op, literalRendering.getOWLAxioms());
         } else if (referenceRendering instanceof OWLEntityReferenceRendering) {
            OWLEntityReferenceRendering entityRendering = (OWLEntityReferenceRendering) referenceRendering;
            if (entityRendering.isOWLClass()) {
            	OWLObjectProperty op = entityRendering.getOWLEntity().asOWLObjectProperty();
               objectPropertyRendering = new OWLObjectPropertyRendering(op, entityRendering.getOWLAxioms());
            } else {
               throw new RendererException("Reference node " + referenceNode + " is not an OWL object property");
            }
         }
      }
      return Optional.ofNullable(objectPropertyRendering);
   }
}

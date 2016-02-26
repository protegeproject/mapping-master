package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLAPIObjectFactory;
import org.mm.renderer.owlapi.OWLAPIReferenceRenderer;
import org.mm.rendering.owlapi.OWLAPIEntityReferenceRendering;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class NamedIndividualRendererDelegator implements RendererDelegator<OWLNamedIndividualRendering>
{
   private OWLAPIReferenceRenderer referenceRenderer;

   public NamedIndividualRendererDelegator(OWLAPIReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
   }

   @Override
   public Optional<OWLNamedIndividualRendering> render(TypeNode typeNode, OWLAPIObjectFactory objectFactory)
         throws RendererException
   {
      if (typeNode instanceof OWLNamedIndividualNode) {
         OWLNamedIndividualNode namedIndividualNode = (OWLNamedIndividualNode) typeNode;
         if (namedIndividualNode.hasNameNode()) {
            return renderNameNode(namedIndividualNode.getNameNode(), objectFactory);
         } else if (namedIndividualNode.hasReferenceNode()) {
            return renderReferenceNode(namedIndividualNode.getReferenceNode(), objectFactory);
         }
      }
      throw new RendererException("Node " + typeNode + " is not an OWL named individual");
   }

   private Optional<OWLNamedIndividualRendering> renderNameNode(NameNode nameNode, OWLAPIObjectFactory objectFactory)
         throws RendererException
   {
      OWLNamedIndividual ind = objectFactory.getOWLNamedIndividual(nameNode.getName());
      return Optional.of(new OWLNamedIndividualRendering(ind));
   }

   private Optional<OWLNamedIndividualRendering> renderReferenceNode(ReferenceNode referenceNode,
         OWLAPIObjectFactory objectFactory) throws RendererException
   {
      OWLNamedIndividualRendering namedIndividualRendering = null;
      Optional<OWLAPIReferenceRendering> rendering = referenceRenderer.renderReference(referenceNode);
      if (rendering.isPresent()) {
         OWLAPIReferenceRendering referenceRendering = rendering.get();
         if (referenceRendering instanceof OWLAPIEntityReferenceRendering) {
            OWLAPIEntityReferenceRendering entityRendering = (OWLAPIEntityReferenceRendering) referenceRendering;
            if (entityRendering.isOWLNamedIndividual()) {
               OWLNamedIndividual ind = entityRendering.getOWLEntity().asOWLNamedIndividual();
               namedIndividualRendering = new OWLNamedIndividualRendering(ind, entityRendering.getOWLAxioms());
            } else {
               throw new RendererException("Reference value " + referenceNode + " is not an OWL named individual");
            }
         }
      }
      return Optional.ofNullable(namedIndividualRendering);
   }
}

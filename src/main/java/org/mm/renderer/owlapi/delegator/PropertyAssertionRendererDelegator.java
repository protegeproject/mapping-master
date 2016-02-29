package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLPropertyAssertionNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLLiteralRenderer;
import org.mm.renderer.owlapi.OWLObjectFactory;
import org.mm.renderer.owlapi.OWLReferenceRenderer;
import org.mm.rendering.owlapi.OWLEntityReferenceRendering;
import org.mm.rendering.owlapi.OWLLiteralReferenceRendering;
import org.mm.rendering.owlapi.OWLLiteralRendering;
import org.mm.rendering.owlapi.OWLReferenceRendering;
import org.mm.rendering.owlapi.OWLPropertyAssertionRendering;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class PropertyAssertionRendererDelegator implements RendererDelegator<OWLPropertyAssertionRendering>
{
   private OWLReferenceRenderer referenceRenderer;
   private OWLLiteralRenderer literalRenderer;

   public PropertyAssertionRendererDelegator(OWLReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
      this.literalRenderer = referenceRenderer.getLiteralRenderer();
   }

   @Override
   public Optional<OWLPropertyAssertionRendering> render(TypeNode typeNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      if (typeNode instanceof OWLPropertyAssertionNode) {
         OWLPropertyAssertionNode propertyAssertionNode = (OWLPropertyAssertionNode) typeNode;
         if (propertyAssertionNode.hasNameNode()) {
            return renderNameNode(propertyAssertionNode.getNameNode(), objectFactory);
         } else if (propertyAssertionNode.hasReferenceNode()) {
            return renderReferenceNode(propertyAssertionNode.getReferenceNode(), objectFactory);
         } else if (propertyAssertionNode.hasLiteralNode()) {
            return renderLiteralNode(propertyAssertionNode.getOWLLiteralNode(), objectFactory);
         }
      }
      throw new RendererException("Node " + typeNode + " is not an OWL property assertion");
   }

   private Optional<OWLPropertyAssertionRendering> renderNameNode(NameNode nameNode,
         OWLObjectFactory objectFactory) throws RendererException
   {
      OWLNamedIndividual ind = objectFactory.getAndCheckOWLNamedIndividual(nameNode.getName());
      return Optional.of(new OWLPropertyAssertionRendering(ind));
   }

   private Optional<OWLPropertyAssertionRendering> renderReferenceNode(ReferenceNode referenceNode,
         OWLObjectFactory objectFactory) throws RendererException
   {
      OWLPropertyAssertionRendering assertionRendering = null;
      Optional<OWLReferenceRendering> rendering = referenceRenderer.renderReference(referenceNode);
      if (rendering.isPresent()) {
         OWLReferenceRendering referenceRendering = rendering.get();
         if (referenceRendering instanceof OWLLiteralReferenceRendering) {
            OWLLiteralReferenceRendering literalRendering = (OWLLiteralReferenceRendering) referenceRendering;
            if (literalRendering.isOWLLiteral()) {
               OWLLiteral lit = literalRendering.getOWLLiteral();
               assertionRendering = new OWLPropertyAssertionRendering(lit);
            } else {
               throw new RendererException("Reference node " + referenceNode + " is not an OWL literal");
            }
         } else if (referenceRendering instanceof OWLEntityReferenceRendering) {
            OWLEntityReferenceRendering entityRendering = (OWLEntityReferenceRendering) referenceRendering;
            if (entityRendering.isOWLNamedIndividual()) {
               OWLNamedIndividual ind = entityRendering.getOWLEntity().asOWLNamedIndividual();
               assertionRendering = new OWLPropertyAssertionRendering(ind);
            } else {
               throw new RendererException("Reference node " + referenceNode + " is not an OWL named individual");
            }
         }
      }
      return Optional.ofNullable(assertionRendering);
   }

   private Optional<OWLPropertyAssertionRendering> renderLiteralNode(OWLLiteralNode literalNode,
         OWLObjectFactory objectFactory) throws RendererException
   {
      OWLPropertyAssertionRendering assertionRendering = null;
      Optional<OWLLiteralRendering> rendering = literalRenderer.renderOWLLiteral(literalNode);
      if (rendering.isPresent()) {
         OWLLiteral lit = rendering.get().getOWLLiteral();
         assertionRendering = new OWLPropertyAssertionRendering(lit);
      }
      return Optional.ofNullable(assertionRendering);
   }
}

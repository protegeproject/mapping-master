package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLObjectFactory;
import org.mm.renderer.owlapi.OWLReferenceRenderer;
import org.mm.rendering.owlapi.OWLEntityReferenceRendering;
import org.mm.rendering.owlapi.OWLLiteralReferenceRendering;
import org.mm.rendering.owlapi.OWLReferenceRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.semanticweb.owlapi.model.OWLClass;

public class ClassRendererDelegator implements RendererDelegator<OWLClassRendering>
{
   private OWLReferenceRenderer referenceRenderer;

   public ClassRendererDelegator(OWLReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
   }

   @Override
   public Optional<OWLClassRendering> render(TypeNode typeNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      if (typeNode instanceof OWLClassNode) {
         OWLClassNode classNode = (OWLClassNode) typeNode;
         if (classNode.hasNameNode()) {
            return renderNameNode(classNode.getNameNode(), objectFactory);
         } else if (classNode.hasReferenceNode()) {
            return renderReferenceNode(classNode.getReferenceNode(), objectFactory);
         }
      }
      throw new RendererException("Node " + typeNode + " is not an OWL class");
   }

   private Optional<OWLClassRendering> renderNameNode(NameNode nameNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      OWLClass cls = objectFactory.getAndCheckOWLClass(nameNode.getName());
      return Optional.of(new OWLClassRendering(cls));
   }

   private Optional<OWLClassRendering> renderReferenceNode(ReferenceNode referenceNode,
         OWLObjectFactory objectFactory) throws RendererException
   {
      OWLClassRendering classRendering = null;
      Optional<? extends OWLReferenceRendering> rendering = referenceRenderer.renderReference(referenceNode);
      if (rendering.isPresent()) {
         OWLReferenceRendering referenceRendering = rendering.get();
         if (referenceRendering instanceof OWLLiteralReferenceRendering) {
            OWLLiteralReferenceRendering literalRendering = (OWLLiteralReferenceRendering) referenceRendering;
            OWLClass cls = objectFactory.getAndCheckOWLClass(literalRendering.getRawValue());
            classRendering = new OWLClassRendering(cls, literalRendering.getOWLAxioms());
         } else if (referenceRendering instanceof OWLEntityReferenceRendering) {
            OWLEntityReferenceRendering entityRendering = (OWLEntityReferenceRendering) referenceRendering;
            if (entityRendering.isOWLClass()) {
               OWLClass cls = entityRendering.getOWLEntity().asOWLClass();
               classRendering = new OWLClassRendering(cls, entityRendering.getOWLAxioms());
            } else {
               throw new RendererException("Reference node " + referenceNode + " is not an OWL class");
            }
         }
      }
      return Optional.ofNullable(classRendering);
   }
}

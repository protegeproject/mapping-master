package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.IRIRefNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLLiteralRenderer;
import org.mm.renderer.owlapi.OWLObjectFactory;
import org.mm.renderer.owlapi.OWLReferenceRenderer;
import org.mm.rendering.owlapi.OWLAnnotationValueRendering;
import org.mm.rendering.owlapi.OWLIRIReferenceRendering;
import org.mm.rendering.owlapi.OWLIRIRendering;
import org.mm.rendering.owlapi.OWLLiteralReferenceRendering;
import org.mm.rendering.owlapi.OWLLiteralRendering;
import org.mm.rendering.owlapi.OWLReferenceRendering;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

public class AnnotationValueRendererDelegator implements RendererDelegator<OWLAnnotationValueRendering>
{
   private OWLReferenceRenderer referenceRenderer;
   private OWLLiteralRenderer literalRenderer;

   public AnnotationValueRendererDelegator(OWLReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
      this.literalRenderer = referenceRenderer.getLiteralRenderer();
   }

   @Override
   public Optional<OWLAnnotationValueRendering> render(TypeNode typeNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      if (typeNode instanceof OWLAnnotationValueNode) {
         OWLAnnotationValueNode annotationValueNode = (OWLAnnotationValueNode) typeNode;
         if (annotationValueNode.hasNameNode()) {
            return renderNameNode(annotationValueNode.getNameNode(), objectFactory);
         } else if (annotationValueNode.hasReferenceNode()) {
            return renderReferenceNode(annotationValueNode.getReferenceNode(), objectFactory);
         } else if (annotationValueNode.hasLiteralNode()) {
            return renderLiteralNode(annotationValueNode.getOWLLiteralNode(), objectFactory);
         } else if (annotationValueNode.hasIRIRefNode()) {
            return renderIRIRefNode(annotationValueNode.getIRIRefNode(), objectFactory);
         }
      }
      throw new RendererException("Node " + typeNode + " is not an OWL annotation value");
   }

   private Optional<OWLAnnotationValueRendering> renderNameNode(NameNode nameNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      OWLLiteral lit = objectFactory.createOWLLiteralString(nameNode.getName());
      return Optional.of(new OWLLiteralRendering(lit));
   }

   private Optional<OWLAnnotationValueRendering> renderReferenceNode(ReferenceNode referenceNode,
         OWLObjectFactory objectFactory) throws RendererException
   {
      OWLAnnotationValueRendering valueRendering = null;
      Optional<OWLReferenceRendering> rendering = referenceRenderer.renderReference(referenceNode);
      if (rendering.isPresent()) {
         OWLReferenceRendering referenceRendering = rendering.get();
         if (referenceRendering instanceof OWLLiteralReferenceRendering) {
            OWLLiteralReferenceRendering literalRendering = (OWLLiteralReferenceRendering) referenceRendering;
            OWLLiteral lit = literalRendering.getOWLLiteral();
            valueRendering = new OWLLiteralRendering(lit);
         } else if (referenceRendering instanceof OWLIRIReferenceRendering) {
            OWLIRIReferenceRendering iriRendering = (OWLIRIReferenceRendering) referenceRendering;
            IRI iri = iriRendering.getIRI();
            valueRendering = new OWLIRIRendering(iri);
         } else {
            /*
             * XXX: Need to implement: annotation value as anonymous individual
             */
            throw new RendererException("Reference node " + referenceNode + " is not an OWL literal or IRI");
         }
      }
      return Optional.ofNullable(valueRendering);
   }

   private Optional<OWLAnnotationValueRendering> renderLiteralNode(OWLLiteralNode literalNode,
         OWLObjectFactory objectFactory) throws RendererException
   {
      OWLAnnotationValueRendering valueRendering = null;
      Optional<OWLLiteralRendering> rendering = literalRenderer.renderOWLLiteral(literalNode);
      if (rendering.isPresent()) {
         OWLLiteral lit = rendering.get().getOWLLiteral();
         valueRendering = new OWLLiteralRendering(lit);
      }
      return Optional.ofNullable(valueRendering);
   }

   private Optional<OWLAnnotationValueRendering> renderIRIRefNode(IRIRefNode iriRefNode, OWLObjectFactory objectFactory)
         throws RendererException
   {
      IRI iri = objectFactory.createIri(iriRefNode.getValue());
      return Optional.of(new OWLIRIRendering(iri));
   }
}

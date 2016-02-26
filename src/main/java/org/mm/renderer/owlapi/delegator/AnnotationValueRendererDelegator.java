package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLAPILiteralRenderer;
import org.mm.renderer.owlapi.OWLAPIObjectHandler;
import org.mm.renderer.owlapi.OWLAPIReferenceRenderer;
import org.mm.rendering.owlapi.OWLAPILiteralReferenceRendering;
import org.mm.rendering.owlapi.OWLAPILiteralRendering;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
import org.mm.rendering.owlapi.OWLAnnotationValueRendering;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;

public class AnnotationValueRendererDelegator implements RendererDelegator<OWLAnnotationValueRendering>
{
   private OWLAPIReferenceRenderer referenceRenderer;
   private OWLAPILiteralRenderer literalRenderer;

   public AnnotationValueRendererDelegator(OWLAPIReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
      this.literalRenderer = referenceRenderer.getLiteralRenderer();
   }

   @Override
   public Optional<OWLAnnotationValueRendering> render(TypeNode typeNode, OWLAPIObjectHandler objectFactory)
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
         }
      }
      throw new RendererException("Node " + typeNode + " is not an OWL annotation value");
   }

   private Optional<OWLAnnotationValueRendering> renderNameNode(NameNode nameNode, OWLAPIObjectHandler objectFactory)
         throws RendererException
   {
      OWLAnnotationValue anno = objectFactory.getOWLAnnotationValue(nameNode.getName(), false);
      return Optional.of(new OWLAnnotationValueRendering(anno));
   }

   private Optional<OWLAnnotationValueRendering> renderReferenceNode(ReferenceNode referenceNode,
         OWLAPIObjectHandler objectFactory) throws RendererException
   {
      OWLAnnotationValueRendering valueRendering = null;
      Optional<OWLAPIReferenceRendering> rendering = referenceRenderer.renderReference(referenceNode);
      if (rendering.isPresent()) {
         OWLAPIReferenceRendering referenceRendering = rendering.get();
         if (referenceRendering instanceof OWLAPILiteralReferenceRendering) {
            OWLAPILiteralReferenceRendering literalRendering = (OWLAPILiteralReferenceRendering) referenceRendering;
            OWLLiteral lit = literalRendering.getOWLLiteral();
            valueRendering = new OWLAnnotationValueRendering(lit);
         } else {
            /*
             * XXX: Need to implement: annotation value can also be IRI or anonymous individual
             */
            throw new RendererException("Reference node " + referenceNode + " is not an OWL literal");
         }
      }
      return Optional.ofNullable(valueRendering);
   }

   private Optional<OWLAnnotationValueRendering> renderLiteralNode(OWLLiteralNode literalNode,
         OWLAPIObjectHandler objectFactory) throws RendererException
   {
      OWLAnnotationValueRendering valueRendering = null;
      Optional<OWLAPILiteralRendering> rendering = literalRenderer.renderOWLLiteral(literalNode);
      if (rendering.isPresent()) {
         OWLLiteral lit = rendering.get().getOWLLiteral();
         OWLAnnotationValue av;
         if (lit.isFloat()) {
            av = objectFactory.getOWLAnnotationValue(lit.parseFloat());
         } else if (lit.isDouble()) {
            av = objectFactory.getOWLAnnotationValue(lit.parseDouble());
         } else if (lit.isInteger()) {
            av = objectFactory.getOWLAnnotationValue(lit.parseInteger());
         } else if (lit.isBoolean()) {
            av = objectFactory.getOWLAnnotationValue(lit.parseBoolean());
         } else if (lit.isRDFPlainLiteral()) {
            av = objectFactory.getOWLAnnotationValue(lit.getLiteral(), lit.getLang());
         } else {
            av = objectFactory.getOWLAnnotationValue(lit.getLiteral(), true);
         }
         valueRendering = new OWLAnnotationValueRendering(av);
      }
      return Optional.ofNullable(valueRendering);
   }
}

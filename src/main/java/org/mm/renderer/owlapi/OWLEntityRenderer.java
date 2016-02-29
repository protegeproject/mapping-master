package org.mm.renderer.owlapi;

import java.util.Optional;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.OWLAnnotationPropertyNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyAssertionNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.renderer.EntityRenderer;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.delegator.AnnotationValueRendererDelegator;
import org.mm.renderer.owlapi.delegator.ClassRendererDelegator;
import org.mm.renderer.owlapi.delegator.NamedIndividualRendererDelegator;
import org.mm.renderer.owlapi.delegator.PropertyAssertionRendererDelegator;
import org.mm.renderer.owlapi.delegator.PropertyRendererDelegator;
import org.mm.rendering.owlapi.OWLAnnotationValueRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.mm.rendering.owlapi.OWLPropertyAssertionRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;

public class OWLEntityRenderer implements EntityRenderer, MappingMasterParserConstants
{
   private OWLObjectFactory objectFactory;

   private ClassRendererDelegator classRendererDelegator;
   private NamedIndividualRendererDelegator namedIndividualRendererDelegator;
   private PropertyRendererDelegator propertyRendererDelegator;
   private PropertyAssertionRendererDelegator propertyAssertionRendererDelegator;
   private AnnotationValueRendererDelegator annotationValueRendererDelegator;

   public OWLEntityRenderer(OWLReferenceRenderer referenceRenderer, OWLObjectFactory objectFactory)
   {
      this.objectFactory = objectFactory;

      classRendererDelegator = new ClassRendererDelegator(referenceRenderer);
      namedIndividualRendererDelegator = new NamedIndividualRendererDelegator(referenceRenderer);
      propertyRendererDelegator = new PropertyRendererDelegator(referenceRenderer);
      propertyAssertionRendererDelegator = new PropertyAssertionRendererDelegator(referenceRenderer);
      annotationValueRendererDelegator = new AnnotationValueRendererDelegator(referenceRenderer);
   }

   @Override
   public Optional<OWLClassRendering> renderOWLClass(OWLClassNode classNode, boolean isDeclaration)
         throws RendererException
   {
      return classRendererDelegator.render(classNode, objectFactory);
   }

   @Override
   public Optional<OWLNamedIndividualRendering> renderOWLNamedIndividual(OWLNamedIndividualNode node,
         boolean isDeclaration) throws RendererException
   {
      return namedIndividualRendererDelegator.render(node, objectFactory);
   }

   @Override
   public Optional<? extends OWLPropertyRendering> renderOWLProperty(OWLPropertyNode node)
         throws RendererException
   {
      return propertyRendererDelegator.render(node, objectFactory);
   }

   @Override
   public Optional<? extends OWLPropertyRendering> renderOWLObjectProperty(OWLPropertyNode node)
         throws RendererException
   {
      return propertyRendererDelegator.render(node, objectFactory);
   }

   @Override
   public Optional<? extends OWLPropertyRendering> renderOWLDataProperty(OWLPropertyNode node)
         throws RendererException
   {
      return propertyRendererDelegator.render(node, objectFactory);
   }

   @Override
   public Optional<? extends OWLPropertyRendering> renderOWLAnnotationProperty(OWLAnnotationPropertyNode node)
         throws RendererException
   {
      return propertyRendererDelegator.render(node, objectFactory);
   }

   @Override
   public Optional<OWLPropertyAssertionRendering> renderOWLPropertyAssertion(OWLPropertyAssertionNode node)
         throws RendererException
   {
      return propertyAssertionRendererDelegator.render(node, objectFactory);
   }

   @Override
   public Optional<OWLAnnotationValueRendering> renderOWLAnnotationValue(OWLAnnotationValueNode node)
         throws RendererException
   {
      return annotationValueRendererDelegator.render(node, objectFactory);
   }
}

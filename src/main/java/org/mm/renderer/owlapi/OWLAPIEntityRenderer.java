package org.mm.renderer.owlapi;

import java.util.Optional;
import java.util.Set;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAnnotationPropertyNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLEntityRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
import org.mm.rendering.owlapi.OWLAnnotationPropertyRendering;
import org.mm.rendering.owlapi.OWLAnnotationValueRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLDataPropertyRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.mm.rendering.owlapi.OWLObjectPropertyRendering;
import org.mm.rendering.owlapi.OWLPropertyAssertionObjectRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

public class OWLAPIEntityRenderer implements OWLEntityRenderer, MappingMasterParserConstants
{
   private OWLAPIObjectHandler handler;
   private OWLAPIReferenceRenderer referenceRenderer;
   private OWLAPILiteralRenderer literalRenderer;

   public OWLAPIEntityRenderer(OWLOntology ontology, OWLAPIReferenceRenderer referenceRenderer)
   {
      this.referenceRenderer = referenceRenderer;
      handler = new OWLAPIObjectHandler(ontology);
      literalRenderer = new OWLAPILiteralRenderer(ontology);
   }

   public OWLAPIReferenceRenderer getReferenceRenderer()
   {
      return referenceRenderer;
   }

   @Override
   public Optional<OWLClassRendering> renderOWLClass(OWLClassNode classNode, boolean isDeclaration) throws RendererException
   {
      if (classNode.hasNameNode()) {
         OWLClass cls = handler.getOWLClass(classNode.getNameNode().getName());
         if (!isDeclaration) {
            validate(cls); // check in the ontology if the class exists already.
         }
         return Optional.of(new OWLClassRendering(cls));
      } else if (classNode.hasReferenceNode()) {
         return renderReferenceForClassNode(classNode.getReferenceNode());
      }
      throw new InternalRendererException("Unknown child for node " + classNode.getNodeName());
   }

   private Optional<OWLClassRendering> renderReferenceForClassNode(ReferenceNode referenceNode) throws RendererException
   {
      Optional<OWLAPIReferenceRendering> referenceRendering = referenceRenderer.renderReference(referenceNode);
      if (!referenceRendering.isPresent()) {
         // TODO Logging here
         return Optional.empty();
      }
      if (referenceRendering.get().isOWLClass()) {
         OWLClass cls = referenceRendering.get().getOWLEntity().get().asOWLClass();
         Set<OWLAxiom> axioms = referenceRendering.get().getOWLAxioms();
         return Optional.of(new OWLClassRendering(cls, axioms));
      } else if (referenceRendering.get().isOWLLiteral()) {
         OWLLiteral lit = referenceRendering.get().getOWLLiteral().get();
         OWLClass cls = handler.getOWLClass(lit.getLiteral());
         Set<OWLAxiom> axioms = referenceRendering.get().getOWLAxioms();
         return Optional.of(new OWLClassRendering(cls, axioms));
      }
      throw new RendererException("Reference value " + referenceNode + " for class is not an OWL class");
   }

   @Override
   public Optional<OWLNamedIndividualRendering> renderOWLNamedIndividual(OWLNamedIndividualNode namedIndividualNode,
         boolean isDeclaration) throws RendererException
   {
      if (namedIndividualNode.hasNameNode()) {
         OWLNamedIndividual ind = handler.getOWLNamedIndividual(namedIndividualNode.getNameNode().getName());
         if (!isDeclaration) {
            validate(ind); // check in the ontology if the individual exists already.
         }
         return Optional.of(new OWLNamedIndividualRendering(ind));
      } else if (namedIndividualNode.hasReferenceNode()) {
         return renderReferenceForNamedIndividualNode(namedIndividualNode.getReferenceNode());
      }
      throw new InternalRendererException("Unknown child for node " + namedIndividualNode.getNodeName());
   }

   private Optional<OWLNamedIndividualRendering> renderReferenceForNamedIndividualNode(ReferenceNode referenceNode)
         throws RendererException
   {
      Optional<OWLAPIReferenceRendering> referenceRendering = referenceRenderer.renderReference(referenceNode);
      if (!referenceRendering.isPresent()) {
         // TODO Logging here
         return Optional.empty();
      }
      if (referenceRendering.get().isOWLNamedIndividual()) {
         OWLNamedIndividual ind = referenceRendering.get().getOWLEntity().get().asOWLNamedIndividual();
         Set<OWLAxiom> axioms = referenceRendering.get().getOWLAxioms();
         return Optional.of(new OWLNamedIndividualRendering(ind, axioms));
      }
      throw new RendererException(
            "Reference value " + referenceNode + " for named individual is not an OWL named individual");
   }

   @Override
   public Optional<? extends OWLPropertyRendering> renderOWLProperty(OWLPropertyNode propertyNode)
         throws RendererException
   {
      if (propertyNode.hasNameNode()) {
         /*
          * We will assume the default property type for named node is an object property. The resolution
          * should be done in the upper client code.
          */
         OWLObjectProperty prop = handler.getOWLObjectProperty(propertyNode.getNameNode().getName());
         validate(prop);
         return Optional.of(new OWLObjectPropertyRendering(prop));
      } else if (propertyNode.hasReferenceNode()) {
         // XXX: Need to refactor
         ReferenceNode referenceNode = propertyNode.getReferenceNode();
         if (referenceNode.getReferenceTypeNode().getReferenceType().isOWLObjectProperty()) {
            return renderReferenceForObjectPropertyNode(referenceNode);
         } else if (referenceNode.getReferenceTypeNode().getReferenceType().isOWLDataProperty()) {
            return renderReferenceForDataPropertyNode(referenceNode);
         }
      }
      throw new InternalRendererException("Unknown child for node " + propertyNode.getNodeName());
   }

   @Override
   public Optional<OWLObjectPropertyRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode)
         throws RendererException
   {
      if (propertyNode.hasNameNode()) {
         OWLObjectProperty prop = handler.getOWLObjectProperty(propertyNode.getNameNode().getName());
         validate(prop); // check in the ontology if the property exists already.
         return Optional.of(new OWLObjectPropertyRendering(prop));
      } else if (propertyNode.hasReferenceNode()) {
         return renderReferenceForObjectPropertyNode(propertyNode.getReferenceNode());
      }
      throw new InternalRendererException("Unknown child for node " + propertyNode.getNodeName());
   }

   private Optional<OWLObjectPropertyRendering> renderReferenceForObjectPropertyNode(ReferenceNode referenceNode)
         throws RendererException
   {
      Optional<OWLAPIReferenceRendering> referenceRendering = referenceRenderer.renderReference(referenceNode);
      if (!referenceRendering.isPresent()) {
         // TODO Logging here
         return Optional.empty();
      }
      if (referenceRendering.get().isOWLObjectProperty()) {
         OWLObjectProperty op = referenceRendering.get().getOWLEntity().get().asOWLObjectProperty();
         return Optional.of(new OWLObjectPropertyRendering(op));
      }
      throw new RendererException(
            "Reference value " + referenceNode + " for object property is not an OWL object property");
   }

   @Override
   public Optional<OWLDataPropertyRendering> renderOWLDataProperty(OWLPropertyNode propertyNode)
         throws RendererException
   {
      if (propertyNode.hasNameNode()) {
         OWLDataProperty prop = handler.getOWLDataProperty(propertyNode.getNameNode().getName());
         validate(prop); // check in the ontology if the property exists already.
         return Optional.of(new OWLDataPropertyRendering(prop));
      } else if (propertyNode.hasReferenceNode()) {
         return renderReferenceForDataPropertyNode(propertyNode.getReferenceNode());
      }
      throw new InternalRendererException("Unknown child for node " + propertyNode.getNodeName());
   }

   private Optional<OWLDataPropertyRendering> renderReferenceForDataPropertyNode(ReferenceNode referenceNode)
         throws RendererException
   {
      Optional<OWLAPIReferenceRendering> referenceRendering = referenceRenderer.renderReference(referenceNode);
      if (!referenceRendering.isPresent()) {
         // TODO Logging here
         return Optional.empty();
      }
      if (referenceRendering.get().isOWLDataProperty()) {
         OWLDataProperty dp = referenceRendering.get().getOWLEntity().get().asOWLDataProperty();
         return Optional.of(new OWLDataPropertyRendering(dp));
      }
      throw new RendererException(
            "Reference value " + referenceNode + " for data property is not an OWL data property");
   }

   @Override
   public Optional<OWLAnnotationPropertyRendering> renderOWLAnnotationProperty(OWLAnnotationPropertyNode propertyNode)
         throws RendererException
   {
      if (propertyNode.hasNameNode()) {
         OWLAnnotationProperty property = handler.getOWLAnnotationProperty(propertyNode.getNameNode().getName());
         validate(property); // check in the ontology if the property exists already.
         return Optional.of(new OWLAnnotationPropertyRendering(property));
      } else if (propertyNode.hasReferenceNode()) {
         return renderReferenceForAnnotationPropertyNode(propertyNode.getReferenceNode());
      }
      throw new InternalRendererException("Unknown child for node " + propertyNode.getNodeName());
   }

   private Optional<OWLAnnotationPropertyRendering> renderReferenceForAnnotationPropertyNode(
         ReferenceNode referenceNode) throws RendererException
   {
      Optional<OWLAPIReferenceRendering> referenceRendering = referenceRenderer.renderReference(referenceNode);
      if (!referenceRendering.isPresent()) {
         // TODO Logging here
         return Optional.empty();
      }
      if (referenceRendering.get().isOWLAnnotationProperty()) {
         OWLAnnotationProperty ap = referenceRendering.get().getOWLEntity().get().asOWLAnnotationProperty();
         return Optional.of(new OWLAnnotationPropertyRendering(ap));
      }
      throw new RendererException(
            "Reference value " + referenceNode + " for annotation property is not an OWL annotation property");
   }

   @Override
   public Optional<OWLPropertyAssertionObjectRendering> renderOWLPropertyAssertion(OWLPropertyAssertionObjectNode value)
         throws RendererException
   {
      if (value.isName()) {
         return renderNameForPropertyAssertionObject(value.getNameNode());
      } else if (value.isLiteral()) {
         return renderLiteralForPropertyAssertionObject(value.getOWLLiteralNode());
      } else if (value.isReference()) {
         return renderReferenceForPropertyAssertionObject(value.getReferenceNode());
      }
      throw new InternalRendererException("Unknown child node for node " + value.getNodeName());
   }

   private Optional<OWLPropertyAssertionObjectRendering> renderNameForPropertyAssertionObject(NameNode nameNode)
         throws RendererException
   {
      OWLNamedIndividual ind = handler.getOWLNamedIndividual(nameNode.getName());
      return Optional.of(new OWLPropertyAssertionObjectRendering(ind));
   }

   private Optional<OWLPropertyAssertionObjectRendering> renderLiteralForPropertyAssertionObject(
         OWLLiteralNode owlLiteralNode) throws RendererException
   {
      OWLLiteral lit = literalRenderer.createOWLLiteral(owlLiteralNode);
      return Optional.of(new OWLPropertyAssertionObjectRendering(lit));
   }

   private Optional<OWLPropertyAssertionObjectRendering> renderReferenceForPropertyAssertionObject(
         ReferenceNode referenceNode) throws RendererException
   {
      Optional<OWLAPIReferenceRendering> referenceRendering = referenceRenderer.renderReference(referenceNode);
      if (!referenceRendering.isPresent()) {
         // TODO Logging here
         return Optional.empty();
      }
      if (referenceRendering.get().isOWLNamedIndividual()) {
         OWLNamedIndividual ind = referenceRendering.get().getOWLEntity().get().asOWLNamedIndividual();
         return Optional.of(new OWLPropertyAssertionObjectRendering(ind));
      } else if (referenceRendering.get().isOWLLiteral()) {
         OWLLiteral lit = referenceRendering.get().getOWLLiteral().get();
         return Optional.of(new OWLPropertyAssertionObjectRendering(lit));
      }
      throw new InternalRendererException(
            "Reference value " + referenceNode + " for property assertion is not either OWL named individual or OWL literal");
   }

   @Override
   public Optional<OWLAnnotationValueRendering> renderOWLAnnotationValue(OWLAnnotationValueNode annotationValueNode)
         throws RendererException
   {
      if (annotationValueNode.isName()) {
         return renderNameForAnnotationValueNode(annotationValueNode.getNameNode());
      } else if (annotationValueNode.isLiteral()) {
         return renderLiteralForAnnotationValueNode(annotationValueNode.getOWLLiteralNode());
      } else if (annotationValueNode.isReference()) {
         return renderReferenceForAnnotationValueNode(annotationValueNode.getReferenceNode());
      }
      throw new InternalRendererException("Unknown child for node " + annotationValueNode.getNodeName());
   }

   private Optional<OWLAnnotationValueRendering> renderNameForAnnotationValueNode(NameNode nameNode)
   {
      OWLAnnotationValue anno = handler.getQualifiedName(nameNode.getName());
      return Optional.of(new OWLAnnotationValueRendering(anno));
   }

   private Optional<OWLAnnotationValueRendering> renderLiteralForAnnotationValueNode(OWLLiteralNode literalNode)
         throws RendererException
   {
      OWLAnnotationValue annoValue;
      if (literalNode.isString()) {
         String value = literalNode.getStringLiteralNode().getValue();
         annoValue = handler.getOWLAnnotationValue(value);
      } else if (literalNode.isBoolean()) {
         boolean value = literalNode.getBooleanLiteralNode().getValue();
         annoValue = handler.getOWLAnnotationValue(value);
      } else if (literalNode.isInt()) {
         int value = literalNode.getIntLiteralNode().getValue();
         annoValue = handler.getOWLAnnotationValue(value);
      } else if (literalNode.isFloat()) {
         float value = literalNode.getFloatLiteralNode().getValue();
         annoValue = handler.getOWLAnnotationValue(value);
      } else {
         throw new InternalRendererException("Unsupported datatype for node " + literalNode.getNodeName());
      }
      return Optional.of(new OWLAnnotationValueRendering(annoValue));
   }

   private Optional<OWLAnnotationValueRendering> renderReferenceForAnnotationValueNode(ReferenceNode referenceNode)
         throws RendererException
   {
      Optional<OWLAPIReferenceRendering> referenceRendering = referenceRenderer.renderReference(referenceNode);
      if (!referenceRendering.isPresent()) {
         // TODO Logging here
         return Optional.empty();
      }
      if (referenceRendering.get().isOWLLiteral()) {
         OWLLiteral lit = referenceRendering.get().getOWLLiteral().get();
         return Optional.of(new OWLAnnotationValueRendering(lit));
      }
      /*
       * Annotation value can also be IRI or anonymous individual.
       */
      throw new InternalRendererException(
            "Reference value " + referenceNode + " for annotation value is not OWL literal");
   }

   private void validate(OWLEntity entity) throws RendererException
   {
      if (!handler.checkInOntology(entity)) {
         throw new RendererException("The " + entity.getEntityType() + " " + entity + " does not exist in the ontology");
      }
   }
}

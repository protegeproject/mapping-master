package org.mm.renderer.owlapi;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLDeclarationRenderer;
import org.mm.renderer.ReferenceRendererConfiguration;
import org.mm.renderer.Renderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.rendering.owlapi.OWLAnnotationPropertyRendering;
import org.mm.rendering.owlapi.OWLAnnotationValueRendering;
import org.mm.rendering.owlapi.OWLClassExpressionRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.mm.rendering.owlapi.OWLPropertyAssertionObjectRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OWLAPIRenderer extends ReferenceRendererConfiguration implements Renderer, OWLDeclarationRenderer,
      MappingMasterParserConstants
{
   public static final int NameEncodings[] = { MM_LOCATION, MM_LITERAL, RDF_ID, RDFS_LABEL };
   public static final int ReferenceValueTypes[] = { OWL_CLASS, OWL_NAMED_INDIVIDUAL, OWL_OBJECT_PROPERTY,
         OWL_DATA_PROPERTY, XSD_INT, XSD_STRING, XSD_FLOAT, XSD_DOUBLE, XSD_SHORT, XSD_BOOLEAN, XSD_TIME, XSD_DATETIME,
         XSD_DURATION };
   public static final int PropertyTypes[] = { OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY };
   public static final int PropertyValueTypes[] = ReferenceValueTypes;
   public static final int DataPropertyValueTypes[] = { XSD_STRING, XSD_BYTE, XSD_SHORT, XSD_INT, XSD_FLOAT, XSD_DOUBLE,
         XSD_BOOLEAN, XSD_TIME, XSD_DATETIME, XSD_DATE, XSD_DURATION };

   private SpreadSheetDataSource dataSource;

   private final OWLAPIObjectHandler handler;
   private final OWLAPIEntityRenderer entityRenderer;
   private final OWLAPIClassExpressionRenderer classExpressionRenderer;
   private final OWLAPIReferenceRenderer referenceRenderer;

   public OWLAPIRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource)
   {
      this.dataSource = dataSource;
      handler = new OWLAPIObjectHandler(ontology);
      referenceRenderer = new OWLAPIReferenceRenderer(ontology, dataSource);
      entityRenderer = new OWLAPIEntityRenderer(ontology, referenceRenderer);
      classExpressionRenderer = new OWLAPIClassExpressionRenderer(ontology, entityRenderer);
   }

   @Override
   public SpreadSheetDataSource getDataSource()
   {
      return dataSource;
   }

   @Override
   public ReferenceRendererConfiguration getReferenceRendererConfiguration()
   {
      return this;
   }

   @Override
   public Optional<OWLAPIRendering> renderMMExpression(MMExpressionNode mmExpressionNode) throws RendererException
   {
      if (mmExpressionNode.hasOWLClassDeclaration()) {
         return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
      } else if (mmExpressionNode.hasOWLIndividualDeclaration()) {
         return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
      } else {
         throw new InternalRendererException("unknown child for node: " + mmExpressionNode.getNodeName());
      }
   }

   @Override
   public Optional<OWLAPIRendering> renderOWLClassDeclaration(OWLClassDeclarationNode node)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      Optional<OWLClassRendering> declaredClassRendering = entityRenderer.renderOWLClass(node.getOWLClassNode());
      if (!declaredClassRendering.isPresent()) {
         return Optional.empty();
      }

      OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
      /*
       * By default, the class declaration node will produce one OWL class declaration axiom
       */
      OWLDeclarationAxiom declaredAxiom = handler.getOWLDeclarationAxiom(declaredClass);
      axioms.add(declaredAxiom);

      /*
       * Add any existing axioms in the class rendering object
       */
      axioms.addAll(declaredClassRendering.get().getOWLAxioms());

      /*
       * In case the class declaration node has a sub-class axiom
       */
      if (node.hasOWLSubclassOfNodes()) {
         for (OWLSubclassOfNode subclassOfNode : node.getOWLSubclassOfNodes()) {
            for (OWLClassExpressionNode classExpressionNode : subclassOfNode.getClassExpressionNodes()) {
               Optional<OWLClassExpressionRendering> classExpressionRendering = classExpressionRenderer
                     .renderOWLClassExpression(classExpressionNode);
               if (!classExpressionRendering.isPresent()) {
                  continue; // just ignore it
               } else {
                  OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
                  OWLSubClassOfAxiom axiom = handler.getOWLSubClassOfAxiom(declaredClass, classExpression);
                  axioms.add(axiom);
               }
            }
         }
      }

      /*
       * In case the class declaration has an equivalent class axiom
       */
      if (node.hasOWLEquivalentClassesNode()) {
         for (OWLEquivalentClassesNode equivalentClassesNode : node.getOWLEquivalentClassesNodes()) {
            for (OWLClassExpressionNode classExpressionNode : equivalentClassesNode.getClassExpressionNodes()) {
               Optional<OWLClassExpressionRendering> classExpressionRendering = classExpressionRenderer
                     .renderOWLClassExpression(classExpressionNode);
               if (!classExpressionRendering.isPresent()) {
                  continue; // just ignore it
               } else {
                  OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
                  OWLEquivalentClassesAxiom axiom = handler.getOWLEquivalentClassesAxiom(declaredClass, classExpression);
                  axioms.add(axiom);
               }
            }
         }
      }

      /*
       * In case the class declaration has an annotation axiom
       */
      if (node.hasAnnotationFactNodes()) {
         for (AnnotationFactNode annotationFactNode : node.getAnnotationFactNodes()) {
            Optional<OWLAnnotationPropertyRendering> propertyRendering = entityRenderer
                  .renderOWLAnnotationProperty(annotationFactNode.getOWLAnnotationPropertyNode());
            OWLAnnotationValueNode annotationValueNode = annotationFactNode.getOWLAnnotationValueNode();
            Optional<OWLAnnotationValueRendering> annotationValueRendering = entityRenderer
                  .renderOWLAnnotationValue(annotationValueNode);

            if (!propertyRendering.isPresent()) {
               continue; // just ignore it
            }
            if (!annotationValueRendering.isPresent()) {
               continue; // just ignore it
            }
            IRI classIri = declaredClass.getIRI();
            OWLAnnotationProperty property = propertyRendering.get().getOWLAnnotationProperty();
            OWLAnnotationValue annotationValue = annotationValueRendering.get().getOWLAnnotationValue();
            OWLAnnotationAssertionAxiom axiom = handler.getOWLAnnotationAssertionAxiom(property, classIri, annotationValue);
            axioms.add(axiom);
         }
      }
      return Optional.of(new OWLAPIRendering(axioms));
   }

   @Override
   public Optional<OWLAPIRendering> renderOWLIndividualDeclaration(OWLIndividualDeclarationNode individualDeclarationNode)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      Optional<OWLNamedIndividualRendering> declaredIndividualRendering = entityRenderer
            .renderOWLNamedIndividual(individualDeclarationNode.getOWLIndividualNode());
      if (!declaredIndividualRendering.isPresent()) {
         return Optional.empty();
      }

      OWLNamedIndividual declaredIndividual = declaredIndividualRendering.get().getOWLNamedIndividual();

      /*
       * By default, the individual declaration node will produce one OWL named
       * individual declaration axiom
       */
      OWLDeclarationAxiom declaredAxiom = handler.getOWLDeclarationAxiom(declaredIndividual);
      axioms.add(declaredAxiom);

      /*
       * Add any existing axioms in the class rendering object
       */
      axioms.addAll(declaredIndividualRendering.get().getOWLAxioms());

      if (individualDeclarationNode.hasFacts()) { // We have a Facts clause
         List<FactNode> factNodes = individualDeclarationNode.getFactNodes();
         Set<OWLAxiom> factsAxioms = processFactsClause(declaredIndividualRendering, factNodes);
         axioms.addAll(factsAxioms);
      }

      if (individualDeclarationNode.hasAnnotations()) { // We have an Annotations clause
         List<AnnotationFactNode> annotationFactNodes = individualDeclarationNode.getAnnotationNodes();
         Set<OWLAxiom> annotationsAxioms = processAnnotationClause(declaredIndividualRendering, annotationFactNodes);
         axioms.addAll(annotationsAxioms);
      }

      if (individualDeclarationNode.hasSameAs()) { // We have a SameAs clause
         Set<OWLAxiom> sameAsAxioms = processSameAsClause(individualDeclarationNode, declaredIndividualRendering);
         axioms.addAll(sameAsAxioms);
      }

      if (individualDeclarationNode.hasDifferentFrom()) { // We have a DifferentFrom clause
         Set<OWLAxiom> differentFromAxioms = processDifferentFromClause(individualDeclarationNode, declaredIndividualRendering);
         axioms.addAll(differentFromAxioms);
      }

      if (individualDeclarationNode.hasTypes()) { // We have a Types: clause
         List<TypeNode> typesNode = individualDeclarationNode.getTypesNode().getTypeNodes();
         Set<OWLAxiom> typesAxioms = this.referenceRenderer.processTypesClause(declaredIndividual, typesNode);
         axioms.addAll(typesAxioms);
      }
      return Optional.of(new OWLAPIRendering(axioms));
   }

   /**
    * Create same individual axioms for the declared individual.
    */
   private Set<OWLAxiom> processSameAsClause(OWLIndividualDeclarationNode individualDeclarationNode,
         Optional<OWLNamedIndividualRendering> declaredIndividualRendering) throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();

      if (declaredIndividualRendering.isPresent()) {
         OWLNamedIndividual individual1 = declaredIndividualRendering.get().getOWLNamedIndividual();
         List<OWLNamedIndividualNode> individualNodes = individualDeclarationNode.getOWLSameAsNode().getIndividualNodes();
         for (OWLNamedIndividualNode sameAsIndividualNode : individualNodes) {
            Optional<OWLNamedIndividualRendering> sameAsIndividualRendering = entityRenderer
                  .renderOWLNamedIndividual(sameAsIndividualNode);
            if (sameAsIndividualRendering.isPresent()) {
               OWLNamedIndividual individual2 = sameAsIndividualRendering.get().getOWLNamedIndividual();
               OWLSameIndividualAxiom axiom = handler.getOWLSameIndividualAxiom(individual1, individual2);
               axioms.add(axiom);
            }
         }
      }
      return axioms;
   }

   /**
    * Create different individuals axioms for the declared individual.
    */
   private Set<OWLAxiom> processDifferentFromClause(OWLIndividualDeclarationNode individualDeclarationNode,
         Optional<OWLNamedIndividualRendering> declaredIndividualRendering) throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();

      if (declaredIndividualRendering.isPresent()) {
         OWLNamedIndividual individual1 = declaredIndividualRendering.get().getOWLNamedIndividual();
         List<OWLNamedIndividualNode> individualNodes = individualDeclarationNode.getOWLDifferentFromNode().getNamedIndividualNodes();
         for (OWLNamedIndividualNode differentFromIndividualNode : individualNodes) {
            Optional<OWLNamedIndividualRendering> differentFromIndividualsRendering = entityRenderer
                  .renderOWLNamedIndividual(differentFromIndividualNode);
            if (differentFromIndividualsRendering.isPresent()) {
               OWLNamedIndividual individual2 = differentFromIndividualsRendering.get().getOWLNamedIndividual();
               OWLDifferentIndividualsAxiom axiom = handler.getOWLDifferentIndividualsAxiom(individual1, individual2);
               axioms.add(axiom);
            }
         }
      }
      return axioms;
   }

   /**
    * Create annotation assertion axioms for the declared individual.
    */
   private Set<OWLAxiom> processAnnotationClause(Optional<OWLNamedIndividualRendering> declaredIndividualRendering,
         List<AnnotationFactNode> annotationFactNodes) throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();

      if (declaredIndividualRendering.isPresent()) {
         OWLNamedIndividual individual = declaredIndividualRendering.get().getOWLNamedIndividual();
         for (AnnotationFactNode annotationFact : annotationFactNodes) {
            Optional<? extends OWLAnnotationPropertyRendering> propertyRendering = entityRenderer
                  .renderOWLAnnotationProperty(annotationFact.getOWLAnnotationPropertyNode());
            if (!propertyRendering.isPresent()) {
               continue; // just ignore it
            }

            OWLAnnotationValueNode annotationValueNode = annotationFact.getOWLAnnotationValueNode();
            Optional<OWLAnnotationValueRendering> annotationValueRendering = entityRenderer
                  .renderOWLAnnotationValue(annotationValueNode);
            if (!annotationValueRendering.isPresent()) {
               continue; // just ignore it
            }

            OWLAnnotationProperty property = propertyRendering.get().getOWLAnnotationProperty();
            if (annotationValueNode.isReference()) {
               ReferenceNode referenceNode = annotationValueNode.getReferenceNode();
               if (!referenceNode.hasExplicitlySpecifiedReferenceType() && handler.isOWLObjectProperty(property.getIRI())) {
                  referenceNode.updateReferenceType(OWL_NAMED_INDIVIDUAL);
               }
            }

            if (handler.isOWLAnnotationProperty(property)) {
               IRI individualIri = individual.getIRI();
               OWLAnnotationValue value = annotationValueRendering.get().getOWLAnnotationValue();
               OWLAnnotationAssertionAxiom axiom = handler.getOWLAnnotationAssertionAxiom(property, individualIri, value);
               axioms.add(axiom);
            } else {
               continue; // just ignore it
            }
         }
      }
      return axioms;
   }

   /**
    * Create property assertion axioms for the declared individual.
    */
   private Set<OWLAxiom> processFactsClause(Optional<OWLNamedIndividualRendering> individualRendering, List<FactNode> factNodes)
         throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();

      if (individualRendering.isPresent()) {
         OWLIndividual individual = individualRendering.get().getOWLNamedIndividual();
         for (FactNode factNode : factNodes) {
            OWLPropertyNode propertyNode = factNode.getOWLPropertyNode();
            Optional<? extends OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);
            if (!propertyRendering.isPresent()) {
               continue; // just ignore it
            }

            OWLPropertyAssertionObjectNode propertyAssertionObjectNode = factNode.getOWLPropertyAssertionObjectNode();
            Optional<OWLPropertyAssertionObjectRendering> propertyAssertionRendering = entityRenderer
                  .renderOWLPropertyAssertion(propertyAssertionObjectNode);
            if (!propertyAssertionRendering.isPresent()) {
               continue; // just ignore it
            }

            OWLProperty property = propertyRendering.get().getOWLProperty();
            OWLPropertyAssertionObject propertyAssertionObject = propertyAssertionRendering.get().getOWLPropertyAssertionObject();
            if (propertyAssertionObjectNode.isReference()) {
               ReferenceNode reference = propertyAssertionObjectNode.getReferenceNode();
               if (!reference.hasExplicitlySpecifiedReferenceType() && handler.isOWLObjectProperty(property)) {
                  reference.updateReferenceType(OWL_NAMED_INDIVIDUAL);
               }
            }
            if (handler.isOWLObjectProperty(property)) {
               OWLObjectProperty op = handler.getOWLObjectProperty(property.getIRI());
               OWLIndividual value = (OWLNamedIndividual) propertyAssertionObject;
               OWLObjectPropertyAssertionAxiom axiom = handler.getOWLObjectPropertyAssertionAxiom(op, individual, value);
               axioms.add(axiom);
            } else if (handler.isOWLDataProperty(property)) {
               OWLDataProperty dp = handler.getOWLDataProperty(property.getIRI());
               OWLLiteral value = (OWLLiteral) propertyAssertionObject;
               OWLDataPropertyAssertionAxiom axiom = handler.getOWLDataPropertyAssertionAxiom(dp, individual, value);
               axioms.add(axiom);
            } else {
               throw new RendererException("Property " + property + " does not exist in the ontology");
            }
         }
      }
      return axioms;
   }
}

package org.mm.renderer.owlapi;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLObjectOneOfNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLSameAsNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.renderer.CoreRenderer;
import org.mm.renderer.RendererException;
import org.mm.renderer.Rendering;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OWLAPICoreRenderer implements CoreRenderer, MappingMasterParserConstants
{
  public static int NameEncodings[] = { MM_LOCATION, MM_DATA_VALUE, RDF_ID, RDFS_LABEL };
  public static int ReferenceValueTypes[] = { OWL_CLASS, OWL_NAMED_INDIVIDUAL, OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY,
    XSD_INT, XSD_STRING, XSD_FLOAT, XSD_DOUBLE, XSD_SHORT, XSD_BOOLEAN, XSD_TIME, XSD_DATETIME, XSD_DURATION };
  public static int PropertyTypes[] = { OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY };
  public static int PropertyValueTypes[] = ReferenceValueTypes;
  public static int DataPropertyValueTypes[] = { XSD_STRING, XSD_BYTE, XSD_SHORT, XSD_INT, XSD_FLOAT, XSD_DOUBLE,
    XSD_BOOLEAN, XSD_TIME, XSD_DATETIME, XSD_DATE, XSD_DURATION };

  private final OWLOntology ontology;
  private final OWLDataFactory owlDataFactory;
  private final OWLAPIObjectHandler owlObjectHandler;
  private final OWLAPIEntityRenderer entityRenderer;
  private final OWLAPILiteralRenderer literalRenderer;
  private final OWLAPIClassExpressionRenderer classExpressionRenderer;
  private final OWLAPIReferenceRenderer referenceRenderer;
  private SpreadSheetDataSource dataSource;

  public OWLAPICoreRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource)
  {
    this.ontology = ontology;
    this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
    this.dataSource = dataSource;
    this.owlObjectHandler = new OWLAPIObjectHandler(ontology);

    this.entityRenderer = new OWLAPIEntityRenderer();
    this.literalRenderer = new OWLAPILiteralRenderer(ontology.getOWLOntologyManager().getOWLDataFactory());
    this.referenceRenderer = new OWLAPIReferenceRenderer(ontology, dataSource, entityRenderer);
    this.classExpressionRenderer = new OWLAPIClassExpressionRenderer(ontology, entityRenderer, referenceRenderer,
      literalRenderer);
  }

  public void reset()
  {
    owlObjectHandler.reset();
  }

  @Override public void setDataSource(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
    this.referenceRenderer.setDataSource(dataSource);
  }

  @Override public OWLAPIEntityRenderer getOWLEntityRenderer()
  {
    return this.entityRenderer;
  }

  @Override public OWLAPIClassExpressionRenderer getOWLClassExpressionRenderer()
  {
    return this.classExpressionRenderer;
  }

  @Override public OWLAPILiteralRenderer getOWLLiteralRenderer()
  {
    return this.literalRenderer;
  }

  @Override public OWLAPIReferenceRenderer getReferenceRenderer()
  {
    return this.referenceRenderer;
  }

  public Optional<OWLAPIRendering> renderExpression(ExpressionNode expressionNode) throws RendererException
  {
    if (expressionNode.hasMMExpression())
      return renderMMExpression(expressionNode.getMMExpressionNode());
    else
      throw new RendererException("unknown expression type " + expressionNode);
  }

  @Override public Optional<OWLAPIRendering> renderMMExpression(MMExpressionNode mmExpressionNode)
    throws RendererException
  {
    if (mmExpressionNode.hasOWLClassDeclaration())
      return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
    else if (mmExpressionNode.hasOWLIndividualDeclaration())
      return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
    else
      throw new RendererException("unknown expression: " + mmExpressionNode);
  }

  @Override public Optional<OWLAPIRendering> renderOWLClassDeclaration(OWLClassDeclarationNode classDeclarationNode)
    throws RendererException
  {
    OWLAPIRendering rendering = new OWLAPIRendering();

    rendering.logLine("=====================OWLClassDeclaration================================");
    rendering.logLine("MappingMaster DSL expression: " + classDeclarationNode);
    if (dataSource.hasCurrentLocation())
      rendering.logLine("********************Current location: " + dataSource.getCurrentLocation() + "*************");

    Optional<OWLClassRendering> declaredClassRendering = entityRenderer
      .renderOWLClass(classDeclarationNode.getOWLClassNode());

    if (!declaredClassRendering.isPresent()) {
      rendering.logLine("processReference: skipping OWL class declaration because of missing class");
    } else {
      if (classDeclarationNode.hasOWLSubclassOfNodes()) {
        for (OWLSubclassOfNode subclassOfNode : classDeclarationNode.getOWLSubclassOfNodes()) {
          for (OWLClassExpressionNode classExpressionNode : subclassOfNode.getClassExpressionNodes()) {
            Optional<OWLClassExpressionRendering> classExpressionRendering = classExpressionRenderer
              .renderOWLClassExpression(classExpressionNode);
            if (!classExpressionRendering.isPresent())
              rendering.logLine(
                "processReference: skipping subclass declaration [" + subclassOfNode + "] because of missing class");
            else {
              OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
              OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
              OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(classExpression, declaredClass);
              rendering.addOWLAxiom(axiom);
            }
          }
        }
      }

      if (classDeclarationNode.hasOWLEquivalentClassesNode()) {
        for (OWLEquivalentClassesNode equivalentClassesNode : classDeclarationNode.getOWLEquivalentClassesNodes()) {
          for (OWLClassExpressionNode classExpressionNode : equivalentClassesNode.getClassExpressionNodes()) {
            Optional<OWLClassExpressionRendering> classExpressionRendering = classExpressionRenderer
              .renderOWLClassExpression(classExpressionNode);
            if (!classExpressionRendering.isPresent())
              rendering.logLine("processReference: skipping equivalent declaration [" + equivalentClassesNode
                + "] because of missing class");
            else {
              OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
              OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
              OWLEquivalentClassesAxiom axiom = owlDataFactory
                .getOWLEquivalentClassesAxiom(classExpression, declaredClass);
              rendering.addOWLAxiom(axiom);
            }
          }
        }
      }

      if (classDeclarationNode.hasAnnotationFactNodes()) {
        for (AnnotationFactNode annotationFactNode : classDeclarationNode.getAnnotationFactNodes()) {
          Optional<OWLAnnotationPropertyRendering> propertyRendering = entityRenderer
            .renderOWLAnnotationProperty(annotationFactNode.getOWLPropertyNode());
          OWLAnnotationValueNode annotationValueNode = annotationFactNode.getOWLAnnotationValueNode();
          Optional<OWLAnnotationValueRendering> annotationValueRendering = renderOWLAnnotationValue(
            annotationValueNode);

          if (!propertyRendering.isPresent()) {
            rendering.logLine("processReference: skipping OWL annotation clause [" + annotationFactNode
              + "] because of missing property name");
            continue;
          }

          if (!annotationValueRendering.isPresent()) {
            rendering.logLine("processReference: skipping OWL annotation clause [" + annotationFactNode
              + "] because of missing property value");
            continue;
          }

          OWLAnnotationProperty property = propertyRendering.get().getOWLAnnotationProperty();
          OWLAnnotationValue annotationValue = annotationValueRendering.get().getOWLAnnotationValue();
          OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
          OWLAnnotationAssertionAxiom axiom = this.owlDataFactory
            .getOWLAnnotationAssertionAxiom(property, declaredClass.getIRI(), annotationValue);
          rendering.addOWLAxiom(axiom);
        }
      }
    }
    return Optional.of(rendering);
  }

  @Override public Optional<OWLAPIRendering> renderOWLIndividualDeclaration(
    OWLIndividualDeclarationNode individualDeclarationNode) throws RendererException
  {
    OWLAPIRendering individualDeclarationRendering = new OWLAPIRendering();

    individualDeclarationRendering
      .logLine("=====================OWLIndividualDeclaration================================");
    individualDeclarationRendering.logLine("MappingMaster DSL expression: " + individualDeclarationNode);
    if (dataSource.hasCurrentLocation())
      individualDeclarationRendering
        .logLine("********************Current location: " + dataSource.getCurrentLocation() + "*************");

    Optional<OWLNamedIndividualRendering> declaredIndividualRendering = entityRenderer
      .renderOWLNamedIndividual(individualDeclarationNode.getOWLIndividualNode());
    if (!declaredIndividualRendering.isPresent()) {
      individualDeclarationRendering.logLine("Skipping OWL individual declaration because of missing individual name");
    } else {
      OWLNamedIndividual declaredIndividual = declaredIndividualRendering.get().getOWLNamedIndividual();

      if (individualDeclarationNode.hasFacts()) { // We have a Facts: clause
        List<FactNode> factNodes = individualDeclarationNode.getFactNodes();
        Set<OWLAxiom> axioms = processFactsClause(individualDeclarationRendering, declaredIndividualRendering,
          factNodes);
        individualDeclarationRendering.addOWLAxioms(axioms);
      }

      if (individualDeclarationNode.hasAnnotations()) { // We have an Annotations: clause
        List<AnnotationFactNode> annotationFactNodes = individualDeclarationNode.getAnnotationNodes();
        Set<OWLAxiom> axioms = processAnnotationClause(individualDeclarationRendering, declaredIndividualRendering,
          annotationFactNodes);
        individualDeclarationRendering.addOWLAxioms(axioms);
      }

      if (individualDeclarationNode.hasSameAs()) { // We have a SameAs: clause
        Set<OWLAxiom> axioms = processSameAsClause(individualDeclarationNode, declaredIndividualRendering);
        individualDeclarationRendering.addOWLAxioms(axioms);
      }

      if (individualDeclarationNode.hasDifferentFrom()) { // We have a DifferentFrom: clause
        Set<OWLAxiom> axioms = processDifferentFromClause(individualDeclarationNode, declaredIndividualRendering);
        individualDeclarationRendering.addOWLAxioms(axioms);
      }

      if (individualDeclarationNode.hasTypes()) { // We have a Types: clause
        Set<OWLAxiom> axioms = referenceRenderer.processTypesClause(individualDeclarationRendering, declaredIndividual,
          individualDeclarationNode.getTypesNode().getTypeNodes());
        individualDeclarationRendering.addOWLAxioms(axioms);
      }
      // TODO individual declaration axioms
    }
    return Optional.of(individualDeclarationRendering);
  }

  @Override public Optional<OWLAPIRendering> renderOWLSubclassOf(OWLClassNode declaredClassNode,
    OWLSubclassOfNode subclassOfNode) throws RendererException
  {
    return Optional.empty(); // TODO
  }


  @Override public Optional<OWLPropertyAssertionObjectRendering> renderOWLPropertyAssertionObject(
    OWLPropertyAssertionObjectNode propertyAssertionObject) throws RendererException
  {
    return Optional.empty(); // TODO
  }

  @Override public Optional<OWLAnnotationValueRendering> renderOWLAnnotationValue(
    OWLAnnotationValueNode annotationValueNode) throws RendererException
  {
    return Optional.empty(); // TODO
  }

  @Override public Optional<? extends Rendering> renderOWLSameAs(OWLSameAsNode sameAsNode) throws RendererException
  {
    return Optional.empty(); // TODO
  }

  @Override public Optional<? extends Rendering> renderFact(FactNode factNode) throws RendererException
  {
    return Optional.empty();
  }

  @Override public Optional<? extends Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode)
    throws RendererException
  {
    return Optional.empty(); // TODO
  }

  @Override public Optional<? extends Rendering> renderName(NameNode nameNode) throws RendererException
  {
    return Optional.empty(); // TODO
  }

  /**
   * Create class assertion axioms for the declared individual using the supplied types.
   */
  private Set<OWLAxiom> processSameAsClause(OWLIndividualDeclarationNode individualDeclarationNode,
    Optional<OWLNamedIndividualRendering> declaredIndividualRendering) throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    if (declaredIndividualRendering.isPresent()) {
      OWLNamedIndividual individual1 = declaredIndividualRendering.get().getOWLNamedIndividual();

      for (OWLNamedIndividualNode sameAsIndividualNode : individualDeclarationNode.getOWLSameAsNode()
        .getIndividualNodes()) {
        Optional<OWLNamedIndividualRendering> sameAsIndividualRendering = entityRenderer
          .renderOWLNamedIndividual(sameAsIndividualNode);
        if (sameAsIndividualRendering.isPresent()) {
          OWLNamedIndividual individual2 = sameAsIndividualRendering.get().getOWLNamedIndividual();
          OWLSameIndividualAxiom axiom = owlDataFactory.getOWLSameIndividualAxiom(individual1, individual2);
          axioms.add(axiom);
        }
      }
    }
    return axioms;
  }

  private Set<OWLAxiom> processDifferentFromClause(OWLIndividualDeclarationNode individualDeclarationNode,
    Optional<OWLNamedIndividualRendering> declaredIndividualRendering) throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    if (declaredIndividualRendering.isPresent()) {
      OWLNamedIndividual individual1 = declaredIndividualRendering.get().getOWLNamedIndividual();
      for (OWLNamedIndividualNode differentFromIndividualNode : individualDeclarationNode.getOWLDifferentFromNode()
        .getNamedIndividualNodes()) {
        Optional<OWLNamedIndividualRendering> differentFromIndividualsRendering = entityRenderer
          .renderOWLNamedIndividual(differentFromIndividualNode);
        if (differentFromIndividualsRendering.isPresent()) {
          OWLNamedIndividual individual2 = differentFromIndividualsRendering.get().getOWLNamedIndividual();
          OWLDifferentIndividualsAxiom axiom = owlDataFactory.getOWLDifferentIndividualsAxiom(individual1, individual2);

          axioms.add(axiom);
        }
      }
    }
    return axioms;
  }

  private Set<OWLAxiom> processAnnotationClause(Rendering individualDeclarationRendering,
    Optional<OWLNamedIndividualRendering> declaredIndividualRendering, List<AnnotationFactNode> annotationFactNodes)
    throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    if (declaredIndividualRendering.isPresent()) {

      for (AnnotationFactNode annotationFact : annotationFactNodes) {
        Optional<OWLPropertyRendering> propertyRendering = entityRenderer
          .renderOWLProperty(annotationFact.getOWLPropertyNode());

        if (!propertyRendering.isPresent()) {
          individualDeclarationRendering
            .logLine("Skipping OWL annotation clause [" + annotationFact + "] because of missing property name");
          continue;
        }

        OWLAnnotationValueNode annotationValueNode = annotationFact.getOWLAnnotationValueNode();
        Optional<OWLAnnotationValueRendering> annotationValueRendering = renderOWLAnnotationValue(annotationValueNode);

        if (!annotationValueRendering.isPresent()) {
          individualDeclarationRendering
            .logLine("Skipping OWL annotation clause [" + annotationFact + "] because of missing annotation value");
          continue;
        }

        OWLNamedIndividual individual = declaredIndividualRendering.get().getOWLNamedIndividual();
        // TODO Check cast
        OWLAnnotationProperty property = (OWLAnnotationProperty)propertyRendering.get().getOWLProperty();
        OWLAnnotationValue annotationValue = annotationValueRendering.get().getOWLAnnotationValue();

        if (annotationValueNode.isReference()) { // We have an object property so tell the reference
          ReferenceNode referenceNode = annotationValueNode.getReferenceNode();
          if (!referenceNode.hasExplicitlySpecifiedReferenceType() && this.owlObjectHandler
            .isOWLObjectProperty(property))
            referenceNode.updateReferenceType(OWL_NAMED_INDIVIDUAL);
        }

        OWLAnnotationAssertionAxiom axiom = this.owlDataFactory
          .getOWLAnnotationAssertionAxiom(property, individual.getIRI(), annotationValue);
        axioms.add(axiom);
      }
    }
    return axioms;
  }

  private Set<OWLAxiom> processFactsClause(Rendering finalRendering,
    Optional<OWLNamedIndividualRendering> subjectIndividualRendering, List<FactNode> factNodes) throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    if (subjectIndividualRendering.isPresent()) {
      OWLIndividual subjectIndividual = subjectIndividualRendering.get().getOWLNamedIndividual();
      for (FactNode factNode : factNodes) {
        Optional<OWLPropertyRendering> propertyRendering = entityRenderer
          .renderOWLProperty(factNode.getOWLPropertyNode());

        if (!propertyRendering.isPresent()) {
          finalRendering
            .logLine("Skipping OWL fact declaration clause [" + factNode + "] because of missing property name");
          continue;
        }

        OWLPropertyAssertionObjectNode propertyAssertionObjectNode = factNode.getOWLPropertyAssertionObjectNode();
        Optional<OWLPropertyAssertionObjectRendering> propertyAssertionObjectRendering = renderOWLPropertyAssertionObject(
          propertyAssertionObjectNode);

        if (!propertyAssertionObjectRendering.isPresent()) {
          finalRendering
            .logLine("Skipping OWL fact declaration clause [" + factNode + "] because of missing property value");
          continue;
        }

        OWLProperty property = propertyRendering.get().getOWLProperty();
        OWLPropertyAssertionObject propertyAssertionObject = propertyAssertionObjectRendering.get()
          .getOWLPropertyAssertionObject();

        if (propertyAssertionObjectNode.isReference()) { // We have an object property so tell the reference the type
          ReferenceNode reference = propertyAssertionObjectNode.getReferenceNode();

          if (!reference.hasExplicitlySpecifiedReferenceType() && this.owlObjectHandler.isOWLObjectProperty(property))
            reference.updateReferenceType(OWL_NAMED_INDIVIDUAL);
        }

        if (this.owlObjectHandler.isOWLObjectProperty(property)) {
          OWLObjectProperty objectProperty = (OWLObjectProperty)property;
          OWLIndividual objectIndividual = (OWLIndividual)propertyAssertionObject; // TODO Check

          OWLObjectPropertyAssertionAxiom axiom = this.owlDataFactory
            .getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, objectIndividual);
          axioms.add(axiom);
        } else if (this.owlObjectHandler.isOWLDataProperty(property)) {
          OWLDataProperty dataProperty = (OWLDataProperty)property;
          OWLLiteral literal = (OWLLiteral)propertyAssertionObject; // TODO Check

          OWLDataPropertyAssertionAxiom axiom = this.owlDataFactory
            .getOWLDataPropertyAssertionAxiom(dataProperty, subjectIndividual, literal);
          axioms.add(axiom);
        } else {
          finalRendering.logLine(
            "Skipping OWL fact declaration clause [" + factNode + "] because property is an annotation property");
          continue;
        }
      }
    }
    return axioms;
  }
}

// TODO Needs to be seriously refactored. Way too long.

package org.mm.renderer.owlapi;

import org.mm.core.OWLEntityType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.LiteralNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassOrRestrictionNode;
import org.mm.parser.node.OWLEnumeratedClassNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIndividualNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLNamedClassNode;
import org.mm.parser.node.OWLPropertyValueNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.StringOrReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.renderer.DefaultRenderer;
import org.mm.renderer.RendererException;
import org.mm.renderer.Rendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class OWLAPIRenderer extends DefaultRenderer implements MappingMasterParserConstants
{
  public static int NameEncodings[] = { MM_LOCATION, MM_DATA_VALUE, RDF_ID, RDFS_LABEL };
  public static int ReferenceValueTypes[] = { OWL_CLASS, OWL_THING, OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY, XSD_INT,
    XSD_STRING, XSD_FLOAT, XSD_DOUBLE, XSD_SHORT, XSD_BOOLEAN, XSD_TIME, XSD_DATETIME, XSD_DURATION };
  public static int PropertyTypes[] = { OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY };
  public static int PropertyValueTypes[] = ReferenceValueTypes;
  public static int DataPropertyValueTypes[] = { XSD_INT, XSD_STRING, XSD_FLOAT, XSD_DOUBLE, XSD_SHORT, XSD_BOOLEAN,
    XSD_TIME, XSD_DATETIME, XSD_DURATION };

  // Configuration options
  public int defaultValueEncoding = RDFS_LABEL;
  public int defaultOWLEntityType = OWL_CLASS;
  public int defaultOWLPropertyType = OWL_OBJECT_PROPERTY;
  public int defaultOWLPropertyValueType = XSD_STRING;
  public int defaultOWLDataPropertyValueType = XSD_STRING;

  public int defaultEmptyLocationDirective = MM_PROCESS_IF_EMPTY_LOCATION;
  public int defaultEmptyRDFIDDirective = MM_PROCESS_IF_EMPTY_ID;
  public int defaultEmptyRDFSLabelDirective = MM_PROCESS_IF_EMPTY_LABEL;
  public int defaultIfExistsDirective = MM_RESOLVE_IF_EXISTS;
  public int defaultIfNotExistsDirective = MM_CREATE_IF_NOT_EXISTS;

  private final OWLOntology ontology;
  private final OWLDataFactory owlDataFactory;
  private final OWLAPIObjectHandler owlObjectHandler;
  private SpreadSheetDataSource dataSource;

  public OWLAPIRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource)
  {
    this.ontology = ontology;
    this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
    this.owlObjectHandler = new OWLAPIObjectHandler(ontology);
    this.dataSource = dataSource;
  }

  public void reset()
  {
    owlObjectHandler.reset();
  }

  public void setDataSource(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override public MMExpressionRendering renderMMExpression(MMExpressionNode mmExpressionNode) throws RendererException
  {
    if (mmExpressionNode.hasOWLClassDeclaration())
      return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
    else if (mmExpressionNode.hasOWLIndividualDeclaration())
      return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
    else
      throw new RendererException("unknown expression: " + mmExpressionNode);
  }

  @Override public MMExpressionRendering renderOWLClassDeclaration(OWLClassDeclarationNode classDeclarationNode)
    throws RendererException
  {
    MMExpressionRendering rendering = new MMExpressionRendering();

    rendering.logLine("=====================OWLClassDeclaration================================");
    rendering.logLine("MappingMaster DSL expression: " + classDeclarationNode);
    if (dataSource.hasCurrentLocation())
      rendering.logLine("********************Current location: " + dataSource.getCurrentLocation() + "*************");

    Rendering declaredClassRendering = renderOWLNamedClass(classDeclarationNode.getOWLNamedClassNode());

    if (declaredClassRendering.nothingRendered()) {
      rendering.logLine("processReference: skipping OWL class declaration because of missing class");
    } else {
      if (classDeclarationNode.hasSubclassOf()) {
        for (OWLSubclassOfNode subclassOfNode : classDeclarationNode.getSubclassOfNodes()) {
          for (OWLClassExpressionNode classExpressionNode : subclassOfNode.getClassExpressionNodes()) {
            Rendering classExpressionRendering = renderOWLClassExpression(classExpressionNode);
            if (classExpressionRendering.nothingRendered())
              rendering.logLine(
                "processReference: skipping subclass declaration [" + subclassOfNode + "] because of missing class");
            else {
              String classExpressionID = classExpressionRendering.getTextRendering();
              String declaredClassID = declaredClassRendering.getTextRendering();
              OWLClassExpression declaredClass = this.owlObjectHandler.getOWLClass(declaredClassID);
              OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(classExpressionID);
              OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(classExpression, declaredClass);
              rendering.addOWLAxiom(axiom);
            }
          }
        }
      }

      if (classDeclarationNode.hasEquivalentTo()) {
        for (OWLClassEquivalentToNode equivalentToNode : classDeclarationNode.getEquivalentToNodes()) {
          for (OWLClassExpressionNode classExpressionNode : equivalentToNode.getClassExpressionNodes()) {
            Rendering classExpressionRendering = renderOWLClassExpression(classExpressionNode);
            if (classExpressionRendering.nothingRendered())
              rendering.logLine(
                "processReference: skipping eqivalent declaration [" + equivalentToNode + "] because of missing class");
            else {
              String declaredClassID = declaredClassRendering.getTextRendering();
              String classExpressionID = classExpressionRendering.getTextRendering();
              OWLClassExpression declaredClass = this.owlObjectHandler.getOWLClass(declaredClassID);
              OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(classExpressionID);
              OWLEquivalentClassesAxiom axiom = owlDataFactory
                .getOWLEquivalentClassesAxiom(classExpression, declaredClass);
              rendering.addOWLAxiom(axiom);
            }
          }
        }
      }

      if (classDeclarationNode.hasAnnotations()) {
        for (AnnotationFactNode annotationFactNode : classDeclarationNode.getAnnotationFactNodes()) {
          Rendering propertyRendering = renderOWLProperty(annotationFactNode.getOWLPropertyNode());
          OWLPropertyValueNode propertyValueNode = annotationFactNode.getOWLPropertyValueNode();
          Rendering propertyValueRendering = renderOWLPropertyValue(propertyValueNode);

          if (propertyRendering.nothingRendered()) {
            rendering.logLine("processReference: skipping OWL annotation clause [" + annotationFactNode
              + "] because of missing property name");
            continue;
          }

          if (propertyValueNode.isReference()) {
            ReferenceNode referenceNode = propertyValueNode.getReferenceNode();
            String propertyShortName = propertyRendering.getTextRendering();
            if (!referenceNode.hasExplicitlySpecifiedEntityType() && this.owlObjectHandler
              .isOWLObjectProperty(propertyShortName))
              referenceNode.updateEntityType(OWL_THING);
          }

          if (propertyValueRendering.nothingRendered()) {
            rendering.logLine("processReference: skipping OWL annotation clause [" + annotationFactNode
              + "] because of missing property value");
            continue;
          }
          String classShortName = declaredClassRendering.getTextRendering();
          String propertyShortName = propertyRendering.getTextRendering();
          OWLClass cls = this.owlObjectHandler.getOWLClass(classShortName);
          OWLAnnotationProperty property = this.owlObjectHandler.getOWLAnnotationProperty(propertyShortName);
          String propertyValue = propertyValueRendering.getTextRendering();
          OWLAxiom axiom = createOWLAnnotationAssertionAxiom(cls, property, propertyValue, propertyValueNode);
          rendering.addOWLAxiom(axiom);
        }
      }
      rendering.setTextRendering(declaredClassRendering.getTextRendering());
    }
    return rendering;
  }

  @Override public MMExpressionRendering renderOWLIndividualDeclaration(
    OWLIndividualDeclarationNode individualDeclarationNode) throws RendererException
  {
    MMExpressionRendering individualDeclarationRendering = new MMExpressionRendering();

    individualDeclarationRendering
      .logLine("=====================OWLIndividualDeclaration================================");
    individualDeclarationRendering.logLine("MappingMaster DSL expression: " + individualDeclarationNode);
    if (dataSource.hasCurrentLocation())
      individualDeclarationRendering
        .logLine("********************Current location: " + dataSource.getCurrentLocation() + "*************");

    Rendering declaredIndividualRendering = renderOWLIndividual(individualDeclarationNode.getOWLIndividualNode());
    if (declaredIndividualRendering.nothingRendered()) {
      individualDeclarationRendering.logLine("Skipping OWL individual declaration because of missing individual name");
    } else {
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
        Set<OWLAxiom> axioms = processTypesClause(individualDeclarationNode, individualDeclarationRendering,
          declaredIndividualRendering);
        individualDeclarationRendering.addOWLAxioms(axioms);
      }

      // Final rendering is declared individual
      individualDeclarationRendering.setTextRendering(declaredIndividualRendering.getTextRendering());
    }

    return individualDeclarationRendering;
  }

  @Override public OWLClassExpressionRendering renderOWLUnionClass(OWLUnionClassNode unionClassNode) throws RendererException
  {
    Set<OWLClassExpression> classes = new HashSet<>();

    for (OWLIntersectionClassNode intersectionClassNode : unionClassNode.getOWLIntersectionClasseNodes()) {
      Rendering classRendering = renderOWLIntersectionClass(intersectionClassNode);
      String classExpressionID = classRendering.getTextRendering();
      if (!classRendering.nothingRendered())
        classes.add(this.owlObjectHandler.getOWLClassExpression(classExpressionID));
    }

    if (!classes.isEmpty()) {
      OWLObjectUnionOf restriction = this.owlDataFactory.getOWLObjectUnionOf(classes);
      String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
      OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

      return new OWLClassExpressionRendering(classExpression);
    } else
      return new OWLClassExpressionRendering(null); // TODO
  }

  @Override public MMExpressionRendering renderOWLIntersectionClass(OWLIntersectionClassNode intersectionClassNode)
    throws RendererException
  {
    Set<OWLClassExpression> classes = new HashSet<>();

    for (OWLClassOrRestrictionNode classOrRestrictionNode : intersectionClassNode.getOWLClassesOrRestrictionNodes()) {
      Rendering classRendering = renderOWLClassOrRestriction(classOrRestrictionNode);
      String classExpressionID = classRendering.getTextRendering();
      if (!classRendering.nothingRendered())
        classes.add(this.owlObjectHandler.getOWLClassExpression(classExpressionID));
    }

    if (!classes.isEmpty()) {
      OWLObjectIntersectionOf restriction = this.owlDataFactory.getOWLObjectIntersectionOf(classes);
      String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
      return new MMExpressionRendering(restrictionID);
    } else
      return new MMExpressionRendering();
  }

  @Override public MMExpressionRendering renderOWLClassOrRestriction(OWLClassOrRestrictionNode classOrRestrictionNode)
    throws RendererException
  {
    OWLClassExpressionRendering classExpressionRendering;

    if (classOrRestrictionNode.hasOWLEnumeratedClass())
      classExpressionRendering = renderOWLEnumeratedClass(classOrRestrictionNode.getOWLEnumeratedClassNode());
    else if (classOrRestrictionNode.hasOWLUnionClass())
      classExpressionRendering = renderOWLUnionClass(classOrRestrictionNode.getOWLUnionClassNode());
    else if (classOrRestrictionNode.hasOWLRestriction())
      classExpressionRendering = renderOWLRestriction(classOrRestrictionNode.getOWLRestrictionNode());
    else if (classOrRestrictionNode.hasOWLNamedClass())
      classExpressionRendering = renderOWLNamedClass(classOrRestrictionNode.getOWLNamedClassNode());
    else
      throw new RendererException("unknown OWLClassOrRestriction node " + classOrRestrictionNode);

    if (classOrRestrictionNode.getIsNegated()) {
      String classExpressionID = classExpressionRendering.getTextRendering();
      OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(classExpressionID);
      OWLObjectComplementOf restriction = this.owlDataFactory.getOWLObjectComplementOf(classExpression);
      String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
      return new MMExpressionRendering(restrictionID);
    } else
      return new MMExpressionRendering(classExpressionRendering.getTextRendering());
  }

  @Override public OWLClassExpressionRendering renderOWLEnumeratedClass(OWLEnumeratedClassNode owlEnumeratedClassNode)
    throws RendererException
  {
    Rendering rendering = super.renderOWLEnumeratedClass(owlEnumeratedClassNode);
    String classExpressionID = rendering.getTextRendering();
    OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(classExpressionID);

    return new OWLClassExpressionRendering(classExpression);
  }

  @Override public OWLClassExpressionRendering renderOWLNamedClass(OWLNamedClassNode owlNamedClassNode)
    throws RendererException
  {
    Rendering rendering = super.renderOWLNamedClass(owlNamedClassNode);
    String classExpressionID = rendering.getTextRendering();
    OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(classExpressionID);

    return new OWLClassExpressionRendering(classExpression);
  }

  // TODO Separate into data and object restrictions
  @Override public OWLClassExpressionRendering renderOWLRestriction(OWLRestrictionNode restrictionNode)
    throws RendererException
  {
    Rendering propertyRendering = renderOWLProperty(restrictionNode.getOWLPropertyNode());

    if (!propertyRendering.nothingRendered()) {
      String propertyShortName = propertyRendering.getTextRendering();
      if (this.owlObjectHandler.isOWLDataProperty(propertyShortName)) {
        OWLDataProperty property = this.owlObjectHandler.getOWLDataProperty(propertyShortName);
        if (restrictionNode.hasOWLMinCardinality()) {
          int cardinality = restrictionNode.getOWLMinCardinalityNode().getCardinality();
          OWLDataMinCardinality restriction = this.owlDataFactory.getOWLDataMinCardinality(cardinality, property);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLMaxCardinality()) {
          int cardinality = restrictionNode.getOWLMaxCardinalityNode().getCardinality();
          OWLDataMaxCardinality restriction = this.owlDataFactory.getOWLDataMaxCardinality(cardinality, property);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLCardinality()) {
          int cardinality = restrictionNode.getOWLCardinalityNode().getCardinality();
          OWLDataExactCardinality restriction = this.owlDataFactory.getOWLDataExactCardinality(cardinality, property);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLHasValue()) {
          OWLPropertyValueNode propertyValueNode = restrictionNode.getOWLHasValueNode().getOWLPropertyValueNode();
          if (!propertyValueNode.isLiteral())
            throw new RendererException("expecting data value for data has value restriction " + restrictionNode);
          OWLLiteral literal = getOWLLiteral(propertyValueNode);
          OWLDataHasValue restriction = this.owlDataFactory.getOWLDataHasValue(property, literal);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLAllValuesFrom()) {
          OWLAllValuesFromNode allValuesFromNode = restrictionNode.getOWLAllValuesFromNode();
          if (!allValuesFromNode.hasOWLAllValuesFromDataType())
            throw new RendererException("expecting data value for all values data restriction " + restrictionNode);
          OWLDatatype datatype = this.owlObjectHandler
            .getOWLDatatype(allValuesFromNode.getOWLAllValuesFromDataTypeNode().getDataTypeName());
          OWLDataAllValuesFrom restriction = this.owlDataFactory.getOWLDataAllValuesFrom(property, datatype);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLSomeValuesFrom()) {
          OWLSomeValuesFromNode someValuesFromNode = restrictionNode.getOWLSomeValuesFromNode();
          if (!someValuesFromNode.hasOWLSomeValuesFromDataType())
            throw new RendererException("expecting data value for some values data restriction " + restrictionNode);
          OWLDatatype datatype = this.owlObjectHandler
            .getOWLDatatype(someValuesFromNode.getOWLSomeValuesFromDataTypeNode().getDataTypeName());
          OWLDataSomeValuesFrom restriction = this.owlDataFactory.getOWLDataSomeValuesFrom(property, datatype);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else
          return new MMExpressionRendering();
      } else { // Object property
        OWLObjectProperty property = this.owlObjectHandler.getOWLObjectProperty(propertyShortName);
        if (restrictionNode.hasOWLMinCardinality()) {
          int cardinality = restrictionNode.getOWLMinCardinalityNode().getCardinality();
          OWLObjectMinCardinality restriction = this.owlDataFactory.getOWLObjectMinCardinality(cardinality, property);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLMaxCardinality()) {
          int cardinality = restrictionNode.getOWLMaxCardinalityNode().getCardinality();
          OWLObjectMaxCardinality restriction = this.owlDataFactory.getOWLObjectMaxCardinality(cardinality, property);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLCardinality()) {
          int cardinality = restrictionNode.getOWLCardinalityNode().getCardinality();
          OWLObjectExactCardinality restriction = this.owlDataFactory
            .getOWLObjectExactCardinality(cardinality, property);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLHasValue()) {
          OWLPropertyValueNode propertyValueNode = restrictionNode.getOWLHasValueNode().getOWLPropertyValueNode();
          if (propertyValueNode.isLiteral())
            throw new RendererException("expecting class for object has value restriction " + restrictionNode);
          Rendering individualRendering = renderOWLPropertyValue(propertyValueNode);
          String individualShortName = individualRendering.getTextRendering();
          OWLNamedIndividual individual = this.owlObjectHandler.getOWLNamedIndividual(individualShortName);
          OWLObjectHasValue restriction = this.owlDataFactory.getOWLObjectHasValue(property, individual);
          String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
          OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

          return new OWLClassExpressionRendering(classExpression);
        } else if (restrictionNode.hasOWLAllValuesFrom()) {
          OWLAllValuesFromNode allValuesFromNode = restrictionNode.getOWLAllValuesFromNode();
          if (allValuesFromNode.hasOWLAllValuesFromDataType())
            throw new RendererException("expecting class for all values object restriction " + restrictionNode);
          Rendering classRendering;
          if (allValuesFromNode.getOWLAllValuesFromClassNode().hasOWLClassExpression())
            classRendering = renderOWLClassExpression(
              allValuesFromNode.getOWLAllValuesFromClassNode().getOWLClassExpressionNode());
          else
            classRendering = renderOWLNamedClass(
              allValuesFromNode.getOWLAllValuesFromClassNode().getOWLNamedClassNode());

          if (!classRendering.nothingRendered()) {
            String classExpressionID = classRendering.getTextRendering();
            OWLClassExpression cls = this.owlObjectHandler.getOWLClassExpression(classExpressionID);
            OWLObjectAllValuesFrom restriction = this.owlDataFactory.getOWLObjectAllValuesFrom(property, cls);
            String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
            OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

            return new OWLClassExpressionRendering(classExpression);
          } else
            return new MMExpressionRendering();
        } else if (restrictionNode.hasOWLSomeValuesFrom()) {
          OWLSomeValuesFromNode someValuesFromNode = restrictionNode.getOWLSomeValuesFromNode();
          if (someValuesFromNode.hasOWLSomeValuesFromDataType())
            throw new RendererException("expecting class for object some values  restriction " + restrictionNode);
          Rendering classRendering;
          if (someValuesFromNode.getOWLSomeValuesFromClassNode().hasOWLClassExpression())
            classRendering = renderOWLClassExpression(
              someValuesFromNode.getOWLSomeValuesFromClassNode().getOWLClassExpressionNode());
          else
            classRendering = renderOWLNamedClass(
              someValuesFromNode.getOWLSomeValuesFromClassNode().getOWLNamedClassNode());
          if (!classRendering.nothingRendered()) {
            String classExpressionID = classRendering.getTextRendering();
            OWLClassExpression cls = this.owlObjectHandler.getOWLClassExpression(classExpressionID);
            OWLObjectSomeValuesFrom restriction = this.owlDataFactory.getOWLObjectSomeValuesFrom(property, cls);
            String restrictionID = this.owlObjectHandler.registerOWLClassExpression(restriction);
            OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(restrictionID);

            return new OWLClassExpressionRendering(classExpression);
          } else
            return new MMExpressionRendering();
        } else
          return new MMExpressionRendering(null);
      }
    } else
      return new MMExpressionRendering(null);
  }

  // TODO Too long. Clean up.
  @Override public MMExpressionRendering renderReference(ReferenceNode referenceNode) throws RendererException
  {
    SpreadsheetLocation location = getLocation(referenceNode.getSourceSpecificationNode());
    String defaultNamespace = getReferenceNamespace(referenceNode);
    String language = getReferenceLanguage(referenceNode);
    MMExpressionRendering rendering = new MMExpressionRendering();

    rendering.logLine("<<<<<<<<<<<<<<<<<<<< Rendering reference [" + referenceNode + "] <<<<<<<<<<<<<<<<<<<<");

    String locationValue = processLocationValue(location, referenceNode, rendering);

    if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION)
      return rendering;

    OWLEntityType entityType = referenceNode.getEntityTypeNode().getEntityType();

    if (entityType.isUntyped())
      throw new RendererException("untyped entity for reference " + referenceNode);

    if (entityType.isOWLDataValue()) { // OWL data value
      String processedOWLDataValue = processOWLDataValue(location, locationValue, entityType, referenceNode, rendering);

      if (processedOWLDataValue.equals("")
        && referenceNode.getActualEmptyDataValueDirective() == MM_SKIP_IF_EMPTY_DATA_VALUE)
        return rendering;

      rendering.addText(processedOWLDataValue);
    } else { // OWL Class, Individual, ObjectProperty, or DataProperty
      String rdfID = processRDFIDValue(locationValue, referenceNode, rendering);
      String rdfsLabelText = processRDFSLabelText(locationValue, referenceNode, rendering);
      OWLEntity entity = this.owlObjectHandler
        .createOrResolveOWLEntity(location, locationValue, entityType, rdfID, rdfsLabelText, defaultNamespace, language,
          referenceNode.getReferenceDirectives());
      addDefiningTypes(entityType, entity, referenceNode);
      rendering.addText(rdfID);
    }

    if (!rendering.nothingRendered())
      rendering.logLine(">>>>>>>>>>>>>>>>>>>> Reference [" + referenceNode.toString() + "] rendered as " + referenceNode
        .getEntityTypeNode() + " " + rendering.getTextRendering() + " >>>>>>>>>>>>>>>>>>>>");
    else
      rendering
        .logLine(">>>>>>>>>>>>>>>>>>>> Reference [" + referenceNode + "] - nothing rendered >>>>>>>>>>>>>>>>>>>>");

    return rendering;
  }

  private Set<OWLAxiom> processTypesClause(OWLIndividualDeclarationNode individualDeclarationNode,
    Rendering individualDeclarationRendering, Rendering declaredIndividualRendering) throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    for (TypeNode typeNode : individualDeclarationNode.getTypeNodes().getTypeNodes()) {
      Rendering typeRendering = renderType(typeNode);
      if (typeRendering.nothingRendered()) {
        individualDeclarationRendering.logLine(
          "processReference: skipping OWL type declaration clause [" + typeNode + "] for individual "
            + declaredIndividualRendering + " because of missing type");
        continue;
      }
      String individualShortName = declaredIndividualRendering.getTextRendering();
      String classShortName = typeRendering.getTextRendering();
      OWLNamedIndividual individual = this.owlObjectHandler.getOWLNamedIndividual(individualShortName);
      OWLClass cls = this.owlObjectHandler.getOWLClass(classShortName);
      OWLClassAssertionAxiom axiom = this.owlDataFactory.getOWLClassAssertionAxiom(cls, individual);

      axioms.add(axiom);
    }

    return axioms;
  }

  private Set<OWLAxiom> processSameAsClause(OWLIndividualDeclarationNode individualDeclarationNode,
    Rendering declaredIndividualRendering) throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    for (OWLIndividualNode sameAsIndividualNode : individualDeclarationNode.getSameAsNode().getIndividualNodes()) {
      Rendering sameAsIndividualRendering = renderOWLIndividual(sameAsIndividualNode);
      if (!sameAsIndividualRendering.nothingRendered()) {
        String individual1ShortName = declaredIndividualRendering.getTextRendering();
        String individual2ShortName = sameAsIndividualRendering.getTextRendering();
        OWLNamedIndividual individual1 = this.owlObjectHandler.getOWLNamedIndividual(individual1ShortName);
        OWLNamedIndividual individual2 = this.owlObjectHandler.getOWLNamedIndividual(individual2ShortName);
        OWLSameIndividualAxiom axiom = owlDataFactory.getOWLSameIndividualAxiom(individual1, individual2);

        axioms.add(axiom);
      }
    }
    return axioms;
  }

  private Set<OWLAxiom> processDifferentFromClause(OWLIndividualDeclarationNode individualDeclarationNode,
    Rendering declaredIndividualRendering) throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    for (OWLIndividualNode differentFromIndividualNode : individualDeclarationNode.getDifferentFromNode()
      .getIndividualNodes()) {
      Rendering differentFromIndividualRendering = renderOWLIndividual(differentFromIndividualNode);
      if (!differentFromIndividualRendering.nothingRendered()) {
        String individual1ShortName = declaredIndividualRendering.getTextRendering();
        String individual2ShortName = differentFromIndividualRendering.getTextRendering();
        OWLNamedIndividual individual1 = this.owlObjectHandler.getOWLNamedIndividual(individual1ShortName);
        OWLNamedIndividual individual2 = this.owlObjectHandler.getOWLNamedIndividual(individual2ShortName);
        OWLDifferentIndividualsAxiom axiom = owlDataFactory.getOWLDifferentIndividualsAxiom(individual1, individual2);

        axioms.add(axiom);
      }
    }
    return axioms;
  }

  private Set<OWLAxiom> processAnnotationClause(Rendering individualDeclarationRendering,
    Rendering declaredIndividualRendering, List<AnnotationFactNode> annotationFactNodes) throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    for (AnnotationFactNode annotationFact : annotationFactNodes) {
      Rendering propertyRendering = renderOWLProperty(annotationFact.getOWLPropertyNode());
      OWLPropertyValueNode propertyValueNode = annotationFact.getOWLPropertyValueNode();
      Rendering propertyValueRendering = renderOWLPropertyValue(propertyValueNode);

      if (propertyRendering.nothingRendered()) {
        individualDeclarationRendering
          .logLine("Skipping OWL annotation clause [" + annotationFact + "] because of missing property name");
        continue;
      }

      if (propertyValueNode.isReference()) { // We have an object property so tell the reference
        ReferenceNode referenceNode = propertyValueNode.getReferenceNode();
        String propertyShortName = propertyRendering.getTextRendering();
        if (!referenceNode.hasExplicitlySpecifiedEntityType() && this.owlObjectHandler
          .isOWLObjectProperty(propertyShortName))
          referenceNode.updateEntityType(OWL_THING);
      }

      if (propertyValueRendering.nothingRendered()) {
        individualDeclarationRendering
          .logLine("Skipping OWL annotation clause [" + annotationFact + "] because of missing property value");
        continue;
      }

      String individualShortName = declaredIndividualRendering.getTextRendering();
      String propertyShortName = propertyRendering.getTextRendering();
      String propertyValue = propertyValueRendering.getTextRendering();
      OWLNamedIndividual individual = this.owlObjectHandler.getOWLNamedIndividual(individualShortName);
      OWLAnnotationProperty property = this.owlObjectHandler.getOWLAnnotationProperty(propertyShortName);

      OWLAnnotationAssertionAxiom axiom = createOWLAnnotationAssertionAxiom(individual, property, propertyValue,
        propertyValueNode);
      axioms.add(axiom);
    }
    return axioms;
  }

  private Set<OWLAxiom> processFactsClause(Rendering individualDeclarationRendering,
    Rendering declaredIndividualRendering, List<FactNode> factNodes) throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    for (FactNode factNode : factNodes) {
      Rendering propertyRendering = renderOWLProperty(factNode.getOWLPropertyNode());
      String propertyShortName = propertyRendering.getTextRendering();
      OWLPropertyValueNode propertyValueNode = factNode.getOWLPropertyValueNode();
      Rendering propertyValueRendering = renderOWLPropertyValue(propertyValueNode);

      if (propertyRendering.nothingRendered()) {
        individualDeclarationRendering
          .logLine("Skipping OWL fact declaration clause [" + factNode + "] because of missing property name");
        continue;
      }
      if (propertyValueRendering.nothingRendered()) {
        individualDeclarationRendering
          .logLine("Skipping OWL fact declaration clause [" + factNode + "] because of missing property value");
        continue;
      }
      if (propertyValueNode.isReference()) { // We have an object property so tell the reference the type
        ReferenceNode reference = propertyValueNode.getReferenceNode();

        if (!reference.hasExplicitlySpecifiedEntityType() && this.owlObjectHandler
          .isOWLObjectProperty(propertyShortName))
          reference.updateEntityType(OWL_THING);
      }

      if (this.owlObjectHandler.isOWLObjectProperty(propertyShortName)) {
        String individualShortName = declaredIndividualRendering.getTextRendering();
        String propertyValue = propertyValueRendering.getTextRendering();
        OWLNamedIndividual individual = this.owlObjectHandler.getOWLNamedIndividual(individualShortName);
        OWLObjectProperty property = this.owlObjectHandler.getOWLObjectProperty(propertyShortName);

        OWLObjectPropertyAssertionAxiom axiom = createOWLObjectPropertyAssertionAxiom(individual, property,
          propertyValue, propertyValueNode);
        axioms.add(axiom);
      } else if (this.owlObjectHandler.isOWLDataProperty(propertyShortName)) {
        String individualShortName = declaredIndividualRendering.getTextRendering();
        String propertyValue = propertyValueRendering.getTextRendering();
        OWLNamedIndividual individual = this.owlObjectHandler.getOWLNamedIndividual(individualShortName);
        OWLDataProperty property = this.owlObjectHandler.getOWLDataProperty(propertyShortName);

        OWLDataPropertyAssertionAxiom axiom = createOWLDataPropertyAssertionAxiom(individual, property, propertyValue,
          propertyValueNode);
        axioms.add(axiom);
      } else {
        individualDeclarationRendering.logLine(
          "Skipping OWL fact declaration clause [" + factNode + "] because property is an annotation property");
        continue;
      }
    }
    return axioms;
  }

  // TODO Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?
  private String processValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode, String value)
    throws RendererException
  {
    List<String> arguments = new ArrayList<>();
    String functionName = valueExtractionFunctionNode.getFunctionName();
    boolean hasExplicitArguments = valueExtractionFunctionNode.hasArguments();
    String processedValue;

    if (valueExtractionFunctionNode.hasArguments()) {
      for (StringOrReferenceNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
        Rendering argumentRendering = renderStringOrReference(argumentNode);
        arguments.add(argumentRendering.getTextRendering());
      }
    }

    switch (valueExtractionFunctionNode.getFunctionID()) {
    case MM_TO_UPPER_CASE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException("function " + functionName + " expecting one argument, got " + arguments.size());
        processedValue = arguments.get(0).toUpperCase();
      } else
        processedValue = value.toUpperCase();
      break;
    case MM_TO_LOWER_CASE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedValue = arguments.get(0).toLowerCase();
      } else
        processedValue = value.toLowerCase();
      break;
    case MM_TRIM:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedValue = arguments.get(0).trim();
      } else
        processedValue = value.trim();
      break;
    case MM_REVERSE:
      if (hasExplicitArguments) {
        if (arguments.size() != 1)
          throw new RendererException(
            "function " + functionName + " expecting only one argument, got " + arguments.size());
        processedValue = reverse(arguments.get(0));
      } else
        processedValue = reverse(value);
      break;
    case MM_CAPTURING:
      if (arguments.size() == 1) {
        processedValue = processCapturingExpression(value, arguments.get(0));
      } else if (arguments.size() == 2) {
        processedValue = processCapturingExpression(arguments.get(0), arguments.get(1));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_PREPEND:
      if (arguments.size() == 1) {
        processedValue = arguments.get(0) + value;
      } else if (arguments.size() == 2) {
        processedValue = arguments.get(0) + arguments.get(1);
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_APPEND:
      if (arguments.size() == 1) {
        processedValue = value + arguments.get(0);
      } else if (arguments.size() == 2) {
        processedValue = arguments.get(0) + arguments.get(1);
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_REPLACE:
      if (arguments.size() == 2) {
        processedValue = value.replace(arguments.get(0), arguments.get(1));
      } else if (arguments.size() == 3) {
        processedValue = arguments.get(0).replace(arguments.get(1), arguments.get(2));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_REPLACE_ALL:
      if (arguments.size() == 2) {
        processedValue = value.replaceAll(arguments.get(0), arguments.get(1));
      } else if (arguments.size() == 3) {
        processedValue = arguments.get(0).replaceAll(arguments.get(1), arguments.get(2));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    case MM_REPLACE_FIRST:
      if (arguments.size() == 2) {
        processedValue = value.replaceFirst(arguments.get(0), arguments.get(1));
      } else if (arguments.size() == 3) {
        processedValue = arguments.get(0).replaceFirst(arguments.get(1), arguments.get(2));
      } else
        throw new RendererException(
          "function " + functionName + " expecting two or three arguments, got " + arguments.size());
      break;
    default:
      throw new RendererException("unknown mapping function " + valueExtractionFunctionNode.getFunctionName() + ")");
    }
    return processedValue;
  }

  private OWLLiteral getOWLLiteral(OWLPropertyValueNode propertyValueNode) throws RendererException
  {
    LiteralNode literalNode = propertyValueNode.getLiteralNode();

    if (literalNode.isBoolean())
      return this.owlDataFactory.getOWLLiteral(literalNode.getBooleanLiteralNode().getValue());
    else if (literalNode.isInteger())
      return this.owlDataFactory.getOWLLiteral(literalNode.getIntegerLiteralNode().getValue());
    else if (literalNode.isFloat())
      return this.owlDataFactory.getOWLLiteral(literalNode.getFloatLiteralNode().getValue());
    else if (literalNode.isString())
      return this.owlDataFactory.getOWLLiteral(literalNode.getStringLiteralNode().getValue());
    else
      throw new RendererException(
        "unknown OWL literal property value " + super.renderOWLPropertyValue(propertyValueNode));
  }

  private SpreadsheetLocation getLocation(SourceSpecificationNode sourceSpecificationNode) throws RendererException
  {
    if (sourceSpecificationNode.hasLiteral())
      return null;
    else
      return dataSource.resolveLocation(sourceSpecificationNode);
  }

  private String processLocationValue(SpreadsheetLocation location, ReferenceNode referenceNode, Rendering rendering)
    throws RendererException
  {
    String locationValue;
    SourceSpecificationNode sourceSpecification = referenceNode.getSourceSpecificationNode();

    if (sourceSpecification.hasLiteral()) {
      // Reference is a literal, e.g., @"Person", @"http://a.com#Person"
      locationValue = sourceSpecification.getLiteral();
      rendering.log("processReference: literal");
    } else { // Reference to data source location

      rendering.log("--processReference: specified location " + location);
      locationValue = dataSource.getLocationValue(location, referenceNode); // Deals with shifting

      if ((locationValue == null || locationValue.equals("")))
        locationValue = referenceNode.getActualDefaultLocationValue();

      if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_ERROR_IF_EMPTY_LOCATION)
        throw new RendererException("empty location " + location + " in reference " + referenceNode);

      if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_WARNING_IF_EMPTY_LOCATION)
        rendering.logLine("processReference: WARNING: empty location " + location + " in reference " + referenceNode);

      rendering.log(", location value [" + locationValue + "], entity type " + referenceNode.getEntityTypeNode());
    }

    if (!referenceNode.getEntityTypeNode().getEntityType().isOWLDataValue()) {
      rendering.log(", namespace " + getReferenceNamespace(referenceNode));
      String language = getReferenceLanguage(referenceNode);
      displayLanguage(language, rendering);
      rendering.log(", valueEncoding");
      for (ValueEncodingNode valueEncoding : referenceNode.getValueEncodingNodes())
        rendering.log(" " + valueEncoding);
    }

    if (!sourceSpecification.hasLiteral()) { // Determine if originally specified location has been shifted
      if (referenceNode.getActualShiftDirective() != MM_NO_SHIFT && referenceNode.hasShiftedLocation()) {
        location = referenceNode.getShiftedLocation();
        rendering.log(", shifted location " + referenceNode.getShiftedLocation());
      }
    }
    rendering.logLine("");

    return locationValue;
  }

  private String processRDFIDValue(String locationValue, ReferenceNode referenceNode, Rendering rendering)
    throws RendererException
  {
    String rdfIDValue;

    if (referenceNode.hasRDFIDValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding())
        rdfIDValue = processValueEncoding(locationValue, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        rdfIDValue = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(), locationValue);
      else
        rdfIDValue = locationValue;
    } else
      rdfIDValue = "";

    if (rdfIDValue.equals("") && !referenceNode.getActualDefaultRDFID().equals(""))
      rdfIDValue = referenceNode.getActualDefaultRDFID();

    if (rdfIDValue.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_ID)
      throw new RendererException("empty RDF ID in reference " + referenceNode);

    if (rdfIDValue.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_ID)
      rendering.logLine("processReference: WARNING: empty RDF ID in reference");

    return rdfIDValue;
  }

  private String processRDFSLabelText(String locationValue, ReferenceNode referenceNode, Rendering rendering)
    throws RendererException
  {
    String rdfsLabelText;

    if (referenceNode.hasRDFSLabelValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding())
        rdfsLabelText = processValueEncoding(locationValue, referenceNode.getRDFSLabelValueEncodingNode(),
          referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        rdfsLabelText = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(), locationValue);
      else
        rdfsLabelText = locationValue;
    } else
      rdfsLabelText = "";

    if (rdfsLabelText.equals("") && !referenceNode.getActualDefaultRDFSLabel().equals(""))
      rdfsLabelText = referenceNode.getActualDefaultRDFSLabel();

    if (rdfsLabelText.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_LABEL)
      throw new RendererException("empty RDFS label in reference " + referenceNode);

    if (rdfsLabelText.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_LABEL)
      rendering.logLine("processReference: WARNING: empty RDFS label in reference");

    return rdfsLabelText;
  }

  private String processOWLDataValue(SpreadsheetLocation location, String locationValue, OWLEntityType entityType,
    ReferenceNode referenceNode, Rendering rendering) throws RendererException
  {
    String processedLocationValue = locationValue.replace("\"", "\\\"");
    String dataValue;

    if (referenceNode.hasLiteralValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
        dataValue = processValueEncoding(processedLocationValue, referenceNode.getLiteralValueEncodingNode(),
          referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        dataValue = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(),
          processedLocationValue);
      else
        dataValue = processedLocationValue;
    } else
      dataValue = "";

    if (dataValue.equals("") && !referenceNode.getActualDefaultDataValue().equals(""))
      dataValue = referenceNode.getActualDefaultDataValue();

    if (dataValue.equals("") && referenceNode.getActualEmptyDataValueDirective() == MM_ERROR_IF_EMPTY_DATA_VALUE)
      throw new RendererException("empty data value in reference " + referenceNode + " at location " + location);

    if (dataValue.equals("") && referenceNode.getActualEmptyDataValueDirective() == MM_WARNING_IF_EMPTY_DATA_VALUE)
      rendering.logLine(
        "processReference: WARNING: empty data value in reference " + referenceNode + " at location " + location);

    if (entityType.isQuotedOWLDataValue())
      dataValue = "\"" + dataValue + "\"";

    return dataValue;
  }

  private void displayLanguage(String language, Rendering rendering)
  {
    String display = "";

    if (language != null) {
      display += ", xml:lang";
      if (language.equals(""))
        display += "=mm:null";
      else if (!language.equals("*"))
        display += "=*";
      else if (language.equals("+"))
        display += "!=mm:null";
      else
        display += language;
    }
    rendering.log(display);
  }

  private String getReferenceNamespace(ReferenceNode referenceNode) throws RendererException
  {
    String namespace;

    // A reference will not have both a prefix and a namespace specified
    if (referenceNode.hasExplicitlySpecifiedPrefix()) {
      String prefix = referenceNode.getPrefixNode().getPrefix();
      namespace = getNamespaceForPrefix(prefix);
      if (namespace == null)
        throw new RendererException("unknown prefix " + prefix + " specified in reference " + referenceNode);
    } else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      namespace = referenceNode.getNamespaceNode().getNamespace();
    } else {
      if (!hasDefaultNamespace())
        throw new RendererException(
          "ontology has no default namespace and no namespace specified by reference " + referenceNode);

      namespace = getDefaultNamespace();
    }

    return namespace;
  }

  private String getReferenceLanguage(ReferenceNode referenceNode) throws RendererException
  {
    if (referenceNode.hasExplicitlySpecifiedLanguage())
      return referenceNode.getActualLanguage();
    else
      return getDefaultLanguage(); // Which might be null or empty
  }

  private String processValueEncoding(String value, ValueEncodingNode valueEncodingNode, ReferenceNode referenceNode)
    throws RendererException
  {
    String processedValue = "";

    if (valueEncodingNode != null) {
      if (valueEncodingNode.hasValueSpecification())
        processedValue = processValueSpecification(value, valueEncodingNode.getValueSpecification(), referenceNode);
      else
        processedValue = value;
    } else
      processedValue = value;

    return processedValue;
  }

  private String processValueSpecification(String value, ValueSpecificationNode valueSpecificationNode,
    ReferenceNode referenceNode) throws RendererException
  {
    String processedValue = "";

    for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
      .getValueSpecificationItemNodes()) {
      if (valueSpecificationItemNode.hasStringLiteral())
        processedValue += valueSpecificationItemNode.getStringLiteral();
      else if (valueSpecificationItemNode.hasReference()) {
        ReferenceNode valueSpecificationItemReferenceNode = valueSpecificationItemNode.getReferenceNode();
        String referenceTextRendering;
        valueSpecificationItemReferenceNode.setDefaultShiftSetting(referenceNode.getActualShiftDirective());
        referenceTextRendering = renderReference(valueSpecificationItemReferenceNode).getTextRendering();
        if (valueSpecificationItemReferenceNode.getEntityTypeNode().getEntityType().isQuotedOWLDataValue()
          && !referenceTextRendering.equals("") && referenceTextRendering.startsWith("\""))
          processedValue += referenceTextRendering.substring(1, referenceTextRendering.length() - 1); // Strip quotes
          // from quoted
          // renderings
        else
          processedValue += referenceTextRendering;
      } else if (valueSpecificationItemNode.hasValueExtractionFunction()) {
        ValueExtractionFunctionNode valueExtractionFunction = valueSpecificationItemNode
          .getValueExtractionFunctionNode();
        processedValue += processValueExtractionFunction(valueExtractionFunction, value);
      } else if (valueSpecificationItemNode.hasCapturingExpression() && value != null) {
        String capturingExpression = valueSpecificationItemNode.getCapturingExpression();
        processedValue += processCapturingExpression(value, capturingExpression);
      }
    }

    return processedValue;
  }

  private String processCapturingExpression(String locationValue, String capturingExpression) throws RendererException
  {
    String result = "";

    try {
      Pattern p = Pattern.compile(capturingExpression); // Pull the value out of the location
      Matcher m = p.matcher(locationValue);
      boolean matchFound = m.find();
      if (matchFound) {
        for (int groupIndex = 1; groupIndex <= m.groupCount(); groupIndex++)
          result += (m.group(groupIndex));
      }
    } catch (PatternSyntaxException e) {
      throw new RendererException("invalid capturing expression: " + capturingExpression + ": " + e.getMessage());
    }
    return result;
  }

  private Set<OWLAxiom> addDefiningTypes(OWLEntityType entityType, OWLEntity entity, ReferenceNode referenceNode)
    throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    if (referenceNode.hasExplicitlySpecifiedTypes()) {
      for (TypeNode typeNode : referenceNode.getTypesNode().getTypeNodes()) {
        Rendering typeRendering = renderType(typeNode);
        if (!typeRendering.nothingRendered()) {
          String typeTextRendering = typeRendering.getTextRendering();
          if (entityType.isOWLClass()) {
            if (!entity.isOWLClass())
              throw new RendererException(
                "expecting class for type in reference " + referenceNode + " for " + entity + ", got " + entity
                  .getClass().getCanonicalName());
            String classExpressionID = typeTextRendering;
            OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(classExpressionID);
            OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(classExpression, entity.asOWLClass());
            axioms.add(axiom);
          } else if (entityType.isOWLIndividual()) {
            if (!entity.isOWLNamedIndividual())
              throw new RendererException(
                "expecting individual for type in reference " + referenceNode + " for " + entity + ", got " + entity
                  .getClass().getCanonicalName());
            String classExpressionID = typeTextRendering;
            OWLClassExpression classExpression = this.owlObjectHandler.getOWLClass(classExpressionID);
            OWLClassAssertionAxiom axiom = this.owlDataFactory
              .getOWLClassAssertionAxiom(classExpression, entity.asOWLNamedIndividual());
            axioms.add(axiom);
          } else if (entityType.isOWLObjectProperty()) {
            if (!entity.isOWLObjectProperty())
              throw new RendererException(
                "expecting object property for type in reference " + referenceNode + " for " + entity);
            String propertyShortName = typeTextRendering;
            OWLObjectProperty property = this.owlObjectHandler.getOWLObjectProperty(propertyShortName);
            OWLSubObjectPropertyOfAxiom axiom = owlDataFactory
              .getOWLSubObjectPropertyOfAxiom(property, entity.asOWLObjectProperty());
            axioms.add(axiom);
          } else if (entityType.isOWLDataProperty()) {
            if (!entity.isOWLDataProperty())
              throw new RendererException(
                "expecting data property for type in reference " + referenceNode + " for " + entity);
            String propertyShortName = typeTextRendering;
            OWLDataProperty property = this.owlObjectHandler.getOWLDataProperty(propertyShortName);
            OWLSubDataPropertyOfAxiom axiom = owlDataFactory
              .getOWLSubDataPropertyOfAxiom(property, entity.asOWLDataProperty());
            axioms.add(axiom);
          } else
            throw new RendererException("invalid entity type " + entityType);
        }
      }
    }
    return axioms;
  }

  private OWLDataPropertyAssertionAxiom createOWLDataPropertyAssertionAxiom(OWLNamedIndividual subject,
    OWLDataProperty property, String propertyValue, OWLPropertyValueNode propertyValueNode) throws RendererException
  {
    if (propertyValueNode.isReference()) {
      ReferenceNode propertyValueReference = propertyValueNode.getReferenceNode();
      if (this.ontology.containsDataPropertyInSignature(property.getIRI())) {
        if (propertyValueReference.hasExplicitlySpecifiedEntityType()) {
          String typeShortName = propertyValueReference.getEntityTypeNode().getEntityType().getTypeName();
          OWLDatatype datatype = this.owlObjectHandler.getOWLDatatype(typeShortName);
          if (datatype.isString()) {
            String language = getReferenceLanguage(propertyValueReference);
            if (language != null) {
              OWLDataPropertyAssertionAxiom axiom = this.owlDataFactory
                .getOWLDataPropertyAssertionAxiom(property, subject, propertyValue);
              // TODO add language
              return axiom;
            } else {
              OWLLiteral literal = this.owlDataFactory.getOWLLiteral(propertyValue, datatype);
              return this.owlDataFactory.getOWLDataPropertyAssertionAxiom(property, subject, literal);
            }
          } else { // Data property but not a string
            OWLLiteral literal = this.owlDataFactory.getOWLLiteral(propertyValue, datatype);
            return this.owlDataFactory.getOWLDataPropertyAssertionAxiom(property, subject, literal);
          }
        } else { // No entity type explicitly specified
          return this.owlDataFactory.getOWLDataPropertyAssertionAxiom(property, subject, propertyValue);
        }
      } else
        throw new RendererException("property " + property + " is not an OWL data property");
    } else { // Not a reference - will be an entity name or a literal value
      if (ontology.containsDataPropertyInSignature(property.getIRI()))
        return this.owlDataFactory.getOWLDataPropertyAssertionAxiom(property, subject, propertyValue);
      else
        throw new RendererException("property " + property + " is not an OWL data property");
    }
  }

  private OWLObjectPropertyAssertionAxiom createOWLObjectPropertyAssertionAxiom(OWLNamedIndividual subject,
    OWLObjectProperty property, String propertyValue, OWLPropertyValueNode propertyValueNode) throws RendererException
  {
    if (propertyValueNode.isReference()) {
      if (ontology.containsObjectPropertyInSignature(property.getIRI())) {
        OWLNamedIndividual object = this.owlObjectHandler.getOWLNamedIndividual(propertyValue);
        return this.owlDataFactory.getOWLObjectPropertyAssertionAxiom(property, subject, object);
      } else
        throw new RendererException("property " + property + " is not an OWL object or data property");
    } else { // Not a reference - will be an entity name
      if (ontology.containsObjectPropertyInSignature(property.getIRI())) {
        OWLNamedIndividual object = this.owlObjectHandler.getOWLNamedIndividual(propertyValue);
        return this.owlDataFactory.getOWLObjectPropertyAssertionAxiom(property, subject, object);
      } else
        throw new RendererException("property " + property + " is not an OWL object or data property");
    }
  }

  private OWLAnnotationAssertionAxiom createOWLAnnotationAssertionAxiom(OWLEntity entity,
    OWLAnnotationProperty property, String propertyValue, OWLPropertyValueNode propertyValueNode)
    throws RendererException
  {
    if (propertyValueNode.isReference()) {
      ReferenceNode propertyValueReference = propertyValueNode.getReferenceNode();
      if (propertyValueReference.hasExplicitlySpecifiedEntityType()) {
        String typeShortName = propertyValueReference.getEntityTypeNode().getEntityType().getTypeName();
        OWLDatatype datatype = this.owlObjectHandler.getOWLDatatype(typeShortName);
        if (datatype.isString()) {
          String language = getReferenceLanguage(propertyValueReference);
          if (language != null && !language.equals("")) {
            //TODO OWLAnnotationAssertionAxiom axiom = this.owlDataFactory.getOWLAnnotationAssertionAxiom(property, entity, propertyValue);
            // TODO Add language
            return null;
          } else {
            // TODO return this.owlDataFactory.getOWLAnnotationAssertionAxiom(property, entity, propertyValue, datatype);
            return null;
          }
        } else { // Not a string
          // TODO return this.owlDataFactory.getOWLAnnotationAssertionAxiom(property, entity, propertyValue, datatype);
          return null;
        }
      } else { // No entity type explicitly specified
        // TODO return this.owlDataFactory.getOWLAnnotationAssertionAxiom(property, entity, propertyValue);
        return null;
      }
    } else { // Not a reference - will be an entity name or a literal value
      // TODO return this.owlDataFactory.getOWLAnnotationAssertionAxiom(property, entity, propertyValue);
      return null;
    }
  }

  private String reverse(String source)
  {
    int i, len = source.length();
    StringBuffer dest = new StringBuffer(len);

    for (i = (len - 1); i >= 0; i--)
      dest.append(source.charAt(i));

    return dest.toString();
  }

  private String getDefaultNamespace()
  {
    return ""; // TODO
  }

  private String getDefaultLanguage()
  {
    return ""; // TODO
  }

  private String getNamespaceForPrefix(String prefix)
  {
    return null; // TODO
  }

  private boolean hasDefaultNamespace()
  {
    return false; // TODO
  }
}

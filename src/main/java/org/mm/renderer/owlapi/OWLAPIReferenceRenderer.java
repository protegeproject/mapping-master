package org.mm.renderer.owlapi;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.renderer.BaseReferenceRenderer;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.Rendering;
import org.mm.rendering.owlapi.OWLAPILiteralRendering;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class OWLAPIReferenceRenderer extends BaseReferenceRenderer
  implements ReferenceRenderer, MappingMasterParserConstants
{
  private final OWLOntology ontology;
  private final OWLDataFactory owlDataFactory;
  private final OWLAPIObjectHandler owlObjectHandler;
  private final OWLAPIEntityRenderer entityRenderer;
  private final OWLAPILiteralRenderer literalRenderer;

  public OWLAPIReferenceRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource,
    OWLAPIEntityRenderer entityRenderer, OWLAPILiteralRenderer literalRenderer)
  {
    super(dataSource);
    this.ontology = ontology;
    this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
    this.owlObjectHandler = new OWLAPIObjectHandler(ontology);
    this.entityRenderer = entityRenderer;
    this.literalRenderer = literalRenderer;
  }

  @Override public Optional<OWLAPIReferenceRendering> renderReference(ReferenceNode referenceNode)
    throws RendererException
  {
    SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();

    if (sourceSpecificationNode.hasLiteral()) {
      String literalValue = sourceSpecificationNode.getLiteral();
      OWLLiteral literal = this.literalRenderer.createOWLLiteral(literalValue);
      return Optional.of(new OWLAPIReferenceRendering(literal));
    } else {
      SpreadsheetLocation location = resolveLocation(sourceSpecificationNode);
      String defaultNamespace = getReferenceNamespace(referenceNode);
      String language = getReferenceLanguage(referenceNode);

      // logLine("<<<<<<<<<<<<<<<<<<<< Rendering reference [" + referenceNode + "] <<<<<<<<<<<<<<<<<<<<");

      String referenceLocationValue = getReferenceValueFromLocation(location, referenceNode);

      if (referenceLocationValue.equals("")
        && referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION)
        return Optional.empty();

      ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

      if (referenceType.isUntyped())
        throw new RendererException("untyped reference " + referenceNode);

      if (referenceType.isOWLLiteral()) { // Reference is an OWL literal
        String literalReferenceValue = processLiteralReferenceValue(location, referenceLocationValue, referenceNode);

        if (literalReferenceValue.length() == 0
          && referenceNode.getActualEmptyDataValueDirective() == MM_SKIP_IF_EMPTY_DATA_VALUE)
          return Optional.empty();

        OWLLiteral literal = literalRenderer.createOWLLiteral(literalReferenceValue, referenceType);

        // logLine(
        //  ">>>>>>>>>>>>>>>>>>>> Reference [" + referenceNode.toString() + "] rendered as " + referenceNode
        //    .getReferenceTypeNode() + " " + referenceRendering.toString() + " >>>>>>>>>>>>>>>>>>>>");

        return Optional.of(new OWLAPIReferenceRendering(literal));
      } else if (referenceType.isOWLEntity()) { // Reference is an OWL entity
        String rdfID = getReferenceRDFIDValue(referenceLocationValue, referenceNode);
        String rdfsLabelText = getReferenceRDFSLabelText(referenceLocationValue, referenceNode);

        OWLEntity owlEntity = this.owlObjectHandler
          .createOrResolveOWLEntity(location, referenceLocationValue, referenceType, rdfID, rdfsLabelText,
            defaultNamespace, language, referenceNode.getReferenceDirectives());
        Set<OWLAxiom> axioms = addDefiningTypesFromReference(owlEntity, referenceNode);

        // logLine(
        //  ">>>>>>>>>>>>>>>>>>>> Reference [" + referenceNode.toString() + "] rendered as " + referenceNode
        //    .getReferenceTypeNode() + " " + referenceRendering.toString() + " >>>>>>>>>>>>>>>>>>>>");

        return Optional.of(new OWLAPIReferenceRendering(owlEntity, axioms));

      } else
        throw new RendererException(
          "internal error: unknown reference type " + referenceType + " for reference " + referenceNode.toString());
    }
  }

  @Override public Optional<? extends Rendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
  {
    return this.literalRenderer.renderOWLLiteral(literalNode);
  }

  public Set<OWLAxiom> processTypesClause(OWLNamedIndividual declaredIndividual, List<TypeNode> typeNodes)
    throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    for (TypeNode typeNode : typeNodes) {
      Optional<OWLEntity> typeRendering = renderType(typeNode);

      if (!typeRendering.isPresent()) {
        //logLine(
        //  "processReference: skipping OWL type declaration clause [" + typeNode + "] for individual "
        //    + individualDeclarationRendering + " because of missing type");
        continue;
      }

      OWLEntity entity = typeRendering.get();

      if (entity.isOWLClass()) {
        OWLClass cls = entity.asOWLClass();
        OWLClassAssertionAxiom axiom = this.owlDataFactory.getOWLClassAssertionAxiom(cls, declaredIndividual);

        axioms.add(axiom);
      } else
        throw new RendererException(
          "expecting OWL class as type for individual " + declaredIndividual.getIRI() + ", got " + entity.getIRI());
    }
    return axioms;
  }

  private Optional<OWLAPILiteralRendering> renderValueExtractionFunctionArgument(
    ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
  {
    if (valueExtractionFunctionArgumentNode.isOWLLiteralNode())
      return this.literalRenderer.renderOWLLiteral(valueExtractionFunctionArgumentNode.getOWLLiteralNode());
    else if (valueExtractionFunctionArgumentNode.isReferenceNode()) {
      ReferenceNode referenceNode = valueExtractionFunctionArgumentNode.getReferenceNode();
      Optional<OWLAPIReferenceRendering> referenceRendering = renderReference(referenceNode);
      if (referenceRendering.isPresent()) {
        if (referenceRendering.get().isOWLLiteral()) {
          OWLLiteral literal = referenceRendering.get().getOWLLiteral().get();
          return Optional.of(new OWLAPILiteralRendering(literal));
        } else
          throw new RendererException("expecting literal reference for value extraction function argument, got "
            + valueExtractionFunctionArgumentNode);
      } else
        return Optional.empty();
    } else
      throw new RendererException("unknown child for node " + valueExtractionFunctionArgumentNode.getNodeName());
  }

  private String processLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
    ReferenceNode referenceNode) throws RendererException
  {
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
    String locationValue = rawLocationValue.replace("\"", "\\\"");
    String processedLocationValue;

    if (referenceNode.hasLiteralValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
        processedLocationValue = processValueEncoding(locationValue, referenceNode.getLiteralValueEncodingNode(),
          referenceNode);
      else if (referenceNode.hasValueExtractionFunction())
        processedLocationValue = processValueExtractionFunction(locationValue,
          referenceNode.getValueExtractionFunctionNode());
      else
        processedLocationValue = locationValue;
    } else
      processedLocationValue = "";

    if (processedLocationValue.equals("") && !referenceNode.getActualDefaultDataValue().equals(""))
      processedLocationValue = referenceNode.getActualDefaultDataValue();

    if (processedLocationValue.equals("")
      && referenceNode.getActualEmptyDataValueDirective() == MM_ERROR_IF_EMPTY_DATA_VALUE)
      throw new RendererException("empty data value in reference " + referenceNode + " at location " + location);

    if (processedLocationValue.equals("")
      && referenceNode.getActualEmptyDataValueDirective() == MM_WARNING_IF_EMPTY_DATA_VALUE)
      //logLine(
      //  "processReference: WARNING: empty data value in reference " + referenceNode + " at location " + location);

      if (referenceType.isQuotedOWLDataValue())
        processedLocationValue = "\"" + processedLocationValue + "\"";

    return processedLocationValue;
  }

  private String getReferenceNamespace(ReferenceNode referenceNode) throws RendererException
  {
    // A reference will not have both a prefix and a namespace specified
    if (referenceNode.hasExplicitlySpecifiedPrefix()) {
      String prefix = referenceNode.getPrefixNode().getPrefix();
      String namespace = this.owlObjectHandler.getNamespaceForPrefix(prefix);
      if (namespace == null)
        throw new RendererException("unknown prefix " + prefix + " specified in reference " + referenceNode);
      return namespace;
    } else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      return referenceNode.getNamespaceNode().getNamespace();
    } else {
      if (!hasDefaultNamespace())
        throw new RendererException(
          "ontology has no default namespace and no namespace specified by reference " + referenceNode);

      return getDefaultNamespace();
    }
  }

  private Set<OWLAxiom> addDefiningTypesFromReference(OWLEntity entity, ReferenceNode referenceNode)
    throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

    if (referenceNode.hasExplicitlySpecifiedTypes()) {
      for (TypeNode typeNode : referenceNode.getTypesNode().getTypeNodes()) {
        Optional<OWLEntity> definingType = renderType(typeNode);
        if (!definingType.isPresent()) {
          if (referenceType.isOWLClass()) {
            if (!entity.isOWLClass())
              throw new RendererException(
                "expecting class for type in reference " + referenceNode + " for " + entity + ", got " + entity
                  .getClass().getCanonicalName());

            OWLClass cls = definingType.get().asOWLClass();
            OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(cls, entity.asOWLClass());
            axioms.add(axiom);
          } else if (referenceType.isOWLNamedIndividual()) {
            if (!entity.isOWLNamedIndividual())
              throw new RendererException(
                "expecting individual for type in reference " + referenceNode + " for " + entity + ", got " + entity
                  .getClass().getCanonicalName());

            OWLClass cls = definingType.get().asOWLClass();
            OWLClassAssertionAxiom axiom = this.owlDataFactory
              .getOWLClassAssertionAxiom(cls, entity.asOWLNamedIndividual());
            axioms.add(axiom);
          } else if (referenceType.isOWLObjectProperty()) {
            if (!entity.isOWLObjectProperty())
              throw new RendererException(
                "expecting object property for type in reference " + referenceNode + " for " + entity);

            OWLObjectProperty property = definingType.get().asOWLObjectProperty();
            OWLSubObjectPropertyOfAxiom axiom = owlDataFactory
              .getOWLSubObjectPropertyOfAxiom(property, entity.asOWLObjectProperty());
            axioms.add(axiom);
          } else if (referenceType.isOWLDataProperty()) {
            if (!entity.isOWLDataProperty())
              throw new RendererException(
                "expecting data property for type in reference " + referenceNode + " for " + entity);

            OWLDataProperty property = definingType.get().asOWLDataProperty();

            OWLSubDataPropertyOfAxiom axiom = owlDataFactory
              .getOWLSubDataPropertyOfAxiom(property, entity.asOWLDataProperty());
            axioms.add(axiom);
          } else
            throw new RendererException("invalid entity type " + referenceType);
        }
      }
    }
    return axioms;
  }

  private Optional<OWLEntity> renderType(TypeNode typeNode) throws RendererException
  {
    if (typeNode.isOWLClassNode()) {
      Optional<OWLClassRendering> classRendering = entityRenderer.renderOWLClass((OWLClassNode)typeNode);
      if (classRendering.isPresent()) {
        return Optional.of(classRendering.get().getOWLClass());
      } else
        return Optional.empty();
    } else if (typeNode.isOWLPropertyNode()) {
      Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty((OWLPropertyNode)typeNode);
      if (propertyRendering.isPresent()) {
        return Optional.of(propertyRendering.get().getOWLProperty());
      } else
        return Optional.empty();
    } else if (typeNode.isReferenceNode()) {
      Optional<OWLAPIReferenceRendering> referenceRendering = renderReference((ReferenceNode)typeNode);
      if (referenceRendering.isPresent()) {
        if (referenceRendering.get().isOWLEntity()) {
          OWLEntity entity = referenceRendering.get().getOWLEntity().get();
          return Optional.of(entity);
        } else
          throw new RendererException("expecting OWL entity for node " + typeNode.getNodeName());
      } else
        return Optional.empty();
    } else
      throw new RendererException("internal error: unknown type " + typeNode + " for node " + typeNode.getNodeName());
  }
}

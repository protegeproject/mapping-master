package org.mm.renderer.owlapi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.ValueEncodingDirectiveNode;
import org.mm.parser.node.ValueExtractionFunctionArgumentNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.ReferenceUtil;
import org.mm.renderer.RendererException;
import org.mm.rendering.OWLLiteralRendering;
import org.mm.rendering.ReferenceRendering;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
import org.mm.rendering.owlapi.OWLClassExpressionRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

public class OWLAPIReferenceRenderer implements ReferenceRenderer, MappingMasterParserConstants
{
  private SpreadSheetDataSource dataSource;
  private OWLAPIObjectHandler handler;
  private OWLAPIEntityRenderer entityRenderer;
  private OWLAPILiteralRenderer literalRenderer;
  private OWLAPIClassExpressionRenderer classExpressionRenderer;

  private String defaultNamespace = "";
  private String defaultLanguage = "";

  public OWLAPIReferenceRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource)
  {
    setDataSource(dataSource);
    handler = new OWLAPIObjectHandler(ontology);
    literalRenderer = new OWLAPILiteralRenderer(ontology);
    entityRenderer = new OWLAPIEntityRenderer(ontology, this);
    classExpressionRenderer = new OWLAPIClassExpressionRenderer(ontology, entityRenderer);
  }

  public void setDataSource(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override
  public Optional<OWLAPIReferenceRendering> renderReference(ReferenceNode referenceNode)
    throws RendererException
  {
    SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

    if (sourceSpecificationNode.hasLiteral()) {
      String literalValue = sourceSpecificationNode.getLiteral();
      OWLLiteral literal = literalRenderer.createOWLLiteral(literalValue);
      return Optional.of(new OWLAPIReferenceRendering(literal, referenceType));
    } else {
      SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
      String resolvedReferenceValue = ReferenceUtil.resolveReferenceValue(dataSource, referenceNode);

      if (referenceType.isUntyped())
        throw new RendererException("untyped reference " + referenceNode);

      if (resolvedReferenceValue.isEmpty()
        && referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION)
        return Optional.empty();

      if (referenceType.isOWLLiteral()) { // Reference is an OWL literal
        String literalReferenceValue = processOWLLiteralReferenceValue(location, resolvedReferenceValue, referenceNode);

        if (literalReferenceValue.isEmpty()
          && referenceNode.getActualEmptyLiteralDirective() == MM_SKIP_IF_EMPTY_LITERAL)
          return Optional.empty();

        OWLLiteral literal = this.literalRenderer.createOWLLiteral(literalReferenceValue, referenceType);

        return Optional.of(new OWLAPIReferenceRendering(literal, referenceType));
      } else if (referenceType.isOWLEntity()) { // Reference is an OWL entity
        String rdfID = getReferenceRDFID(resolvedReferenceValue, referenceNode);
        String rdfsLabel = getReferenceRDFSLabel(resolvedReferenceValue, referenceNode);
        String defaultNamespace = getReferenceNamespace(referenceNode);
        String language = getReferenceLanguage(referenceNode);
        
        OWLEntity owlEntity = this.handler
          .createOrResolveOWLEntity(location, resolvedReferenceValue, referenceType, rdfID, rdfsLabel, defaultNamespace,
            language, referenceNode.getReferenceDirectives());
        if (owlEntity == null) {
           return Optional.empty();
        }
        Set<OWLAxiom> axioms = addDefiningTypesFromReference(owlEntity, referenceNode);
        return Optional.of(new OWLAPIReferenceRendering(owlEntity, axioms, referenceType));
        
      } else
        throw new InternalRendererException(
          "unknown reference type " + referenceType + " for reference " + referenceNode);
    }
  }

  public SpreadsheetLocation resolveLocation(SourceSpecificationNode sourceSpecificationNode)
      throws RendererException
  {
    return this.dataSource.resolveLocation(sourceSpecificationNode);
  }

  public Optional<? extends OWLLiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
  {
    return this.literalRenderer.renderOWLLiteral(literalNode);
  }

  public Set<OWLAxiom> processTypesClause(OWLNamedIndividual declaredIndividual, List<TypeNode> typeNodes)
    throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();

    for (TypeNode typeNode : typeNodes) {
      Optional<OWLObject> typeRendering = renderType(typeNode);

      if (!typeRendering.isPresent()) {
        //logLine(
        //  "processReference: skipping OWL type declaration clause [" + typeNode + "] for individual "
        //    + individualDeclarationRendering + " because of missing type");
        continue;
      }

      OWLObject obj = typeRendering.get();
      if (obj instanceof OWLEntity) {
        OWLEntity entity = (OWLEntity) obj;
        if (entity.isOWLClass()) {
          OWLClass cls = entity.asOWLClass();
          OWLClassAssertionAxiom axiom = handler.getOWLClassAssertionAxiom(cls, declaredIndividual);
          axioms.add(axiom);
        } else {
          throw new RendererException(
              "expecting OWL class as type for individual " + declaredIndividual.getIRI() + ", got " + entity.getIRI());
        }
      } else if (obj instanceof OWLClassExpression) {
        OWLClassExpression clsExp = (OWLClassExpression) obj;
        OWLClassAssertionAxiom axiom = handler.getOWLClassAssertionAxiom(clsExp, declaredIndividual);
        axioms.add(axiom);
      } else {
        throw new RendererException("unknown type node " + typeNode.getNodeName() + " for individual " + declaredIndividual);
      }
    }
    return axioms;
  }

  private String getReferenceNamespace(ReferenceNode referenceNode) throws RendererException
  {
    // A reference will not have both a prefix and a namespace specified
    if (referenceNode.hasExplicitlySpecifiedPrefix()) {
      String prefix = referenceNode.getPrefixDirectiveNode().getPrefix();
      String namespace = this.handler.getNamespaceForPrefix(prefix);
      if (namespace == null)
        throw new RendererException("unknown prefix " + prefix + " specified in reference " + referenceNode);
      return namespace;
    } else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
      return referenceNode.getNamespaceDirectiveNode().getNamespace();
    }
    // TODO Recheck this later because default namespace is always empty and the API doesn't allow to updating it.
//    else {
//      if (!hasDefaultNamespace())
//        throw new RendererException(
//          "ontology has no default namespace and no namespace specified by reference " + referenceNode);

      return getDefaultNamespace();
//    }
  }

  private Set<OWLAxiom> addDefiningTypesFromReference(OWLEntity entity, ReferenceNode referenceNode)
    throws RendererException
  {
    Set<OWLAxiom> axioms = new HashSet<>();
    ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
    
    if (referenceNode.hasExplicitlySpecifiedTypes()) {
      for (TypeNode typeNode : referenceNode.getTypesNode().getTypeNodes()) {
        Optional<OWLObject> definingType = renderType(typeNode);
        if (definingType.isPresent()) {
          if (definingType.get() instanceof OWLEntity) {
            OWLEntity definingTypeEntity = (OWLEntity) definingType.get();
            if (referenceType.isOWLClass()) {
              // TODO Make as a separate checking method
              if (!entity.isOWLClass()) {
                throw new RendererException(
                    "expecting class for type in reference " + referenceNode + " for " + entity + ", got " + entity
                        .getClass().getCanonicalName());
              }
              OWLClass cls = definingTypeEntity.asOWLClass();
              OWLSubClassOfAxiom axiom = handler.getOWLSubClassOfAxiom(cls, entity.asOWLClass());
              axioms.add(axiom);
            } else if (referenceType.isOWLNamedIndividual()) {
              if (!entity.isOWLNamedIndividual()) {
                throw new RendererException(
                    "expecting individual for type in reference " + referenceNode + " for " + entity + ", got " + entity
                      .getClass().getCanonicalName());
              }
              OWLClass cls = definingTypeEntity.asOWLClass();
              OWLClassAssertionAxiom axiom = handler.getOWLClassAssertionAxiom(cls, entity.asOWLNamedIndividual());
              axioms.add(axiom);
            } else if (referenceType.isOWLObjectProperty()) {
              if (!entity.isOWLObjectProperty()) {
                throw new RendererException(
                    "expecting object property for type in reference " + referenceNode + " for " + entity);
              }
              OWLObjectProperty property = definingTypeEntity.asOWLObjectProperty();
              OWLSubObjectPropertyOfAxiom axiom = handler.getOWLSubObjectPropertyOfAxiom(property, entity.asOWLObjectProperty());
              axioms.add(axiom);
            } else if (referenceType.isOWLDataProperty()) {
              if (!entity.isOWLDataProperty()) {
                throw new RendererException(
                    "expecting data property for type in reference " + referenceNode + " for " + entity);
              }
              OWLDataProperty property = definingTypeEntity.asOWLDataProperty();
              OWLSubDataPropertyOfAxiom axiom = handler.getOWLSubDataPropertyOfAxiom(property, entity.asOWLDataProperty());
              axioms.add(axiom);
            } else
              throw new InternalRendererException("unknown entity type " + referenceType);
          } else {
            throw new RendererException("unsupported type node " + definingType + " for reference " + referenceNode);
          }
        }
      }
    }
    return axioms;
  }

  private Optional<OWLObject> renderType(TypeNode typeNode) throws RendererException
  {
    if (typeNode.isOWLClassNode()) {
      Optional<OWLClassRendering> classRendering = this.entityRenderer.renderOWLClass((OWLClassNode)typeNode);
      if (classRendering.isPresent()) {
        return Optional.of(classRendering.get().getOWLClass());
      }
    } else if (typeNode.isOWLClassExpressionNode()) {
        Optional<OWLClassExpressionRendering> classExpresssionRendering =
            this.classExpressionRenderer.renderOWLClassExpression((OWLClassExpressionNode)typeNode);
        if (classExpresssionRendering.isPresent()) {
          return Optional.of(classExpresssionRendering.get().getOWLClassExpression());
        }
    } else if (typeNode.isOWLPropertyNode()) {
      Optional<? extends OWLPropertyRendering> propertyRendering =
          this.entityRenderer.renderOWLProperty((OWLPropertyNode)typeNode);
      if (propertyRendering.isPresent()) {
        return Optional.of(propertyRendering.get().getOWLProperty());
      }
    } else if (typeNode.isReferenceNode()) {
      Optional<OWLAPIReferenceRendering> referenceRendering = renderReference((ReferenceNode)typeNode);
      if (referenceRendering.isPresent()) {
        if (referenceRendering.get().isOWLEntity()) {
          OWLEntity entity = referenceRendering.get().getOWLEntity().get();
          return Optional.of(entity);
        } else {
          throw new RendererException("expecting OWL entity for node " + typeNode.getNodeName());
        }
      }
    } else {
      throw new InternalRendererException("unknown type " + typeNode + " for node " + typeNode.getNodeName());
    }
    return Optional.empty();
  }

  private String processOWLLiteralReferenceValue(SpreadsheetLocation location, String rawLocationValue,
      ReferenceNode referenceNode) throws RendererException
  {
    String sourceValue = rawLocationValue.replace("\"", "\\\"");
    String processedReferenceValue;

    if (referenceNode.hasLiteralValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
        processedReferenceValue = generateReferenceValue(sourceValue, referenceNode.getLiteralValueEncodingNode(),
            referenceNode);
      else if (referenceNode.hasValueExtractionFunctionNode()) {
        ValueExtractionFunctionNode valueExtractionFunctionNode = referenceNode.getValueExtractionFunctionNode();
        processedReferenceValue = generateReferenceValue(sourceValue, valueExtractionFunctionNode);
      } else
        processedReferenceValue = sourceValue;
    } else
      processedReferenceValue = "";

    if (processedReferenceValue.isEmpty() && !referenceNode.getActualDefaultLiteral().isEmpty())
      processedReferenceValue = referenceNode.getActualDefaultLiteral();

    if (processedReferenceValue.isEmpty()
        && referenceNode.getActualEmptyLiteralDirective() == MM_ERROR_IF_EMPTY_LITERAL)
      throw new RendererException("empty literal in reference " + referenceNode + " at location " + location);

    return processedReferenceValue;
  }

  private String getReferenceRDFID(String sourceValue, ReferenceNode referenceNode) throws RendererException
  {
    String rdfIDValue;

    if (referenceNode.hasRDFIDValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding())
        rdfIDValue = generateReferenceValue(sourceValue, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
      else if (referenceNode.hasValueExtractionFunctionNode())
        rdfIDValue = generateReferenceValue(sourceValue, referenceNode.getValueExtractionFunctionNode());
      else
        rdfIDValue = sourceValue;
    } else
      rdfIDValue = "";

    if (rdfIDValue.isEmpty() && !referenceNode.getActualDefaultRDFID().isEmpty())
      rdfIDValue = referenceNode.getActualDefaultRDFID();

    if (rdfIDValue.isEmpty() && referenceNode.getActualEmptyRDFIDDirective() == MM_ERROR_IF_EMPTY_ID)
      throw new RendererException("empty RDF ID in reference " + referenceNode);

    return rdfIDValue;
  }

  private String getReferenceRDFSLabel(String sourceValue, ReferenceNode referenceNode) throws RendererException
  {
    String rdfsLabelText;

    if (referenceNode.hasRDFSLabelValueEncoding()) {
      if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding())
        rdfsLabelText = generateReferenceValue(sourceValue, referenceNode.getRDFSLabelValueEncodingNode(),
            referenceNode);
      else if (referenceNode.hasValueExtractionFunctionNode())
        rdfsLabelText = generateReferenceValue(sourceValue, referenceNode.getValueExtractionFunctionNode());
      else
        rdfsLabelText = sourceValue;
    } else
      rdfsLabelText = "";

    if (rdfsLabelText.isEmpty() && !referenceNode.getActualDefaultRDFSLabel().isEmpty())
      rdfsLabelText = referenceNode.getActualDefaultRDFSLabel();

    if (rdfsLabelText.isEmpty() && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_LABEL)
      throw new RendererException("empty RDFS label in reference " + referenceNode);

    return rdfsLabelText;
  }

  private String generateReferenceValue(String sourceValue, ValueEncodingDirectiveNode valueEncodingDirectiveNode,
      ReferenceNode referenceNode) throws RendererException
  {
    if (valueEncodingDirectiveNode != null) {
      if (valueEncodingDirectiveNode.hasValueSpecificationNode())
        return generateReferenceValue(sourceValue, valueEncodingDirectiveNode.getValueSpecificationNode(),
            referenceNode);
      else
        return sourceValue;
    } else
      return sourceValue;
  }

  private String generateReferenceValue(String sourceValue, ValueSpecificationNode valueSpecificationNode,
      ReferenceNode referenceNode) throws RendererException
  {
    String processedReferenceValue = "";

    for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
        .getValueSpecificationItemNodes()) {
      if (valueSpecificationItemNode.hasStringLiteral())
        processedReferenceValue += valueSpecificationItemNode.getStringLiteral();
      else if (valueSpecificationItemNode.hasReferenceNode()) {
        ReferenceNode valueSpecificationItemReferenceNode = valueSpecificationItemNode.getReferenceNode();
        valueSpecificationItemReferenceNode.setDefaultShiftSetting(referenceNode.getActualShiftDirective());
        Optional<? extends ReferenceRendering> referenceRendering = renderReference(
            valueSpecificationItemReferenceNode);
        if (referenceRendering.isPresent()) {
          if (referenceRendering.get().isOWLLiteral()) {
            processedReferenceValue += referenceRendering.get().getRawValue();
          } else
            throw new RendererException(
                "expecting OWL literal for value specification, got " + referenceRendering.get());
        }
      } else if (valueSpecificationItemNode.hasValueExtractionFunctionNode()) {
        ValueExtractionFunctionNode valueExtractionFunction = valueSpecificationItemNode
            .getValueExtractionFunctionNode();
        processedReferenceValue += generateReferenceValue(sourceValue, valueExtractionFunction);
      } else if (valueSpecificationItemNode.hasCapturingExpression() && sourceValue != null) {
        String capturingExpression = valueSpecificationItemNode.getCapturingExpression();
        processedReferenceValue += ReferenceUtil.capture(sourceValue, capturingExpression);
      }
    }
    return processedReferenceValue;
  }

  // Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

  private String generateReferenceValue(String sourceValue, ValueExtractionFunctionNode valueExtractionFunctionNode)
      throws RendererException
  {
    List<String> arguments = new ArrayList<>();
    if (valueExtractionFunctionNode.hasArguments()) {
      for (ValueExtractionFunctionArgumentNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
        String argumentValue = generateValueExtractionFunctionArgument(argumentNode);
        arguments.add(argumentValue);
      }
    }
    return ReferenceUtil.evaluateReferenceValue(
        valueExtractionFunctionNode.getFunctionName(),
        valueExtractionFunctionNode.getFunctionID(),
        arguments,
        sourceValue,
        valueExtractionFunctionNode.hasArguments());
  }

  /**
   * Arguments to value extraction functions cannot be dropped if the reference resolves to nothing.
   */
  private String generateValueExtractionFunctionArgument(
      ValueExtractionFunctionArgumentNode valueExtractionFunctionArgumentNode) throws RendererException
  {
    if (valueExtractionFunctionArgumentNode.isOWLLiteralNode()) {
      Optional<? extends OWLLiteralRendering> literalRendering = renderOWLLiteral(
          valueExtractionFunctionArgumentNode.getOWLLiteralNode());
      if (literalRendering.isPresent()) {
        return literalRendering.get().getRawValue();
      } else
        throw new RendererException("empty literal for value extraction function argument");
    } else if (valueExtractionFunctionArgumentNode.isReferenceNode()) {
      ReferenceNode referenceNode = valueExtractionFunctionArgumentNode.getReferenceNode();
      Optional<? extends ReferenceRendering> referenceRendering = renderReference(referenceNode);
      if (referenceRendering.isPresent()) {
        if (referenceRendering.get().isOWLLiteral()) {
          return referenceRendering.get().getRawValue();
        } else
          throw new RendererException("expecting literal reference for value extraction function argument, got "
              + valueExtractionFunctionArgumentNode);
      } else
        throw new RendererException("empty reference " + referenceNode + " for value extraction function argument");
    } else
      throw new InternalRendererException(
          "unknown child for node " + valueExtractionFunctionArgumentNode.getNodeName());
  }

  private String getReferenceLanguage(ReferenceNode referenceNode) throws RendererException
  {
    if (referenceNode.hasExplicitlySpecifiedLanguage())
      return referenceNode.getActualLanguage();
    else
      return getDefaultLanguage(); // Which might be null or empty
  }

  private String getDefaultNamespace()
  {
    return this.defaultNamespace;
  }

  private String getDefaultLanguage()
  {
    return this.defaultLanguage;
  }

  public boolean hasDefaultNamespace()
  {
    return !this.defaultNamespace.isEmpty();
  }
}

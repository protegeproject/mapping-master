package org.mm.renderer.owlapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.mm.core.ReferenceDirectives;
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
import org.semanticweb.owlapi.model.IRI;
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

  /*
   * Map of namespace to map of rdfs:label to rdf:ID
   */
  private final Map<String, Map<String, OWLEntity>> createdOWLEntitiesUsingLabel = new HashMap<>();

  /*
   * Map of namespace to map of location to rdf:ID
   */
  private final Map<String, Map<SpreadsheetLocation, OWLEntity>> createdOWLEntitiesUsingLocation = new HashMap<>();

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
    ReferenceType type = referenceNode.getReferenceTypeNode().getReferenceType();
    if (type.isUntyped()) {
      throw new RendererException("untyped reference " + referenceNode);
    }
    
    SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
    if (sourceSpecificationNode.hasLiteral()) {
      String literalValue = sourceSpecificationNode.getLiteral();
      OWLLiteral literal = literalRenderer.createOWLLiteral(literalValue);
      return Optional.of(new OWLAPIReferenceRendering(literal, type));
    
    } else {
      SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
      String value = ReferenceUtil.resolveReferenceValue(dataSource, referenceNode);
      if (value.isEmpty() && referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION) {
        return Optional.empty();
      }
      
      if (type.isOWLLiteral()) { // Reference is an OWL literal
        String literalValue = processOWLLiteralReferenceValue(location, value, referenceNode);
        if (literalValue.isEmpty() && referenceNode.getActualEmptyLiteralDirective() == MM_SKIP_IF_EMPTY_LITERAL) {
          return Optional.empty();
        }
        OWLLiteral literal = this.literalRenderer.createOWLLiteral(literalValue, type);
        return Optional.of(new OWLAPIReferenceRendering(literal, type));
        
      } else if (type.isOWLEntity()) { // Reference is an OWL entity
        String identifier = getReferenceRDFID(value, referenceNode);
        String label = getReferenceRDFSLabel(value, referenceNode);
        String namespace = getReferenceNamespace(referenceNode);
        String language = getReferenceLanguage(referenceNode);
        ReferenceDirectives directives = referenceNode.getReferenceDirectives();
        
        OWLEntity owlEntity = createOrResolveOWLEntity(namespace, identifier, label, language, type, location, directives);
        if (owlEntity == null) {
          return Optional.empty();
        } else {
           Set<OWLAxiom> axioms = addDefiningTypesFromReference(owlEntity, referenceNode);
           if (!label.isEmpty()) {
              OWLAxiom labelAnnotation = handler.getLabelAnnotationAxiom(owlEntity, label, language);
              axioms.add(labelAnnotation);
           }
           return Optional.of(new OWLAPIReferenceRendering(owlEntity, axioms, type));
        }
      }
    }
    throw new InternalRendererException("unknown reference type " + type + " for reference " + referenceNode);
  }

  public OWLEntity createOrResolveOWLEntity(String namespace, String identifier, String label, String language,
      ReferenceType type, SpreadsheetLocation location, ReferenceDirectives directives) throws RendererException
  {
    OWLEntity entity = null;
    if (directives.usesLocationWithDuplicatesEncoding()) {
      entity = resolveOWLEntityWithDuplicatesEncoding(namespace, type, location);
    } else if (directives.usesLocationEncoding()) {
      entity = resolveOWLEntityWithLocationEncoding(namespace, type, location);
    } else {
      // Uses rdf:ID or rdfs:label encoding
      switch (determinePossibleCases(identifier, label)) {
        /*
         * Has an rdfs:label but doesn't have ID value. Use label to resolve possible existing entity.
         */
        case 0:
          if (directives.actualEmptyRDFSLabelDirectiveIsSkipIfEmpty()) {
            // log "skipping because of empty rdfs:label"
            entity = null;
          } else if (directives.actualEmptyRDFIDDirectiveIsSkipIfEmpty()) {
            // log "skipping because of empty rdf:ID"
            entity = null;
          } else {
            entity = createOrResolveOWLEntityWithEmptyIDAndEmptyLabel(namespace, type, location);
          }
          break;
        /*
         *  Has an rdfs:label but rdf:ID is blank. Use label to resolve possible existing entity.
         */
        case 1:
          if (directives.actualEmptyRDFIDDirectiveIsSkipIfEmpty()) {
            // log "skipping because of empty rdf:ID"
            entity = null;
          } else {
            if (shouldCreateOrResolveOWLEntityWithRDFSLabel(label, language, directives)) {
              entity = createOrResolveOWLEntityWithEmptyIDAndNonEmptyLabel(namespace, label, language, type, location);
            }
          }
          break;
         /*
          * Has an rdf:ID value but rdfs:label is blank
          */
        case 2:
          if (directives.actualEmptyRDFSLabelDirectiveIsSkipIfEmpty()) {
            // log "skipping because of empty rdfs:label"
            entity = null;
          } else {
            if (shouldCreateOrResolveOWLEntityWithRDFID(namespace, identifier, directives)) {
              entity = createOrResolveOWLEntityWithNonEmptyIDAndEmptyLabel(namespace, identifier, type, location);
            }
          }
          break;
        /*
         * Has both rdf:identifier and rdfs:label values. Use rdf:ID to resolve resolve possible existing entity.
         */
        case 3:
          if (shouldCreateOrResolveOWLEntityWithRDFID(namespace, identifier, directives)
              && shouldCreateOrResolveOWLEntityWithRDFSLabel(label, language, directives)) {
          entity = createOrResolveOWLEntityWithNonEmptyIDAndNonEmptyLabel(namespace, identifier, type, location);
          }
          break;
      }
    }
    return entity;
  }

  private int determinePossibleCases(String identifier, String label)
  {
     String b1 = (identifier == null || identifier.isEmpty()) ? "0" : "1";
     String b2 = (label == null || label.isEmpty()) ? "0" : "1";
     return Integer.parseInt(b1 + b2, 2);
  }

  private OWLEntity resolveOWLEntityWithDuplicatesEncoding(String namespace, ReferenceType type, SpreadsheetLocation location)
      throws RendererException
  {
    // Create entity with an auto-generated rdf:ID
    // log "creating " + referenceType + " at this location using location with duplicates encoding"
    String identifier = "";
    OWLEntity entity = createOWLEntity(namespace, identifier, type, location);
    return entity;
  }

  private OWLEntity resolveOWLEntityWithLocationEncoding(String namespace, ReferenceType type, SpreadsheetLocation location)
		  throws RendererException
  {
    OWLEntity entity = null;
    if (hasOWLEntityBeenCreatedAtLocation(location, namespace)) {
      // log "using existing " + referenceType + " " + resolvedOWLEntity
      // log " created at this location using location encoding"
      entity = getOWLEntityAtLocation(type, location, namespace);
    } else {
      // log "--processReference: creating " + referenceType + " at this location using location encoding"
      String identifier = "";
      entity = createOWLEntity(namespace, identifier, type, location);
      recordOWLEntityAtLocation(type, location, namespace, entity);
    }
    return entity;
  }

  private OWLEntity createOrResolveOWLEntityWithEmptyIDAndEmptyLabel(String namespace, ReferenceType type, SpreadsheetLocation location)
      throws RendererException
  {
    return resolveOWLEntityWithLocationEncoding(namespace, type, location);
  }

  private OWLEntity createOrResolveOWLEntityWithNonEmptyIDAndEmptyLabel(String namespace, String identifier, ReferenceType type,
      SpreadsheetLocation location) throws RendererException
  {
    OWLEntity entity = null;
    if (hasOWLEntityBeenCreatedAtLocation(location, namespace)) {
      // log using existing " + referenceType + " " + resolvedOWLEntity + " created at this location"
      entity = getOWLEntityAtLocation(type, location, namespace);
    } else {
      // log "--processReference: creating/resolving " + referenceType + " using rdf:ID " + rdfID
      entity = createOWLEntity(namespace, identifier, type, location); // If entity exists, it will be retrieved
      recordOWLEntityAtLocation(type, location, namespace, entity);
    }
    return entity;
  }

  private OWLEntity createOrResolveOWLEntityWithEmptyIDAndNonEmptyLabel(String namespace, String label, String language,
      ReferenceType type, SpreadsheetLocation location) throws RendererException
  {
    OWLEntity entity = null;
    if (hasOWLEntityBeenCreatedWithLabel(namespace, label, language)) {
      // log "using existing " + referenceType + " " + resolvedOWLEntity + " with rdfs:label " + labelText
      entity = getOWLEntityWithRDFSLabel(type, namespace, label, language); // Find the existing one
    } else if (hasOWLEntityBeenCreatedInOntology(label, language)) {
      // log "using existing ontology " + referenceType + " " + resolvedOWLEntity+ " with rdfs:label " + labelText
      entity = getOWLEntityWithRDFSLabel(label, language);
    } else {
      String identifier = label;
      entity = createOWLEntity(namespace, identifier, type, location);
      recordOWLEntityWithRDFSLabel(type, namespace, label, language, entity);
    }
    return entity;
  }

  private OWLEntity createOrResolveOWLEntityWithNonEmptyIDAndNonEmptyLabel(String namespace, String identifier, ReferenceType type,
      SpreadsheetLocation location) throws RendererException
  {
    OWLEntity entity = null;
    if (hasOWLEntityBeenCreatedAtLocation(location, namespace)) {
      // log"--using existing " + referenceType + " " + resolvedOWLEntity + " created at this location"
      entity = getOWLEntityAtLocation(type, location, namespace);
    } else {
      // log "--processReference: creating/resolving " + referenceType + " using rdf:ID " + rdfID
      entity = createOWLEntity(namespace, identifier, type, location);
      recordOWLEntityAtLocation(type, location, namespace, entity);
    }
    return entity;
  }

  private boolean shouldCreateOrResolveOWLEntityWithRDFID(String namespace, String identifier, ReferenceDirectives directives)
      throws RendererException
  {
    if (handler.isOWLEntity(IRI.create(identifier))) {
      if (directives.actualIfExistsDirectiveIsError()) {
        throwOWLEntityExistsWithRDFIDException(namespace, identifier);
      } else if (directives.actualIfExistsDirectiveIsWarning()) {
        warnOWLEntityExistsWithRDFID(namespace, identifier);
      } else if (directives.actualIfOWLEntityExistsDirectiveIsSkip()) {
        return false;
      }
      // If setting is MM_RESOLVE_IF_EXISTS we resolve it.
    } else { // No existing entity
      if (directives.actualIfOWLEntityDoesNotExistDirectiveIsError()) {
        throwNoExistingOWLEntityWithRDFIDException(namespace, identifier);
      } else if (directives.actualIfOWLEntityDoesNotExistDirectiveIsWarning()) {
        warnNoExistingOWLEntityWithRDFID(namespace, identifier);
      } else if (directives.actualIfOWLEntityDoesNotExistDirectiveIsSkip()) {
        return false;
      }
      // If setting is MM_CREATE_IF_NOT_EXISTS we create it.
    }
    return true;
  }

  private boolean shouldCreateOrResolveOWLEntityWithRDFSLabel(String label, String language, ReferenceDirectives directives)
      throws RendererException
  {
    // XXX: Currently we are ignoring the language tag
    if (handler.isOWLEntity(IRI.create(label))) {
      if (directives.actualIfExistsDirectiveIsError()) {
        throwOWLEntityExistsWithLabelException(label, language);
      } else if (directives.actualIfExistsDirectiveIsWarning()) {
        warnOWLEntityExistsWithRDFSLabel(label, language);
      } else if (directives.actualIfOWLEntityExistsDirectiveIsSkip()) {
        // log "skipping because OWL entity with this label already exists"
        return false;
      }
      // If setting is MM_RESOLVE_IF_EXISTS we resolve it.
    } else { // No existing entity
      if (directives.actualIfOWLEntityDoesNotExistDirectiveIsError()) {
        throwNoExistingOWLEntityWithRDFSLabelException(label, language);
      } else if (directives.actualIfOWLEntityDoesNotExistDirectiveIsWarning()) {
        warnNoExistingOWLEntityWithRDFSLabel(label, language);
      } else if (directives.actualIfOWLEntityDoesNotExistDirectiveIsSkip()) {
        // log "skipping because OWL entity with this label does not exist");
        return false;
      }
      // If setting is MM_CREATE_IF_NOT_EXISTS we create it.
    }
    return true;
  }

  private boolean hasOWLEntityBeenCreatedAtLocation(SpreadsheetLocation location, String namespace)
      throws RendererException
  {
    if (createdOWLEntitiesUsingLocation.containsKey(namespace)) {
      return createdOWLEntitiesUsingLocation.get(namespace).containsKey(location);
    }
    return false;
  }

  private OWLEntity getOWLEntityAtLocation(ReferenceType type, SpreadsheetLocation location, String namespace)
      throws RendererException
  {
    OWLEntity entity = createdOWLEntitiesUsingLocation.get(namespace).get(location);
    if (entity != null) {
      return entity;
    }
    throw new InternalRendererException(type + " with namespace " + namespace + " was not created at location " + location);
  }

  private void recordOWLEntityAtLocation(ReferenceType type, SpreadsheetLocation location, String namespace, OWLEntity entity)
      throws RendererException
  {
    if (createdOWLEntitiesUsingLocation.containsKey(namespace)) {
      if (!createdOWLEntitiesUsingLocation.get(namespace).containsKey(location)) {
        createdOWLEntitiesUsingLocation.get(namespace).put(location, entity);
      } else {
        checkOWLReferenceType(type, entity);
      }
    } else {
      createdOWLEntitiesUsingLocation.put(namespace, new HashMap<>());
      createdOWLEntitiesUsingLocation.get(namespace).put(location, entity);
    }
  }

  private boolean hasOWLEntityBeenCreatedWithLabel(String namespace, String label, String language)
  {
    // Duplicate labels are allowed if they are in different namespaces.
    String key = (language == null || language.isEmpty()) ? label : label + "@" + language;
    if (createdOWLEntitiesUsingLabel.containsKey(namespace)) {
      return createdOWLEntitiesUsingLabel.get(namespace).containsKey(key);
    } else
      return false;
  }

  private boolean hasOWLEntityBeenCreatedInOntology(String label, String language)
  {
    // XXX: Currently we are ignoring the language tag
    return handler.isOWLEntity(IRI.create(label));
  }

  private OWLEntity getOWLEntityWithRDFSLabel(ReferenceType type, String namespace, String label, String language)
      throws RendererException
  {
    String key = (language == null) ? label : label + "@" + language;
    OWLEntity entity = createdOWLEntitiesUsingLabel.get(namespace).get(key);
    if (entity != null) {
      return entity;
    }
    throw new InternalRendererException(type + " with namespace " + namespace + " was not created with rdfs:label " + key);
  }

  private void recordOWLEntityWithRDFSLabel(ReferenceType type, String namespace, String label, String language, OWLEntity entity)
      throws RendererException
  {
    String key = language == null ? label : label + "@" + language;

    if (createdOWLEntitiesUsingLabel.containsKey(namespace)) {
      if (!createdOWLEntitiesUsingLabel.get(namespace).containsKey(key)) {
        createdOWLEntitiesUsingLabel.get(namespace).put(key, entity);
      } else {
        checkOWLReferenceType(type, entity);
      }
    } else {
      createdOWLEntitiesUsingLabel.put(namespace, new HashMap<>());
      createdOWLEntitiesUsingLabel.get(namespace).put(key, entity);
    }
  }

  private OWLEntity createOWLEntity(String namespace, String identifier, ReferenceType type, SpreadsheetLocation location)
      throws RendererException
  {
    if (type.isOWLClass()) {
      return handler.getOWLClass(namespace, identifier);
    } else if (type.isOWLNamedIndividual()) {
      return handler.getOWLNamedIndividual(namespace, identifier);
    } else if (type.isOWLObjectProperty()) {
      return handler.getOWLObjectProperty(namespace, identifier);
    } else if (type.isOWLDataProperty()) {
      return handler.getOWLDataProperty(namespace, identifier);
    } else if (type.isOWLAnnotationProperty()) {
      return handler.getOWLAnnotationProperty(namespace, identifier);
    }
    throw new RendererException("unknown entity type '" + type + "' for entity '" + namespace + identifier + "' at location " + location);
  }

  private OWLEntity getOWLEntityWithRDFSLabel(String labelText, String language) throws RendererException
  {
    // TODO Confirm with Martin
    if (language != null && "*".equals(language)) {
      return handler.getOWLEntityWithRDFSLabel(labelText).iterator().next(); // Match on any language or none
    } else if (language != null && "+".equals(language)) {
      return handler.getOWLEntityWithRDFSLabelAndAtLeastOneLanguage(labelText).iterator().next(); // Match on at least one language
    } else {
      return handler.getOWLEntityWithRDFSLabelAndLanguage(labelText, language).iterator().next(); // Match on specific language
    }
  }

  private void checkOWLReferenceType(ReferenceType type, OWLEntity owlEntity) throws RendererException
  {
    if (type.isOWLClass() && !handler.isOWLClass(owlEntity.getIRI())
        || type.isOWLNamedIndividual() && !handler.isOWLNamedIndividual(owlEntity.getIRI())
        || type.isOWLObjectProperty() && !handler.isOWLObjectProperty(owlEntity.getIRI())
        || type.isOWLDataProperty() && !handler.isOWLDataProperty(owlEntity.getIRI())) {
      throw new RendererException("mismatch entity type found in the ontology: " + owlEntity);
    }
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

  private void throwOWLEntityExistsWithLabelException(String label, String language) throws RendererException
  {
    String errorMessage = "an OWL entity already exists with the rdfs:label " + label;
    if (language != null && !language.isEmpty()) {
      errorMessage += " and language " + language;
    }
    throw new RendererException(errorMessage);
  }

  private void warnOWLEntityExistsWithRDFSLabel(String label, String language)
  {
    String errorMessage = "WARNING: an OWL entity already exists with the rdfs:label " + label;
    if (language != null && !language.isEmpty()) {
      errorMessage += " and language " + language;
    }
    // log errorMessage
  }

  private void throwNoExistingOWLEntityWithRDFSLabelException(String label, String language) throws RendererException
  {
    String errorMessage = "an OWL entity does not exists with the rdfs:label " + label;
    if (language != null && !language.isEmpty()) {
      errorMessage += " and language " + language;
    }
    throw new RendererException(errorMessage);
  }

  private void warnNoExistingOWLEntityWithRDFSLabel(String label, String language)
  {
    String errorMessage = "WARNING: an OWL entity does not exists with the rdfs:label " + label;
    if (language != null && !language.isEmpty()) {
      errorMessage += " and language " + language;
    }
    // log errorMessage
  }

  private void throwOWLEntityExistsWithRDFIDException(String namespace, String identifier) throws RendererException
  {
    throw new RendererException("an OWL entity already exists in namespace " + namespace + " with the rdf:ID " + identifier);
  }

  private void warnOWLEntityExistsWithRDFID(String namespace, String identifier)
  {
    // log "WARNING: an entity already exists in namespace " + namespace + " with the rdf:ID " + rdfID
  }

  private void throwNoExistingOWLEntityWithRDFIDException(String namespace, String identifier) throws RendererException
  {
    throw new RendererException("an entity does not exist in namespace '" + namespace + "' with the rdf:ID " + identifier);
  }

  private void warnNoExistingOWLEntityWithRDFID(String namespace, String identifier)
  {
    // log "WARNING: an entity does not exists in namespace " + namespace + " with the rdf:ID " + rdfID
  }
}

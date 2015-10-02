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
import org.mm.renderer.NameUtil;
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

public class OWLAPIReferenceRenderer implements ReferenceRenderer, MappingMasterParserConstants
{
   private SpreadSheetDataSource dataSource;
   private OWLAPIObjectHandler handler;
   private OWLAPIEntityRenderer entityRenderer;
   private OWLAPILiteralRenderer literalRenderer;
   private OWLAPIClassExpressionRenderer classExpressionRenderer;

   private String defaultPrefix;

   // The outer map uses a grouping key before getting to the inner map.
   private final Map<String, Map<String, OWLEntity>> entityLabel2OWLEntityMap = new HashMap<>();
   private final Map<String, Map<String, OWLEntity>> entityName2OWLEntityMap = new HashMap<>();
   private final Map<String, Map<SpreadsheetLocation, OWLEntity>> entityLocation2OWLEntityMap = new HashMap<>();

   public OWLAPIReferenceRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource)
   {
      this.dataSource = dataSource;
      handler = new OWLAPIObjectHandler(ontology);
      literalRenderer = new OWLAPILiteralRenderer(ontology);
      entityRenderer = new OWLAPIEntityRenderer(ontology, this);
      classExpressionRenderer = new OWLAPIClassExpressionRenderer(ontology, entityRenderer);
      defaultPrefix = handler.getDefaultPrefix();
   }

   public String getDefaultPrefix()
   {
      return defaultPrefix;
   }

   @Override
   public Optional<OWLAPIReferenceRendering> renderReference(ReferenceNode referenceNode) throws RendererException
   {
      ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
      if (referenceType.isUntyped()) {
         throw new RendererException("untyped reference " + referenceNode);
      }

      SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
      if (sourceSpecificationNode.hasLiteral()) {
         String literalValue = sourceSpecificationNode.getLiteral();
         OWLLiteral literal = literalRenderer.createOWLLiteral(literalValue);
         return Optional.of(new OWLAPIReferenceRendering(literal, referenceType));

      } else {
         SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
         String resolvedValue = ReferenceUtil.resolveReferenceValue(dataSource, referenceNode);
         
         // Decide what will happen if the resolved value is empty
         if (resolvedValue.isEmpty()) {
            switch (referenceNode.getActualEmptyLocationDirective()) {
               case MM_SKIP_IF_EMPTY_LOCATION:
                  return Optional.empty();
               case MM_WARNING_IF_EMPTY_LOCATION:
                  // TODO Warn in log files
                  return Optional.empty();
               case MM_ERROR_IF_EMPTY_LOCATION:
                  throw new RendererException("Empty cell values for " + referenceNode + " at location " + location);
            }
         }
         // Decide what will happen if the resolved value is not empty based on the entity types
         if (referenceType.isOWLLiteral()) { // Reference is an OWL literal
            String literalValue = processOWLLiteralReferenceValue(location, resolvedValue, referenceNode);
            if (literalValue.isEmpty()) {
               switch (referenceNode.getActualEmptyLiteralDirective()) {
                  case MM_SKIP_IF_EMPTY_LITERAL:
                     return Optional.empty();
                  case MM_WARNING_IF_EMPTY_LITERAL:
                     // TODO Warn in log file
                     return Optional.empty();
                  case MM_ERROR_IF_EMPTY_LITERAL:
                     throw new RendererException("Empty literal for " + referenceNode + " at location " + location);
               }
            }
            OWLLiteral literal = literalRenderer.createOWLLiteral(literalValue, referenceType);
            return Optional.of(new OWLAPIReferenceRendering(literal, referenceType));

         } else if (referenceType.isOWLEntity()) { // Reference is an OWL entity
            String entityName = getReferenceRDFID(resolvedValue, referenceNode);
            if (entityName.isEmpty()) {
               switch (referenceNode.getActualEmptyRDFIDDirective()) {
                  case MM_SKIP_IF_EMPTY_ID:
                     return Optional.empty();
                  case MM_WARNING_IF_EMPTY_ID:
                     // TODO Warn in log file
                     return Optional.empty();
                  case MM_ERROR_IF_EMPTY_ID:
                     throw new RendererException("Empty rdf:ID for " + referenceNode + " at location " + location);
               }
            }
            String label = getReferenceRDFSLabel(resolvedValue, referenceNode);
            if (label.isEmpty()) {
               switch (referenceNode.getActualEmptyRDFSLabelDirective()) {
                  case MM_SKIP_IF_EMPTY_LABEL:
                     return Optional.empty();
                  case MM_WARNING_IF_EMPTY_LABEL:
                     // TODO Warn in log file
                     return Optional.empty();
                  case MM_ERROR_IF_EMPTY_LABEL:
                     throw new RendererException("Empty rdfs:label for " + referenceNode + " at location " + location);
               }
            }
            String language = getReferenceLanguage(referenceNode);
            
            Optional<OWLEntity> createdEntity = resolveOWLEntity(entityName, label, language, referenceType, referenceNode);
            if (createdEntity.isPresent()) {
               OWLEntity entity = createdEntity.get();
               Set<OWLAxiom> axioms = new HashSet<>();
               addOWLAxiom(entity, referenceType, referenceNode, axioms);
               addOWLAnnotationAxiom(entity, label, language, axioms);
               return Optional.of(new OWLAPIReferenceRendering(entity, axioms, referenceType));
            }
            return Optional.empty();
         }
      }
      throw new InternalRendererException("Unknown reference type " + referenceType + " for " + referenceNode);
   }

   public Optional<OWLEntity> resolveOWLEntity(String entityName, String label, String language, ReferenceType referenceType,
         ReferenceNode referenceNode) throws RendererException
   {
      ReferenceDirectives directives = referenceNode.getReferenceDirectives();
      if (directives.usesLocationEncoding()) {
         SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
         return createOWLEntityUsingLocationEncoding(location, referenceType, referenceNode);
      } else if (entityName.isEmpty() && label.isEmpty()) {
         SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
         return createOWLEntityUsingLocationEncoding(location, referenceType, referenceNode);
      } else if (entityName.isEmpty() && !label.isEmpty()) {
         int creationSettings = directives.getActualIfOWLEntityDoesNotExistDirective();
         return createOWLEntityUsingEntityLabel(label, language, referenceType, referenceNode, creationSettings);
      } else if (!entityName.isEmpty() && label.isEmpty()) {
         int creationSettings = directives.getActualIfOWLEntityDoesNotExistDirective();
         return createOWLEntityUsingEntityName(entityName, referenceType, referenceNode, creationSettings);
      } else if (!entityName.isEmpty() && !label.isEmpty()) {
         int creationSettings = directives.getActualIfOWLEntityDoesNotExistDirective();
         return createOWLEntityUsingEntityName(entityName, referenceType, referenceNode, creationSettings);
      }
      return Optional.empty();
   }

   private Optional<OWLEntity> createOWLEntityUsingLocationEncoding(SpreadsheetLocation location, ReferenceType referenceType,
         ReferenceNode referenceNode) throws RendererException
   {
      final String prefix = getUserDefinedPrefix(referenceNode);
      
      OWLEntity entity = findOWLEntityByLocation(location, prefix);
      if (entity == null) {
         String localName = ReferenceUtil.produceIdentifierString(location);
         entity = createOWLEntity(prefix, localName, referenceType);
         cacheOWLEntityByLocation(location, entity, prefix);
      }
      return Optional.of(entity);
   }

   private Optional<OWLEntity> createOWLEntityUsingEntityName(String entityName, ReferenceType referenceType,
         ReferenceNode referenceNode, int creationSettings) throws RendererException
   {
      OWLEntity entity = getOWLEntityFromOntology(entityName, referenceType);
      if (entity == null) {
         switch (creationSettings) {
            case MM_SKIP_IF_OWL_ENTITY_DOES_NOT_EXIST:
               break;
            case MM_WARNING_IF_OWL_ENTITY_DOES_NOT_EXIST:
               // TODO Log warning message
               break;
            case MM_ERROR_IF_OWL_ENTITY_DOES_NOT_EXIST:
               throw new RendererException(
                     "Entity with name '" + entityName + "' cannot be found in the ontology. Please provide it first.");
            case MM_CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST:
               if (NameUtil.isValidIriString(entityName)) {
                  entity = createOWLEntity(IRI.create(entityName), referenceType);
               } else {
                  String localName = entityName; // let's assume the given entity name is the entity's local name
                  entity = findOWLEntityByName(localName, getUserDefinedPrefix(referenceNode));
                  if (entity == null) {
                     String prefix = getUserDefinedPrefix(referenceNode);
                     entity = createOWLEntity(prefix, localName, referenceType);
                     cacheOWLEntityByName(localName, entity, prefix);
                  }
                  break;
               }
         }
      }
      return Optional.ofNullable(entity);
   }

   private Optional<OWLEntity> createOWLEntityUsingEntityLabel(String label, String language, ReferenceType referenceType,
         ReferenceNode referenceNode, int creationSettings) throws RendererException
   {
      OWLEntity entity = getOWLEntityFromOntology(label, language);
      if (entity == null) {
         switch (creationSettings) {
            case MM_SKIP_IF_OWL_ENTITY_DOES_NOT_EXIST:
               break;
            case MM_WARNING_IF_OWL_ENTITY_DOES_NOT_EXIST:
               // TODO Log warning message
               break;
            case MM_ERROR_IF_OWL_ENTITY_DOES_NOT_EXIST:
               throw new RendererException(
                     "Entity with label '" + label + "' cannot be found in the ontology. Please provide it first.");
            case MM_CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST:
               entity = findOWLEntityByLabel(label, language, getUserDefinedPrefix(referenceNode));
               if (entity == null) {
                  String prefix = getUserDefinedPrefix(referenceNode);
                  String localName = ReferenceUtil.produceIdentifierString(label);
                  entity = createOWLEntity(prefix, localName, referenceType);
                  cacheOWLEntityByLabel(label, language, entity, prefix);
               }
               break;
         }
      }
      return Optional.ofNullable(entity);
   }

   private OWLEntity createOWLEntity(IRI iri, ReferenceType referenceType) throws RendererException
   {
      if (referenceType.isOWLClass()) {
         return handler.getOWLClass(iri);
      } else if (referenceType.isOWLNamedIndividual()) {
         return handler.getOWLNamedIndividual(iri);
      } else if (referenceType.isOWLObjectProperty()) {
         return handler.getOWLObjectProperty(iri);
      } else if (referenceType.isOWLDataProperty()) {
         return handler.getOWLDataProperty(iri);
      } else if (referenceType.isOWLAnnotationProperty()) {
         return handler.getOWLAnnotationProperty(iri);
      }
      throw new RendererException("Unsupported entity type '" + referenceType + "' for name '" + iri + "'");
   }

   private OWLEntity createOWLEntity(String prefix, String localName, ReferenceType referenceType)
         throws RendererException
   {
      if (referenceType.isOWLClass()) {
         return handler.getOWLClass(prefix, localName);
      } else if (referenceType.isOWLNamedIndividual()) {
         return handler.getOWLNamedIndividual(prefix, localName);
      } else if (referenceType.isOWLObjectProperty()) {
         return handler.getOWLObjectProperty(prefix, localName);
      } else if (referenceType.isOWLDataProperty()) {
         return handler.getOWLDataProperty(prefix, localName);
      } else if (referenceType.isOWLAnnotationProperty()) {
         return handler.getOWLAnnotationProperty(prefix, localName);
      }
      throw new RendererException("Unsupported entity type '" + referenceType + "' for name '" + prefix + localName + "'");
   }

   private OWLEntity findOWLEntityByLocation(SpreadsheetLocation location, String group)
   {
      try {
         return entityLocation2OWLEntityMap.get(group).get(location);
      } catch (NullPointerException e) {
         return null;
      }
   }

   private OWLEntity findOWLEntityByName(String entityName, String group)
   {
      try {
         return entityName2OWLEntityMap.get(group).get(entityName);
      } catch (NullPointerException e) {
         return null;
      }
   }

   private OWLEntity findOWLEntityByLabel(String label, String language, String group)
   {
      try {
         label = (language == null || language.isEmpty()) ? label : label + "@" + language;
         return entityLabel2OWLEntityMap.get(group).get(label);
      } catch (NullPointerException e) {
         return null;
      }
   }

   private void cacheOWLEntityByLocation(SpreadsheetLocation location, OWLEntity entity, String group)
   {
       entityLocation2OWLEntityMap.put(group, new HashMap<>());
       entityLocation2OWLEntityMap.get(group).put(location, entity);
   }

   private void cacheOWLEntityByName(String entityName, OWLEntity entity, String group)
   {
       entityName2OWLEntityMap.put(group, new HashMap<>());
       entityName2OWLEntityMap.get(group).put(entityName, entity);
   }

   private void cacheOWLEntityByLabel(String label, String language, OWLEntity entity, String group)
   {
      label = (language == null || language.isEmpty()) ? label : label + "@" + language;
      entityLabel2OWLEntityMap.put(group, new HashMap<>());
      entityLabel2OWLEntityMap.get(group).put(label, entity);
   }

   private OWLEntity getOWLEntityFromOntology(String entityName, ReferenceType referenceType)
   {
      return handler.getOWLEntities(entityName, referenceType.getType());
   }

   private OWLEntity getOWLEntityFromOntology(String labelText, String language) throws RendererException
   {
      if (language != null) {
         if ("*".equals(language)) { // Match on any language or none
            return handler.getOWLEntityWithRDFSLabel(labelText);
         } else if ("+".equals(language)) { // Match on at least one language
            // TODO Need better implementation for this case
            return handler.getOWLEntityWithRDFSLabelAndAtLeastOneLanguage(labelText);
         } else { // Match on a specific language
            return handler.getOWLEntityWithRDFSLabelAndLanguage(labelText, language);
         }
      } else { // Match on any language or none
         return handler.getOWLEntityWithRDFSLabel(labelText);
      }
   }

   public Optional<? extends OWLLiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
   {
      return literalRenderer.renderOWLLiteral(literalNode);
   }

   public Set<OWLAxiom> processTypesClause(OWLNamedIndividual individual, List<TypeNode> typeNodes) throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      for (TypeNode typeNode : typeNodes) {
         Optional<OWLObject> typeRendering = renderType(typeNode);
         if (!typeRendering.isPresent()) {
            continue;
         }
         OWLObject obj = typeRendering.get();
         if (obj instanceof OWLEntity) {
            OWLEntity entity = (OWLEntity) obj;
            if (entity.isOWLClass()) {
               OWLClassAssertionAxiom axiom = handler.getOWLClassAssertionAxiom(entity.asOWLClass(), individual);
               axioms.add(axiom);
            } else {
               IRI identifier = individual.getIRI();
               throw new RendererException("Expecting OWL class for individual " + identifier + ", got " + entity.getClass());
            }
         } else if (obj instanceof OWLClassExpression) {
            OWLClassExpression clsExp = (OWLClassExpression) obj;
            OWLClassAssertionAxiom axiom = handler.getOWLClassAssertionAxiom(clsExp, individual);
            axioms.add(axiom);
         } else {
            IRI identifier = individual.getIRI();
            throw new RendererException("Unsupported type '" + typeNode.getNodeName() + "' for individual " + identifier);
         }
      }
      return axioms;
   }

   private String getUserDefinedPrefix(ReferenceNode referenceNode) throws RendererException
   {
      // A reference will not have both a prefix and a prefix specified
      if (referenceNode.hasExplicitlySpecifiedPrefix()) {
         String prefixLabel = referenceNode.getPrefixDirectiveNode().getPrefix();
         return handler.getPrefixForPrefixLabel(prefixLabel);
      } else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
         return referenceNode.getNamespaceDirectiveNode().getNamespace();
      }
      return getDefaultPrefix(); // If there is no user defined prefix then use the default ontology prefix
   }

   private void addOWLAxiom(OWLEntity entity, ReferenceType referenceType, ReferenceNode referenceNode, Set<OWLAxiom> axioms)
         throws RendererException
   {
      if (!referenceNode.hasExplicitlySpecifiedTypes()) {
         return;
      }
      for (TypeNode typeNode : referenceNode.getTypesNode().getTypeNodes()) {
         Optional<OWLObject> typeRendering = renderType(typeNode);
         if (typeRendering.isPresent()) {
            OWLObject typeObject = typeRendering.get();
            if (typeObject instanceof OWLEntity) {
               OWLEntity typeEntity = (OWLEntity) typeObject;
               if (referenceType.isOWLClass() && typeEntity instanceof OWLClass) {
                  OWLAxiom axiom = handler.getOWLSubClassOfAxiom(typeEntity.asOWLClass(), entity.asOWLClass());
                  axioms.add(axiom);
               } else if (referenceType.isOWLNamedIndividual() && typeEntity instanceof OWLClass) {
                  OWLAxiom axiom = handler.getOWLClassAssertionAxiom(typeEntity.asOWLClass(), entity.asOWLNamedIndividual());
                  axioms.add(axiom);
               } else if (referenceType.isOWLObjectProperty() && typeEntity instanceof OWLObjectProperty) {
                  OWLAxiom axiom = handler.getOWLSubObjectPropertyOfAxiom(typeEntity.asOWLObjectProperty(), entity.asOWLObjectProperty());
                  axioms.add(axiom);
               } else if (referenceType.isOWLDataProperty() && typeEntity instanceof OWLDataProperty) {
                  OWLAxiom axiom = handler.getOWLSubDataPropertyOfAxiom(typeEntity.asOWLDataProperty(), entity.asOWLDataProperty());
                  axioms.add(axiom);
               } else throw new InternalRendererException("Unsupported entity type " + referenceType);
            } else {
               throw new RendererException("Unsupported type node '" + typeRendering + "' for " + referenceNode);
            }
         }
      }
   }

   private void addOWLAnnotationAxiom(OWLEntity entity, String label, String language, Set<OWLAxiom> axioms)
   {
      if (!label.isEmpty()) {
         OWLAxiom labelAnnotation = handler.getLabelAnnotationAxiom(entity, label, language);
         axioms.add(labelAnnotation);
      }
   }

   private Optional<OWLObject> renderType(TypeNode typeNode) throws RendererException
   {
      if (typeNode.isOWLClassNode()) {
         OWLClassNode node = (OWLClassNode) typeNode;
         Optional<OWLClassRendering> classRendering = entityRenderer.renderOWLClass(node);
         if (classRendering.isPresent()) {
            return Optional.of(classRendering.get().getOWLClass());
         }
      } else if (typeNode.isOWLClassExpressionNode()) {
         OWLClassExpressionNode node = (OWLClassExpressionNode) typeNode;
         Optional<OWLClassExpressionRendering> classExpressionRendering = classExpressionRenderer.renderOWLClassExpression(node);
         if (classExpressionRendering.isPresent()) {
            return Optional.of(classExpressionRendering.get().getOWLClassExpression());
         }
      } else if (typeNode.isOWLPropertyNode()) {
         OWLPropertyNode node = (OWLPropertyNode) typeNode;
         Optional<? extends OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(node);
         if (propertyRendering.isPresent()) {
            return Optional.of(propertyRendering.get().getOWLProperty());
         }
      } else if (typeNode.isReferenceNode()) {
         ReferenceNode node = (ReferenceNode) typeNode;
         Optional<OWLAPIReferenceRendering> referenceRendering = renderReference(node);
         if (referenceRendering.isPresent()) {
            if (referenceRendering.get().isOWLEntity()) {
               OWLEntity entity = referenceRendering.get().getOWLEntity().get();
               return Optional.of(entity);
            } else {
               throw new RendererException("Expecting OWL entity for node " + typeNode.getNodeName());
            }
         }
      } else {
         throw new InternalRendererException("Unknown type '" + typeNode + "' for node " + typeNode.getNodeName());
      }
      return Optional.empty();
   }

   private String processOWLLiteralReferenceValue(SpreadsheetLocation location, String resolvedValue,
         ReferenceNode referenceNode) throws RendererException
   {
      resolvedValue = resolvedValue.replace("\"", "\\\"");
      String literal = "";
      if (referenceNode.hasLiteralValueEncoding()) {
         if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding()) {
            literal = generateReferenceValue(resolvedValue, referenceNode.getLiteralValueEncodingNode(), referenceNode);
         } else if (referenceNode.hasValueExtractionFunctionNode()) {
            ValueExtractionFunctionNode valueExtractionFunctionNode = referenceNode.getValueExtractionFunctionNode();
            literal = generateReferenceValue(resolvedValue, valueExtractionFunctionNode);
         } else {
            literal = resolvedValue;
         }
      }
      if (literal.isEmpty() && !referenceNode.getActualDefaultLiteral().isEmpty()) {
         literal = referenceNode.getActualDefaultLiteral();
      }
      return literal;
   }

   private String getReferenceRDFID(String value, ReferenceNode referenceNode) throws RendererException
   {
      String id = "";
      if (referenceNode.hasRDFIDValueEncoding()) {
         if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding()) {
            id = generateReferenceValue(value, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
         } else if (referenceNode.hasValueExtractionFunctionNode()) {
            id = generateReferenceValue(value, referenceNode.getValueExtractionFunctionNode());
         } else {
            id = value;
         }
      }
      if (id.isEmpty() && !referenceNode.getActualDefaultRDFID().isEmpty()) {
         id = referenceNode.getActualDefaultRDFID();
      }
      return id;
   }

   private String getReferenceRDFSLabel(String value, ReferenceNode referenceNode) throws RendererException
   {
      String label = "";
      if (referenceNode.hasRDFSLabelValueEncoding()) {
         if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding()) {
            label = generateReferenceValue(value, referenceNode.getRDFSLabelValueEncodingNode(), referenceNode);
         } else if (referenceNode.hasValueExtractionFunctionNode()) {
            label = generateReferenceValue(value, referenceNode.getValueExtractionFunctionNode());
         } else {
            label = value;
         }
      }
      if (label.isEmpty() && !referenceNode.getActualDefaultRDFSLabel().isEmpty()) {
         label = referenceNode.getActualDefaultRDFSLabel();
      }
      return label;
   }

   private String generateReferenceValue(String sourceValue, ValueEncodingDirectiveNode valueEncodingDirectiveNode,
         ReferenceNode referenceNode) throws RendererException
   {
      if (valueEncodingDirectiveNode != null) {
         if (valueEncodingDirectiveNode.hasValueSpecificationNode()) {
            return generateReferenceValue(sourceValue, valueEncodingDirectiveNode.getValueSpecificationNode(), referenceNode);
         } else {
            return sourceValue;
         }
      } else return sourceValue;
   }

   private String generateReferenceValue(String sourceValue, ValueSpecificationNode valueSpecificationNode,
         ReferenceNode referenceNode) throws RendererException
   {
      String processedReferenceValue = "";

      for (ValueSpecificationItemNode specificationItemNode : valueSpecificationNode.getValueSpecificationItemNodes()) {
         if (specificationItemNode.hasStringLiteral()) {
            processedReferenceValue += specificationItemNode.getStringLiteral();
         } else if (specificationItemNode.hasReferenceNode()) {
            ReferenceNode valueSpecificationItemReferenceNode = specificationItemNode.getReferenceNode();
            valueSpecificationItemReferenceNode.setDefaultShiftSetting(referenceNode.getActualShiftDirective());
            Optional<? extends ReferenceRendering> referenceRendering = renderReference(valueSpecificationItemReferenceNode);
            if (referenceRendering.isPresent()) {
               if (referenceRendering.get().isOWLLiteral()) {
                  processedReferenceValue += referenceRendering.get().getRawValue();
               } else {
                  throw new RendererException("expecting OWL literal for value specification, got " + referenceRendering.get());
               }
            }
         } else if (specificationItemNode.hasValueExtractionFunctionNode()) {
            ValueExtractionFunctionNode valueExtractionFunction = specificationItemNode.getValueExtractionFunctionNode();
            processedReferenceValue += generateReferenceValue(sourceValue, valueExtractionFunction);
         } else if (specificationItemNode.hasCapturingExpression() && sourceValue != null) {
            String capturingExpression = specificationItemNode.getCapturingExpression();
            processedReferenceValue += ReferenceUtil.capture(sourceValue, capturingExpression);
         }
      }
      return processedReferenceValue;
   }

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
      return ReferenceUtil.evaluateReferenceValue(valueExtractionFunctionNode.getFunctionName(),
            valueExtractionFunctionNode.getFunctionID(), arguments, sourceValue,
            valueExtractionFunctionNode.hasArguments());
   }

   /**
    * Arguments to value extraction functions cannot be dropped if the reference
    * resolves to nothing.
    */
   private String generateValueExtractionFunctionArgument(ValueExtractionFunctionArgumentNode argumentNode)
         throws RendererException
   {
      if (argumentNode.isOWLLiteralNode()) {
         OWLLiteralNode literalNode = argumentNode.getOWLLiteralNode();
         Optional<? extends OWLLiteralRendering> literalRendering = renderOWLLiteral(literalNode);
         if (literalRendering.isPresent()) {
            return literalRendering.get().getRawValue();
         } else {
            throw new RendererException("Empty literal for value extraction function argument");
         }
      } else if (argumentNode.isReferenceNode()) {
         ReferenceNode referenceNode = argumentNode.getReferenceNode();
         Optional<? extends ReferenceRendering> referenceRendering = renderReference(referenceNode);
         if (referenceRendering.isPresent()) {
            if (referenceRendering.get().isOWLLiteral()) {
               return referenceRendering.get().getRawValue();
            } else {
               throw new RendererException("Expecting literal for value extraction argument, got " + argumentNode);
            }
         } else
            throw new RendererException("Empty reference for value extraction function argument");
      } else {
         throw new InternalRendererException("Unknown child for node " + argumentNode.getNodeName());
      }
   }

   private String getReferenceLanguage(ReferenceNode referenceNode) throws RendererException
   {
      if (referenceNode.hasExplicitlySpecifiedLanguage()) {
         return referenceNode.getActualLanguage();
      }
      return null;
   }
}

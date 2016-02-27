package org.mm.renderer.owlapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import org.mm.core.ReferenceDirectives;
import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLNamedIndividualNode;
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
import org.mm.renderer.NameUtil;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.ReferenceUtil;
import org.mm.renderer.RendererException;
import org.mm.rendering.OWLLiteralRendering;
import org.mm.rendering.ReferenceRendering;
import org.mm.rendering.owlapi.OWLAPIEntityReferenceRendering;
import org.mm.rendering.owlapi.OWLAPILiteralReferenceRendering;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
import org.mm.rendering.owlapi.OWLClassExpressionRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OWLAPIReferenceRenderer implements ReferenceRenderer, MappingMasterParserConstants
{
   private final Logger logger = LoggerFactory.getLogger(OWLAPIReferenceRenderer.class);

   private SpreadSheetDataSource dataSource;
   private OWLAPIObjectFactory objectFactory;
   private OWLAPIEntityRenderer entityRenderer;
   private OWLAPILiteralRenderer literalRenderer;
   private OWLAPIClassExpressionRenderer classExpressionRenderer;

   private LocationEncodingCache locationEncodingCache = new LocationEncodingCache();

   public OWLAPIReferenceRenderer(SpreadSheetDataSource dataSource, OWLAPIObjectFactory objectFactory)
   {
      this.dataSource = dataSource;
      this.objectFactory = objectFactory;
      
      literalRenderer = new OWLAPILiteralRenderer(objectFactory);
      entityRenderer = new OWLAPIEntityRenderer(this, objectFactory);
      classExpressionRenderer = new OWLAPIClassExpressionRenderer(this, objectFactory);
   }

   public OWLAPIEntityRenderer getEntityRenderer()
   {
      return entityRenderer;
   }

   public OWLAPILiteralRenderer getLiteralRenderer()
   {
      return literalRenderer;
   }

   public String getDefaultPrefix()
   {
      return objectFactory.getDefaultPrefix();
   }

   @Override
   public Optional<OWLAPIReferenceRendering> renderReference(ReferenceNode referenceNode) throws RendererException
   {
      ReferenceType referenceType = getReferenceType(referenceNode);
      SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();

      if (sourceSpecificationNode.hasLiteral()) {
         String literalValue = sourceSpecificationNode.getLiteral();
         if (referenceNode.hasLiteralType()) {
            OWLLiteral literal = createOWLLiteral(literalValue, referenceType);
            return Optional.of(new OWLAPILiteralReferenceRendering(literal, referenceType));
         } else if (referenceNode.hasEntityType()) {
            /*
             * If the source specification node is written as a literal value without datatype, e.g., @"XYZ", then
             * MM will assume it is an OWL entity. The assignments below assume that the entity name and its label are
             * equal to the literal value.
             */
            Optional<String> entityName = Optional.of(literalValue);
            Optional<String> entityLabel = Optional.of(literalValue);
            Optional<String> languageTag = getLanguage(referenceNode);
            
            /*
             * Create the OWL entity based on the inputs of its name, label and language tag. The method createOWLEntity
             * will check first if the name (or label) exists in the ontology.
             */
            OWLAPIEntityReferenceRendering entityRendering = null;
            Optional<OWLEntity> createdEntity = createOWLEntity(entityName, entityLabel, languageTag, referenceNode);
            if (createdEntity.isPresent()) {
               OWLEntity entity = createdEntity.get();
               Set<OWLAxiom> axioms = createOWLAxioms(entity, entityLabel, languageTag, referenceType, referenceNode);
               entityRendering = new OWLAPIEntityReferenceRendering(entity, axioms, referenceType);
            }
            return Optional.ofNullable(entityRendering);
         }
      } else if (sourceSpecificationNode.hasLocation()) {
         /*
          * Resolve the value given a location reference to an input spreadsheet
          */
         SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
         Optional<String> resolvedValue = ReferenceUtil.resolveReferenceValue(dataSource, referenceNode);
         resolvedValue = processResolvedValue(resolvedValue, location, referenceNode.getReferenceDirectives());
         
         if (referenceNode.hasLiteralType()) {
            Optional<String> literalValue = getLiteral(resolvedValue, referenceNode);
            literalValue = processLiteral(literalValue, location, referenceNode.getReferenceDirectives());
            /*
             * Create the OWL literal if the literal value is not null
             */
            OWLAPILiteralReferenceRendering literalRendering = null;
            if (literalValue.isPresent()) {
               OWLLiteral literal = createOWLLiteral(literalValue.get(), referenceType);
               literalRendering = new OWLAPILiteralReferenceRendering(literal, referenceType);
            }
            return Optional.ofNullable(literalRendering);
         } else if (referenceNode.hasEntityType()) {
            /*
             * Setup the entity name, label and language tag based on user's input. There are validation process
             * for the entity name and label to decide if an empty value will throw a warning, an error or just
             * simply ignore it.
             */
            Optional<String> entityName = getEntityName(resolvedValue, referenceNode);
            entityName = processIdentifier(entityName, location, referenceNode.getReferenceDirectives());
            Optional<String> entityLabel = getEntityLabel(resolvedValue, referenceNode);
            entityLabel = validateLabel(entityLabel, location, referenceNode.getReferenceDirectives());
            Optional<String> languageTag = getLanguage(referenceNode);
            
            /*
             * Create the OWL entity based on the input of its name, label and language tag. The method createOWLEntity
             * will check first if the name (or label) exists in the ontology.
             */
            OWLAPIEntityReferenceRendering entityRendering = null;
            Optional<OWLEntity> createdEntity = createOWLEntity(entityName, entityLabel, languageTag, referenceNode);
            if (createdEntity.isPresent()) {
               OWLEntity entity = createdEntity.get();
               Set<OWLAxiom> axioms = createOWLAxioms(entity, entityLabel, languageTag, referenceType, referenceNode);
               entityRendering = new OWLAPIEntityReferenceRendering(entity, axioms, referenceType);
            }
            return Optional.ofNullable(entityRendering);
         }
         throw new InternalRendererException("Unknown type (" + referenceType + ") for reference node: " + referenceNode);
      }
      throw new InternalRendererException("Unknown definition for reference node: " + referenceNode);
   }

   private Optional<String> processResolvedValue(Optional<String> resolvedValue, SpreadsheetLocation location, ReferenceDirectives directives)
         throws RendererException
   {
      Optional<String> finalValue = resolvedValue;
      if (!resolvedValue.isPresent()) {
         switch (directives.getActualEmptyLocationDirective()) {
            case MM_PROCESS_IF_EMPTY_LOCATION:
               finalValue = Optional.of("");
               break;
            case MM_SKIP_IF_EMPTY_LOCATION:
               break;
            case MM_WARNING_IF_EMPTY_LOCATION:
               logger.warn("The cell location {} has an empty value", location);
               break;
            case MM_ERROR_IF_EMPTY_LOCATION:
               throw new RendererException("The cell location " + location + " has an empty value");
         }
      }
      return finalValue;
   }

   private Optional<String> processLiteral(Optional<String> literalValue, SpreadsheetLocation location, ReferenceDirectives directives)
         throws RendererException
   {
      Optional<String> finalLiteral = literalValue;
      if (!literalValue.isPresent()) {
         switch (directives.getActualEmptyLiteralDirective()) {
            case MM_PROCESS_IF_EMPTY_LITERAL:
               finalLiteral = Optional.of("");
               break;
            case MM_SKIP_IF_EMPTY_LITERAL:
               break;
            case MM_WARNING_IF_EMPTY_LITERAL:
               logger.warn("The cell location {} has an empty value", location);
               break;
            case MM_ERROR_IF_EMPTY_LITERAL:
               throw new RendererException("The cell location " + location + " has an empty value");
         }
      }
      return finalLiteral;
   }

   private Optional<String> processIdentifier(Optional<String> entityName, SpreadsheetLocation location, ReferenceDirectives directives)
         throws RendererException
   {
      Optional<String> finalName = entityName;
      if (!entityName.isPresent()) {
         switch (directives.getActualEmptyRDFIDDirective()) {
            case MM_PROCESS_IF_EMPTY_ID:
               finalName = Optional.of("");
               break;
            case MM_SKIP_IF_EMPTY_ID:
               break;
            case MM_WARNING_IF_EMPTY_ID:
               logger.warn("The cell location {} has an empty value", location);
               break;
            case MM_ERROR_IF_EMPTY_ID:
               throw new RendererException("The cell location " + location + " has an empty value");
         }
      }
      return finalName;
   }

   private Optional<String> validateLabel(Optional<String> entityLabel, SpreadsheetLocation location, ReferenceDirectives directives)
         throws RendererException
   {
      Optional<String> finalLabel = entityLabel;
      if (!entityLabel.isPresent()) {
         switch (directives.getActualEmptyRDFSLabelDirective()) {
            case MM_PROCESS_IF_EMPTY_LABEL:
               finalLabel = Optional.of("");
            case MM_SKIP_IF_EMPTY_LABEL:
               break;
            case MM_WARNING_IF_EMPTY_LABEL:
               logger.warn("The cell location {} has an empty value", location);
               break;
            case MM_ERROR_IF_EMPTY_LABEL:
               throw new RendererException("The cell location " + location + " has an empty value");
         }
      }
      return finalLabel;
   }

   private ReferenceType getReferenceType(ReferenceNode referenceNode) throws RendererException
   {
      ReferenceType referenceType = referenceNode.getReferenceType();
      if (referenceType.isUntyped()) {
         throw new RendererException("Untyped reference " + referenceNode);
      }
      return referenceType;
   }

   public Optional<OWLEntity> createOWLEntity(Optional<String> entityName, Optional<String> label, Optional<String> language,
         ReferenceNode referenceNode) throws RendererException
   {
      OWLEntity entity = null;
      String prefix = getUserDefinedPrefix(referenceNode);
      ReferenceType referenceType = getReferenceType(referenceNode);
      ReferenceDirectives directives = referenceNode.getReferenceDirectives();
      if (directives.usesLocationEncoding()) {
         SpreadsheetLocation location = ReferenceUtil.resolveLocation(dataSource, referenceNode);
         entity = createOWLEntityUsingLocationEncoding(prefix, location, referenceType);
      } else {
         if (!entityName.isPresent() && label.isPresent()) {
            int creationSetting = directives.getActualIfOWLEntityDoesNotExistDirective();
            entity = createOWLEntityUsingEntityLabel(prefix, label.get(), language, referenceType, creationSetting);
         } else if (entityName.isPresent() && !label.isPresent()) {
            int creationSetting = directives.getActualIfOWLEntityDoesNotExistDirective();
            entity = createOWLEntityUsingEntityName(entityName.get(), referenceType, creationSetting);
         } else if (entityName.isPresent() && label.isPresent()) {
            int creationSetting = directives.getActualIfOWLEntityDoesNotExistDirective();
            entity = createOWLEntityUsingEntityName(entityName.get(), referenceType, creationSetting);
         }
      }
      return Optional.ofNullable(entity);
   }

   private OWLEntity createOWLEntityUsingLocationEncoding(String prefix, SpreadsheetLocation location, ReferenceType referenceType)
            throws RendererException
   {
      Optional<OWLEntity> foundEntity = getOWLEntityFromLocationCache(prefix, location);
      if (!foundEntity.isPresent()) {
         String localName = ReferenceUtil.createNameUsingCellLocation(location);
         OWLEntity newEntity = createOWLEntity(prefix, localName, referenceType);
         putOWLEntityToLocationCache(prefix, location, newEntity);
         return newEntity;
      } else {
         return foundEntity.get();
      }
   }

   private OWLEntity createOWLEntityUsingEntityName(String entityName, ReferenceType referenceType, int creationSetting)
         throws RendererException
   {
      Optional<OWLEntity> foundEntity = getOWLEntityFromOntology(entityName, referenceType);
      if (!foundEntity.isPresent()) {
         OWLEntity newEntity = null;
         switch (creationSetting) {
            case MM_SKIP_IF_OWL_ENTITY_DOES_NOT_EXIST:
               break;
            case MM_WARNING_IF_OWL_ENTITY_DOES_NOT_EXIST:
               logger.warn("An entity with identifier '" + entityName + "' does not exist in the ontology");
               break;
            case MM_ERROR_IF_OWL_ENTITY_DOES_NOT_EXIST:
               throw new RendererException("An entity with identifier '" + entityName + "' does not exist in the ontology.\n"
                     + "Please provide it first with the proper IRI identifier.");
            case MM_CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST:
               newEntity = createOWLEntity(NameUtil.toSnakeCase(entityName), referenceType);
         }
         return newEntity;
      } else {
         return foundEntity.get();
      }
   }

   private OWLEntity createOWLEntityUsingEntityLabel(String labelPrefix, String entityLabel, Optional<String> languageTag,
         ReferenceType referenceType, int creationSetting) throws RendererException
   {
      Optional<OWLEntity> foundEntity = getOWLEntityFromOntology(entityLabel, languageTag);
      if (!foundEntity.isPresent()) {
         OWLEntity newEntity = null;
         switch (creationSetting) {
            case MM_SKIP_IF_OWL_ENTITY_DOES_NOT_EXIST:
               break;
            case MM_WARNING_IF_OWL_ENTITY_DOES_NOT_EXIST:
               logger.warn("An entity with display name '" + entityLabel + "' does not exist in the ontology");
               break;
            case MM_ERROR_IF_OWL_ENTITY_DOES_NOT_EXIST:
               throw new RendererException("An entity with display name '" + entityLabel + "' does not exist in the ontology.\n"
                     + "Please provide it first with the proper rdfs:label annotation.");
            case MM_CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST:
               newEntity = createOWLEntity(labelPrefix, NameUtil.toSnakeCase(entityLabel), referenceType);
         }
         return newEntity;
      } else {
         return foundEntity.get();
      }
   }

   private OWLEntity createOWLEntity(String prefix, String localName, ReferenceType referenceType)
         throws RendererException
   {
      String entityName = prefix + localName;
      if (!NameUtil.isValidUriConstruct(entityName)) {
         throw new RendererException("Failed to construct valid IRI using cell value: " + localName);
      }
      return createOWLEntity(entityName, referenceType);
   }

   private OWLEntity createOWLEntity(String entityName, ReferenceType referenceType) throws RendererException
   {
      return objectFactory.createOWLEntity(entityName, referenceType.getType());
   }

   private Optional<OWLEntity> getOWLEntityFromOntology(String displayName, Optional<String> languageTag) throws RendererException
   {
      return objectFactory.getOWLEntityFromDisplayName(displayName, languageTag);
   }

   private Optional<OWLEntity> getOWLEntityFromOntology(String entityName, ReferenceType referenceType) throws RendererException
   {
      return objectFactory.getOWLEntity(entityName, referenceType.getType());
   }

   private Optional<OWLEntity> getOWLEntityFromLocationCache(String prefix, SpreadsheetLocation location)
   {
      return locationEncodingCache.get(prefix, location);
   }

   private void putOWLEntityToLocationCache(String prefix, SpreadsheetLocation location, OWLEntity newEntity)
   {
      locationEncodingCache.put(prefix, location, newEntity);
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
               OWLClassAssertionAxiom axiom = objectFactory.createOWLClassAssertionAxiom(entity.asOWLClass(), individual);
               axioms.add(axiom);
            } else {
               IRI identifier = individual.getIRI();
               throw new RendererException("Expecting OWL class for individual " + identifier + ", got " + entity.getClass());
            }
         } else if (obj instanceof OWLClassExpression) {
            OWLClassExpression clsExp = (OWLClassExpression) obj;
            OWLClassAssertionAxiom axiom = objectFactory.createOWLClassAssertionAxiom(clsExp, individual);
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
         return objectFactory.getPrefix(prefixLabel);
      } else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
         return referenceNode.getNamespaceDirectiveNode().getNamespace();
      }
      return getDefaultPrefix(); // If there is no user defined prefix then use the default ontology prefix
   }

   private Set<OWLAxiom> createOWLAxioms(OWLEntity entity, Optional<String> entityLabel, Optional<String> languageTag,
         ReferenceType referenceType, ReferenceNode referenceNode) throws RendererException
   {
      Set<OWLAxiom> axioms = new HashSet<>();
      addOWLEntityAxiom(entity, referenceType, referenceNode, axioms);
      addOWLAnnotationAxiom(entity, entityLabel, languageTag, axioms);
      return axioms;
   }

   private void addOWLEntityAxiom(OWLEntity entity, ReferenceType referenceType, ReferenceNode referenceNode, Set<OWLAxiom> axioms)
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
                  OWLAxiom axiom = objectFactory.createOWLSubClassOfAxiom(typeEntity.asOWLClass(), entity.asOWLClass());
                  axioms.add(axiom);
               } else if (referenceType.isOWLNamedIndividual() && typeEntity instanceof OWLClass) {
                  OWLAxiom axiom = objectFactory.createOWLClassAssertionAxiom(typeEntity.asOWLClass(), entity.asOWLNamedIndividual());
                  axioms.add(axiom);
               } else if (referenceType.isOWLObjectProperty() && typeEntity instanceof OWLObjectProperty) {
                  OWLAxiom axiom = objectFactory.createOWLSubObjectPropertyOfAxiom(typeEntity.asOWLObjectProperty(), entity.asOWLObjectProperty());
                  axioms.add(axiom);
               } else if (referenceType.isOWLDataProperty() && typeEntity instanceof OWLDataProperty) {
                  OWLAxiom axiom = objectFactory.createOWLSubDataPropertyOfAxiom(typeEntity.asOWLDataProperty(), entity.asOWLDataProperty());
                  axioms.add(axiom);
               } else throw new InternalRendererException("Unsupported entity type " + referenceType);
            } else {
               throw new RendererException("Unsupported type node '" + typeRendering + "' for " + referenceNode);
            }
         }
      }
   }

   private void addOWLAnnotationAxiom(OWLEntity entity, Optional<String> label, Optional<String> language, Set<OWLAxiom> axioms)
   {
      if (label.isPresent()) {
         OWLAxiom labelAnnotation = objectFactory.createLabelAnnotationAxiom(entity, label.get(), language);
         axioms.add(labelAnnotation);
      }
   }

   private Optional<OWLObject> renderType(TypeNode typeNode) throws RendererException
   {
      if (typeNode instanceof OWLClassNode) {
         OWLClassNode node = (OWLClassNode) typeNode;
         Optional<OWLClassRendering> rendering = entityRenderer.renderOWLClass(node, false);
         if (rendering.isPresent()) {
            return Optional.of(rendering.get().getOWLClass());
         }
      } else if (typeNode instanceof OWLPropertyNode) {
         OWLPropertyNode node = (OWLPropertyNode) typeNode;
         Optional<? extends OWLPropertyRendering> rendering = entityRenderer.renderOWLProperty(node);
         if (rendering.isPresent()) {
            return Optional.of(rendering.get().getOWLProperty());
         }
      } else if (typeNode instanceof OWLNamedIndividualNode) {
         OWLNamedIndividualNode node = (OWLNamedIndividualNode) typeNode;
         Optional<OWLNamedIndividualRendering> rendering = entityRenderer.renderOWLNamedIndividual(node, false);
         if (rendering.isPresent()) {
            return Optional.of(rendering.get().getOWLNamedIndividual());
         }
      } else if (typeNode instanceof OWLPropertyNode) {
         OWLPropertyNode node = (OWLPropertyNode) typeNode;
         Optional<? extends OWLPropertyRendering> rendering = entityRenderer.renderOWLProperty(node);
         if (rendering.isPresent()) {
            return Optional.of(rendering.get().getOWLProperty());
         }
      } else if (typeNode instanceof OWLNamedIndividualNode) {
         OWLNamedIndividualNode node = (OWLNamedIndividualNode) typeNode;
         Optional<OWLNamedIndividualRendering> rendering = entityRenderer.renderOWLNamedIndividual(node, false);
         if (rendering.isPresent()) {
            return Optional.of(rendering.get().getOWLNamedIndividual());
         }
      } else if (typeNode instanceof OWLClassExpressionNode) {
         OWLClassExpressionNode node = (OWLClassExpressionNode) typeNode;
         Optional<OWLClassExpressionRendering> rendering = classExpressionRenderer.renderOWLClassExpression(node);
         if (rendering.isPresent()) {
            return Optional.of(rendering.get().getOWLClassExpression());
         }
      } else {
         throw new InternalRendererException("Unknown type '" + typeNode + "' for node " + typeNode.getNodeName());
      }
      return Optional.empty();
   }

   private Optional<String> getLiteral(Optional<String> value, ReferenceNode referenceNode) throws RendererException
   {
      String finalValue = null;
      if (value.isPresent()) {
         String inputValue = value.get();
         /*
          * Apply any extraction function that would apply to the given value string
          */
         if (referenceNode.hasLiteralValueEncoding()) {
            if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding()) {
               finalValue = generateReferenceValue(inputValue, referenceNode.getLiteralValueEncodingNode(), referenceNode);
            } else if (referenceNode.hasValueExtractionFunctionNode()) {
               finalValue = generateReferenceValue(inputValue, referenceNode.getValueExtractionFunctionNode());
            } else {
               finalValue = inputValue;
            }
         }
      }
      /*
       * Override the current final value if there is a default literal string
       */
      if (referenceNode.hasExplicitlySpecifiedDefaultLiteral()) {
         finalValue = referenceNode.getActualDefaultLiteral();
      }
      return Optional.ofNullable(finalValue);
   }

   private Optional<String> getEntityName(Optional<String> value, ReferenceNode referenceNode) throws RendererException
   {
      String finalName = null;
      if (value.isPresent()) {
         String inputValue = value.get();
         /*
          * Apply any extraction function that would apply to the given value string
          */
         if (referenceNode.hasRDFIDValueEncoding()) {
            if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding()) {
               finalName = generateReferenceValue(inputValue, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
            } else if (referenceNode.hasValueExtractionFunctionNode()) {
               finalName = generateReferenceValue(inputValue, referenceNode.getValueExtractionFunctionNode());
            } else {
               finalName = inputValue;
            }
         }
      }
      /*
       * Override the current final label if there is a default rdf:ID string
       */
      if (referenceNode.hasExplicitlySpecifiedDefaultRDFID()) {
         finalName = referenceNode.getActualDefaultRDFID();
      }
      return Optional.ofNullable(finalName);
   }

   private Optional<String> getEntityLabel(Optional<String> value, ReferenceNode referenceNode) throws RendererException
   {
      String finalLabel = null;
      if (value.isPresent()) {
         String inputValue = value.get();
         /*
          * Apply any extraction function that would apply to the given value string
          */
         if (referenceNode.hasRDFSLabelValueEncoding()) {
            if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding()) {
               finalLabel = generateReferenceValue(inputValue, referenceNode.getRDFSLabelValueEncodingNode(), referenceNode);
            } else if (referenceNode.hasValueExtractionFunctionNode()) {
               finalLabel = generateReferenceValue(inputValue, referenceNode.getValueExtractionFunctionNode());
            } else {
               finalLabel = inputValue;
            }
         }
      }
      /*
       * Override the current final label if there is a default rdfs:label string
       */
      if (referenceNode.hasExplicitlySpecifiedDefaultRDFSLabel()) {
         finalLabel = referenceNode.getActualDefaultRDFSLabel();
      }
      return Optional.ofNullable(finalLabel);
   }

   private String generateReferenceValue(@Nonnull String value, ValueEncodingDirectiveNode valueEncodingDirectiveNode,
         ReferenceNode referenceNode) throws RendererException
   {
      if (valueEncodingDirectiveNode != null) {
         if (valueEncodingDirectiveNode.hasValueSpecificationNode()) {
            return generateReferenceValue(value, valueEncodingDirectiveNode.getValueSpecificationNode(), referenceNode);
         }
      }
      return value;
   }

   private String generateReferenceValue(@Nonnull String value, ValueSpecificationNode valueSpecificationNode,
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
               processedReferenceValue += referenceRendering.get().getRawValue();
            }
         } else if (specificationItemNode.hasValueExtractionFunctionNode()) {
            ValueExtractionFunctionNode valueExtractionFunction = specificationItemNode.getValueExtractionFunctionNode();
            processedReferenceValue += generateReferenceValue(value, valueExtractionFunction);
         } else if (specificationItemNode.hasCapturingExpression() && value != null) {
            String capturingExpression = specificationItemNode.getCapturingExpression();
            processedReferenceValue += ReferenceUtil.capture(value, capturingExpression);
         }
      }
      return processedReferenceValue;
   }

   private String generateReferenceValue(@Nonnull String value, ValueExtractionFunctionNode valueExtractionFunctionNode)
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
            valueExtractionFunctionNode.getFunctionID(), arguments, value,
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

   private Optional<String> getLanguage(ReferenceNode referenceNode) throws RendererException
   {
      String languageTag = null;
      if (referenceNode.hasExplicitlySpecifiedLanguage()) {
         languageTag = referenceNode.getActualLanguage();
      }
      return Optional.ofNullable(languageTag);
   }

   private OWLLiteral createOWLLiteral(String value, ReferenceType referenceType) throws RendererException
   {
      return objectFactory.createOWLLiteral(value, referenceType.getType());
   }

   class LocationEncodingCache
   {
      private final Map<String, Map<SpreadsheetLocation, OWLEntity>> cache = new HashMap<>();

      public Optional<OWLEntity> get(String prefix, SpreadsheetLocation location)
      {
         OWLEntity entity = cache.get(prefix).get(location);
         return Optional.ofNullable(entity);
      }

      public void put(String prefix, SpreadsheetLocation location, OWLEntity entity)
      {
         Map<SpreadsheetLocation, OWLEntity> innerMap = cache.get(prefix);
         if (innerMap == null) {
            innerMap = new HashMap<>();
         }
         innerMap.put(location, entity);
      }
   }
}

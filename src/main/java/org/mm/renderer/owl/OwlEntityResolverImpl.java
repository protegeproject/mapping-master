package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OwlEntityResolverImpl implements OwlEntityResolver {

   private final OWLOntology ontology;
   private final PrefixManager prefixManager;

   public OwlEntityResolverImpl(@Nonnull OWLOntology ontology) {
      this.ontology = checkNotNull(ontology);
      prefixManager = buildPrefixManager();
   }

   private PrefixManager buildPrefixManager() {
      PrefixManager prefixManager = new DefaultPrefixManager();
      OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
      if (format.isPrefixOWLOntologyFormat()) {
         Map<String, String> prefixMap = format.asPrefixOWLOntologyFormat()
               .getPrefixName2PrefixMap();
         for (String prefixName : prefixMap.keySet()) {
            prefixManager.setPrefix(prefixName, prefixMap.get(prefixName));
         }
      }
      setDefaultPrefix(prefixManager);
      return prefixManager;
   }

   private void setDefaultPrefix(PrefixManager prefixManager) {
      com.google.common.base.Optional<IRI> ontologyIri = ontology.getOntologyID().getDefaultDocumentIRI();
      if (ontologyIri.isPresent()) {
         prefixManager.setDefaultPrefix(ontologyIri.get().toString());
      }
   }

   /**
    * Resolves the given entity name and returns the OWL entity object with the specified type.
    * The method will scan the entity name in the active ontology and return the found object.
    * If no entity was found, the method will check against a list of built-in entities before
    * throwing a checked exception if still no entity was found.
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@link OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return Returns an OWL entity object according to its type.
    * @throws EntityNotFoundException If the entity name does not exist in the ontology.
    */
   @Override
   public <T extends OWLEntity> T resolve(String entityName, final Class<T> entityType)
         throws EntityNotFoundException {
      T entity = null;
      IRI entityIri = prefixManager.getIRI(entityName);
      Optional<OWLEntity> foundEntity = ontology.getEntitiesInSignature(entityIri).stream().findFirst();
      if (!foundEntity.isPresent()) {
         entity = createNewForBuiltInEntity(entityName, entityType);
      } else {
         entity = entityType.cast(foundEntity.get());
      }
      if (entity == null) {
         throw new EntityNotFoundException(
               String.format("The expected entity name '%s' does not exist in the ontology",
                     entityName));
      }
      return entity;
   }

   private <T extends OWLEntity> T createNewForBuiltInEntity(String entityName, final Class<T> entityType) {
      IRI entityIri = prefixManager.getIRI(entityName);
      if (OWLRDFVocabulary.BUILT_IN_VOCABULARY_IRIS.contains(entityIri)) {
         return createNew(entityName, entityType);
      } else {
         return null;
      }
   }

   /**
    * Resolves the given entity name and returns the OWL entity object with the specified type.
    * The method will scan the entity name in the active ontology and return the found object.
    * If no entity was found, the method will check against a list of built-in entities before
    * throwing a runtime exception if still no entity was found.
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@link OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return Returns an OWL entity object according to its type.
    * @throws EntityNotFoundException If the entity name does not exist in the ontology.
    */
   @Override
   public <T extends OWLEntity> T resolveUnchecked(String entityName, Class<T> entityType) {
      try {
         return resolve(entityName, entityType);
      } catch (EntityNotFoundException e) {
         throw new RuntimeException(e.getMessage());
      }
   }

   /**
    * Creates an OWL entity following its given name and type. The method will first look
    * the entity name in the loaded ontology and reuse the same object. If no entity was
    * found, this method will create a new object
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@link OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return Returns an OWL entity object following its given name and type.
    * @throws EntityCreationException If the entity creation was failed
    */
   @Override
   public <T extends OWLEntity> T create(String entityName, final Class<T> entityType)
         throws EntityCreationException {
      IRI entityIRI = prefixManager.getIRI(entityName);
      Optional<OWLEntity> foundEntity = ontology.getEntitiesInSignature(entityIRI).stream().findFirst();
      if (!foundEntity.isPresent()) {
         return createNew(entityName, entityType);
      }
      return entityType.cast(foundEntity.get());
   }

   private <T extends OWLEntity> T createNew(String entityName, final Class<T> entityType) {
      final OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
      if (OWLClass.class.isAssignableFrom(entityType)) {
         return entityType.cast(dataFactory.getOWLClass(entityName, prefixManager));
      } else if (OWLObjectProperty.class.isAssignableFrom(entityType)) {
         return entityType.cast(dataFactory.getOWLObjectProperty(entityName, prefixManager));
      } else if (OWLDataProperty.class.isAssignableFrom(entityType)) {
         return entityType.cast(dataFactory.getOWLDataProperty(entityName, prefixManager));
      } else if (OWLNamedIndividual.class.isAssignableFrom(entityType)) {
         return entityType.cast(dataFactory.getOWLNamedIndividual(entityName, prefixManager));
      } else if (OWLAnnotationProperty.class.isAssignableFrom(entityType)) {
         return entityType.cast(dataFactory.getOWLAnnotationProperty(entityName, prefixManager));
      } else if (OWLDatatype.class.isAssignableFrom(entityType)) {
         return entityType.cast(dataFactory.getOWLDatatype(entityName, prefixManager));
      }
      throw new IllegalStateException(String.format("Unknown entity type %s", entityType));
   }

   @Override
   public <T extends OWLEntity> T createUnchecked(String entityName, Class<T> entityType) {
      try {
         return create(entityName, entityType);
      } catch (EntityCreationException e) {
         throw new RuntimeException(e.getMessage());
      }
   }

   @Override
   public <T extends OWLEntity> boolean hasType(String entityName, Class<T> entityType) {
      IRI entityIRI = prefixManager.getIRI(entityName);
      Optional<OWLEntity> foundEntity = ontology.getEntitiesInSignature(entityIRI).stream().findFirst();
      return (foundEntity.isPresent()) ? entityType.isInstance(foundEntity.get()) : false;
   }
}

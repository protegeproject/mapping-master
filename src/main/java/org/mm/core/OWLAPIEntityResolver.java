package org.mm.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.mm.exceptions.EntityCreationException;
import org.mm.exceptions.EntityNotFoundException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class OWLAPIEntityResolver implements OWLEntityResolver {

   private final OWLOntology ontology;
   private final PrefixManager prefixManager;
   private final OWLDataFactory dataFactory;

   public OWLAPIEntityResolver(@Nonnull OWLOntology ontology, @Nonnull PrefixManager prefixManager) {
      this.ontology = checkNotNull(ontology);
      this.prefixManager = checkNotNull(prefixManager);
      dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
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

   @Override
   public <T extends OWLEntity> T createUnchecked(String entityName, Class<T> entityType) {
      try {
         return create(entityName, entityType);
      } catch (EntityCreationException e) {
         throw new RuntimeException(e.getMessage());
      }
   }

   private <T extends OWLEntity> T createNew(String entityName, final Class<T> entityType) {
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
}

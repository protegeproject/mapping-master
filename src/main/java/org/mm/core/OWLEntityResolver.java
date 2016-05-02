package org.mm.core;

import org.mm.exceptions.EntityCreationException;
import org.mm.exceptions.EntityNotFoundException;
import org.semanticweb.owlapi.model.OWLEntity;

public interface OWLEntityResolver
{
   /**
    * Resolves the given entity name string and its type to an {@code OWLEntity} object.
    * The method will check to the current active ontology and return the object
    * found in the ontology.
    *
    * @param entityName
    *          The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@linke OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return The {@code OWLEntity} object with the specific type.
    * @throws EntityNotFoundException If the entity name does not exist in the ontology.
    */
   <T extends OWLEntity> T resolve(String entityName, final Class<T> entityType)
         throws EntityNotFoundException;

   /**
    * Creates the {@code OWLEntity} object given the entity name string and its type.
    * This method will reuse the entity object if the object can be found in the
    * ontology.
    *
    * @param entityName
    *        The entity name in short form or as a prefixed name string.
    * @param entityType
    *          The entity type following the OWLAPI class hierarchy. The types an be
    *          one of these: {@link OWLClass}, {@linke OWLDataProperty},
    *          {@link OWLObjectProperty}. {@link OWLNamedIndividual} or
    *          {@link OWLDatatype}.
    * @return An {@code OWLEntity} object with the specific type.
    * @throws EntityCreationException If the entity creation was failed
    */
   <T extends OWLEntity> T create(String entityName, final Class<T> entityType)
         throws EntityCreationException;
}

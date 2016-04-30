package org.mm.core;

import org.mm.exceptions.EntityCreationException;
import org.mm.exceptions.EntityNotFoundException;
import org.semanticweb.owlapi.model.OWLEntity;

public interface OWLEntityResolver
{
   String getDefaultPrefix();

   <T extends OWLEntity> T resolve(String shortName, final Class<T> entityType)
         throws EntityNotFoundException;

   <T extends OWLEntity> T create(String shortName, final Class<T> entityType)
         throws EntityCreationException;
}

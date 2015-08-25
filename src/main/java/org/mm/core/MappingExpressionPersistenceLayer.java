package org.mm.core;

import org.mm.exceptions.MappingMasterException;

import java.util.Set;

// TODO No implementation for this yet. Previous Protege 3-based implementation used an OWL-based serialization, which
// was overly complex. Perhaps use JSON or a text-based serialization.
// TODO We also do not save any global configuration options made by the user.
public interface MappingExpressionPersistenceLayer
{
  Set<MappingExpression> getMappingExpressions(String fileName) throws MappingMasterException;

  void putMappingExpressions(Set<MappingExpression> mappingExpressions, String fileName) throws MappingMasterException;
}

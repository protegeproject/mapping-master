package org.mm.core;

import org.mm.exceptions.MappingMasterException;

import java.util.Set;

public interface MappingExpressionsPersistenceLayer
{
  Set<MappingExpression> getMappingExpressions(String fileName) throws MappingMasterException;

  void putMappingExpressions(Set<MappingExpression> mappingExpressions, String fileName) throws MappingMasterException;
}

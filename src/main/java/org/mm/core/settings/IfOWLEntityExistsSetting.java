package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum IfOWLEntityExistsSetting
{
   RESOLVE_IF_OWL_ENTITY_EXISTS(MappingMasterParserConstants.MM_RESOLVE_IF_OWL_ENTITY_EXISTS),
   SKIP_IF_OWL_ENTITY_EXISTS(MappingMasterParserConstants.MM_SKIP_IF_OWL_ENTITY_EXISTS),
   WARNING_IF_OWL_ENTITY_EXISTS(MappingMasterParserConstants.MM_WARNING_IF_OWL_ENTITY_EXISTS),
   ERROR_IF_OWL_ENTITY_EXISTS(MappingMasterParserConstants.MM_ERROR_IF_OWL_ENTITY_EXISTS);

   private int value;

   private IfOWLEntityExistsSetting(int value)
   {
      this.value = value;
   }

   public int getConstant()
   {
      return value;
   }
};
package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum IfOWLEntityDoesNotExistSetting
{
   CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST(MappingMasterParserConstants.MM_CREATE_IF_OWL_ENTITY_DOES_NOT_EXIST),
   SKIP_IF_OWL_ENTITY_DOES_NOT_EXIST(MappingMasterParserConstants.MM_SKIP_IF_OWL_ENTITY_DOES_NOT_EXIST),
   WARNING_IF_OWL_ENTITY_DOES_NOT_EXIST(MappingMasterParserConstants.MM_WARNING_IF_OWL_ENTITY_DOES_NOT_EXIST),
   ERROR_IF_OWL_ENTITY_DOES_NOT_EXIST(MappingMasterParserConstants.MM_ERROR_IF_OWL_ENTITY_DOES_NOT_EXIST);

   private int value;

   private IfOWLEntityDoesNotExistSetting(int value)
   {
      this.value = value;
   }

   public int getConstant()
   {
      return value;
   }
};
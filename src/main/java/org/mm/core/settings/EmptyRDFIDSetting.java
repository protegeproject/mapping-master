package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum EmptyRDFIDSetting
{
   ERROR_IF_EMPTY_ID(MappingMasterParserConstants.MM_ERROR_IF_EMPTY_ID),
   WARNING_IF_EMPTY_ID(MappingMasterParserConstants.MM_WARNING_IF_EMPTY_ID),
   SKIP_IF_EMPTY_ID(MappingMasterParserConstants.MM_SKIP_IF_EMPTY_ID),
   PROCESS_IF_EMPTY_ID(MappingMasterParserConstants.MM_PROCESS_IF_EMPTY_ID);

   private int value;

   private EmptyRDFIDSetting(int value)
   {
      this.value = value;
   }

   public int getConstant()
   {
      return value;
   }
};
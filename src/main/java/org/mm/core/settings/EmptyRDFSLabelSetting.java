package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum EmptyRDFSLabelSetting
{
   ERROR_IF_EMPTY_LABEL(MappingMasterParserConstants.MM_ERROR_IF_EMPTY_LABEL),
   WARNING_IF_EMPTY_LABEL(MappingMasterParserConstants.MM_WARNING_IF_EMPTY_LABEL),
   SKIP_IF_EMPTY_LABEL(MappingMasterParserConstants.MM_SKIP_IF_EMPTY_LABEL),
   PROCESS_IF_EMPTY_LABEL(MappingMasterParserConstants.MM_PROCESS_IF_EMPTY_LABEL);

   private int value;

   private EmptyRDFSLabelSetting(int value)
   {
      this.value = value;
   }

   public int getConstant()
   {
      return value;
   }
};
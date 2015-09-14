package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum EmptyLiteralSetting
{
   ERROR_IF_EMPTY_LITERAL(MappingMasterParserConstants.MM_ERROR_IF_EMPTY_LITERAL),
   WARNING_IF_EMPTY_LITERAL(MappingMasterParserConstants.MM_WARNING_IF_EMPTY_LITERAL),
   SKIP_IF_EMPTY_LITERAL(MappingMasterParserConstants.MM_SKIP_IF_EMPTY_LITERAL),
   PROCESS_IF_EMPTY_LITERAL(MappingMasterParserConstants.MM_PROCESS_IF_EMPTY_LITERAL);

   private int value;

   private EmptyLiteralSetting(int value)
   {
      this.value = value;
   }

   public int getConstant()
   {
      return value;
   }
};
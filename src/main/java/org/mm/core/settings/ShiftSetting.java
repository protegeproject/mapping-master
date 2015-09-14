package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum ShiftSetting
{
   NO_SHIFT(MappingMasterParserConstants.MM_NO_SHIFT),
   SHIFT_UP(MappingMasterParserConstants.MM_SHIFT_UP),
   SHIFT_DOWN(MappingMasterParserConstants.MM_SHIFT_DOWN),
   SHIFT_LEFT(MappingMasterParserConstants.MM_SHIFT_LEFT),
   SHIFT_RIGHT(MappingMasterParserConstants.MM_SHIFT_RIGHT);

   private int value;

   private ShiftSetting(int value)
   {
      this.value = value;
   }

   public int getConstant()
   {
      return value;
   }
};
package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum IRIEncodingSetting
{
   MM_NO_ENCODE(MappingMasterParserConstants.MM_NO_ENCODE, "None"),
   MM_CAMELCASE_ENCODE(MappingMasterParserConstants.MM_CAMELCASE_ENCODE, "CamelCase"),
   MM_SNAKECASE_ENCODE(MappingMasterParserConstants.MM_SNAKECASE_ENCODE, "Snake_Case"),
   MM_UUID_ENCODE(MappingMasterParserConstants.MM_UUID_ENCODE, "UUID"),
   MM_HASH_ENCODE(MappingMasterParserConstants.MM_HASH_ENCODE, "Hash");

   private int value;
   private String label;

   private IRIEncodingSetting(int value, String label)
   {
      this.value = value;
      this.label = label;
   }

   public int getConstant()
   {
      return value;
   }

   @Override
   public String toString()
   {
      return label;
   }
}

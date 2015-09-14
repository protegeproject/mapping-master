package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum PropertyTypeSetting
{
   OWL_OBJECT_PROPERTY(MappingMasterParserConstants.OWL_OBJECT_PROPERTY),
   OWL_DATA_PROPERTY(MappingMasterParserConstants.OWL_DATA_PROPERTY),
   OWL_ANNOTATION_PROPERTY(MappingMasterParserConstants.OWL_ANNOTATION_PROPERTY);

   private int value;

   private PropertyTypeSetting(int value)
   {
      this.value = value;
   }

   public int getConstant()
   {
      return value;
   }
};
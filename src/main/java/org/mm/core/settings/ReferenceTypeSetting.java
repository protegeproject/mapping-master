package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum ReferenceTypeSetting {
   OWL_CLASS(MappingMasterParserConstants.OWL_CLASS),
   OWL_NAMED_INDIVIDUAL(MappingMasterParserConstants.OWL_NAMED_INDIVIDUAL),
   OWL_OBJECT_PROPERTY(MappingMasterParserConstants.OWL_OBJECT_PROPERTY),
   OWL_DATA_PROPERTY(MappingMasterParserConstants.OWL_DATA_PROPERTY),
   OWL_ANNOTATION_PROPERTY(MappingMasterParserConstants.OWL_ANNOTATION_PROPERTY);

   private int value;

   private ReferenceTypeSetting(int value)
   {
      this.value = value;
   }

   public int getConstant()
   {
      return value;
   }
};

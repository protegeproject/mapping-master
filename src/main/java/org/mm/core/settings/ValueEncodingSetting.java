package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum ValueEncodingSetting
{
   RDF_ID(MappingMasterParserConstants.RDF_ID, "IRI"),
   RDFS_LABEL(MappingMasterParserConstants.RDFS_LABEL, "rdfs:label"),
   MM_LITERAL(MappingMasterParserConstants.MM_LITERAL, "Literal"),
   MM_LOCATION(MappingMasterParserConstants.MM_LOCATION, "Location");

   private int value;
   private String label;

   private ValueEncodingSetting(int value, String label)
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
};
package org.mm.core.settings;

import org.mm.parser.MappingMasterParserConstants;

public enum ValueEncodingSetting
{
  RDF_ID(MappingMasterParserConstants.RDF_ID),
  RDFS_LABEL(MappingMasterParserConstants.RDFS_LABEL),
  MM_LITERAL(MappingMasterParserConstants.MM_LITERAL),
  MM_LOCATION(MappingMasterParserConstants.MM_LOCATION);

  private int value;

  private ValueEncodingSetting(int value) {
    this.value = value;
  }
};

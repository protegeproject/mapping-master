package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultPropertyType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class MMDefaultPropertyTypeNode implements MMNode, MappingMasterParserConstants
{
  private final int defaultType;

  public MMDefaultPropertyTypeNode(ASTMMDefaultPropertyType node) throws ParseException
  {
    this.defaultType = node.defaultType;
  }

  public int getType()
  {
    return this.defaultType;
  }

  public String getTypeName()
  {
    return tokenImage[this.defaultType].substring(1, tokenImage[this.defaultType].length() - 1);
  }

  @Override public String getNodeName()
  {
    return "MMDefaultPropertyType";
  }

  public String toString()
  {
    return "MM:DefaultPropertyType " + getTypeName();
  }
}

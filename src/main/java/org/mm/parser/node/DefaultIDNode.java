 
package org.mm.parser.node;

import org.mm.parser.ASTDefaultID;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class DefaultIDNode implements MMNode, MappingMasterParserConstants
{
  private String defaultID;
  
  public DefaultIDNode(ASTDefaultID node) throws ParseException
  { 
    this.defaultID = node.defaultID;
  } 

  public String getDefaultRDFID() { return defaultID; }

  @Override public String getNodeName()
  {
    return "DefaultID";
  }

  public String toString()
  { 
    String representation = ParserUtil.getTokenName(MM_DEFAULT_ID) + "=\"" + defaultID + "\"";

    return representation;
  } 

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    DefaultIDNode dv = (DefaultIDNode)obj;
    return defaultID != null && dv.defaultID != null && defaultID.equals(dv.defaultID);
  } 

  public int hashCode()
  {
    int hash = 14;

    hash = hash + (null == defaultID ? 0 : defaultID.hashCode());

    return hash;
  }
}

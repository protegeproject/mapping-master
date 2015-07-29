 
package org.mm.parser.node;

import org.mm.parser.ASTDefaultID;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class DefaultIDDirectiveNode implements MMNode, MappingMasterParserConstants
{
  private final String defaultID;
  
  public DefaultIDDirectiveNode(ASTDefaultID node) throws ParseException
  { 
    this.defaultID = node.defaultID;
  } 

  public String getDefaultRDFID() { return this.defaultID; }

  @Override public String getNodeName()
  {
    return "DefaultIDDirective";
  }

  public String toString()
  { 
    String representation = ParserUtil.getTokenName(MM_DEFAULT_ID) + "=\"" + this.defaultID + "\"";

    return representation;
  } 

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    DefaultIDDirectiveNode dv = (DefaultIDDirectiveNode)obj;
    return this.defaultID != null && dv.defaultID != null && this.defaultID.equals(dv.defaultID);
  } 

  public int hashCode()
  {
    int hash = 14;

    hash = hash + (null == this.defaultID ? 0 : this.defaultID.hashCode());

    return hash;
  }
}

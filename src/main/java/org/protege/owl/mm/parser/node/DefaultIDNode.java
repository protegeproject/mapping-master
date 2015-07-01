 
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTDefaultID;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class DefaultIDNode implements MappingMasterParserConstants
{
  private String defaultID;
  
  public DefaultIDNode(ASTDefaultID node) throws ParseException
  { 
    this.defaultID = node.defaultID;
  } 

  public String getDefaultRDFID() { return defaultID; }
  
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

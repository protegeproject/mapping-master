 
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTDefaultLocationValue;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class DefaultLocationValueNode implements MappingMasterParserConstants
{
  private String defaultLocationValue;
  
  public DefaultLocationValueNode(ASTDefaultLocationValue node) throws ParseException
  { 
    this.defaultLocationValue = node.defaultLocationValue;
  }
  
  public String getDefaultLocationValue() { return defaultLocationValue; }

  public String toString() 
  { 
    String representation = ParserUtil.getTokenName(MM_DEFAULT_LOCATION_VALUE) + "=\"" + defaultLocationValue + "\"";

    return representation;
  } 

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    DefaultLocationValueNode dv = (DefaultLocationValueNode)obj;
    return defaultLocationValue != null && dv.defaultLocationValue != null && defaultLocationValue.equals(dv.defaultLocationValue);
  } 

  public int hashCode()
  {
    int hash = 14;

    hash = hash + (null == defaultLocationValue ? 0 : defaultLocationValue.hashCode());

    return hash;
  }
}

 
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTDefaultLabel;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class DefaultLabelNode implements MappingMasterParserConstants
{
  private String defaultLabel;
  
  public DefaultLabelNode(ASTDefaultLabel node) throws ParseException
  { 
    this.defaultLabel = node.defaultLabel;
  } 

  public String getDefaultRDFSLabel() { return defaultLabel; }
  
  public String toString() 
  { 
    String representation = ParserUtil.getTokenName(MM_DEFAULT_LABEL) + "=\"" + defaultLabel + "\"";

    return representation;
  } 

  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if ((obj == null) || (obj.getClass() != this.getClass())) return false;
    DefaultLabelNode dv = (DefaultLabelNode)obj;
    return defaultLabel != null && dv.defaultLabel != null && defaultLabel.equals(dv.defaultLabel);
  } 

  public int hashCode()
  {
    int hash = 14;

    hash = hash + (null == defaultLabel ? 0 : defaultLabel.hashCode());

    return hash;
  }
}

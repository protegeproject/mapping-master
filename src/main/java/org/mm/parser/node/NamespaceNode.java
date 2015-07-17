
package org.mm.parser.node;

import org.mm.parser.ASTNamespace;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class NamespaceNode implements MMNode, MappingMasterParserConstants
{
  private final String namespace;

  NamespaceNode(ASTNamespace node) throws ParseException
  {
    this.namespace = node.namespace;
  }

  public String getNamespace() { return namespace; }

  @Override public String getNodeName()
  {
    return "Namespace";
  }

  public String toString()
  { 
  	return ParserUtil.getTokenName(MM_NAMESPACE) + "=\"" + namespace + "\"";
  } 
}

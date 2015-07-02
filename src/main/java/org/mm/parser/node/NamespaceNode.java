
package org.mm.parser.node;

import org.mm.parser.ASTNamespace;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class NamespaceNode implements MappingMasterParserConstants
{
  String namespace;

  NamespaceNode(ASTNamespace node) throws ParseException
  {
    namespace = node.namespace;
  }

  public String getNamespace() { return namespace; }

  public String toString() 
  { 
  	return ParserUtil.getTokenName(MM_NAMESPACE) + "=\"" + namespace + "\"";
  } 
}


package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTNamespace;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

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

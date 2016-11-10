
package org.mm.parser.node;

import org.mm.parser.ASTNamespace;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class NamespaceDirectiveNode implements ReferenceDirectiveNode
{
   private final String namespace;

   NamespaceDirectiveNode(ASTNamespace node) throws ParseException
   {
      this.namespace = node.namespace;
   }

   public String getNamespace()
   {
      return this.namespace;
   }

   @Override
   public String getNodeName()
   {
      return "NamespaceDirective";
   }

   public String toString()
   {
      return ParserUtil.getTokenName(MM_NAMESPACE) + "=\"" + this.namespace + "\"";
   }
}

package org.mm.parser.node;

import org.mm.parser.ASTDefaultLabel;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class DefaultLabelDirectiveNode implements ReferenceDirectiveNode
{
   private final String defaultLabel;

   public DefaultLabelDirectiveNode(ASTDefaultLabel node) throws ParseException
   {
      this.defaultLabel = node.defaultLabel;
   }

   public String getDefaultRDFSLabel()
   {
      return this.defaultLabel;
   }

   @Override
   public String getNodeName()
   {
      return "DefaultLabelDirective";
   }

   public String toString()
   {
      String representation = ParserUtil.getTokenName(MM_DEFAULT_LABEL) + "=\"" + this.defaultLabel + "\"";
      return representation;
   }

   public boolean equals(Object obj)
   {
      if (this == obj) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      DefaultLabelDirectiveNode dv = (DefaultLabelDirectiveNode) obj;
      return this.defaultLabel != null && dv.defaultLabel != null && this.defaultLabel.equals(dv.defaultLabel);
   }

   public int hashCode()
   {
      int hash = 14;
      hash = hash + (null == this.defaultLabel ? 0 : this.defaultLabel.hashCode());
      return hash;
   }
}

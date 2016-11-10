package org.mm.parser.node;

import org.mm.parser.ASTDefaultLocationValue;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class DefaultLocationValueDirectiveNode implements ReferenceDirectiveNode
{
   private final String defaultLocationValue;

   public DefaultLocationValueDirectiveNode(ASTDefaultLocationValue node) throws ParseException
   {
      this.defaultLocationValue = node.defaultLocationValue;
   }

   public String getDefaultLocationValue()
   {
      return this.defaultLocationValue;
   }

   @Override
   public String getNodeName()
   {
      return "DefaultLocationValueDirective";
   }

   public String toString()
   {
      return ParserUtil.getTokenName(MM_DEFAULT_LOCATION_VALUE) + "=\"" + this.defaultLocationValue + "\"";
   }

   public boolean equals(Object obj)
   {
      if (this == obj) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      DefaultLocationValueDirectiveNode dv = (DefaultLocationValueDirectiveNode) obj;
      return this.defaultLocationValue != null && dv.defaultLocationValue != null
            && this.defaultLocationValue.equals(dv.defaultLocationValue);
   }

   public int hashCode()
   {
      int hash = 14;
      hash = hash + (null == this.defaultLocationValue ? 0 : this.defaultLocationValue.hashCode());
      return hash;
   }
}

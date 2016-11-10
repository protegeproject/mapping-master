package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultPropertyType;
import org.mm.parser.ParseException;

public class MMDefaultPropertyTypeDirectiveNode implements RuleDirectiveNode
{
   private final int defaultPropertyType;

   public MMDefaultPropertyTypeDirectiveNode(ASTMMDefaultPropertyType node) throws ParseException
   {
      this.defaultPropertyType = node.defaultType;
   }

   public int getType()
   {
      return this.defaultPropertyType;
   }

   public String getTypeName()
   {
      return tokenImage[this.defaultPropertyType].substring(1, tokenImage[this.defaultPropertyType].length() - 1);
   }

   @Override
   public String getNodeName()
   {
      return "MMDefaultPropertyTypeDirective";
   }

   public String toString()
   {
      return "MM:DefaultPropertyType " + getTypeName();
   }
}

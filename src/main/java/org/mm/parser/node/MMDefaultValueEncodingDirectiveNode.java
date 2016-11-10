package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultValueEncoding;
import org.mm.parser.ParseException;

public class MMDefaultValueEncodingDirectiveNode implements RuleDirectiveNode
{
   private final int valueEncodingType;

   public MMDefaultValueEncodingDirectiveNode(ASTMMDefaultValueEncoding node) throws ParseException
   {
      this.valueEncodingType = node.encodingType;
   }

   @Override
   public String getNodeName()
   {
      return "MMDefaultValueEncodingDirective";
   }

   public int getValueEncodingType()
   {
      return this.valueEncodingType;
   }

   public String getEncodingTypeName()
   {
      return tokenImage[this.valueEncodingType].substring(1, tokenImage[this.valueEncodingType].length() - 1);
   }

   public String toString()
   {
      return "MM:DefaultValueEncoding " + getEncodingTypeName();
   }
}

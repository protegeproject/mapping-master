package org.mm.parser.node;

import org.mm.parser.ASTIRIRef;
import org.mm.parser.ParseException;

public class IRIRefNode implements MMNode
{
   private final String value;

   public IRIRefNode(ASTIRIRef node) throws ParseException
   {
      this.value = node.value;
   }

   public String getValue()
   {
      return value;
   }

   @Override
   public String getNodeName()
   {
      return "IRIRef";
   }

   public String toString()
   {
      return getValue();
   }
}

package org.mm.parser.node;

import org.mm.parser.ASTBooleanLiteral;
import org.mm.parser.ParseException;

public class BooleanLiteralNode implements LiteralNode
{
   private final boolean value;

   public BooleanLiteralNode(ASTBooleanLiteral node) throws ParseException
   {
      this.value = node.value;
   }

   public boolean getValue()
   {
      return this.value;
   }

   public String getNodeName()
   {
      return "BooleanLiteral";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      return "" + this.value;
   }
}

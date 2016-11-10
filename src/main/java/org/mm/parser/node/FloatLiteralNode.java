package org.mm.parser.node;

import org.mm.parser.ASTFloatLiteral;
import org.mm.parser.ParseException;

public class FloatLiteralNode implements LiteralNode
{
   private final float value;

   public FloatLiteralNode(ASTFloatLiteral node) throws ParseException
   {
      this.value = node.value;
   }

   public float getValue()
   {
      return this.value;
   }

   public String getNodeName()
   {
      return "FloatLiteral";
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
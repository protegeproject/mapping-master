package org.mm.parser.node;

import org.mm.parser.ASTOWLMaxCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLMaxCardinalityNode implements OWLNode
{
   private final int cardinality;

   public OWLMaxCardinalityNode(ASTOWLMaxCardinalityRestriction node) throws ParseException
   {
      this.cardinality = node.cardinality;
   }

   public int getCardinality()
   {
      return this.cardinality;
   }

   @Override
   public String getNodeName()
   {
      return "OWLMaxCardinalityRestriction";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      return "MAX " + this.cardinality;
   }
}

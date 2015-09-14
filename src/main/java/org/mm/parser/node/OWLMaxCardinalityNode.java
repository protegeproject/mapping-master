package org.mm.parser.node;

import org.mm.parser.ASTOWLMaxCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLMaxCardinalityNode implements MMNode
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

   public String toString()
   {
      return "MAX " + this.cardinality;
   }
}

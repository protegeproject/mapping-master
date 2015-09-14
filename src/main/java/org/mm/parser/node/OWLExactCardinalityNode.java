package org.mm.parser.node;

import org.mm.parser.ASTOWLExactCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLExactCardinalityNode
{
   private final int cardinality;

   public OWLExactCardinalityNode(ASTOWLExactCardinalityRestriction node) throws ParseException
   {
      this.cardinality = node.cardinality;
   }

   public int getCardinality()
   {
      return this.cardinality;
   }

   public String toString()
   {
      return "EXACTLY " + this.cardinality;
   }
}

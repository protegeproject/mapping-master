package org.mm.parser.node;

import org.mm.parser.ASTOWLMinCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLMinCardinalityNode implements MMNode
{
   private final int cardinality;

   OWLMinCardinalityNode(ASTOWLMinCardinalityRestriction node) throws ParseException
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
      return "OWLMinCardinaliyRestriction";
   }

   public String toString()
   {
      return "MIN " + this.cardinality;
   }
}

package org.mm.parser.node;

import org.mm.parser.ASTOWLMinCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLMinCardinalityNode implements OWLNode
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

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      return "MIN " + this.cardinality;
   }
}

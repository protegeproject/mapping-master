
package org.mm.parser.node;

import org.mm.parser.ASTOWLExactCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLExactCardinalityRestrictionNode
{
  int cardinality;

  public OWLExactCardinalityRestrictionNode(ASTOWLExactCardinalityRestriction node) throws ParseException
  {
    cardinality = node.cardinality;
  } 

  public int getCardinality() { return  cardinality; }

  public String toString() { return "EXACTLY " + cardinality; }
}

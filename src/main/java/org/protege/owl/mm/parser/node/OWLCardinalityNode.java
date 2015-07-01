
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLCardinality;
import org.protege.owl.mm.parser.ParseException;

public class OWLCardinalityNode
{
  int cardinality;

  public OWLCardinalityNode(ASTOWLCardinality node) throws ParseException
  {
    cardinality = node.cardinality;
  } 

  public int getCardinality() { return  cardinality; }

  public String toString() { return "EXACTLY " + cardinality; }
}

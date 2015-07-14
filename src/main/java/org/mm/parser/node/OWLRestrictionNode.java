
package org.mm.parser.node;

import org.mm.parser.ASTOWLAllValuesFromRestriction;
import org.mm.parser.ASTOWLExactCardinalityRestriction;
import org.mm.parser.ASTOWLHasValueRestriction;
import org.mm.parser.ASTOWLMaxCardinalityRestriction;
import org.mm.parser.ASTOWLMinCardinalityRestriction;
import org.mm.parser.ASTOWLRestriction;
import org.mm.parser.ASTOWLSomeValuesFromRestriction;
import org.mm.parser.ParseException;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

public class OWLRestrictionNode
{
  private OWLPropertyNode propertNode;
  private OWLMaxCardinalityRestrictionNode maxCardinalityRestrictionNode = null;
  private OWLMinCardinalityRestrictionNode minCardinalityNode = null;
  private OWLExactCardinalityRestrictionNode exactCardinalityRestrictionNode = null;
  private OWLHasValueRestrictionNode hasValueRestrictionNode = null;
  private OWLAllValuesFromRestrictionNode allValuesFromRestrictionNode = null;
  private OWLSomeValuesFromRestrictionNode someValuesFromRestrictionNode = null;

  public OWLRestrictionNode(ASTOWLRestriction node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLProperty"))
        propertNode = new OWLPropertyNode((ASTOWLProperty)child);
      else if (ParserUtil.hasName(child, "OWLMaxCardinalityRestriction"))
        maxCardinalityRestrictionNode = new OWLMaxCardinalityRestrictionNode((ASTOWLMaxCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLMinCardinalityRestriction"))
        minCardinalityNode = new OWLMinCardinalityRestrictionNode((ASTOWLMinCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLExactCardinalityRestriction"))
        exactCardinalityRestrictionNode = new OWLExactCardinalityRestrictionNode((ASTOWLExactCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLHasValueRestriction"))
        hasValueRestrictionNode = new OWLHasValueRestrictionNode((ASTOWLHasValueRestriction)child);
      else if (ParserUtil.hasName(child, "OWLAllValuesFromRestriction"))
        allValuesFromRestrictionNode = new OWLAllValuesFromRestrictionNode((ASTOWLAllValuesFromRestriction)child);
      else if (ParserUtil.hasName(child, "OWLSomeValuesFromRestriction"))
        someValuesFromRestrictionNode = new OWLSomeValuesFromRestrictionNode((ASTOWLSomeValuesFromRestriction)child);
      else throw new InternalParseException("invalid child node " + child.toString() + " for OWLRestrictionNode");
    } 
  }

  public OWLPropertyNode getOWLPropertyNode() { return propertNode; }
  public OWLMaxCardinalityRestrictionNode getOWLMaxCardinalityNode() { return maxCardinalityRestrictionNode; }
  public OWLMinCardinalityRestrictionNode getOWLMinCardinalityNode() { return minCardinalityNode; }
  public OWLExactCardinalityRestrictionNode getOWLCardinalityNode() { return exactCardinalityRestrictionNode; }
  public OWLHasValueRestrictionNode getOWLHasValueNode() { return hasValueRestrictionNode; }
  public OWLAllValuesFromRestrictionNode getOWLAllValuesFromNode() { return allValuesFromRestrictionNode; }
  public OWLSomeValuesFromRestrictionNode getOWLSomeValuesFromNode() { return someValuesFromRestrictionNode; }

  public boolean hasOWLMaxCardinality() { return maxCardinalityRestrictionNode != null; }
  public boolean hasOWLMinCardinality() { return minCardinalityNode != null; }
  public boolean hasOWLCardinality() { return exactCardinalityRestrictionNode != null; }
  public boolean hasOWLHasValue() { return hasValueRestrictionNode != null; }
  public boolean hasOWLAllValuesFrom() { return allValuesFromRestrictionNode != null; }
  public boolean hasOWLSomeValuesFrom() { return someValuesFromRestrictionNode != null; }

  public String toString() 
  { 
    String representation = "(" + getOWLPropertyNode() + " ";

    if (minCardinalityNode != null) representation += minCardinalityNode.toString();
    else if (maxCardinalityRestrictionNode != null) representation += maxCardinalityRestrictionNode.toString();
    else if (exactCardinalityRestrictionNode != null) representation += exactCardinalityRestrictionNode.toString();
    else if (hasValueRestrictionNode != null) representation += hasValueRestrictionNode.toString();
    else if (allValuesFromRestrictionNode != null) representation += allValuesFromRestrictionNode.toString();
    else if (someValuesFromRestrictionNode != null) representation += someValuesFromRestrictionNode.toString();

    representation += ")";

    return representation;
  }

} 

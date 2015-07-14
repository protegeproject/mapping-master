package org.mm.parser.node;

import org.mm.parser.ASTOWLAllValuesFromRestriction;
import org.mm.parser.ASTOWLExactCardinalityRestriction;
import org.mm.parser.ASTOWLHasValueRestriction;
import org.mm.parser.ASTOWLMaxCardinalityRestriction;
import org.mm.parser.ASTOWLMinCardinalityRestriction;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.ASTOWLRestriction;
import org.mm.parser.ASTOWLSomeValuesFromRestriction;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLRestrictionNode
{
  private OWLPropertyNode propertyNode;
  private OWLMaxCardinalityRestrictionNode maxCardinalityRestrictionNode;
  private OWLMinCardinalityRestrictionNode minCardinalityRestrictionNode;
  private OWLExactCardinalityRestrictionNode exactCardinalityRestrictionNode;
  private OWLHasValueRestrictionNode hasValueRestrictionNode;
  private OWLAllValuesFromRestrictionNode allValuesFromRestrictionNode;
  private OWLSomeValuesFromRestrictionNode someValuesFromRestrictionNode;

  public OWLRestrictionNode(ASTOWLRestriction node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLProperty"))
        propertyNode = new OWLPropertyNode((ASTOWLProperty)child);
      else if (ParserUtil.hasName(child, "OWLMaxCardinalityRestriction"))
        maxCardinalityRestrictionNode = new OWLMaxCardinalityRestrictionNode((ASTOWLMaxCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLMinCardinalityRestriction"))
        minCardinalityRestrictionNode = new OWLMinCardinalityRestrictionNode((ASTOWLMinCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLExactCardinalityRestriction"))
        exactCardinalityRestrictionNode = new OWLExactCardinalityRestrictionNode(
          (ASTOWLExactCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLHasValueRestriction"))
        hasValueRestrictionNode = new OWLHasValueRestrictionNode((ASTOWLHasValueRestriction)child);
      else if (ParserUtil.hasName(child, "OWLAllValuesFromRestriction"))
        allValuesFromRestrictionNode = new OWLAllValuesFromRestrictionNode((ASTOWLAllValuesFromRestriction)child);
      else if (ParserUtil.hasName(child, "OWLSomeValuesFromRestriction"))
        someValuesFromRestrictionNode = new OWLSomeValuesFromRestrictionNode((ASTOWLSomeValuesFromRestriction)child);
      else
        throw new InternalParseException("invalid child node " + child.toString() + " for OWLRestrictionNode");
    }
  }

  public OWLPropertyNode getOWLPropertyNode() { return propertyNode; }

  public OWLMaxCardinalityRestrictionNode getOWLMaxCardinalityRestrictionNode() { return maxCardinalityRestrictionNode; }

  public OWLMinCardinalityRestrictionNode getOWLMinCardinalityRestrictionNode() { return minCardinalityRestrictionNode; }

  public OWLExactCardinalityRestrictionNode getOWLExactCardinalityRestrictionNode() { return exactCardinalityRestrictionNode; }

  public OWLHasValueRestrictionNode getOWLHasValueRestrictionNode() { return hasValueRestrictionNode; }

  public OWLAllValuesFromRestrictionNode getOWLAllValuesFromRestrictionNode() { return allValuesFromRestrictionNode; }

  public OWLSomeValuesFromRestrictionNode getOWLSomeValuesFromRestrictionNode() { return someValuesFromRestrictionNode; }

  public boolean isOWLMaxCardinality() { return maxCardinalityRestrictionNode != null; }

  public boolean isOWLMinCardinality() { return minCardinalityRestrictionNode != null; }

  public boolean isOWLExactCardinality() { return exactCardinalityRestrictionNode != null; }

  public boolean isOWLHasValue() { return hasValueRestrictionNode != null; }

  public boolean isOWLAllValuesFrom() { return allValuesFromRestrictionNode != null; }

  public boolean isOWLSomeValuesFrom() { return someValuesFromRestrictionNode != null; }

  public String toString()
  {
    String representation = "(" + getOWLPropertyNode() + " ";

    if (minCardinalityRestrictionNode != null)
      representation += minCardinalityRestrictionNode.toString();
    else if (maxCardinalityRestrictionNode != null)
      representation += maxCardinalityRestrictionNode.toString();
    else if (exactCardinalityRestrictionNode != null)
      representation += exactCardinalityRestrictionNode.toString();
    else if (hasValueRestrictionNode != null)
      representation += hasValueRestrictionNode.toString();
    else if (allValuesFromRestrictionNode != null)
      representation += allValuesFromRestrictionNode.toString();
    else if (someValuesFromRestrictionNode != null)
      representation += someValuesFromRestrictionNode.toString();

    representation += ")";

    return representation;
  }

} 

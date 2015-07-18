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

public class OWLRestrictionNode implements MMNode
{
  private OWLPropertyNode propertyNode;
  private OWLMaxCardinalityNode maxCardinalityNode;
  private OWLMinCardinalityNode minCardinalityNode;
  private OWLExactCardinalityNode exactCardinalityNode;
  private OWLHasValueNode hasValueNode;
  private OWLAllValuesFromNode allValuesFromNode;
  private OWLSomeValuesFromNode someValuesFromNode;

  public OWLRestrictionNode(ASTOWLRestriction node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLProperty"))
        propertyNode = new OWLPropertyNode((ASTOWLProperty)child);
      else if (ParserUtil.hasName(child, "OWLMaxCardinalityRestriction"))
        maxCardinalityNode = new OWLMaxCardinalityNode((ASTOWLMaxCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLMinCardinalityRestriction"))
        minCardinalityNode = new OWLMinCardinalityNode((ASTOWLMinCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLExactCardinalityRestriction"))
        exactCardinalityNode = new OWLExactCardinalityNode(
          (ASTOWLExactCardinalityRestriction)child);
      else if (ParserUtil.hasName(child, "OWLHasValueRestriction"))
        hasValueNode = new OWLHasValueNode((ASTOWLHasValueRestriction)child);
      else if (ParserUtil.hasName(child, "OWLAllValuesFromRestriction"))
        allValuesFromNode = new OWLAllValuesFromNode((ASTOWLAllValuesFromRestriction)child);
      else if (ParserUtil.hasName(child, "OWLSomeValuesFromRestriction"))
        someValuesFromNode = new OWLSomeValuesFromNode((ASTOWLSomeValuesFromRestriction)child);
      else
        throw new InternalParseException("invalid child node " + child.toString() + " for node " + getNodeName());
    }
  }

  public OWLPropertyNode getOWLPropertyNode() { return propertyNode; }

  public OWLMaxCardinalityNode getOWLMaxCardinalityNode() { return maxCardinalityNode; }

  public OWLMinCardinalityNode getOWLMinCardinalityNode() { return minCardinalityNode; }

  public OWLExactCardinalityNode getOWLExactCardinalityNode() { return exactCardinalityNode; }

  public OWLHasValueNode getOWLHasValueNode() { return hasValueNode; }

  public OWLAllValuesFromNode getOWLAllValuesFromNode() { return allValuesFromNode; }

  public OWLSomeValuesFromNode getOWLSomeValuesFromNode() { return someValuesFromNode; }

  public boolean isOWLMaxCardinality() { return maxCardinalityNode != null; }

  public boolean isOWLMinCardinality() { return minCardinalityNode != null; }

  public boolean isOWLExactCardinality() { return exactCardinalityNode != null; }

  public boolean isOWLHasValue() { return hasValueNode != null; }

  public boolean isOWLAllValuesFrom() { return allValuesFromNode != null; }

  public boolean isOWLSomeValuesFrom() { return someValuesFromNode != null; }

  @Override public String getNodeName()
  {
    return "OWLRestriction";
  }

  public String toString()
  {
    String representation = "(" + getOWLPropertyNode() + " ";

    if (minCardinalityNode != null)
      representation += minCardinalityNode.toString();
    else if (maxCardinalityNode != null)
      representation += maxCardinalityNode.toString();
    else if (exactCardinalityNode != null)
      representation += exactCardinalityNode.toString();
    else if (hasValueNode != null)
      representation += hasValueNode.toString();
    else if (allValuesFromNode != null)
      representation += allValuesFromNode.toString();
    else if (someValuesFromNode != null)
      representation += someValuesFromNode.toString();

    representation += ")";

    return representation;
  }

} 

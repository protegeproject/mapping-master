
package org.mm.parser.node;

import org.mm.parser.ASTOWLAllValuesFrom;
import org.mm.parser.ASTOWLCardinality;
import org.mm.parser.ASTOWLHasValue;
import org.mm.parser.ASTOWLMaxCardinality;
import org.mm.parser.ASTOWLRestriction;
import org.mm.parser.ASTOWLSomeValuesFrom;
import org.mm.parser.ParseException;
import org.mm.parser.ASTOWLMinCardinality;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

public class OWLRestrictionNode
{
  private OWLPropertyNode owlPropertNode;
  private OWLMaxCardinalityNode owlMaxCardinalityNode = null;
  private OWLMinCardinalityNode owlMinCardinalityNode = null;
  private OWLCardinalityNode owlCardinalityNode = null;
  private OWLHasValueNode owlHasValueNode = null;
  private OWLAllValuesFromNode owlAllValuesFromNode = null;
  private OWLSomeValuesFromNode owlSomeValuesFromNode = null;

  public OWLRestrictionNode(ASTOWLRestriction node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLProperty"))
        owlPropertNode = new OWLPropertyNode((ASTOWLProperty)child);
      else if (ParserUtil.hasName(child, "OWLMaxCardinality")) 
        owlMaxCardinalityNode = new OWLMaxCardinalityNode((ASTOWLMaxCardinality)child);
      else if (ParserUtil.hasName(child, "OWLMinCardinality")) 
        owlMinCardinalityNode = new OWLMinCardinalityNode((ASTOWLMinCardinality)child);
      else if (ParserUtil.hasName(child, "OWLCardinality")) 
        owlCardinalityNode = new OWLCardinalityNode((ASTOWLCardinality)child);
      else if (ParserUtil.hasName(child, "OWLHasValue")) 
        owlHasValueNode = new OWLHasValueNode((ASTOWLHasValue)child);
      else if (ParserUtil.hasName(child, "OWLAllValuesFrom")) 
        owlAllValuesFromNode = new OWLAllValuesFromNode((ASTOWLAllValuesFrom)child);
      else if (ParserUtil.hasName(child, "OWLSomeValuesFrom")) 
        owlSomeValuesFromNode = new OWLSomeValuesFromNode((ASTOWLSomeValuesFrom)child);
      else throw new InternalParseException("invalid child node " + child.toString() + " for OWLRestrictionNode");
    } 
  }

  public OWLPropertyNode getOWLPropertyNode() { return owlPropertNode; }
  public OWLMaxCardinalityNode getOWLMaxCardinalityNode() { return owlMaxCardinalityNode; }
  public OWLMinCardinalityNode getOWLMinCardinalityNode() { return owlMinCardinalityNode; }
  public OWLCardinalityNode getOWLCardinalityNode() { return owlCardinalityNode; }
  public OWLHasValueNode getOWLHasValueNode() { return owlHasValueNode; }
  public OWLAllValuesFromNode getOWLAllValuesFromNode() { return owlAllValuesFromNode; }
  public OWLSomeValuesFromNode getOWLSomeValuesFromNode() { return owlSomeValuesFromNode; }

  public boolean hasOWLMaxCardinality() { return owlMaxCardinalityNode != null; }
  public boolean hasOWLMinCardinality() { return owlMinCardinalityNode != null; }
  public boolean hasOWLCardinality() { return owlCardinalityNode != null; }
  public boolean hasOWLHasValue() { return owlHasValueNode != null; }
  public boolean hasOWLAllValuesFrom() { return owlAllValuesFromNode != null; }
  public boolean hasOWLSomeValuesFrom() { return owlSomeValuesFromNode != null; }

  public String toString() 
  { 
    String representation = "(" + getOWLPropertyNode() + " ";

    if (owlMinCardinalityNode != null) representation += owlMinCardinalityNode.toString();
    else if (owlMaxCardinalityNode != null) representation += owlMaxCardinalityNode.toString();
    else if (owlCardinalityNode != null) representation += owlCardinalityNode.toString();
    else if (owlHasValueNode != null) representation += owlHasValueNode.toString();
    else if (owlAllValuesFromNode != null) representation += owlAllValuesFromNode.toString();
    else if (owlSomeValuesFromNode != null) representation += owlSomeValuesFromNode.toString();

    representation += ")";

    return representation;
  }

} 

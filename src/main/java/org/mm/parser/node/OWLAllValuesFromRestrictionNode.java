package org.mm.parser.node;

import org.mm.parser.ASTOWLAllValuesFromRestriction;
import org.mm.parser.ASTOWLDataAllValuesFrom;
import org.mm.parser.ASTOWLObjectAllValuesFrom;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLAllValuesFromRestrictionNode implements MMNode
{
  private OWLDataAllValuesFromNode dataAllValuesFromNode;
  private OWLObjectAllValuesFromNode objectAllValuesFromNode;

  public OWLAllValuesFromRestrictionNode(ASTOWLAllValuesFromRestriction node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLAllDataValuesFrom"))
        dataAllValuesFromNode = new OWLDataAllValuesFromNode((ASTOWLDataAllValuesFrom)child);
      else if (ParserUtil.hasName(child, "OWLObjectAllValuesFrom"))
        objectAllValuesFromNode = new OWLObjectAllValuesFromNode((ASTOWLObjectAllValuesFrom)child);
      else
        throw new InternalParseException("invalid child node " + child.toString() + " for node " + getNodeName());
    }
  }

  public OWLDataAllValuesFromNode getOWLDataAllValuesFromNode()
  {
    return dataAllValuesFromNode;
  }

  public OWLObjectAllValuesFromNode getObjectOWLAllValuesFromNode()
  {
    return objectAllValuesFromNode;
  }

  public boolean hasOWLDataAllValuesFromNode()
  {
    return dataAllValuesFromNode != null;
  }

  public boolean hasOWLObjectAllValuesFromNode()
  {
    return objectAllValuesFromNode != null;
  }

  @Override public String getNodeName()
  {
    return "OWLAllValuesFromRestriction";
  }

  public String toString()
  {
    String representation = "";

    if (dataAllValuesFromNode != null)
      representation += dataAllValuesFromNode.toString();
    else if (objectAllValuesFromNode != null)
      representation += objectAllValuesFromNode.toString();

    return representation;
  }
}

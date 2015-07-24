package org.mm.parser.node;

import org.mm.parser.ASTOWLAllValuesFromRestriction;
import org.mm.parser.ASTOWLDataAllValuesFrom;
import org.mm.parser.ASTOWLObjectAllValuesFrom;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLAllValuesFromNode implements MMNode
{
  private OWLDataAllValuesFromNode dataAllValuesFromNode;
  private OWLObjectAllValuesFromNode objectAllValuesFromNode;

  public OWLAllValuesFromNode(ASTOWLAllValuesFromRestriction node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLAllDataValuesFrom"))
        this.dataAllValuesFromNode = new OWLDataAllValuesFromNode((ASTOWLDataAllValuesFrom)child);
      else if (ParserUtil.hasName(child, "OWLObjectAllValuesFrom"))
        this.objectAllValuesFromNode = new OWLObjectAllValuesFromNode((ASTOWLObjectAllValuesFrom)child);
      else
        throw new InternalParseException("invalid child node " + child + " for node " + getNodeName());
    }
  }

  public OWLDataAllValuesFromNode getOWLDataAllValuesFromNode()
  {
    return this.dataAllValuesFromNode;
  }

  public OWLObjectAllValuesFromNode getObjectOWLAllValuesFromNode()
  {
    return this.objectAllValuesFromNode;
  }

  public boolean hasOWLDataAllValuesFromNode()
  {
    return this.dataAllValuesFromNode != null;
  }

  public boolean hasOWLObjectAllValuesFromNode()
  {
    return this.objectAllValuesFromNode != null;
  }

  @Override public String getNodeName()
  {
    return "OWLAllValuesFromRestriction";
  }

  public String toString()
  {
    String representation = "";

    if (this.dataAllValuesFromNode != null)
      representation += this.dataAllValuesFromNode.toString();
    else if (this.objectAllValuesFromNode != null)
      representation += this.objectAllValuesFromNode.toString();

    return representation;
  }
}

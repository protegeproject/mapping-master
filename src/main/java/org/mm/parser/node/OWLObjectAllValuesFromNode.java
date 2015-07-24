package org.mm.parser.node;

import org.mm.parser.ASTOWLClass;
import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLObjectAllValuesFrom;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLObjectAllValuesFromNode implements MMNode
{
  private OWLClassExpressionNode classExpressionNode;
  private OWLClassNode classNode;

  public OWLObjectAllValuesFromNode(ASTOWLObjectAllValuesFrom node) throws ParseException
  {
    Node child = node.jjtGetChild(0);
    if (ParserUtil.hasName(child, "OWLClassExpression"))
      this.classExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression)child);
    else if (ParserUtil.hasName(child, "OWLClass"))
      this.classNode = new OWLClassNode((ASTOWLClass)child);
    else
      throw new InternalParseException(getNodeName() +
        " node expecting OWLClassExpression or OWLClass children got " + child);
  }

  public boolean hasOWLClassExpression()
  {
    return this.classExpressionNode != null;
  }

  public boolean hasOWLClass()
  {
    return this.classNode != null;
  }

  public OWLClassExpressionNode getOWLClassExpressionNode()
  {
    return this.classExpressionNode;
  }

  public OWLClassNode getOWLClassNode()
  {
    return this.classNode;
  }

  @Override public String getNodeName()
  {
    return "OWLObjectAllValuesFrom";
  }

  public String toString()
  {
    String representation = "ONLY ";

    if (hasOWLClassExpression())
      representation += this.classExpressionNode.toString();
    else if (hasOWLClass())
      representation += this.classNode.toString();

    representation += ")";

    return representation;
  }
}

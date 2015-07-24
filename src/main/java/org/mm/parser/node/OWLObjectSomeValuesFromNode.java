package org.mm.parser.node;

import org.mm.parser.ASTOWLClass;
import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLObjectSomeValuesFrom;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLObjectSomeValuesFromNode implements MMNode
{
  private OWLClassExpressionNode classExpressionNode;
  private OWLClassNode classNode;

  public OWLObjectSomeValuesFromNode(ASTOWLObjectSomeValuesFrom node) throws ParseException
  {
    Node child = node.jjtGetChild(0);
    if (ParserUtil.hasName(child, "OWLClassExpression"))
      this.classExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression)child);
    else if (ParserUtil.hasName(child, "OWLClass"))
      this.classNode = new OWLClassNode((ASTOWLClass)child);
    else
      throw new InternalParseException(
        getNodeName() + " node expecting OWLCLass or OWLClassExpression child, got " + child);
  }

  public boolean hasOWLClassExpressionNode()
  {
    return this.classExpressionNode != null;
  }

  public boolean hasOWLClassNode()
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
    return "OWLSomeValuesFrom";
  }

  public String toString()
  {
    String representation = "SOME ";

    if (hasOWLClassExpressionNode())
      representation += this.classExpressionNode.toString();
    else if (hasOWLClassNode())
      representation += this.classNode.toString();

    return representation;
  }
}

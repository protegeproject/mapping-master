package org.mm.parser.node;

import org.mm.parser.ASTName;
import org.mm.parser.ASTOWLHasValueRestriction;
import org.mm.parser.ASTOWLLiteral;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLHasValueNode implements MMNode
{
  private ReferenceNode referenceNode;
  private NameNode nameNode;
  private OWLLiteralNode literalNode;

  public OWLHasValueNode(ASTOWLHasValueRestriction node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)
      throw new InternalParseException("expecting one child of OWLHasValueRestriction node");
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "Reference"))
        referenceNode = new ReferenceNode((ASTReference)child);
      else if (ParserUtil.hasName(child, "Name"))
        nameNode = new NameNode((ASTName)child);
      else if (ParserUtil.hasName(child, "OWLLiteral"))
        literalNode = new OWLLiteralNode((ASTOWLLiteral)child);
      else
        throw new InternalParseException(
          "unexpected child node " + child.toString() + " for OWLHasValueRestriction node");
    }
  }

  public String getNodeName()
  {
    return "OWLHasValueRestriction";
  }

  public ReferenceNode getReferenceNode()
  {
    return referenceNode;
  }

  public NameNode getNameNode()
  {
    return nameNode;
  }

  public OWLLiteralNode getOWLLiteralNode()
  {
    return literalNode;
  }

  public boolean isReference()
  {
    return referenceNode != null;
  }

  public boolean isName()
  {
    return nameNode != null;
  }

  public boolean isLiteral()
  {
    return literalNode != null;
  }

  public String toString()
  {
    if (isReference())
      return referenceNode.toString();
    else if (isName())
      return nameNode.toString();
    else if (isLiteral())
      return literalNode.toString();
    else
      return "";
  }
}

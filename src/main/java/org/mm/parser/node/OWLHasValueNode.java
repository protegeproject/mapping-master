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
        this.referenceNode = new ReferenceNode((ASTReference)child);
      else if (ParserUtil.hasName(child, "Name"))
        this.nameNode = new NameNode((ASTName)child);
      else if (ParserUtil.hasName(child, "OWLLiteral"))
        this.literalNode = new OWLLiteralNode((ASTOWLLiteral)child);
      else
        throw new InternalParseException(
          "unexpected child node " + child + " for OWLHasValueRestriction node");
    }
  }

  public String getNodeName()
  {
    return "OWLHasValueRestriction";
  }

  public ReferenceNode getReferenceNode()
  {
    return this.referenceNode;
  }

  public NameNode getNameNode()
  {
    return this.nameNode;
  }

  public OWLLiteralNode getOWLLiteralNode()
  {
    return this.literalNode;
  }

  public boolean hasReferenceNode()
  {
    return this.referenceNode != null;
  }

  public boolean hasNameNone()
  {
    return this.nameNode != null;
  }

  public boolean hasLiteralNode()
  {
    return this.literalNode != null;
  }

  public String toString()
  {
    if (hasReferenceNode())
      return this.referenceNode.toString();
    else if (hasNameNone())
      return this.nameNode.getName();
    else if (hasLiteralNode())
      return this.literalNode.toString();
    else
      return "";
  }
}

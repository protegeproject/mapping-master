package org.mm.parser.node;

import org.mm.parser.ASTOWLLiteral;
import org.mm.parser.ASTReference;
import org.mm.parser.ASTValueExtractionFunctionArgument;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ValueExtractionFunctionArgumentNode implements MMNode
{
  private ReferenceNode referenceNode;
  private OWLLiteralNode literalNode;

  public ValueExtractionFunctionArgumentNode(ASTValueExtractionFunctionArgument node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)
      throw new InternalParseException("expecting one child node for node " + getNodeName());
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "OWLLiteral"))
        this.literalNode = new OWLLiteralNode((ASTOWLLiteral)child);
      else if (ParserUtil.hasName(child, "Reference"))
        this.referenceNode = new ReferenceNode((ASTReference)child);
      else
        throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
    }
  }

  public ReferenceNode getReferenceNode()
  {
    return this.referenceNode;
  }

  public OWLLiteralNode getOWLLiteralNode()
  {
    return this.literalNode;
  }

  public boolean isOWLLiteralNode()
  {
    return this.literalNode != null;
  }

  public boolean isReferenceNode()
  {
    return this.referenceNode != null;
  }

  @Override public String getNodeName()
  {
    return "ValueExtractionFunctionArgument";
  }

  public String toString()
  {
    if (isOWLLiteralNode())
      return this.literalNode.toString();
    else if (isReferenceNode())
      return this.referenceNode.toString();
    else
      return "";
  }
}

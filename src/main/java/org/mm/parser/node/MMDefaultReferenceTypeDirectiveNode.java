package org.mm.parser.node;

import org.mm.core.ReferenceType;
import org.mm.parser.ASTMMDefaultReferenceType;
import org.mm.parser.ASTReferenceType;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class MMDefaultReferenceTypeDirectiveNode implements MMNode
{
  private ReferenceTypeNode referenceTypeNode;

  public MMDefaultReferenceTypeDirectiveNode(ASTMMDefaultReferenceType node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)
      throw new InternalParseException("expecting one ReferenceType child of node " + getNodeName());
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "ReferenceType"))
        this.referenceTypeNode = new ReferenceTypeNode((ASTReferenceType)child);
      else
        throw new InternalParseException(
          "expecting ReferenceType child, got " + child + " for node " + getNodeName());
    }
  }

  public ReferenceType getReferenceType()
  {
    return this.referenceTypeNode.getReferenceType();
  }

  @Override public String getNodeName()
  {
    return "MMDefaultReferenceTypeDirective";
  }

  public String toString()
  {
    return "MM:DefaultReferenceType " + this.referenceTypeNode;
  }
}

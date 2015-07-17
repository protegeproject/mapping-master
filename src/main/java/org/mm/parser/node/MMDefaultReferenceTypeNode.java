package org.mm.parser.node;

import org.mm.core.ReferenceType;
import org.mm.parser.ASTMMDefaultReferenceType;
import org.mm.parser.ASTReferenceType;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class MMDefaultReferenceTypeNode implements MMNode
{
  private ReferenceTypeNode referenceTypeNode;

  public MMDefaultReferenceTypeNode(ASTMMDefaultReferenceType node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)
      throw new InternalParseException("expecting one ReferenceType child of MMDefaultReferenceType node");
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "ReferenceType"))
        this.referenceTypeNode = new ReferenceTypeNode((ASTReferenceType)child);
      else
        throw new InternalParseException(
          "MMDefaultReferenceType node expecting ReferenceType child, got " + child.toString());
    }
  }

  public ReferenceType getReferenceType()
  {
    return referenceTypeNode.getReferenceType();
  }

  @Override public String getNodeName()
  {
    return "MMDefaultReferenceTypeNode";
  }

  public String toString()
  {
    return "MM:DefaultReferenceType " + referenceTypeNode.toString();
  }
}

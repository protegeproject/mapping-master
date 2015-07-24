package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultPropertyValueType;
import org.mm.parser.ASTMMDefaultReferenceType;
import org.mm.parser.ASTMMDefaultValueEncoding;
import org.mm.parser.ASTMMDirective;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class MMDirectiveNode implements MMNode
{
  private MMDefaultValueEncodingNode defaultValueEncodingNode;
  private MMDefaultReferenceTypeNode defaultReferenceTypeNode;
  private MMDefaultPropertyValueTypeNode defaultPropertyValueTypeNode;

  public MMDirectiveNode(ASTMMDirective node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "MMDefaultValueEncoding")) {
        this.defaultValueEncodingNode = new MMDefaultValueEncodingNode((ASTMMDefaultValueEncoding)child);
      } else if (ParserUtil.hasName(child, "MMDefaultReferenceType")) {
        this.defaultReferenceTypeNode = new MMDefaultReferenceTypeNode((ASTMMDefaultReferenceType)child);
      } else if (ParserUtil.hasName(child, "MMDefaultPropertyValueType")) {
        this.defaultPropertyValueTypeNode = new MMDefaultPropertyValueTypeNode((ASTMMDefaultPropertyValueType)child);
      } else
        throw new InternalParseException("invalid child node " + child + " to MMExpression");
    }
  }

  public MMDefaultValueEncodingNode getDefaultValueEncodingNode()
  {
    return this.defaultValueEncodingNode;
  }

  public MMDefaultReferenceTypeNode getDefaultReferenceTypeNode()
  {
    return this.defaultReferenceTypeNode;
  }

  public MMDefaultPropertyValueTypeNode getDefaultPropertyValueTypeNode()
  {
    return this.defaultPropertyValueTypeNode;
  }

  public boolean hasDefaultValueEncoding()
  {
    return this.defaultValueEncodingNode != null;
  }

  public boolean hasDefaultReferenceType()
  {
    return this.defaultReferenceTypeNode != null;
  }

  public boolean hasDefaultPropertyValueType()
  {
    return this.defaultPropertyValueTypeNode != null;
  }

  @Override public String getNodeName()
  {
    return "MMDirective";
  }

  public String toString()
  {
    String representation = "";

    if (hasDefaultValueEncoding())
      representation += this.defaultValueEncodingNode.toString();
    if (hasDefaultReferenceType())
      representation += this.defaultReferenceTypeNode.toString();
    if (hasDefaultPropertyValueType())
      representation += this.defaultPropertyValueTypeNode.toString();

    return representation;
  }

}

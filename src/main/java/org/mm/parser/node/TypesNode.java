package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTTypes;
import org.mm.parser.ParseException;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class TypesNode implements MMNode
{
  private final List<TypeNode> typeNodes;

  public TypesNode(ASTTypes node) throws ParseException
  {
    this.typeNodes = new ArrayList<>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLClassExpression")) {
        OWLClassExpressionNode classExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression)child);
        typeNodes.add(classExpressionNode);
      } else if (ParserUtil.hasName(child, "Reference")) {
        ReferenceNode referenceNode = new ReferenceNode((ASTReference)child);
        typeNodes.add(referenceNode);
      } else
        throw new InternalParseException(
          "Types node expecting OWLClassExpression child, got " + child.toString());
    }
  }

  public List<TypeNode> getTypeNodes()
  {
    return typeNodes;
  }

  public String getNodeName()
  {
    return "Types";
  }

  public String toString()
  {
    String representation = "";
    boolean isFirst = true;

    for (TypeNode typeNode : typeNodes) {
      if (!isFirst)
        representation += ", ";
      representation += typeNode.toString();
      isFirst = false;
    }

    return representation;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if ((obj == null) || (obj.getClass() != this.getClass()))
      return false;
    TypesNode dt = (TypesNode)obj;
    return (typeNodes != null && dt.typeNodes != null && typeNodes.equals(dt.typeNodes));
  }

  public int hashCode()
  {
    int hash = 25;
    hash = hash + (null == typeNodes ? 0 : typeNodes.hashCode());
    return hash;
  }
}

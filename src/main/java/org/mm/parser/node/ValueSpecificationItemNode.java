package org.mm.parser.node;

import org.mm.parser.ASTReference;
import org.mm.parser.ASTValueExtractionFunction;
import org.mm.parser.ASTValueSpecificationItem;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ValueSpecificationItemNode implements MMNode
{
  private String stringLiteral;
  private ReferenceNode referenceNode;
  private ValueExtractionFunctionNode valueExtractionFunctionNode;
  private String capturingExpression;

  public ValueSpecificationItemNode(ASTValueSpecificationItem node) throws ParseException
  {
    stringLiteral = node.stringLiteral;
    capturingExpression = node.captureExpression;

    if (node.jjtGetNumChildren() != 0) {
      Node child = node.jjtGetChild(0);

      if (ParserUtil.hasName(child, "Reference"))
        referenceNode = new ReferenceNode((ASTReference)child);
      else if (ParserUtil.hasName(child, "ValueExtractionFunction"))
        valueExtractionFunctionNode = new ValueExtractionFunctionNode((ASTValueExtractionFunction)child);
      else
        throw new InternalParseException("invalid child node " + child.toString() + " for node " + getNodeName());
    }
  }

  public boolean hasStringLiteral()
  {
    return stringLiteral != null;
  }

  public String getStringLiteral()
  {
    return stringLiteral;
  }

  public boolean hasReference()
  {
    return referenceNode != null;
  }

  public ReferenceNode getReferenceNode()
  {
    return referenceNode;
  }

  public boolean hasValueExtractionFunction()
  {
    return valueExtractionFunctionNode != null;
  }

  public ValueExtractionFunctionNode getValueExtractionFunctionNode()
  {
    return valueExtractionFunctionNode;
  }

  public boolean hasCapturingExpression()
  {
    return capturingExpression != null;
  }

  public String getCapturingExpression()
  {
    return capturingExpression;
  }

  @Override public String getNodeName()
  {
    return "ValueSpecificationItem";
  }

  public String toString()
  {
    String representation = "";

    if (hasStringLiteral())
      representation = "\"" + stringLiteral + "\"";
    else if (hasReference())
      representation = referenceNode.toString();
    else if (hasValueExtractionFunction())
      representation = valueExtractionFunctionNode.toString();
    else if (hasCapturingExpression())
      representation = "[\"" + capturingExpression + "\"]";

    return representation;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if ((obj == null) || (obj.getClass() != this.getClass()))
      return false;
    ValueSpecificationItemNode vs = (ValueSpecificationItemNode)obj;
    return (stringLiteral != null && vs.stringLiteral != null && stringLiteral.equals(vs.stringLiteral)) && (
      referenceNode != null && vs.referenceNode != null && referenceNode.equals(vs.referenceNode)) && (
      valueExtractionFunctionNode != null && vs.valueExtractionFunctionNode != null && valueExtractionFunctionNode
        .equals(vs.valueExtractionFunctionNode)) && (capturingExpression != null && vs.capturingExpression != null
      && capturingExpression.equals(vs.capturingExpression));
  }

  public int hashCode()
  {
    int hash = 15;

    hash = hash + (null == stringLiteral ? 0 : stringLiteral.hashCode());
    hash = hash + (null == referenceNode ? 0 : referenceNode.hashCode());
    hash = hash + (null == valueExtractionFunctionNode ? 0 : valueExtractionFunctionNode.hashCode());
    hash = hash + (null == capturingExpression ? 0 : capturingExpression.hashCode());

    return hash;
  }
}

package org.mm.parser.node;

import org.mm.parser.ASTExpression;
import org.mm.parser.ASTMMDirective;
import org.mm.parser.ASTMMExpression;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ExpressionNode implements MMNode
{
  private MMDirectiveNode mmDirectiveNode;
  private MMExpressionNode mmExpressionNode;

  public ExpressionNode(ASTExpression node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "MMDirective")) {
        this.mmDirectiveNode = new MMDirectiveNode((ASTMMDirective)child);
      } else if (ParserUtil.hasName(child, "MMExpression")) {
        this.mmExpressionNode = new MMExpressionNode((ASTMMExpression)child);
      } else
        throw new InternalParseException("invalid child node " + child + " to Expression");
    }
  }

  public MMDirectiveNode getMMDirectiveNode()
  {
    return this.mmDirectiveNode;
  }

  public MMExpressionNode getMMExpressionNode()
  {
    return this.mmExpressionNode;
  }

  public boolean hasMMDirective()
  {
    return this.mmDirectiveNode != null;
  }

  public boolean hasMMExpression()
  {
    return this.mmExpressionNode != null;
  }

  @Override public String getNodeName()
  {
    return "Expression";
  }

  public String toString()
  {
    if (hasMMDirective())
      return this.mmDirectiveNode.toString();
    else if (hasMMExpression())
      return this.mmExpressionNode.toString();
    else
      return "";
  }
}

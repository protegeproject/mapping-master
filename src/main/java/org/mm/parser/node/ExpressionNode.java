package org.mm.parser.node;

import org.mm.parser.ASTExpression;
import org.mm.parser.ASTMMDirective;
import org.mm.parser.ASTMMExpression;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ExpressionNode
{
  private MMDirectiveNode mmDirectiveNode = null;
  private MMExpressionNode mmExpressionNode = null;

  public ExpressionNode(ASTExpression node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "MMExpression")) {
        mmDirectiveNode = new MMDirectiveNode((ASTMMDirective)child);
      } else if (ParserUtil.hasName(child, "OWLExpression")) {
        mmExpressionNode = new MMExpressionNode((ASTMMExpression)child);
      } else
        throw new InternalParseException("invalid child node " + child.toString() + " to Expression");
    }
  }

  public MMDirectiveNode getMMDirectiveNode()
  {
    return mmDirectiveNode;
  }

  public MMExpressionNode getMMExpressionNode()
  {
    return mmExpressionNode;
  }

  public boolean hasMMDirective()
  {
    return mmDirectiveNode != null;
  }

  public boolean hasMMExpression()
  {
    return mmExpressionNode != null;
  }

  public String toString()
  {
    if (hasMMDirective())
      return mmDirectiveNode.toString();
    else if (hasMMExpression())
      return mmExpressionNode.toString();
    else
      return "";
  }
}

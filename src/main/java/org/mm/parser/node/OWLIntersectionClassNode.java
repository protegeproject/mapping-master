package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLIntersectionClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLIntersectionClassNode implements MMNode
{
  private List<OWLClassExpressionNode> classExpressionNodes;

  public OWLIntersectionClassNode(ASTOWLIntersectionClass node) throws ParseException
  {
    classExpressionNodes = new ArrayList<>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLClassExpression")) {
        OWLClassExpressionNode owlClassExpression = new OWLClassExpressionNode((ASTOWLClassExpression)child);
        classExpressionNodes.add(owlClassExpression);
      } else
        throw new InternalParseException(getNodeName() +
          " node expecting OWLClassExpression child, got " + child.toString());
    }
  }

  public List<OWLClassExpressionNode> getOWLClassExpressionNodes()
  {
    return classExpressionNodes;
  }

  @Override public String getNodeName()
  {
    return "OWLIntersectionClass";
  }

  public String toString()
  {
    String representation = "";

    if (this.classExpressionNodes.size() == 1)
      representation = this.classExpressionNodes.get(0).toString();
    else {
      boolean isFirst = true;

      representation += "(";
      for (OWLClassExpressionNode owlClassExpression : this.classExpressionNodes) {
        if (!isFirst)
          representation += " AND ";
        representation += owlClassExpression.toString();
        isFirst = false;
      }
      representation += ")";
    }
    return representation;
  }
}

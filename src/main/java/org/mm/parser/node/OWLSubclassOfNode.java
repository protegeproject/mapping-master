package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLSubclassOf;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLSubclassOfNode
{
  private List<OWLClassExpressionNode> owlClassExpressionNodes;

  public OWLSubclassOfNode(ASTOWLSubclassOf node) throws ParseException
  {
    owlClassExpressionNodes = new ArrayList<OWLClassExpressionNode>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLClassExpression")) {
        OWLClassExpressionNode owlClassExpression = new OWLClassExpressionNode((ASTOWLClassExpression)child);
        owlClassExpressionNodes.add(owlClassExpression);
      } else
        throw new InternalParseException(
          "OWLSubclassOf node expecting OWLClassExpression child, got " + child.toString());
    }
  }

  public List<OWLClassExpressionNode> getClassExpressionNodes()
  {
    return owlClassExpressionNodes;
  }

  public String toString()
  {
    String representation = " SubClassOf: ";

    if (owlClassExpressionNodes.size() == 1)
      representation += owlClassExpressionNodes.get(0).toString();
    else {
      boolean isFirst = true;

      for (OWLClassExpressionNode owlClassExpression : owlClassExpressionNodes) {
        if (!isFirst)
          representation += ", ";
        representation += owlClassExpression.toString();
        isFirst = false;
      }
    }

    return representation;
  }
}

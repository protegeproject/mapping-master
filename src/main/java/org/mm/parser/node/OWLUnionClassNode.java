package org.mm.parser.node;

import org.mm.parser.ASTOWLIntersectionClass;
import org.mm.parser.ASTOWLUnionClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLUnionClassNode implements MMNode
{
  private List<OWLIntersectionClassNode> intersectionClassNodes;

  public OWLUnionClassNode(ASTOWLUnionClass node) throws ParseException
  {
    intersectionClassNodes = new ArrayList<>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLIntersectionClass")) {
        OWLIntersectionClassNode owlIntersectionClass = new OWLIntersectionClassNode((ASTOWLIntersectionClass)child);
        intersectionClassNodes.add(owlIntersectionClass);
      } else
        throw new InternalParseException(
          getNodeName() + "node expecting OWLIntersectionClass child, got " + child.toString());
    }
  }

  public List<OWLIntersectionClassNode> getOWLIntersectionClassNodes()
  {
    return intersectionClassNodes;
  }

  @Override public String getNodeName()
  {
    return "OWLUnionClass";
  }

  public String toString()
  {
    String representation = "";

    if (intersectionClassNodes.size() == 1)
      representation = intersectionClassNodes.get(0).toString();
    else {
      boolean isFirst = true;

      representation += "(";
      for (OWLIntersectionClassNode owlIntersectionClass : intersectionClassNodes) {
        if (!isFirst)
          representation += " OR ";
        representation += owlIntersectionClass.toString();
        isFirst = false;
      }
      representation += ")";
    }
    return representation;
  }
}

package org.mm.parser.node;

import org.mm.parser.ASTOWLIndividual;
import org.mm.parser.InternalParseException;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;
import org.mm.parser.ASTOWLEnumeratedClass;
import org.mm.parser.Node;

import java.util.ArrayList;
import java.util.List;

public class OWLEnumeratedClassNode
{
  private List<OWLIndividualNode> individualNodes;

  public OWLEnumeratedClassNode(ASTOWLEnumeratedClass node) throws ParseException
  {
    individualNodes = new ArrayList<>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLIndividual")) {
        OWLIndividualNode owlIndividual = new OWLIndividualNode((ASTOWLIndividual)child);
        individualNodes.add(owlIndividual);
      } else
        throw new InternalParseException(
          "OWLEnumeratedClass node expecting OWLIndividual child, got " + child.toString());
    }
  }

  public List<OWLIndividualNode> getOWLIndividualNodes()
  {
    return individualNodes;
  }

  public String toString()
  {
    String representation = "";

    if (individualNodes.size() == 1)
      representation = individualNodes.get(0).toString();
    else {
      boolean isFirst = true;

      representation += "{";
      for (OWLIndividualNode owlIndividual : individualNodes) {
        if (!isFirst)
          representation += " ";
        representation += owlIndividual.toString();
        isFirst = false;
      }
      representation += "}";
    }

    return representation;
  }
}

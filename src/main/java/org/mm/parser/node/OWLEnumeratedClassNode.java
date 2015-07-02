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
  private List<OWLIndividualNode> owlIndividualNodes;

  public OWLEnumeratedClassNode(ASTOWLEnumeratedClass node) throws ParseException
  {
    owlIndividualNodes = new ArrayList<OWLIndividualNode>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLIndividual")) {
        OWLIndividualNode owlIndividual = new OWLIndividualNode((ASTOWLIndividual)child);
        owlIndividualNodes.add(owlIndividual);
      } else
        throw new InternalParseException(
          "OWLEnumeratedClass node expecting OWLIndividual child, got " + child.toString());
    }
  }

  public List<OWLIndividualNode> getOWLIndividualNodes()
  {
    return owlIndividualNodes;
  }

  public String toString()
  {
    String representation = "";

    if (owlIndividualNodes.size() == 1)
      representation = owlIndividualNodes.get(0).toString();
    else {
      boolean isFirst = true;

      representation += "{";
      for (OWLIndividualNode owlIndividual : owlIndividualNodes) {
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

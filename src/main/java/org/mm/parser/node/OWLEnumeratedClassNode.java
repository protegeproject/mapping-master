package org.mm.parser.node;

import org.mm.parser.ASTOWLNamedIndividual;
import org.mm.parser.InternalParseException;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;
import org.mm.parser.ASTOWLEnumeratedClass;
import org.mm.parser.Node;

import java.util.ArrayList;
import java.util.List;

public class OWLEnumeratedClassNode
{
  private List<OWLNamedIndividualNode> namedIndividualNodes;

  public OWLEnumeratedClassNode(ASTOWLEnumeratedClass node) throws ParseException
  {
    namedIndividualNodes = new ArrayList<>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLNamedIndividual")) {
        OWLNamedIndividualNode owlIndividual = new OWLNamedIndividualNode((ASTOWLNamedIndividual)child);
        namedIndividualNodes.add(owlIndividual);
      } else
        throw new InternalParseException(
          "OWLEnumeratedClass node expecting OWLNamedIndividual child, got " + child.toString());
    }
  }

  public List<OWLNamedIndividualNode> getOWLNamedIndividualNodes()
  {
    return namedIndividualNodes;
  }

  public String toString()
  {
    String representation = "";

    if (namedIndividualNodes.size() == 1)
      representation = namedIndividualNodes.get(0).toString();
    else {
      boolean isFirst = true;

      representation += "{";
      for (OWLNamedIndividualNode owlIndividual : namedIndividualNodes) {
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

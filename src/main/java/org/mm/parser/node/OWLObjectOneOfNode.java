package org.mm.parser.node;

import org.mm.parser.ASTOWLNamedIndividual;
import org.mm.parser.ASTOWLObjectOneOf;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLObjectOneOfNode implements MMNode
{
  private List<OWLNamedIndividualNode> namedIndividualNodes;

  public OWLObjectOneOfNode(ASTOWLObjectOneOf node) throws ParseException
  {
    this.namedIndividualNodes = new ArrayList<>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLNamedIndividual")) {
        OWLNamedIndividualNode namedIndividualNode = new OWLNamedIndividualNode((ASTOWLNamedIndividual)child);
        this.namedIndividualNodes.add(namedIndividualNode);
      } else
        throw new InternalParseException(getNodeName() +
          " node expecting OWLNamedIndividual child node, got " + child.toString());
    }
  }

  public List<OWLNamedIndividualNode> getOWLNamedIndividualNodes()
  {
    return namedIndividualNodes;
  }

  @Override public String getNodeName()
  {
    return "OWLObjectOneOf";
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

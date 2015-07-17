package org.mm.parser.node;

import org.mm.parser.ASTOWLNamedIndividual;
import org.mm.parser.ASTOWLSameAs;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLSameAsNode implements MMNode
{
  private List<OWLNamedIndividualNode> namedIndividualNodes;

  public OWLSameAsNode(ASTOWLSameAs node) throws ParseException
  {
    namedIndividualNodes = new ArrayList<>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLNamedIndividual")) {
        OWLNamedIndividualNode owlIndividual = new OWLNamedIndividualNode((ASTOWLNamedIndividual)child);
        namedIndividualNodes.add(owlIndividual);
      } else
        throw new InternalParseException(
          getNodeName() + "node expecting OWLNamedIndividual child, got " + child.toString());
    }
  }

  public List<OWLNamedIndividualNode> getIndividualNodes()
  {
    return namedIndividualNodes;
  }

  @Override public String getNodeName()
  {
    return "OWLSameAs";
  }

  public String toString()
  {
    String representation = " SameAs: ";

    if (namedIndividualNodes.size() == 1)
      representation += namedIndividualNodes.get(0).toString();
    else {
      boolean isFirst = true;

      for (OWLNamedIndividualNode namedIndividualNode : namedIndividualNodes) {
        if (!isFirst)
          representation += ", ";
        representation += namedIndividualNode.toString();
        isFirst = false;
      }
    }
    return representation;
  }

}

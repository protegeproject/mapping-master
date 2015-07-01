package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLClassOrRestriction;
import org.protege.owl.mm.parser.ASTOWLIntersectionClass;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLIntersectionClassNode
{
  private List<OWLClassOrRestrictionNode> owlClassesOrRestrictionNodes;

  public OWLIntersectionClassNode(ASTOWLIntersectionClass node) throws ParseException
  {
    owlClassesOrRestrictionNodes = new ArrayList<OWLClassOrRestrictionNode>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLClassOrRestriction")) {
        OWLClassOrRestrictionNode owlClassOrRestriction = new OWLClassOrRestrictionNode(
          (ASTOWLClassOrRestriction)child);
        owlClassesOrRestrictionNodes.add(owlClassOrRestriction);
      } else
        throw new InternalParseException(
          "OWLIntersectionClass node expecting OWLClassOrRestriction child, got " + child.toString());
    }
  }

  public List<OWLClassOrRestrictionNode> getOWLClassesOrRestrictionNodes()
  {
    return owlClassesOrRestrictionNodes;
  }

  public String toString()
  {
    String representation = "";

    if (owlClassesOrRestrictionNodes.size() == 1)
      representation = owlClassesOrRestrictionNodes.get(0).toString();
    else {
      boolean isFirst = true;

      representation += "(";
      for (OWLClassOrRestrictionNode owlClassOrRestriction : owlClassesOrRestrictionNodes) {
        if (!isFirst)
          representation += " AND ";
        representation += owlClassOrRestriction.toString();
        isFirst = false;
      }
      representation += ")";
    }

    return representation;
  }
}

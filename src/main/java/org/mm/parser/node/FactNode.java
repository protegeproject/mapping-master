package org.mm.parser.node;

import org.mm.parser.ASTFact;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.ASTOWLPropertyAssertionObject;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class FactNode implements MMNode
{
  private OWLPropertyNode propertyNode;
  private OWLPropertyAssertionObjectNode propertyAssertionObjectNode;

  public FactNode(ASTFact node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);
      if (ParserUtil.hasName(child, "OWLProperty"))
        this.propertyNode = new OWLPropertyNode((ASTOWLProperty)child);
      else if (ParserUtil.hasName(child, "OWLPropertyAssertionObject"))
        this.propertyAssertionObjectNode = new OWLPropertyAssertionObjectNode((ASTOWLPropertyAssertionObject)child);
      else
        throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
    }
  }

  public OWLPropertyNode getOWLPropertyNode()
  {
    return this.propertyNode;
  }

  public OWLPropertyAssertionObjectNode getOWLPropertyAssertionObjectNode()
  {
    return this.propertyAssertionObjectNode;
  }

  @Override public String getNodeName()
  {
    return "Fact";
  }

  public String toString()
  {
    return this.propertyNode + " " + this.propertyAssertionObjectNode;
  }
}

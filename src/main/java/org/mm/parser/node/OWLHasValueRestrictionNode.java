
package org.mm.parser.node;

import org.mm.parser.ASTOWLHasValueRestriction;
import org.mm.parser.ASTOWLPropertyAssertionObject;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLHasValueRestrictionNode
{
  private OWLPropertyAssertionObjectNode propertyAssertionObjectNode = null;

  public OWLHasValueRestrictionNode(ASTOWLHasValueRestriction node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);
      if (ParserUtil.hasName(child, "OWLPropertyAssertionObject"))
        propertyAssertionObjectNode = new OWLPropertyAssertionObjectNode((ASTOWLPropertyAssertionObject)child);
      else throw new InternalParseException("invalid child node " + child.toString() + " for OWLHasPropertyAssertionObject");
    } 
  }

  public OWLPropertyAssertionObjectNode getOWLPropertyAssertionObjectNode() { return propertyAssertionObjectNode; }

  public String toString() { return "VALUE " + propertyAssertionObjectNode.toString(); }
}

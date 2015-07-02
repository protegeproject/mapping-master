
package org.mm.parser.node;

import org.mm.parser.ASTOWLHasValue;
import org.mm.parser.ASTOWLPropertyValue;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLHasValueNode
{
  private OWLPropertyValueNode owlPropertyValueNode = null;

  public OWLHasValueNode(ASTOWLHasValue node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);
      if (ParserUtil.hasName(child, "OWLPropertyValue"))
        owlPropertyValueNode = new OWLPropertyValueNode((ASTOWLPropertyValue)child);
      else throw new InternalParseException("invalid child node " + child.toString() + " for OWLHasValue");
    } 
  }

  public OWLPropertyValueNode getOWLPropertyValueNode() { return owlPropertyValueNode; }

  public String toString() { return "VALUE " + owlPropertyValueNode.toString(); }
}

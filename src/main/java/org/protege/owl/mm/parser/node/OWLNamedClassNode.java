
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTName;
import org.protege.owl.mm.parser.ASTOWLNamedClass;
import org.protege.owl.mm.parser.ASTReference;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class OWLNamedClassNode
{
  private ReferenceNode referenceNode = null;
  private NameNode nameNode = null;

  public OWLNamedClassNode(ASTOWLNamedClass node) throws ParseException
  {
    if (node.jjtGetNumChildren() != 1)  
      throw new InternalParseException("expecting one chld node for OWLNamedClass node");
    else {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "Name")) nameNode = new NameNode((ASTName)child);
      else if (ParserUtil.hasName(child, "Reference")) referenceNode = new ReferenceNode((ASTReference)child);
      else throw new InternalParseException("unexpected child node " + child.toString() + " for OWLNamedClass node");
    }
  }

  public ReferenceNode getReferenceNode() { return referenceNode; }
  public NameNode getNameNode() { return nameNode; }

  public boolean isName() { return nameNode != null; }
  public boolean isReference() { return referenceNode != null; }

  public String toString() 
  { 
    if (isName()) return nameNode.toString();
    else if (isReference()) return referenceNode.toString();
    else return "";
  } 
}

package org.mm.parser.node;

import org.mm.parser.ASTOWLClassDeclaration;
import org.mm.parser.ASTOWLExpression;
import org.mm.parser.ASTOWLIndividualDeclaration;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLExpressionNode implements MMNode
{
  private OWLClassDeclarationNode owlClassDeclarationNode;
  private OWLIndividualDeclarationNode owlIndividualDeclarationNode;

  public OWLExpressionNode(ASTOWLExpression node) throws ParseException
  {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "OWLClassDeclaration")) {
        owlClassDeclarationNode = new OWLClassDeclarationNode((ASTOWLClassDeclaration)child);
      } else if (ParserUtil.hasName(child, "OWLIndividualDeclaration")) {
        owlIndividualDeclarationNode = new OWLIndividualDeclarationNode((ASTOWLIndividualDeclaration)child);
      } else
        throw new InternalParseException("invalid child node " + child.toString() + " to OWLExpression");
    }
  }

  public OWLClassDeclarationNode getOWLClassDeclarationNode()
  {
    return owlClassDeclarationNode;
  }

  public OWLIndividualDeclarationNode getOWLIndividualDeclarationNode()
  {
    return owlIndividualDeclarationNode;
  }

  public boolean hasOWLClassDeclaration()
  {
    return owlClassDeclarationNode != null;
  }

  public boolean hasOWLIndividualDeclaration()
  {
    return owlIndividualDeclarationNode != null;
  }

  public String getNodeName()
  {
    return "OWLClassExpression";
  }

  public String toString()
  {
    if (hasOWLClassDeclaration())
      return owlClassDeclarationNode.toString();
    else if (hasOWLIndividualDeclaration())
      return owlIndividualDeclarationNode.toString();
    else
      return "";
  }
}

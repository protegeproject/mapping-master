package org.mm.parser.node;

import org.mm.parser.ASTMMExpression;
import org.mm.parser.ASTOWLClassDeclaration;
import org.mm.parser.ASTOWLIndividualDeclaration;
import org.mm.parser.ASTOWLObjectPropertyDeclaration;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class MMExpressionNode implements MMNode
{
   private OWLClassDeclarationNode owlClassDeclarationNode;
   private OWLIndividualDeclarationNode owlIndividualDeclarationNode;
   private OWLObjectPropertyDeclarationNode owlObjectPropertyDeclarationNode;

   public MMExpressionNode(ASTMMExpression node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);

         if (ParserUtil.hasName(child, "OWLClassDeclaration")) {
            this.owlClassDeclarationNode = new OWLClassDeclarationNode((ASTOWLClassDeclaration) child);
         } else if (ParserUtil.hasName(child, "OWLIndividualDeclaration")) {
            this.owlIndividualDeclarationNode = new OWLIndividualDeclarationNode((ASTOWLIndividualDeclaration) child);
         } else if (ParserUtil.hasName(child, "OWLObjectPropertyDeclaration")) {
        	this.owlObjectPropertyDeclarationNode = new OWLObjectPropertyDeclarationNode((ASTOWLObjectPropertyDeclaration) child);
         } else {
            throw new InternalParseException("invalid child node " + child + " to OWLExpression");
         }
      }
   }

   public OWLClassDeclarationNode getOWLClassDeclarationNode()
   {
      return this.owlClassDeclarationNode;
   }

   public OWLIndividualDeclarationNode getOWLIndividualDeclarationNode()
   {
      return this.owlIndividualDeclarationNode;
   }
   
   public OWLObjectPropertyDeclarationNode getOWLObjectPropertyDeclarationNode()
   {
      return this.owlObjectPropertyDeclarationNode;
   }

   public boolean hasOWLClassDeclaration()
   {
      return this.owlClassDeclarationNode != null;
   }

   public boolean hasOWLIndividualDeclaration()
   {
      return this.owlIndividualDeclarationNode != null;
   }
   
   public boolean hasOWLObjectPropertyDeclaration()
   {
      return this.owlObjectPropertyDeclarationNode != null;
   }

   public String getNodeName()
   {
      return "OWLClassExpression";
   }

   public String toString()
   {
      if (hasOWLClassDeclaration())
         return this.owlClassDeclarationNode.toString();
      else if (hasOWLIndividualDeclaration())
         return this.owlIndividualDeclarationNode.toString();
      else if (hasOWLObjectPropertyDeclaration())
    	  return this.owlObjectPropertyDeclarationNode.toString();
      else return "";
   }
}

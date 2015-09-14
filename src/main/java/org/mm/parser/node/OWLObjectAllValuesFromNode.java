package org.mm.parser.node;

import org.mm.parser.ASTOWLClass;
import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLObjectAllValuesFrom;
import org.mm.parser.ASTOWLObjectOneOf;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLObjectAllValuesFromNode implements MMNode
{
   private OWLClassExpressionNode classExpressionNode;
   private OWLClassNode classNode;
   private OWLObjectOneOfNode objectOneOfNode;

   public OWLObjectAllValuesFromNode(ASTOWLObjectAllValuesFrom node) throws ParseException
   {
      Node child = node.jjtGetChild(0);
      if (ParserUtil.hasName(child, "OWLClassExpression"))
         this.classExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression) child);
      else if (ParserUtil.hasName(child, "OWLClass"))
         this.classNode = new OWLClassNode((ASTOWLClass) child);
      else if (ParserUtil.hasName(child, "OWLObjectOneOf"))
         this.objectOneOfNode = new OWLObjectOneOfNode((ASTOWLObjectOneOf) child);
      else throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
   }

   public boolean hasOWLClassExpression()
   {
      return this.classExpressionNode != null;
   }

   public boolean hasOWLClass()
   {
      return this.classNode != null;
   }

   public OWLObjectOneOfNode getOWLObjectOneOfNode()
   {
      return this.objectOneOfNode;
   }

   public OWLClassExpressionNode getOWLClassExpressionNode()
   {
      return this.classExpressionNode;
   }

   public OWLClassNode getOWLClassNode()
   {
      return this.classNode;
   }

   public boolean hasOWLObjectOneOfNode()
   {
      return this.objectOneOfNode != null;
   }

   @Override
   public String getNodeName()
   {
      return "OWLObjectAllValuesFrom";
   }

   public String toString()
   {
      String representation = "ONLY ";
      if (hasOWLClassExpression())
         representation += "(" + this.classExpressionNode.toString() + ")";
      else if (hasOWLClass())
         representation += this.classNode.toString();
      else if (hasOWLObjectOneOfNode()) representation += this.objectOneOfNode.toString();
      return representation;
   }
}

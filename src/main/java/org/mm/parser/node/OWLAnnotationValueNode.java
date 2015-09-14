package org.mm.parser.node;

import org.mm.parser.ASTOWLLiteral;
import org.mm.parser.ASTOWLAnnotationValue;
import org.mm.parser.ParseException;
import org.mm.parser.ASTName;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

public class OWLAnnotationValueNode implements MMNode
{
   private ReferenceNode referenceNode;
   private NameNode nameNode;
   private OWLLiteralNode literalNode;

   public OWLAnnotationValueNode(ASTOWLAnnotationValue node) throws ParseException
   {
      if (node.jjtGetNumChildren() != 1)
         throw new InternalParseException("expecting one child of OWLAnnotationValue node");
      else {
         Node child = node.jjtGetChild(0);
         if (ParserUtil.hasName(child, "Reference"))
            this.referenceNode = new ReferenceNode((ASTReference) child);
         else if (ParserUtil.hasName(child, "Name"))
            this.nameNode = new NameNode((ASTName) child);
         else if (ParserUtil.hasName(child, "OWLLiteral"))
            this.literalNode = new OWLLiteralNode((ASTOWLLiteral) child);
         else throw new InternalParseException("unexpected child node " + child + " for OWLAnnotationValue node");
      }
   }

   public String getNodeName()
   {
      return "OWLAnnotationValue";
   }

   public ReferenceNode getReferenceNode()
   {
      return this.referenceNode;
   }

   public NameNode getNameNode()
   {
      return this.nameNode;
   }

   public OWLLiteralNode getOWLLiteralNode()
   {
      return this.literalNode;
   }

   public boolean isReference()
   {
      return this.referenceNode != null;
   }

   public boolean isName()
   {
      return this.nameNode != null;
   }

   public boolean isLiteral()
   {
      return this.literalNode != null;
   }

   public String toString()
   {
      if (isReference())
         return this.referenceNode.toString();
      else if (isName())
         return this.nameNode.toString();
      else if (isLiteral())
         return this.literalNode.toString();
      else return "";
   }
}

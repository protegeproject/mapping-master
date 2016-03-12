package org.mm.parser.node;

import org.mm.parser.ASTIRIRef;
import org.mm.parser.ASTName;
import org.mm.parser.ASTOWLAnnotationValue;
import org.mm.parser.ASTOWLLiteral;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLAnnotationValueNode implements TypeNode
{
   private ReferenceNode referenceNode;
   private NameNode nameNode;
   private OWLLiteralNode literalNode;
   private IRIRefNode iriRefNode;

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
         else if (ParserUtil.hasName(child, "IRIRef"))
            this.iriRefNode = new IRIRefNode((ASTIRIRef) child);
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

   public IRIRefNode getIRIRefNode()
   {
      return this.iriRefNode;
   }

   public boolean hasReferenceNode()
   {
      return this.referenceNode != null;
   }

   public boolean hasNameNode()
   {
      return this.nameNode != null;
   }

   public boolean hasLiteralNode()
   {
      return this.literalNode != null;
   }

   public boolean hasIRIRefNode()
   {
      return this.iriRefNode != null;
   }

   public String toString()
   {
      if (hasReferenceNode())
         return this.referenceNode.toString();
      else if (hasNameNode())
         return this.nameNode.toString();
      else if (hasLiteralNode())
         return this.literalNode.toString();
      else if (hasIRIRefNode())
         return this.iriRefNode.toString();
      else return "";
   }
}

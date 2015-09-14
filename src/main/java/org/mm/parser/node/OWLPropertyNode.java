package org.mm.parser.node;

import org.mm.parser.ASTName;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLPropertyNode implements TypeNode
{
   private ReferenceNode referenceNode;
   private NameNode nameNode;

   public OWLPropertyNode(ASTOWLProperty node) throws ParseException
   {
      if (node.jjtGetNumChildren() != 1)
         throw new InternalParseException("expecting one child node for node " + getNodeName());
      else {
         Node child = node.jjtGetChild(0);
         if (ParserUtil.hasName(child, "Name"))
            this.nameNode = new NameNode((ASTName) child);
         else if (ParserUtil.hasName(child, "Reference"))
            this.referenceNode = new ReferenceNode((ASTReference) child);
         else throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
      }
   }

   public String getNodeName()
   {
      return "OWLProperty";
   }

   public ReferenceNode getReferenceNode()
   {
      return this.referenceNode;
   }

   public NameNode getNameNode()
   {
      return this.nameNode;
   }

   public boolean hasNameNode()
   {
      return this.nameNode != null;
   }

   public boolean hasReferenceNode()
   {
      return this.referenceNode != null;
   }

   @Override
   public boolean isOWLClassNode()
   {
      return false;
   }

   @Override
   public boolean isOWLClassExpressionNode()
   {
      return false;
   }

   @Override
   public boolean isOWLPropertyNode()
   {
      return false;
   }

   @Override
   public boolean isReferenceNode()
   {
      return false;
   }

   public String toString()
   {
      if (hasNameNode())
         return this.nameNode.toString();
      else if (hasReferenceNode())
         return this.referenceNode.toString();
      else return "";
   }
}

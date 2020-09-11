package org.mm.parser.node;

import org.mm.parser.ASTName;
import org.mm.parser.ASTOWLObjectProperty;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLObjectPropertyNode implements TypeNode
{
   private ReferenceNode referenceNode;
   private NameNode nameNode;

   public OWLObjectPropertyNode(ASTOWLObjectProperty node) throws ParseException
   {
      if (node.jjtGetNumChildren() != 1)
         throw new InternalParseException("expecting one child node for node " + getNodeName());
      else {
         Node child = node.jjtGetChild(0);
         if (ParserUtil.hasName(child, "Name"))
            this.nameNode = new NameNode((ASTName) child);
         else if (ParserUtil.hasName(child, "Reference"))
            this.referenceNode = new ReferenceNode((ASTReference) child);
         else throw new InternalParseException("unexpected child node " + child + " for " + getNodeName() + " node");
      }
   }

   @Override
   public String getNodeName()
   {
      return "OWLObjectProperty";
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

   public String toString()
   {
      if (hasNameNode())
         return this.nameNode.toString();
      else if (hasReferenceNode())
         return this.referenceNode.toString();
      else return "";
   }
}

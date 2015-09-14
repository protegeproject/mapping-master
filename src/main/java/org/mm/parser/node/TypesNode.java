package org.mm.parser.node;

import org.mm.parser.ASTOWLClass;
import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTTypes;
import org.mm.parser.ParseException;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class TypesNode implements MMNode
{
   private final List<TypeNode> typeNodes;

   public TypesNode(ASTTypes node) throws ParseException
   {
      this.typeNodes = new ArrayList<>();

      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLClass")) {
            OWLClassNode classNode = new OWLClassNode((ASTOWLClass) child);
            this.typeNodes.add(classNode);
         } else if (ParserUtil.hasName(child, "OWLClassExpression")) {
            OWLClassExpressionNode classNode = new OWLClassExpressionNode((ASTOWLClassExpression) child);
            this.typeNodes.add(classNode);
         } else if (ParserUtil.hasName(child, "Reference")) {
            ReferenceNode referenceNode = new ReferenceNode((ASTReference) child);
            this.typeNodes.add(referenceNode);
         } else throw new InternalParseException("unexpected child node  " + child + " for node " + getNodeName());
      }
   }

   public List<TypeNode> getTypeNodes()
   {
      return this.typeNodes;
   }

   public String getNodeName()
   {
      return "Types";
   }

   public String toString()
   {
      String representation = "";
      boolean isFirst = true;
      for (TypeNode typeNode : this.typeNodes) {
         if (!isFirst) representation += ", ";
         representation += typeNode.toString();
         isFirst = false;
      }
      return representation;
   }

   public boolean equals(Object obj)
   {
      if (this == obj) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      TypesNode dt = (TypesNode) obj;
      return this.typeNodes != null && dt.typeNodes != null && this.typeNodes.equals(dt.typeNodes);
   }

   public int hashCode()
   {
      int hash = 25;
      hash = hash + (null == this.typeNodes ? 0 : this.typeNodes.hashCode());
      return hash;
   }
}

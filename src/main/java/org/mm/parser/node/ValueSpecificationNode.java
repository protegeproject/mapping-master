package org.mm.parser.node;

import java.util.ArrayList;
import java.util.List;

import org.mm.parser.ASTValueSpecification;
import org.mm.parser.ASTValueSpecificationItem;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ValueSpecificationNode implements MMNode
{
   private final List<ValueSpecificationItemNode> valueSpecificationItemNodes = new ArrayList<>();

   public ValueSpecificationNode(ASTValueSpecification node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);

         if (ParserUtil.hasName(child, "ValueSpecificationItem")) {
            ValueSpecificationItemNode valueSpecificationItem = new ValueSpecificationItemNode(
                  (ASTValueSpecificationItem) child);
            this.valueSpecificationItemNodes.add(valueSpecificationItem);
         } else throw new InternalParseException("invalid child node " + child + " for node " + getNodeName());
      }

      if (this.valueSpecificationItemNodes.isEmpty())
         throw new ParseException("ValueSpecification node must have at least one child");
   }

   public int getNumberOfValueSpecificationItems()
   {
      return this.valueSpecificationItemNodes.size();
   }

   public List<ValueSpecificationItemNode> getValueSpecificationItemNodes()
   {
      return this.valueSpecificationItemNodes;
   }

   @Override
   public String getNodeName()
   {
      return "ValueSpecification";
   }

   public String toString()
   {
      String representation = "";
      if (!this.valueSpecificationItemNodes.isEmpty()) {
         boolean isFirst = true;
         representation += "=";
         if (this.valueSpecificationItemNodes.size() > 1) representation += "(";
         for (ValueSpecificationItemNode valueSpecificationItem : this.valueSpecificationItemNodes) {
            if (!isFirst) representation += ", ";
            representation += valueSpecificationItem.toString();
            isFirst = false;
         }
         if (this.valueSpecificationItemNodes.size() > 1) representation += ")";
      }

      return representation;
   }

   public boolean equals(Object obj)
   {
      if (this == obj) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      ValueSpecificationNode ve = (ValueSpecificationNode) obj;
      return this.valueSpecificationItemNodes != null && ve.valueSpecificationItemNodes != null
            && this.valueSpecificationItemNodes.equals(ve.valueSpecificationItemNodes);
   }

   public int hashCode()
   {
      int hash = 15;
      hash = hash + (null == this.valueSpecificationItemNodes ? 0 : this.valueSpecificationItemNodes.hashCode());
      return hash;
   }
}

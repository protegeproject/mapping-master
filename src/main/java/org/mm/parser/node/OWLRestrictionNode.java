package org.mm.parser.node;

import org.mm.parser.ASTOWLAllValuesFromRestriction;
import org.mm.parser.ASTOWLExactCardinalityRestriction;
import org.mm.parser.ASTOWLHasValueRestriction;
import org.mm.parser.ASTOWLMaxCardinalityRestriction;
import org.mm.parser.ASTOWLMinCardinalityRestriction;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.ASTOWLRestriction;
import org.mm.parser.ASTOWLSomeValuesFromRestriction;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLRestrictionNode implements OWLNode
{
   private OWLPropertyNode propertyNode;
   private OWLMaxCardinalityNode maxCardinalityNode;
   private OWLMinCardinalityNode minCardinalityNode;
   private OWLExactCardinalityNode exactCardinalityNode;
   private OWLHasValueNode hasValueNode;
   private OWLAllValuesFromNode allValuesFromNode;
   private OWLSomeValuesFromNode someValuesFromNode;

   public OWLRestrictionNode(ASTOWLRestriction node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLProperty"))
            this.propertyNode = new OWLPropertyNode((ASTOWLProperty) child);
         else if (ParserUtil.hasName(child, "OWLMaxCardinalityRestriction"))
            this.maxCardinalityNode = new OWLMaxCardinalityNode((ASTOWLMaxCardinalityRestriction) child);
         else if (ParserUtil.hasName(child, "OWLMinCardinalityRestriction"))
            this.minCardinalityNode = new OWLMinCardinalityNode((ASTOWLMinCardinalityRestriction) child);
         else if (ParserUtil.hasName(child, "OWLExactCardinalityRestriction"))
            this.exactCardinalityNode = new OWLExactCardinalityNode((ASTOWLExactCardinalityRestriction) child);
         else if (ParserUtil.hasName(child, "OWLHasValueRestriction"))
            this.hasValueNode = new OWLHasValueNode((ASTOWLHasValueRestriction) child);
         else if (ParserUtil.hasName(child, "OWLAllValuesFromRestriction"))
            this.allValuesFromNode = new OWLAllValuesFromNode((ASTOWLAllValuesFromRestriction) child);
         else if (ParserUtil.hasName(child, "OWLSomeValuesFromRestriction"))
            this.someValuesFromNode = new OWLSomeValuesFromNode((ASTOWLSomeValuesFromRestriction) child);
         else throw new InternalParseException("invalid child node " + child + " for node " + getNodeName());
      }
   }

   public OWLPropertyNode getOWLPropertyNode()
   {
      return this.propertyNode;
   }

   public OWLMaxCardinalityNode getOWLMaxCardinalityNode()
   {
      return this.maxCardinalityNode;
   }

   public OWLMinCardinalityNode getOWLMinCardinalityNode()
   {
      return this.minCardinalityNode;
   }

   public OWLExactCardinalityNode getOWLExactCardinalityNode()
   {
      return this.exactCardinalityNode;
   }

   public OWLHasValueNode getOWLHasValueNode()
   {
      return this.hasValueNode;
   }

   public OWLAllValuesFromNode getOWLAllValuesFromNode()
   {
      return this.allValuesFromNode;
   }

   public OWLSomeValuesFromNode getOWLSomeValuesFromNode()
   {
      return this.someValuesFromNode;
   }

   public boolean isOWLMaxCardinality()
   {
      return this.maxCardinalityNode != null;
   }

   public boolean isOWLMinCardinality()
   {
      return this.minCardinalityNode != null;
   }

   public boolean isOWLExactCardinality()
   {
      return this.exactCardinalityNode != null;
   }

   public boolean isOWLHasValue()
   {
      return this.hasValueNode != null;
   }

   public boolean isOWLAllValuesFrom()
   {
      return this.allValuesFromNode != null;
   }

   public boolean isOWLSomeValuesFrom()
   {
      return this.someValuesFromNode != null;
   }

   @Override
   public String getNodeName()
   {
      return "OWLRestriction";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      String representation = "(" + getOWLPropertyNode() + " ";
      if (this.minCardinalityNode != null)
         representation += this.minCardinalityNode.toString();
      else if (this.maxCardinalityNode != null)
         representation += this.maxCardinalityNode.toString();
      else if (this.exactCardinalityNode != null)
         representation += this.exactCardinalityNode.toString();
      else if (this.hasValueNode != null)
         representation += this.hasValueNode.toString();
      else if (this.allValuesFromNode != null)
         representation += this.allValuesFromNode.toString();
      else if (this.someValuesFromNode != null) representation += this.someValuesFromNode.toString();
      representation += ")";
      return representation;
   }
}

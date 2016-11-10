package org.mm.parser.node;

import org.mm.parser.ASTFact;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.ASTOWLPropertyAssertionObject;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class FactNode implements OWLNode // TODO: Rename to OWLPropertyAssertionNode
{
   private OWLPropertyNode propertyNode;
   private OWLPropertyAssertionNode propertyAssertionObjectNode;

   public FactNode(ASTFact node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLProperty"))
            this.propertyNode = new OWLPropertyNode((ASTOWLProperty) child);
         else if (ParserUtil.hasName(child, "OWLPropertyAssertionObject"))
            this.propertyAssertionObjectNode = new OWLPropertyAssertionNode(
                  (ASTOWLPropertyAssertionObject) child);
         else throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
      }
   }

   public OWLPropertyNode getOWLPropertyNode()
   {
      return this.propertyNode;
   }

   public OWLPropertyAssertionNode getOWLPropertyAssertionObjectNode()
   {
      return this.propertyAssertionObjectNode;
   }

   @Override
   public String getNodeName()
   {
      return "Fact";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      return this.propertyNode + " " + this.propertyAssertionObjectNode;
   }
}

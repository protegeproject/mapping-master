package org.mm.parser.node;

import java.util.ArrayList;
import java.util.List;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLIntersectionClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLIntersectionClassNode implements OWLNode
{
   private final List<OWLClassExpressionNode> classExpressionNodes;

   public OWLIntersectionClassNode(ASTOWLIntersectionClass node) throws ParseException
   {
      this.classExpressionNodes = new ArrayList<>();

      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);

         if (ParserUtil.hasName(child, "OWLClassExpression")) {
            OWLClassExpressionNode owlClassExpression = new OWLClassExpressionNode((ASTOWLClassExpression) child);
            this.classExpressionNodes.add(owlClassExpression);
         } else
            throw new InternalParseException(getNodeName() + " node expecting OWLClassExpression child, got " + child);
      }
   }

   public List<OWLClassExpressionNode> getOWLClassExpressionNodes()
   {
      return this.classExpressionNodes;
   }

   @Override
   public String getNodeName()
   {
      return "OWLIntersectionClass";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      String representation = "";

      if (this.classExpressionNodes.size() == 1)
         representation = this.classExpressionNodes.get(0).toString();
      else {
         boolean isFirst = true;

         representation += "(";
         for (OWLClassExpressionNode owlClassExpression : this.classExpressionNodes) {
            if (!isFirst) representation += " AND ";
            representation += owlClassExpression.toString();
            isFirst = false;
         }
         representation += ")";
      }
      return representation;
   }
}

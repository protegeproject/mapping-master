package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLEquivalentClasses;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLEquivalentClassesNode implements OWLNode // TODO: Rename to OWLEquivalentNode
{
   private final List<OWLClassExpressionNode> classExpressionNodes;

   public OWLEquivalentClassesNode(ASTOWLEquivalentClasses node) throws ParseException
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

   public List<OWLClassExpressionNode> getClassExpressionNodes()
   {
      return this.classExpressionNodes;
   }

   @Override
   public String getNodeName()
   {
      return "OWLClassEquivalentTo";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      String representation = " EquivalentTo: ";
      if (this.classExpressionNodes.size() == 1)
         representation += this.classExpressionNodes.get(0).toString();
      else {
         boolean isFirst = true;
         for (OWLClassExpressionNode owlClassExpression : this.classExpressionNodes) {
            if (!isFirst) representation += ", ";
            representation += owlClassExpression.toString();
            isFirst = false;
         }
      }
      return representation;
   }
}

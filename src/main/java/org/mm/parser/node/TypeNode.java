package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTType;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class TypeNode implements OWLNode { // TODO: Rename to OWLClassAssertionNode

   private OWLClassExpressionNode classExpressionNode;

   public TypeNode(ASTType node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLClassExpression")) {
            this.classExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression) child);
         }
         else {
            throw new InternalParseException("unexpected child node  " + child + " for node " + getNodeName());
         }
      }
   }

   public OWLClassExpressionNode getClassExpressionNode()
   {
      return classExpressionNode;
   }

   @Override
   public String getNodeName()
   {
      return "Type";
   }

   @Override
   public void accept(OWLNodeVisitor visitor)
   {
      visitor.visit(this);
   }

   public String toString()
   {
      return classExpressionNode.toString();
   }
}

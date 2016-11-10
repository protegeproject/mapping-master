package org.mm.parser.node;

import org.mm.parser.ASTOWLNamedIndividual;
import org.mm.parser.ASTOWLSameAs;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLSameAsNode implements OWLNode
{
   private final List<OWLNamedIndividualNode> namedIndividualNodes;

   public OWLSameAsNode(ASTOWLSameAs node) throws ParseException
   {
      this.namedIndividualNodes = new ArrayList<>();

      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLNamedIndividual")) {
            OWLNamedIndividualNode owlIndividual = new OWLNamedIndividualNode((ASTOWLNamedIndividual) child);
            this.namedIndividualNodes.add(owlIndividual);
         } else throw new InternalParseException(getNodeName() + "node expecting OWLNamedIndividual child, got " + child);
      }
   }

   public List<OWLNamedIndividualNode> getIndividualNodes()
   {
      return this.namedIndividualNodes;
   }

   @Override
   public String getNodeName()
   {
      return "OWLSameAs";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      String representation = " SameAs: ";
      if (this.namedIndividualNodes.size() == 1)
         representation += this.namedIndividualNodes.get(0).toString();
      else {
         boolean isFirst = true;
         for (OWLNamedIndividualNode namedIndividualNode : this.namedIndividualNodes) {
            if (!isFirst) representation += ", ";
            representation += namedIndividualNode.toString();
            isFirst = false;
         }
      }
      return representation;
   }
}

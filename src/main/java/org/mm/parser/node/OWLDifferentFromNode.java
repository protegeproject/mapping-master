package org.mm.parser.node;

import org.mm.parser.ASTOWLDifferentFrom;
import org.mm.parser.ASTOWLNamedIndividual;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLDifferentFromNode implements MMNode
{
   private final List<OWLNamedIndividualNode> namedIndividualNodes;

   public OWLDifferentFromNode(ASTOWLDifferentFrom node) throws ParseException
   {
      this.namedIndividualNodes = new ArrayList<>();

      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);

         if (ParserUtil.hasName(child, "OWLNamedIndividual")) {
            OWLNamedIndividualNode owlIndividual = new OWLNamedIndividualNode((ASTOWLNamedIndividual) child);
            this.namedIndividualNodes.add(owlIndividual);
         } else
            throw new InternalParseException("OWLDifferentFrom node expecting OWLNamedIndividual child, got " + child);
      }
   }

   public List<OWLNamedIndividualNode> getIndividualNodes()
   {
      return this.namedIndividualNodes;
   }

   @Override
   public String getNodeName()
   {
      return "OWLDifferentFrom";
   }

   public String toString()
   {
      String representation = " DifferentFrom: ";
      if (this.namedIndividualNodes.size() == 1)
         representation += this.namedIndividualNodes.get(0).toString();
      else {
         boolean isFirst = true;
         for (OWLNamedIndividualNode owlIndividual : this.namedIndividualNodes) {
            if (!isFirst) representation += ", ";
            representation += owlIndividual.toString();
            isFirst = false;
         }
      }
      return representation;
   }
}

package org.mm.parser.node;

import org.mm.parser.ASTOWLIntersectionClass;
import org.mm.parser.ASTOWLUnionClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OWLUnionClassNode implements OWLNode
{
   private final List<OWLIntersectionClassNode> intersectionClassNodes;

   public OWLUnionClassNode(ASTOWLUnionClass node) throws ParseException
   {
      this.intersectionClassNodes = new ArrayList<>();

      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);

         if (ParserUtil.hasName(child, "OWLIntersectionClass")) {
            OWLIntersectionClassNode owlIntersectionClass = new OWLIntersectionClassNode((ASTOWLIntersectionClass) child);
            this.intersectionClassNodes.add(owlIntersectionClass);
         } else throw new InternalParseException(getNodeName() + "node expecting OWLIntersectionClass child, got " + child);
      }
   }

   public List<OWLIntersectionClassNode> getOWLIntersectionClassNodes()
   {
      return Collections.unmodifiableList(this.intersectionClassNodes);
   }

   @Override
   public String getNodeName()
   {
      return "OWLUnionClass";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      String representation = "";
      if (this.intersectionClassNodes.size() == 1)
         representation = this.intersectionClassNodes.get(0).toString();
      else {
         boolean isFirst = true;
         representation += "(";
         for (OWLIntersectionClassNode intersectionClass : this.intersectionClassNodes) {
            if (!isFirst) representation += " OR ";
            representation += intersectionClass.toString();
            isFirst = false;
         }
         representation += ")";
      }
      return representation;
   }
}

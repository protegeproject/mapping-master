package org.mm.parser.node;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTOWLAnnotationProperty;
import org.mm.parser.ASTOWLAnnotationValue;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class AnnotationFactNode implements MMNode
{
   private OWLAnnotationValueNode owlAnnotationValueNode;
   private OWLAnnotationPropertyNode owlAnnotationPropertyNode;

   public AnnotationFactNode(ASTAnnotationFact node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLAnnotationProperty"))
            this.owlAnnotationPropertyNode = new OWLAnnotationPropertyNode((ASTOWLAnnotationProperty) child);
         else if (ParserUtil.hasName(child, "OWLAnnotationValue"))
            this.owlAnnotationValueNode = new OWLAnnotationValueNode((ASTOWLAnnotationValue) child);
         else throw new InternalParseException("unexpected child node " + child + " for " + getNodeName());
      }
   }

   public OWLAnnotationPropertyNode getOWLAnnotationPropertyNode()
   {
      return this.owlAnnotationPropertyNode;
   }

   public OWLAnnotationValueNode getOWLAnnotationValueNode()
   {
      return this.owlAnnotationValueNode;
   }

   @Override
   public String getNodeName()
   {
      return "AnnotationFact";
   }

   public String toString()
   {
      return this.owlAnnotationPropertyNode + " " + this.owlAnnotationValueNode;
   }
}

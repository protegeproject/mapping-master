package org.mm.parser.node;

import java.util.ArrayList;
import java.util.List;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTOWLObjectProperty;
import org.mm.parser.ASTOWLObjectPropertyDeclaration;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLObjectPropertyDeclarationNode implements MMNode
{
   private OWLObjectPropertyNode objectPropertyNode;
   private final List<AnnotationFactNode> annotationFactNodes = new ArrayList<>();

   OWLObjectPropertyDeclarationNode(ASTOWLObjectPropertyDeclaration node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLObjectProperty")) {
            this.objectPropertyNode = new OWLObjectPropertyNode((ASTOWLObjectProperty) child);
         } else if (ParserUtil.hasName(child, "AnnotationFact")) {
            AnnotationFactNode fact = new AnnotationFactNode((ASTAnnotationFact) child);
            this.annotationFactNodes.add(fact);
         } else throw new InternalParseException("unknown child " + child + " to node " + getNodeName());
      }
   }

   public OWLObjectPropertyNode getOWLObjectPropertyNode()
   {
      return this.objectPropertyNode;
   }

   public List<AnnotationFactNode> getAnnotationFactNodes()
   {
      return this.annotationFactNodes;
   }

   public boolean hasAnnotationFactNodes()
   {
      return !this.annotationFactNodes.isEmpty();
   }

   @Override
   public String getNodeName()
   {
      return "OWLObjectPropertyDeclaration";
   }

   public String toString()
   {
      String representation = "ObjectProperty: " + this.objectPropertyNode;
      boolean isFirst = true;

      isFirst = true;
      if (hasAnnotationFactNodes()) {
         representation += " Annotations: ";
         for (AnnotationFactNode fact : this.annotationFactNodes) {
            if (!isFirst) representation += ", ";
            representation += fact.toString();
            isFirst = false;
         }
      }

      return representation;
   }

}

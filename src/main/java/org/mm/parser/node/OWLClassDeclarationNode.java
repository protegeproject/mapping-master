package org.mm.parser.node;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTOWLClass;
import org.mm.parser.ASTOWLClassDeclaration;
import org.mm.parser.ASTOWLEquivalentClasses;
import org.mm.parser.ASTOWLSubclassOf;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLClassDeclarationNode implements MMNode
{
   private OWLClassNode classNode;
   private final List<OWLEquivalentClassesNode> equivalentClassesNodes = new ArrayList<>();
   private final List<OWLSubclassOfNode> subclassOfNodes = new ArrayList<>();
   private final List<AnnotationFactNode> annotationFactNodes = new ArrayList<>();

   OWLClassDeclarationNode(ASTOWLClassDeclaration node) throws ParseException
   {
      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLClass")) {
            this.classNode = new OWLClassNode((ASTOWLClass) child);
         } else if (ParserUtil.hasName(child, "OWLEquivalentClasses")) {
            this.equivalentClassesNodes.add(new OWLEquivalentClassesNode((ASTOWLEquivalentClasses) child));
         } else if (ParserUtil.hasName(child, "OWLSubclassOf")) {
            this.subclassOfNodes.add(new OWLSubclassOfNode((ASTOWLSubclassOf) child));
         } else if (ParserUtil.hasName(child, "AnnotationFact")) {
            AnnotationFactNode fact = new AnnotationFactNode((ASTAnnotationFact) child);
            this.annotationFactNodes.add(fact);
         } else throw new InternalParseException("unknown child " + child + " to node " + getNodeName());
      }
   }

   public OWLClassNode getOWLClassNode()
   {
      return this.classNode;
   }

   public List<OWLEquivalentClassesNode> getOWLEquivalentClassesNodes()
   {
      return this.equivalentClassesNodes;
   }

   public List<OWLSubclassOfNode> getOWLSubclassOfNodes()
   {
      return this.subclassOfNodes;
   }

   public List<AnnotationFactNode> getAnnotationFactNodes()
   {
      return this.annotationFactNodes;
   }

   public boolean hasOWLEquivalentClassesNode()
   {
      return !this.equivalentClassesNodes.isEmpty();
   }

   public boolean hasOWLSubclassOfNodes()
   {
      return !this.subclassOfNodes.isEmpty();
   }

   public boolean hasAnnotationFactNodes()
   {
      return !this.annotationFactNodes.isEmpty();
   }

   @Override
   public String getNodeName()
   {
      return "OWLClassDeclaration";
   }

   public String toString()
   {
      String representation = "Class: " + this.classNode;
      boolean isFirst = true;

      if (hasOWLSubclassOfNodes()) {
         representation += " SubclassOf: ";
         for (OWLSubclassOfNode subclassOf : this.subclassOfNodes) {
            representation += subclassOf.toString();
         }
      }

      if (hasOWLEquivalentClassesNode()) {
         representation += " EquivalentTo: ";
         for (OWLEquivalentClassesNode equivalentTo : this.equivalentClassesNodes) {
            representation += equivalentTo.toString();
         }
      }

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

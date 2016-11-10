package org.mm.parser.node;

import java.util.ArrayList;
import java.util.List;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTFact;
import org.mm.parser.ASTOWLDifferentFrom;
import org.mm.parser.ASTOWLIndividualDeclaration;
import org.mm.parser.ASTOWLNamedIndividual;
import org.mm.parser.ASTOWLSameAs;
import org.mm.parser.ASTType;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLIndividualDeclarationNode implements OWLNode
{
   private OWLNamedIndividualNode namedIndividualNode;
   private final List<FactNode> factNodes;
   private final List<TypeNode> typeNodes;
   private final List<AnnotationFactNode> annotationNodes;
   private OWLSameAsNode sameAsNode;
   private OWLDifferentFromNode differentFromNode;

   public OWLIndividualDeclarationNode(ASTOWLIndividualDeclaration node) throws ParseException
   {
      this.factNodes = new ArrayList<>();
      this.typeNodes = new ArrayList<>();
      this.annotationNodes = new ArrayList<>();

      for (int i = 0; i < node.jjtGetNumChildren(); i++) {
         Node child = node.jjtGetChild(i);
         if (ParserUtil.hasName(child, "OWLNamedIndividual"))
            this.namedIndividualNode = new OWLNamedIndividualNode((ASTOWLNamedIndividual) child);
         else if (ParserUtil.hasName(child, "Fact")) {
            FactNode fact = new FactNode((ASTFact) child);
            this.factNodes.add(fact);
         } else if (ParserUtil.hasName(child, "AnnotationFact")) {
            AnnotationFactNode fact = new AnnotationFactNode((ASTAnnotationFact) child);
            this.annotationNodes.add(fact);
         } else if (ParserUtil.hasName(child, "Type")) {
            TypeNode type = new TypeNode((ASTType) child);
            typeNodes.add(type);
         } else if (ParserUtil.hasName(child, "OWLSameAs")) {
            this.sameAsNode = new OWLSameAsNode((ASTOWLSameAs) child);
         } else if (ParserUtil.hasName(child, "OWLDifferentFrom")) {
            this.differentFromNode = new OWLDifferentFromNode((ASTOWLDifferentFrom) child);
         } else throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
      }
   }

   public boolean hasFacts()
   {
      return !this.factNodes.isEmpty();
   }

   public boolean hasAnnotations()
   {
      return !this.annotationNodes.isEmpty();
   }

   public boolean hasTypes()
   {
      return !typeNodes.isEmpty();
   }

   public boolean hasSameAs()
   {
      return this.sameAsNode != null;
   }

   public boolean hasDifferentFrom()
   {
      return this.differentFromNode != null;
   }

   public OWLNamedIndividualNode getOWLIndividualNode()
   {
      return this.namedIndividualNode;
   }

   public List<FactNode> getFactNodes()
   {
      return this.factNodes;
   }

   public List<AnnotationFactNode> getAnnotationNodes()
   {
      return this.annotationNodes;
   }

   public List<TypeNode> getTypeNodes()
   {
      return typeNodes;
   }

   public OWLSameAsNode getOWLSameAsNode()
   {
      return this.sameAsNode;
   }

   public OWLDifferentFromNode getOWLDifferentFromNode()
   {
      return this.differentFromNode;
   }

   @Override
   public String getNodeName()
   {
      return "OWLIndividualDeclaration";
   }

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      String representation = "Individual: " + this.namedIndividualNode;
      boolean isFirst = true;

      if (hasFacts()) {
         representation += " Facts: ";
         for (FactNode fact : this.factNodes) {
            if (!isFirst) representation += ", ";
            representation += fact.toString();
            isFirst = false;
         }
      }

      if (hasTypes()) {
         representation += " Types: ";
         boolean needComma = false;
         for (TypeNode type : typeNodes) {
            if (needComma) {
               representation += ", ";
            }
            representation += type.toString();
            needComma = true;
         }
      }

      isFirst = true;
      if (hasAnnotations()) {
         representation += " Annotations: ";
         for (AnnotationFactNode annotationFact : this.annotationNodes) {
            if (!isFirst) representation += ", ";
            representation += annotationFact.toString();
            isFirst = false;
         }
      }

      if (hasSameAs()) representation += this.sameAsNode.toString();
      if (hasDifferentFrom()) representation += this.differentFromNode.toString();

      return representation;
   }
}

package org.mm.renderer.owl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.parser.NodeType;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationAssertion;
import org.mm.parser.node.ASTClassAssertion;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTDifferentFrom;
import org.mm.parser.node.ASTFact;
import org.mm.parser.node.ASTIndividualDeclaration;
import org.mm.parser.node.ASTIndividualFrame;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTProperty;
import org.mm.parser.node.ASTPropertyAssertion;
import org.mm.parser.node.ASTPropertyFact;
import org.mm.parser.node.ASTPropertyValue;
import org.mm.parser.node.ASTReferencedFact;
import org.mm.parser.node.ASTReferencedProperty;
import org.mm.parser.node.ASTSameAs;
import org.mm.parser.node.Node;
import org.mm.parser.node.SimpleNode;
import org.mm.renderer.CellCursor;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.IndividualIri;
import org.mm.renderer.internal.IndividualName;
import org.mm.renderer.internal.LiteralValue;
import org.mm.renderer.internal.PlainLiteralValue;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.UntypedIri;
import org.mm.renderer.internal.UntypedPrefixedName;
import org.mm.renderer.internal.UntypedValue;
import org.mm.renderer.internal.Value;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class IndividualFrameNodeVisitor extends EntityNodeVisitor {

   private OWLNamedIndividual subject;

   private Set<OWLAxiom> axioms = new HashSet<>();

   protected IndividualFrameNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      super(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }

   public Collection<OWLAxiom> getAxioms() {
      return axioms;
   }

   @Override
   public void visit(ASTIndividualFrame node) {
      visitIndividualDeclarationNode(node);
      visitClassAssertionNode(node);
      visitPropertyAssertionNode(node);
      visitAnnotationAssertionNode(node);
      visitSameAsNode(node);
      visitDifferentFrom(node);
   }

   @Override
   public void visit(ASTIndividualDeclaration node) {
      ASTNamedIndividual individualNode = ParserUtils.getChild(node, NodeType.INDIVIDUAL);
      individualNode.accept(this);
      subject = (OWLNamedIndividual) getEntity();
      if (subject != null) {
         axioms.add(owlFactory.createOWLDeclarationAxiom(subject));
      }
   }

   private void visitIndividualDeclarationNode(ASTIndividualFrame individualSectionNode) {
      ASTIndividualDeclaration individualDeclarationNode = ParserUtils.getChild(
            individualSectionNode,
            NodeType.INDIVIDUAL_DECLARATION);
      individualDeclarationNode.accept(this);
   }

   private void visitClassAssertionNode(ASTIndividualFrame individualSectionNode) {
      final List<ASTClassAssertion> classAssertionNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.CLASS_ASSERTION);
      for (ASTClassAssertion classAssertionNode : classAssertionNodes) {
         visitEachClassAssertionNode(classAssertionNode);
      }
   }

   private void visitEachClassAssertionNode(ASTClassAssertion classAssertionNode) {
      final List<ASTClassExpressionCategory> classExpressionNodes = ParserUtils.getChildren(
            classAssertionNode,
            NodeType.CLASS_EXPRESSION);
      for (ASTClassExpressionCategory classExpressionNode : classExpressionNodes) {
         visitEachClassExpressionNodeForClassAssertionAxiom(classExpressionNode);
      }
   }

   private void visitEachClassExpressionNodeForClassAssertionAxiom(
         ASTClassExpressionCategory classExpressionNode) {
      ClassExpressionNodeVisitor visitor = createNewClassExpressionNodeVisitor();
      visitor.visit(classExpressionNode);
      OWLClassExpression classExpression = visitor.getClassExpression();
      if (classExpression != null && subject != null) {
         axioms.add(owlFactory.createOWLClassAssertionAxiom(classExpression, subject));
      }
   }

   private void visitPropertyAssertionNode(ASTIndividualFrame individualSectionNode) {
      final List<ASTPropertyAssertion> propertyAssertionNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.PROPERTY_ASSERTION);
      for (ASTPropertyAssertion propertyAssertionNode : propertyAssertionNodes) {
         visitPropertyAssertionNode(propertyAssertionNode);
      }
   }

   private void visitPropertyAssertionNode(ASTPropertyAssertion propertyAssertionNode) {
      final List<ASTFact> factNodes = ParserUtils.getChildren(
            propertyAssertionNode,
            NodeType.FACT);
      for (ASTFact factNode : factNodes) {
         factNode.accept(this);
      }
   }

   @Override
   public void visit(ASTFact node) {
      Node factNode = ParserUtils.getChild(node);
      factNode.accept(this);
   }

   @Override
   public void visit(ASTPropertyFact node) {
      OWLEntity property = getProperty(node);
      if (property != null) {
         Value value = getPropertyValue(node);
         if (property.isOWLDataProperty()) {
            if (value instanceof UntypedValue) {
               visitDataPropertyAssertion(property.asOWLDataProperty(), ((UntypedValue) value).asLiteralValue());
            } else {
               visitDataPropertyAssertion(property.asOWLDataProperty(), value);
            }
         } else if (property.isOWLObjectProperty()) {
            if (value instanceof UntypedValue) {
               visitObjectPropertyAssertion(property.asOWLObjectProperty(), ((UntypedValue) value).asIndividualName());
            } else {
               visitObjectPropertyAssertion(property.asOWLObjectProperty(), value);
            }
         }
      }
   }

   @Nullable
   private OWLEntity getProperty(ASTPropertyFact node) {
      ASTProperty propertyNode = ParserUtils.getChild(node, NodeType.PROPERTY);
      propertyNode.accept(this);
      return getEntity();
   }

   private Value getPropertyValue(SimpleNode node) {
      ASTPropertyValue valueNode = ParserUtils.getChild(node, NodeType.PROPERTY_VALUE);
      valueNode.accept(this);
      return getValue();
   }

   private void visitDataPropertyAssertion(OWLDataProperty property, Value value) {
      if (value instanceof LiteralValue) {
         OWLLiteral literal = owlFactory.getOWLTypedLiteral((LiteralValue) value);
         axioms.add(owlFactory.createOWLDataPropertyAssertionAxiom(property, subject, literal));
      } else if (value instanceof PlainLiteralValue) {
         OWLLiteral literal = owlFactory.getOWLPlainLiteral((PlainLiteralValue) value);
         axioms.add(owlFactory.createOWLDataPropertyAssertionAxiom(property, subject, literal));
      }
   }

   private void visitObjectPropertyAssertion(OWLObjectProperty property, Value value) {
      if (value instanceof IndividualName) {
         OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((IndividualName) value);
         axioms.add(owlFactory.createOWLObjectPropertyAssertionAxiom(property, subject, individual));
      } else if (value instanceof IndividualIri) {
         OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((IndividualIri) value);
         axioms.add(owlFactory.createOWLObjectPropertyAssertionAxiom(property, subject, individual));
      } else if (value instanceof UntypedPrefixedName) {
         OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((UntypedPrefixedName) value);
         axioms.add(owlFactory.createOWLObjectPropertyAssertionAxiom(property, subject, individual));
      }  else if (value instanceof UntypedIri) {
         OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((UntypedIri) value);
         axioms.add(owlFactory.createOWLObjectPropertyAssertionAxiom(property, subject, individual));
      } 
   }

   @Override
   public void visit(ASTReferencedFact node) {
      OWLEntity property = getProperty(node);
      if (property != null) {
         Value value = getPropertyValue(node);
         if (property.isOWLDataProperty()) {
            if (value instanceof UntypedValue) {
               visitDataPropertyAssertion(property.asOWLDataProperty(), ((UntypedValue) value).asLiteralValue());
            } else {
               visitDataPropertyAssertion(property.asOWLDataProperty(), value);
            }
         } else if (property.isOWLObjectProperty()) {
            if (value instanceof UntypedValue) {
               visitObjectPropertyAssertion(property.asOWLObjectProperty(), ((UntypedValue) value).asIndividualName());
            } else {
               visitObjectPropertyAssertion(property.asOWLObjectProperty(), value);
            }
         }
      }
   }

   @Nullable
   private OWLEntity getProperty(ASTReferencedFact node) {
      ASTReferencedProperty propertyNode = ParserUtils.getChild(node, NodeType.REFERENCED_PROPERTY);
      propertyNode.accept(this);
      return getEntity();
   }

   private void visitAnnotationAssertionNode(ASTIndividualFrame individualSectionNode) {
      final List<ASTAnnotationAssertion> annotationAssertionNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.ANNOTATION_ASSERTION);
      for (ASTAnnotationAssertion annotationAssertionNode : annotationAssertionNodes) {
         visitEachAnnotationAssertionNode(annotationAssertionNode);
      }
   }

   private void visitEachAnnotationAssertionNode(ASTAnnotationAssertion annotationAssertionNode) {
      final List<ASTAnnotation> annotationNodes = ParserUtils.getChildren(
            annotationAssertionNode,
            NodeType.ANNOTATION);
      AnnotationNodeVisitor visitor = createNewAnnotationNodeVisitor();
      for (ASTAnnotation annotationNode : annotationNodes) {
         visitor.visit(annotationNode);
         OWLAnnotation annotation = visitor.getAnnotation();
         if (annotation != null && subject != null) {
            axioms.add(owlFactory.createOWLAnnotationAssertionAxiom(subject, annotation));
         }
      }
   }

   private void visitSameAsNode(ASTIndividualFrame individualSectionNode) {
      final List<ASTSameAs> sameAsNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.SAME_AS);
      for (ASTSameAs sameAsNode : sameAsNodes) {
         visitEachSameAsNode(sameAsNode);
      }
   }

   private void visitEachSameAsNode(ASTSameAs sameAsNode) {
      final List<ASTNamedIndividual> individualNodes = ParserUtils.getChildren(
            sameAsNode,
            NodeType.INDIVIDUAL);
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      for (ASTNamedIndividual individualNode : individualNodes) {
         visitor.visit(individualNode);
         OWLEntity entity = visitor.getEntity();
         if (entity != null) {
            OWLNamedIndividual individual = entity.asOWLNamedIndividual();
            if (subject != null) {
               axioms.add(owlFactory.createOWLSameIndividualAxiom(subject, individual));
            }
         }
      }
   }

   private void visitDifferentFrom(ASTIndividualFrame individualSectionNode) {
      final List<ASTDifferentFrom> differentFromNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.DIFFERENT_FROM);
      for (ASTDifferentFrom differentFromNode : differentFromNodes) {
         visitEachDifferentFromNode(differentFromNode);
      }
   }

   private void visitEachDifferentFromNode(ASTDifferentFrom differentFromNode) {
      final List<ASTNamedIndividual> individualNodes = ParserUtils.getChildren(
            differentFromNode,
            NodeType.INDIVIDUAL);
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      for (ASTNamedIndividual individualNode : individualNodes) {
         visitor.visit(individualNode);
         OWLEntity entity = visitor.getEntity();
         if (entity != null) {
            OWLNamedIndividual individual = entity.asOWLNamedIndividual();
            if (subject != null) {
               axioms.add(owlFactory.createOWLDifferentIndividualsAxiom(subject, individual));
            }
         }
      }
   }

   private EntityNodeVisitor createNewEntityNodeVisitor() {
      return new EntityNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }

   private ClassExpressionNodeVisitor createNewClassExpressionNodeVisitor() {
      return new ClassExpressionNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }

   private AnnotationNodeVisitor createNewAnnotationNodeVisitor() {
      return new AnnotationNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }
}

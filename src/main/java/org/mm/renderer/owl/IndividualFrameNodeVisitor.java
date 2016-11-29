package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.mm.parser.NodeType;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationAssertion;
import org.mm.parser.node.ASTClassAssertion;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTDataPropertyAssertion;
import org.mm.parser.node.ASTDifferentFrom;
import org.mm.parser.node.ASTIndividualDeclaration;
import org.mm.parser.node.ASTIndividualFrame;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTObjectProperty;
import org.mm.parser.node.ASTObjectPropertyAssertion;
import org.mm.parser.node.ASTPropertyAssertion;
import org.mm.parser.node.ASTSameAs;
import org.mm.renderer.AbstractNodeVisitor;
import org.mm.renderer.internal.Value;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class IndividualFrameNodeVisitor extends AbstractNodeVisitor {

   private final OwlFactory owlFactory;
   private final ValueNodeVisitor valueNodeVisitor;

   private OWLNamedIndividual subject;

   private Set<OWLAxiom> axioms = new HashSet<>();

   protected IndividualFrameNodeVisitor(@Nonnull OwlFactory owlFactory,
         @Nonnull ValueNodeVisitor valueNodeVisitor) {
      super(valueNodeVisitor);
      this.owlFactory = checkNotNull(owlFactory);
      this.valueNodeVisitor = checkNotNull(valueNodeVisitor);
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
   public void visit(ASTNamedIndividual node) {
      Value<?> individualNameValue = getValue(node);
      subject = owlFactory.getOWLNamedIndividual(individualNameValue);
   }

   @Override
   public void visit(ASTIndividualDeclaration node) {
      ASTNamedIndividual individualNode = ParserUtils.getChild(node, NodeType.INDIVIDUAL);
      individualNode.accept(this);
      axioms.add(owlFactory.createOWLDeclarationAxiom(subject));
   }

   private void visitIndividualDeclarationNode(ASTIndividualFrame individualSectionNode) {
      ASTIndividualDeclaration individualDeclarationNode = ParserUtils.getChild(
            individualSectionNode,
            NodeType.INDIVIDUAL_DECLARATION);
      individualDeclarationNode.accept(this);
   }

   private void visitClassAssertionNode(ASTIndividualFrame individualSectionNode) {
      final Set<ASTClassAssertion> classAssertionNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.CLASS_ASSERTION);
      for (ASTClassAssertion classAssertionNode : classAssertionNodes) {
         visitEachClassAssertionNode(classAssertionNode);
      }
   }

   private void visitEachClassAssertionNode(ASTClassAssertion classAssertionNode) {
      final Set<ASTClassExpressionCategory> classExpressionNodes = ParserUtils.getChildren(
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
      axioms.add(owlFactory.createOWLClassAssertionAxiom(classExpression, subject));
   }

   private void visitPropertyAssertionNode(ASTIndividualFrame individualSectionNode) {
      final Set<ASTPropertyAssertion> propertyAssertionNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.PROPERTY_ASSERTION);
      for (ASTPropertyAssertion propertyAssertionNode : propertyAssertionNodes) {
         visitEachPropertyAssertionNode(propertyAssertionNode);
      }
   }

   private void visitEachPropertyAssertionNode(ASTPropertyAssertion propertyAssertionNode) {
      visitDataPropertyAssertionNode(propertyAssertionNode);
      visitObjectPropertyAssertionNode(propertyAssertionNode);
   }

   private void visitDataPropertyAssertionNode(ASTPropertyAssertion propertyAssertionNode) {
      Set<ASTDataPropertyAssertion> propertyAssertionNodes = ParserUtils.getChildren(
            propertyAssertionNode,
            NodeType.DATA_PROPERTY_ASSERTION);
      for (ASTDataPropertyAssertion dataPropertyAssertionNode : propertyAssertionNodes) {
         dataPropertyAssertionNode.accept(this);
      }
   }

   @Override
   public void visit(ASTDataPropertyAssertion node) {
      OWLDataProperty property = visitDataPropertyNode(node);
      OWLLiteral literal = visitDataPropertyValueNode(node);
      axioms.add(owlFactory.createOWLDataPropertyAssertionAxiom(property, subject, literal));
   }

   private OWLDataProperty visitDataPropertyNode(ASTDataPropertyAssertion dataPropertyAssertionNode) {
      ASTDataProperty propertyNode = ParserUtils.getChild(
            dataPropertyAssertionNode,
            NodeType.DATA_PROPERTY);
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      visitor.visit(propertyNode);
      return visitor.getEntity().asOWLDataProperty();
   }

   private OWLLiteral visitDataPropertyValueNode(ASTDataPropertyAssertion dataPropertyAssertionNode) {
      Value<?> value = getLiteralValue(dataPropertyAssertionNode);
      return owlFactory.getOWLLiteral(value);
   }

   private void visitObjectPropertyAssertionNode(ASTPropertyAssertion propertyAssertionNode) {
      Set<ASTObjectPropertyAssertion> propertyAssertionNodes = ParserUtils.getChildren(
            propertyAssertionNode,
            NodeType.OBJECT_PROPERTY_ASSERTION);
      for (ASTObjectPropertyAssertion objectPropertyAssertionNode : propertyAssertionNodes) {
         objectPropertyAssertionNode.accept(this);
      }
   }

   @Override
   public void visit(ASTObjectPropertyAssertion node) {
      OWLObjectProperty property = visitObjectPropertyNode(node);
      OWLNamedIndividual individual = visitObjectPropertyValueNode(node);
      axioms.add(owlFactory.createOWLObjectPropertyAssertionAxiom(property, subject, individual));
   }

   private OWLObjectProperty visitObjectPropertyNode(ASTObjectPropertyAssertion objectPropertyAssertionNode) {
      ASTObjectProperty propertyNode = ParserUtils.getChild(
            objectPropertyAssertionNode,
            NodeType.OBJECT_PROPERTY);
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      visitor.visit(propertyNode);
      return visitor.getEntity().asOWLObjectProperty();
   }

   private OWLNamedIndividual visitObjectPropertyValueNode(ASTObjectPropertyAssertion objectPropertyAssertionNode) {
      Value<?> value = getObjectValue(objectPropertyAssertionNode);
      return owlFactory.getOWLNamedIndividual(value);
   }

   private void visitAnnotationAssertionNode(ASTIndividualFrame individualSectionNode) {
      final Set<ASTAnnotationAssertion> annotationAssertionNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.ANNOTATION_ASSERTION);
      for (ASTAnnotationAssertion annotationAssertionNode : annotationAssertionNodes) {
         visitEachAnnotationAssertionNode(annotationAssertionNode);
      }
   }

   private void visitEachAnnotationAssertionNode(ASTAnnotationAssertion annotationAssertionNode) {
      final Set<ASTAnnotation> annotationNodes = ParserUtils.getChildren(
            annotationAssertionNode,
            NodeType.ANNOTATION);
      AnnotationNodeVisitor visitor = createNewAnnotationNodeVisitor();
      for (ASTAnnotation annotationNode : annotationNodes) {
         visitor.visit(annotationNode);
         OWLAnnotation annotation = visitor.getAnnotation();
         axioms.add(owlFactory.createOWLAnnotationAssertionAxiom(subject, annotation));
      }
   }

   private void visitSameAsNode(ASTIndividualFrame individualSectionNode) {
      final Set<ASTSameAs> sameAsNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.SAME_AS);
      for (ASTSameAs sameAsNode : sameAsNodes) {
         visitEachSameAsNode(sameAsNode);
      }
   }

   private void visitEachSameAsNode(ASTSameAs sameAsNode) {
      final Set<ASTNamedIndividual> individualNodes = ParserUtils.getChildren(
            sameAsNode,
            NodeType.INDIVIDUAL);
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      for (ASTNamedIndividual individualNode : individualNodes) {
         visitor.visit(individualNode);
         OWLNamedIndividual individual = visitor.getEntity().asOWLNamedIndividual();
         axioms.add(owlFactory.createOWLSameIndividualAxiom(subject, individual));
      }
   }

   private void visitDifferentFrom(ASTIndividualFrame individualSectionNode) {
      final Set<ASTDifferentFrom> differentFromNodes = ParserUtils.getChildren(
            individualSectionNode,
            NodeType.DIFFERENT_FROM);
      for (ASTDifferentFrom differentFromNode : differentFromNodes) {
         visitEachDifferentFromNode(differentFromNode);
      }
   }

   private void visitEachDifferentFromNode(ASTDifferentFrom differentFromNode) {
      final Set<ASTNamedIndividual> individualNodes = ParserUtils.getChildren(
            differentFromNode,
            NodeType.INDIVIDUAL);
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      for (ASTNamedIndividual individualNode : individualNodes) {
         visitor.visit(individualNode);
         OWLNamedIndividual individual = visitor.getEntity().asOWLNamedIndividual();
         axioms.add(owlFactory.createOWLDifferentIndividualsAxiom(subject, individual));
      }
   }

   private EntityNodeVisitor createNewEntityNodeVisitor() {
      return new EntityNodeVisitor(owlFactory, valueNodeVisitor);
   }

   private ClassExpressionNodeVisitor createNewClassExpressionNodeVisitor() {
      return new ClassExpressionNodeVisitor(owlFactory, valueNodeVisitor);
   }

   private AnnotationNodeVisitor createNewAnnotationNodeVisitor() {
      return new AnnotationNodeVisitor(owlFactory, valueNodeVisitor);
   }
}

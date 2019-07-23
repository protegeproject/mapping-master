package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import org.mm.parser.NodeType;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTDataAllValuesFrom;
import org.mm.parser.node.ASTDataExactCardinality;
import org.mm.parser.node.ASTDataHasValue;
import org.mm.parser.node.ASTDataMaxCardinality;
import org.mm.parser.node.ASTDataMinCardinality;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTDataSomeValuesFrom;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTObjectAllValuesFrom;
import org.mm.parser.node.ASTObjectComplement;
import org.mm.parser.node.ASTObjectExactCardinality;
import org.mm.parser.node.ASTObjectHasValue;
import org.mm.parser.node.ASTObjectIntersection;
import org.mm.parser.node.ASTObjectMaxCardinality;
import org.mm.parser.node.ASTObjectMinCardinality;
import org.mm.parser.node.ASTObjectOneOf;
import org.mm.parser.node.ASTObjectProperty;
import org.mm.parser.node.ASTObjectSomeValuesFrom;
import org.mm.parser.node.ASTObjectUnion;
import org.mm.parser.node.Node;
import org.mm.parser.node.SimpleNode;
import org.mm.renderer.AbstractNodeVisitor;
import org.mm.renderer.internal.Value;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ClassExpressionNodeVisitor extends AbstractNodeVisitor {

   private final OwlFactory owlFactory;
   private final ValueNodeVisitor valueNodeVisitor;

   private OWLClassExpression classExpression;

   public ClassExpressionNodeVisitor(@Nonnull OwlFactory owlFactory,
         @Nonnull ValueNodeVisitor valueNodeVisitor) {
      super(valueNodeVisitor);
      this.owlFactory = checkNotNull(owlFactory);
      this.valueNodeVisitor = checkNotNull(valueNodeVisitor);
   }

   public OWLClassExpression getClassExpression() {
      return classExpression;
   }

   @Override
   public void visit(ASTClassExpressionCategory node) {
      Node classExpressionNode = ParserUtils.getChild(node);
      classExpressionNode.accept(this);
   }

   @Override
   public void visit(ASTClass classNode) {
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      visitor.visit(classNode);
      classExpression = visitor.getEntity().asOWLClass();
   }

   @Override
   public void visit(ASTObjectUnion node) {
      Set<OWLClassExpression> classExpressions = new HashSet<>();
      for (ASTObjectIntersection objectIntersectionNode
            : ParserUtils.getChildren(node, NodeType.OBJECT_INTERSECTION)) {
         OWLClassExpression ce = visitInnerObjectIntersectionNode(objectIntersectionNode);
         classExpressions.add(ce);
      }
      classExpression = owlFactory.createOWLObjectUnionOf(classExpressions);
   }

   @Override
   public void visit(ASTObjectOneOf node) {
      Set<OWLNamedIndividual> individuals = getOWLIndividuals(node);
      owlFactory.createOWLObjectOneOf(individuals);
   }

   @Override
   public void visit(ASTObjectIntersection node) {
      Set<OWLClassExpression> classExpressions = new HashSet<>();
      for (ASTClassExpressionCategory classExpressionNode
            : ParserUtils.getChildren(node, NodeType.CLASS_EXPRESSION)) {
         OWLClassExpression ce = visitInnerClassExpressionNode(classExpressionNode);
         classExpressions.add(ce);
      }
      classExpression = owlFactory.createOWLObjectIntersectionOf(classExpressions);
   }

   @Override
   public void visit(ASTObjectComplement node) {
      OWLClassExpression innerClassExpression = getOWLClassExpression(node);
      classExpression = owlFactory.createOWLObjectComplementOf(innerClassExpression);
   }

   @Override
   public void visit(ASTDataExactCardinality node) {
      OWLDataProperty property = getOWLDataProperty(node);
      int cardinality = node.getCardinality();
      classExpression = owlFactory.createOWLDataExactCardinality(
            cardinality,
            property,
            DatatypeUtils.getOWLDatatype(node.getDatatype()));
   }

   @Override
   public void visit(ASTObjectExactCardinality node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      int cardinality = node.getCardinality();
      if (node.hasExplicitClassExpression()) {
         OWLClassExpression innerClassExpression = getOWLClassExpression(node);
         classExpression = owlFactory.createOWLObjectExactCardinality(
               cardinality,
               property,
               innerClassExpression);
      } else {
         classExpression = owlFactory.createOWLObjectExactCardinality(
               cardinality,
               property);
      }
   }

   @Override
   public void visit(ASTDataMaxCardinality node) {
      OWLDataProperty property = getOWLDataProperty(node);
      int cardinality = node.getCardinality();
      classExpression = owlFactory.createOWLDataMaxCardinality(
            cardinality,
            property,
            DatatypeUtils.getOWLDatatype(node.getDatatype()));
   }

   @Override
   public void visit(ASTObjectMaxCardinality node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      int cardinality = node.getCardinality();
      if (node.hasExplicitClassExpression()) {
         OWLClassExpression innerClassExpression = getOWLClassExpression(node);
         classExpression = owlFactory.createOWLObjectMaxCardinality(
               cardinality,
               property,
               innerClassExpression);
      } else {
         classExpression = owlFactory.createOWLObjectMaxCardinality(
               cardinality,
               property);
      }
   }

   @Override
   public void visit(ASTDataMinCardinality node) {
      OWLDataProperty property = getOWLDataProperty(node);
      int cardinality = node.getCardinality();
      classExpression = owlFactory.createOWLDataMinCardinality(
            cardinality,
            property,
            DatatypeUtils.getOWLDatatype(node.getDatatype()));
   }

   @Override
   public void visit(ASTObjectMinCardinality node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      int cardinality = node.getCardinality();
      if (node.hasExplicitClassExpression()) {
         OWLClassExpression innerClassExpression = getOWLClassExpression(node);
         classExpression = owlFactory.createOWLObjectMinCardinality(
               cardinality,
               property,
               innerClassExpression);
      } else {
         classExpression = owlFactory.createOWLObjectMinCardinality(
               cardinality,
               property);
      }
   }

   @Override
   public void visit(ASTDataHasValue node) {
      OWLDataProperty property = getOWLDataProperty(node);
      Value value = getLiteralValue(node);
      OWLLiteral literal = owlFactory.getOWLLiteral(value);
      classExpression = owlFactory.createOWLDataHasValue(property, literal);
   }

   @Override
   public void visit(ASTObjectHasValue node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      Value value = getObjectValue(node);
      OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual(value);
      classExpression = owlFactory.createOWLObjectHasValue(property, individual);
   }

   @Override
   public void visit(ASTDataAllValuesFrom node) {
      OWLDataProperty property = getOWLDataProperty(node);
      OWLDatatype datatype = DatatypeUtils.getOWLDatatype(node.getDatatype());
      classExpression = owlFactory.createOWLDataAllValuesFrom(property, datatype);
   }

   @Override
   public void visit(ASTObjectAllValuesFrom node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      OWLClassExpression innerClassExpression = getOWLClassExpression(node);
      classExpression = owlFactory.createOWLObjectAllValuesFrom(property, innerClassExpression);
   }

   @Override
   public void visit(ASTDataSomeValuesFrom node) {
      OWLDataProperty property = getOWLDataProperty(node);
      OWLDatatype datatype = DatatypeUtils.getOWLDatatype(node.getDatatype());
      classExpression = owlFactory.createOWLDataSomeValuesFrom(property, datatype);
   }

   @Override
   public void visit(ASTObjectSomeValuesFrom node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      OWLClassExpression innerClassExpression = getOWLClassExpression(node);
      classExpression = owlFactory.createOWLObjectSomeValuesFrom(property, innerClassExpression);
   }

   private OWLDataProperty getOWLDataProperty(SimpleNode node) {
      ASTDataProperty propertyNode = ParserUtils.getChild(
            node,
            NodeType.DATA_PROPERTY);
      return visitDataPropertyNode(propertyNode);
   }

   private OWLDataProperty visitDataPropertyNode(ASTDataProperty propertyNode) {
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      visitor.visit(propertyNode);
      return visitor.getEntity().asOWLDataProperty();
   }

   private OWLObjectProperty getOWLObjectProperty(SimpleNode node) {
      ASTObjectProperty propertyNode = ParserUtils.getChild(
            node,
            NodeType.OBJECT_PROPERTY);
      return visitObjectPropertyNode(propertyNode);
   }

   private OWLObjectProperty visitObjectPropertyNode(ASTObjectProperty propertyNode) {
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      visitor.visit(propertyNode);
      return visitor.getEntity().asOWLObjectProperty();
   }

   private Set<OWLNamedIndividual> getOWLIndividuals(SimpleNode node) {
      final Set<ASTNamedIndividual> individualNodes = ParserUtils.getChildren(
            node,
            NodeType.INDIVIDUAL);
      Set<OWLNamedIndividual> individuals = new HashSet<>();
      for (ASTNamedIndividual individualNode : individualNodes) {
         OWLNamedIndividual ind = visitIndividualNode(individualNode);
         individuals.add(ind);
      }
      return individuals;
   }

   private OWLNamedIndividual visitIndividualNode(ASTNamedIndividual individualNode) {
      EntityNodeVisitor visitor = createNewEntityNodeVisitor();
      visitor.visit(individualNode);
      return visitor.getEntity().asOWLNamedIndividual();
   }

   private OWLClassExpression getOWLClassExpression(SimpleNode node) {
      ASTClassExpressionCategory classExpressionNode = ParserUtils.getChild(
            node,
            NodeType.CLASS_EXPRESSION);
      return visitInnerClassExpressionNode(classExpressionNode);
   }

   private OWLClassExpression visitInnerObjectIntersectionNode(ASTObjectIntersection objectIntersectionNode) {
      ClassExpressionNodeVisitor innerVisitor = createNewClassExpressionNodeVisitor();
      innerVisitor.visit(objectIntersectionNode);
      return innerVisitor.getClassExpression();
   }

   private OWLClassExpression visitInnerClassExpressionNode(ASTClassExpressionCategory classExpressionNode) {
      ClassExpressionNodeVisitor innerVisitor = createNewClassExpressionNodeVisitor();
      innerVisitor.visit(classExpressionNode);
      return innerVisitor.getClassExpression();
   }

   private EntityNodeVisitor createNewEntityNodeVisitor() {
      return new EntityNodeVisitor(owlFactory, valueNodeVisitor);
   }

   private ClassExpressionNodeVisitor createNewClassExpressionNodeVisitor() {
      return new ClassExpressionNodeVisitor(owlFactory, valueNodeVisitor);
   }
}

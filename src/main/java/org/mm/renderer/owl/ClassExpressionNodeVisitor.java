package org.mm.renderer.owl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.parser.NodeType;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTCardinalityValue;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTDataAllValuesFrom;
import org.mm.parser.node.ASTDataExactCardinality;
import org.mm.parser.node.ASTDataHasValue;
import org.mm.parser.node.ASTDataMaxCardinality;
import org.mm.parser.node.ASTDataMinCardinality;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTDataSomeValuesFrom;
import org.mm.parser.node.ASTLiteralValue;
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
import org.mm.parser.node.ASTObjectValue;
import org.mm.parser.node.ASTProperty;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTUntypedExactCardinality;
import org.mm.parser.node.ASTUntypedHasValue;
import org.mm.parser.node.ASTUntypedMaxCardinality;
import org.mm.parser.node.ASTUntypedMinCardinality;
import org.mm.parser.node.Node;
import org.mm.parser.node.SimpleNode;
import org.mm.renderer.CellCursor;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.IndividualIri;
import org.mm.renderer.internal.IndividualName;
import org.mm.renderer.internal.LiteralValue;
import org.mm.renderer.internal.PlainLiteralValue;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.UntypedValue;
import org.mm.renderer.internal.Value;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ClassExpressionNodeVisitor extends EntityNodeVisitor {

   private OWLClassExpression classExpression;

   public ClassExpressionNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      super(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }

   @Nullable
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
      super.visit(classNode);
      OWLEntity entity = getEntity();
      if (entity != null) {
         classExpression = entity.asOWLClass();
      }
   }

   @Override
   public void visit(ASTObjectUnion node) {
      Set<OWLClassExpression> classExpressions = new HashSet<>();
      for (ASTObjectIntersection objectIntersectionNode
            : ParserUtils.getChildren(node, NodeType.OBJECT_INTERSECTION)) {
         OWLClassExpression ce = visitInnerObjectIntersectionNode(objectIntersectionNode);
         if (ce != null) {
            classExpressions.add(ce);
         }
      }
      if (classExpressions.size() > 1) {
         classExpression = owlFactory.createOWLObjectUnionOf(classExpressions);
      }
      classExpression = owlFactory.createOWLObjectUnionOf(classExpressions);
   }

   @Override
   public void visit(ASTObjectOneOf node) {
      Set<OWLNamedIndividual> individuals = getOWLIndividuals(node);
      if (individuals.size() > 1) {
         classExpression = owlFactory.createOWLObjectOneOf(individuals);
      }
   }

   @Override
   public void visit(ASTObjectIntersection node) {
      Set<OWLClassExpression> classExpressions = new HashSet<>();
      for (ASTClassExpressionCategory classExpressionNode
            : ParserUtils.getChildren(node, NodeType.CLASS_EXPRESSION)) {
         OWLClassExpression ce = visitInnerClassExpressionNode(classExpressionNode);
         if (ce != null) {
            classExpressions.add(ce);
         }
      }
      if (classExpressions.size() > 1) {
         classExpression = owlFactory.createOWLObjectIntersectionOf(classExpressions);
      }
      classExpression = owlFactory.createOWLObjectIntersectionOf(classExpressions);
   }

   @Override
   public void visit(ASTObjectComplement node) {
      OWLClassExpression innerClassExpression = getOWLClassExpression(node);
      if (innerClassExpression != null) {
         classExpression = owlFactory.createOWLObjectComplementOf(innerClassExpression);
      }
   }

   @Override
   public void visit(ASTDataExactCardinality node) {
      OWLDataProperty property = getOWLDataProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         classExpression = owlFactory.createOWLDataExactCardinality(
               cardinality,
               property,
               DatatypeUtils.getOWLDatatype(node.getDatatype()));
      }
   }

   private int getCardinality(SimpleNode node) {
      ASTCardinalityValue valueNode = ParserUtils.getChild(node, NodeType.CARDINALITY_VALUE);
      ValueNodeVisitor visitor = new ValueNodeVisitor(referenceResolver, builtInFunctionHandler, cellCursor);
      visitor.visit(valueNode);
      Value v = visitor.getValue();
      return Integer.parseInt(v.getString());
   }

   @Override
   public void visit(ASTObjectExactCardinality node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (node.hasExplicitClassExpression()) {
            OWLClassExpression innerClassExpression = getOWLClassExpression(node);
            if (innerClassExpression != null) {
               classExpression = owlFactory.createOWLObjectExactCardinality(
                     cardinality,
                     property,
                     innerClassExpression);
            }
         } else {
            classExpression = owlFactory.createOWLObjectExactCardinality(
                  cardinality,
                  property);
         }
      }
   }

   @Override
   public void visit(ASTUntypedExactCardinality node) {
      OWLEntity property = getOWLProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         Value filler = getFiller(node);
         if (property.isOWLDataProperty()) {
            classExpression = owlFactory.createOWLDataExactCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  owlFactory.createOWLDatatype(filler.getString()));
         } else if (property.isOWLObjectProperty()) {
            classExpression = owlFactory.createOWLObjectExactCardinality(
                  cardinality,
                  property.asOWLObjectProperty(),
                  owlFactory.createOWLClass(filler.getString()));
         }
      }
   }

   @Nullable
   private OWLEntity getOWLProperty(SimpleNode node) {
      ASTProperty propertyNode = ParserUtils.getChild(node, NodeType.PROPERTY);
      propertyNode.accept(this);
      return getEntity();
   }

   private Value getFiller(SimpleNode node) {
      ASTReference valueNode = ParserUtils.getChild(node, NodeType.REFERENCE);
      valueNode.accept(this);
      return getValue();
   }

   @Override
   public void visit(ASTDataMaxCardinality node) {
      OWLDataProperty property = getOWLDataProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         classExpression = owlFactory.createOWLDataMaxCardinality(
               cardinality,
               property,
               DatatypeUtils.getOWLDatatype(node.getDatatype()));
      }
   }

   @Override
   public void visit(ASTObjectMaxCardinality node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (node.hasExplicitClassExpression()) {
            OWLClassExpression innerClassExpression = getOWLClassExpression(node);
            if (innerClassExpression != null) {
               classExpression = owlFactory.createOWLObjectMaxCardinality(
                     cardinality,
                     property,
                     innerClassExpression);
            }
         } else {
            classExpression = owlFactory.createOWLObjectMaxCardinality(
                  cardinality,
                  property);
         }
      }
   }

   @Override
   public void visit(ASTUntypedMaxCardinality node) {
      OWLEntity property = getOWLProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         Value filler = getFiller(node);
         if (property.isOWLDataProperty()) {
            classExpression = owlFactory.createOWLDataMaxCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  owlFactory.createOWLDatatype(filler.getString()));
         } else if (property.isOWLObjectProperty()) {
            classExpression = owlFactory.createOWLObjectMaxCardinality(
                  cardinality,
                  property.asOWLObjectProperty(),
                  owlFactory.createOWLClass(filler.getString()));
         }
      }
   }

   @Override
   public void visit(ASTDataMinCardinality node) {
      OWLDataProperty property = getOWLDataProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         classExpression = owlFactory.createOWLDataMinCardinality(
               cardinality,
               property,
               DatatypeUtils.getOWLDatatype(node.getDatatype()));
      }
   }

   @Override
   public void visit(ASTObjectMinCardinality node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (node.hasExplicitClassExpression()) {
            OWLClassExpression innerClassExpression = getOWLClassExpression(node);
            if (innerClassExpression != null) {
               classExpression = owlFactory.createOWLObjectMinCardinality(
                     cardinality,
                     property,
                     innerClassExpression);
            }
         } else {
            classExpression = owlFactory.createOWLObjectMinCardinality(
                  cardinality,
                  property);
         }
      }
   }

   @Override
   public void visit(ASTUntypedMinCardinality node) {
      OWLEntity property = getOWLProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         Value filler = getFiller(node);
         if (property.isOWLDataProperty()) {
            classExpression = owlFactory.createOWLDataMinCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  owlFactory.createOWLDatatype(filler.getString()));
         } else if (property.isOWLObjectProperty()) {
            classExpression = owlFactory.createOWLObjectMinCardinality(
                  cardinality,
                  property.asOWLObjectProperty(),
                  owlFactory.createOWLClass(filler.getString()));
         }
      }
   }

   @Override
   public void visit(ASTDataHasValue node) {
      OWLDataProperty property = getOWLDataProperty(node);
      if (property != null) {
         Value value = getLiteralValue(node);
         if (value instanceof LiteralValue) {
            OWLLiteral literal = owlFactory.getOWLTypedLiteral((LiteralValue) value);
            classExpression = owlFactory.createOWLDataHasValue(property, literal);
         } else if (value instanceof PlainLiteralValue) {
            OWLLiteral literal = owlFactory.getOWLPlainLiteral((PlainLiteralValue) value);
            classExpression = owlFactory.createOWLDataHasValue(property, literal);
         } else if (value instanceof UntypedValue) {
            OWLLiteral literal = owlFactory.getOWLTypedLiteral(((UntypedValue) value).asLiteralValue());
            classExpression = owlFactory.createOWLDataHasValue(property, literal);
         }
      }
   }

   protected Value getLiteralValue(SimpleNode node) {
      ASTLiteralValue literalValueNode = ParserUtils.getChild(node, NodeType.LITERAL_VALUE);
      ValueNodeVisitor visitor = new ValueNodeVisitor(referenceResolver, builtInFunctionHandler, cellCursor);
      visitor.visit(literalValueNode);
      return visitor.getValue();
   }

   @Override
   public void visit(ASTObjectHasValue node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      if (property != null) {
         Value value = getObjectValue(node);
         if (value instanceof IndividualName) {
            OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((IndividualName) value);
            classExpression = owlFactory.createOWLObjectHasValue(property, individual);
         } else if (value instanceof IndividualIri) {
            OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((IndividualIri) value);
            classExpression = owlFactory.createOWLObjectHasValue(property, individual);
         }
      }
   }

   protected Value getObjectValue(SimpleNode node) {
      ASTObjectValue objectValueNode = ParserUtils.getChild(node, NodeType.OBJECT_VALUE);
      ValueNodeVisitor visitor = new ValueNodeVisitor(referenceResolver, builtInFunctionHandler, cellCursor);
      visitor.visit(objectValueNode);
      return visitor.getValue();
   }

   @Override
   public void visit(ASTUntypedHasValue node) {
      OWLEntity property = getOWLProperty(node);
      if (property != null) {
         Value filler = getFiller(node);
         if (property.isOWLDataProperty()) {
            classExpression = owlFactory.createOWLDataHasValue(
                  property.asOWLDataProperty(),
                  owlFactory.getOWLTypedLiteral(((UntypedValue) filler).asLiteralValue()));
         } else if (property.isOWLObjectProperty()) {
            classExpression = owlFactory.createOWLObjectHasValue(
                  property.asOWLObjectProperty(),
                  owlFactory.createOWLNamedIndividual(filler.getString()));
         }
      }
   }

   @Override
   public void visit(ASTDataAllValuesFrom node) {
      OWLDataProperty property = getOWLDataProperty(node);
      if (property != null) {
         OWLDatatype datatype = DatatypeUtils.getOWLDatatype(node.getDatatype());
         classExpression = owlFactory.createOWLDataAllValuesFrom(property, datatype);
      }
   }

   @Override
   public void visit(ASTObjectAllValuesFrom node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      OWLClassExpression innerClassExpression = getOWLClassExpression(node);
      if (property != null && innerClassExpression != null) {
         classExpression = owlFactory.createOWLObjectAllValuesFrom(property, innerClassExpression);
      }
   }

   @Override
   public void visit(ASTDataSomeValuesFrom node) {
      OWLDataProperty property = getOWLDataProperty(node);
      if (property != null) {
         OWLDatatype datatype = DatatypeUtils.getOWLDatatype(node.getDatatype());
         classExpression = owlFactory.createOWLDataSomeValuesFrom(property, datatype);
      }
   }

   @Override
   public void visit(ASTObjectSomeValuesFrom node) {
      OWLObjectProperty property = getOWLObjectProperty(node);
      OWLClassExpression innerClassExpression = getOWLClassExpression(node);
      if (property != null && innerClassExpression != null) {
         classExpression = owlFactory.createOWLObjectSomeValuesFrom(property, innerClassExpression);
      }
   }

   @Nullable
   private OWLDataProperty getOWLDataProperty(SimpleNode node) {
      ASTDataProperty propertyNode = ParserUtils.getChild(
            node,
            NodeType.DATA_PROPERTY);
      return visitDataPropertyNode(propertyNode);
   }

   @Nullable
   private OWLDataProperty visitDataPropertyNode(ASTDataProperty propertyNode) {
      propertyNode.accept(this);
      OWLEntity entity = getEntity();
      return (entity != null) ? entity.asOWLDataProperty() : null;
   }

   @Nullable
   private OWLObjectProperty getOWLObjectProperty(SimpleNode node) {
      ASTObjectProperty propertyNode = ParserUtils.getChild(
            node,
            NodeType.OBJECT_PROPERTY);
      return visitObjectPropertyNode(propertyNode);
   }

   private OWLObjectProperty visitObjectPropertyNode(ASTObjectProperty propertyNode) {
      propertyNode.accept(this);
      OWLEntity entity = getEntity();
      return (entity != null) ? entity.asOWLObjectProperty() : null;
   }

   private Set<OWLNamedIndividual> getOWLIndividuals(SimpleNode node) {
      final List<ASTNamedIndividual> individualNodes = ParserUtils.getChildren(
            node,
            NodeType.INDIVIDUAL);
      Set<OWLNamedIndividual> individuals = new HashSet<>();
      for (ASTNamedIndividual individualNode : individualNodes) {
         OWLNamedIndividual ind = visitIndividualNode(individualNode);
         if (ind != null) {
            individuals.add(ind);
         }
      }
      return individuals;
   }

   @Nullable
   private OWLNamedIndividual visitIndividualNode(ASTNamedIndividual individualNode) {
      individualNode.accept(this);
      OWLEntity entity = getEntity();
      return (entity != null) ? entity.asOWLNamedIndividual() : null;
   }

   @Nullable
   private OWLClassExpression getOWLClassExpression(SimpleNode node) {
      ASTClassExpressionCategory classExpressionNode = ParserUtils.getChild(
            node,
            NodeType.CLASS_EXPRESSION);
      return visitInnerClassExpressionNode(classExpressionNode);
   }

   @Nullable
   private OWLClassExpression visitInnerObjectIntersectionNode(ASTObjectIntersection objectIntersectionNode) {
      ClassExpressionNodeVisitor innerVisitor = createNewClassExpressionNodeVisitor();
      innerVisitor.visit(objectIntersectionNode);
      return innerVisitor.getClassExpression();
   }

   @Nullable
   private OWLClassExpression visitInnerClassExpressionNode(ASTClassExpressionCategory classExpressionNode) {
      ClassExpressionNodeVisitor innerVisitor = createNewClassExpressionNodeVisitor();
      innerVisitor.visit(classExpressionNode);
      return innerVisitor.getClassExpression();
   }

   private ClassExpressionNodeVisitor createNewClassExpressionNodeVisitor() {
      return new ClassExpressionNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }
}

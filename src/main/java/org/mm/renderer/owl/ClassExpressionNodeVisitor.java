package org.mm.renderer.owl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.parser.NodeType;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnyValue;
import org.mm.parser.node.ASTCardinalityValue;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTFiller;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTObjectComplement;
import org.mm.parser.node.ASTObjectIntersection;
import org.mm.parser.node.ASTObjectOneOf;
import org.mm.parser.node.ASTObjectProperty;
import org.mm.parser.node.ASTObjectUnion;
import org.mm.parser.node.ASTProperty;
import org.mm.parser.node.ASTPropertyAllValuesFrom;
import org.mm.parser.node.ASTPropertyExactCardinality;
import org.mm.parser.node.ASTPropertyHasValue;
import org.mm.parser.node.ASTPropertyMaxCardinality;
import org.mm.parser.node.ASTPropertyMinCardinality;
import org.mm.parser.node.ASTPropertySomeValuesFrom;
import org.mm.parser.node.ASTReferencedAllValuesFrom;
import org.mm.parser.node.ASTReferencedExactCardinality;
import org.mm.parser.node.ASTReferencedHasValue;
import org.mm.parser.node.ASTReferencedMaxCardinality;
import org.mm.parser.node.ASTReferencedMinCardinality;
import org.mm.parser.node.ASTReferencedProperty;
import org.mm.parser.node.ASTReferencedSomeValuesFrom;
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
      int expressionSize = classExpressions.size();
      if (expressionSize == 1){
         classExpression = classExpressions.stream().findFirst().get();
      } else if (expressionSize > 1) {
         classExpression = owlFactory.createOWLObjectUnionOf(classExpressions);
      }
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
      int expressionSize = classExpressions.size();
      if (expressionSize == 1){
         classExpression = classExpressions.stream().findFirst().get();
      } else if (expressionSize > 1) {
         classExpression = owlFactory.createOWLObjectIntersectionOf(classExpressions);
      }
   }

   @Override
   public void visit(ASTObjectComplement node) {
      OWLClassExpression innerClassExpression = getOWLClassExpression(node);
      if (innerClassExpression != null) {
         classExpression = owlFactory.createOWLObjectComplementOf(innerClassExpression);
      }
   }

   @Override
   public void visit(ASTPropertyExactCardinality node) {
      OWLEntity property = getDeclaredProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataExactCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  datatype);
         } else if (property.isOWLObjectProperty()) {
            if (node.hasFiller()) {
               OWLClassExpression fillerExpression = getClassExpressionFiller(node);
               classExpression = owlFactory.createOWLObjectExactCardinality(
                     cardinality,
                     property.asOWLObjectProperty(),
                     fillerExpression);
            } else {
               classExpression = owlFactory.createOWLObjectExactCardinality(
                     cardinality,
                     property.asOWLObjectProperty());
            }
         }
      }
   }

   @Nullable
   private OWLEntity getDeclaredProperty(SimpleNode node) {
      ASTProperty propertyNode = ParserUtils.getChild(node, NodeType.PROPERTY);
      propertyNode.accept(this);
      return getEntity();
   }

   private int getCardinality(SimpleNode node) {
      ASTCardinalityValue valueNode = ParserUtils.getChild(node, NodeType.CARDINALITY_VALUE);
      ValueNodeVisitor visitor = new ValueNodeVisitor(referenceResolver, builtInFunctionHandler, cellCursor);
      visitor.visit(valueNode);
      Value v = visitor.getValue();
      return Integer.parseInt(v.getString());
   }

   private OWLDatatype getDatatypeFiller(SimpleNode node) {
      ASTFiller fillerNode = ParserUtils.getChild(node, NodeType.FILLER);
      if (fillerNode.getDatatype() != -1) {
         return DatatypeUtils.getOWLDatatype(fillerNode.getDatatype());
      } else {
         fillerNode.accept(this);
         Value fillerValue = getValue();
         return owlFactory.createOWLDatatype(fillerValue.getString());
      }
   }

   @Nullable
   private OWLClassExpression getClassExpressionFiller(SimpleNode node) {
      ASTFiller valueNode = ParserUtils.getChild(node, NodeType.FILLER);
      FillerNodeVisitor visitor = createNewFillerNodeVisitor();
      visitor.visit(valueNode);
      return visitor.getClassExpression();
   }

   @Override
   public void visit(ASTReferencedExactCardinality node) {
      OWLEntity property = getReferencedProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataExactCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  datatype);
         } else if (property.isOWLObjectProperty()) {
            if (node.hasFiller()) {
               OWLClassExpression fillerExpression = getClassExpressionFiller(node);
               classExpression = owlFactory.createOWLObjectExactCardinality(
                     cardinality,
                     property.asOWLObjectProperty(),
                     fillerExpression);
            } else {
               classExpression = owlFactory.createOWLObjectExactCardinality(
                     cardinality,
                     property.asOWLObjectProperty());
            }
         }
      }
   }

   @Nullable
   private OWLEntity getReferencedProperty(SimpleNode node) {
      ASTReferencedProperty propertyNode = ParserUtils.getChild(node, NodeType.REFERENCED_PROPERTY);
      propertyNode.accept(this);
      return getEntity();
   }

   @Override
   public void visit(ASTPropertyMaxCardinality node) {
      OWLEntity property = getDeclaredProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataMaxCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  datatype);
         } else if (property.isOWLObjectProperty()) {
            if (node.hasFiller()) {
               OWLClassExpression fillerExpression = getClassExpressionFiller(node);
               classExpression = owlFactory.createOWLObjectMaxCardinality(
                     cardinality,
                     property.asOWLObjectProperty(),
                     fillerExpression);
            } else {
               classExpression = owlFactory.createOWLObjectMaxCardinality(
                     cardinality,
                     property.asOWLObjectProperty());
            }
         }
      }
   }

   @Override
   public void visit(ASTReferencedMaxCardinality node) {
      OWLEntity property = getReferencedProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataMaxCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  datatype);
         } else if (property.isOWLObjectProperty()) {
            if (node.hasFiller()) {
               OWLClassExpression fillerExpression = getClassExpressionFiller(node);
               classExpression = owlFactory.createOWLObjectMaxCardinality(
                     cardinality,
                     property.asOWLObjectProperty(),
                     fillerExpression);
            } else {
               classExpression = owlFactory.createOWLObjectMaxCardinality(
                     cardinality,
                     property.asOWLObjectProperty());
            }
         }
      }
   }

   @Override
   public void visit(ASTPropertyMinCardinality node) {
      OWLEntity property = getDeclaredProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataMinCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  datatype);
         } else if (property.isOWLObjectProperty()) {
            if (node.hasFiller()) {
               OWLClassExpression fillerExpression = getClassExpressionFiller(node);
               classExpression = owlFactory.createOWLObjectMinCardinality(
                     cardinality,
                     property.asOWLObjectProperty(),
                     fillerExpression);
            } else {
               classExpression = owlFactory.createOWLObjectMinCardinality(
                     cardinality,
                     property.asOWLObjectProperty());
            }
         }
      }
   }

   @Override
   public void visit(ASTReferencedMinCardinality node) {
      OWLEntity property = getReferencedProperty(node);
      if (property != null) {
         int cardinality = getCardinality(node);
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataMinCardinality(
                  cardinality,
                  property.asOWLDataProperty(),
                  datatype);
         } else if (property.isOWLObjectProperty()) {
            if (node.hasFiller()) {
               OWLClassExpression fillerExpression = getClassExpressionFiller(node);
               classExpression = owlFactory.createOWLObjectMinCardinality(
                     cardinality,
                     property.asOWLObjectProperty(),
                     fillerExpression);
            } else {
               classExpression = owlFactory.createOWLObjectMinCardinality(
                     cardinality,
                     property.asOWLObjectProperty());
            }
         }
      }
   }

   @Override
   public void visit(ASTPropertyHasValue node) {
      OWLEntity property = getDeclaredProperty(node);
      if (property != null) {
         if (property.isOWLDataProperty()) {
            Value value = getHasValue(node);
            if (value instanceof LiteralValue) {
               OWLLiteral literal = owlFactory.getOWLTypedLiteral((LiteralValue) value);
               classExpression = owlFactory.createOWLDataHasValue(property.asOWLDataProperty(), literal);
            } else if (value instanceof PlainLiteralValue) {
               OWLLiteral literal = owlFactory.getOWLPlainLiteral((PlainLiteralValue) value);
               classExpression = owlFactory.createOWLDataHasValue(property.asOWLDataProperty(), literal);
            } else if (value instanceof UntypedValue) {
               OWLLiteral literal = owlFactory.getOWLTypedLiteral(((UntypedValue) value).asLiteralValue());
               classExpression = owlFactory.createOWLDataHasValue(property.asOWLDataProperty(), literal);
            }
         } else if (property.isOWLObjectProperty()) {
            Value value = getHasValue(node);
            if (value instanceof IndividualName) {
               OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((IndividualName) value);
               classExpression = owlFactory.createOWLObjectHasValue(property.asOWLObjectProperty(), individual);
            } else if (value instanceof IndividualIri) {
               OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((IndividualIri) value);
               classExpression = owlFactory.createOWLObjectHasValue(property.asOWLObjectProperty(), individual);
            } else if (value instanceof UntypedValue) {
               OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual(((UntypedValue) value).asIndividualName());
               classExpression = owlFactory.createOWLObjectHasValue(property.asOWLObjectProperty(), individual);
            }
         }
      }
   }

   private Value getHasValue(SimpleNode node) {
      ASTAnyValue anyValueNode = ParserUtils.getChild(node, NodeType.ANY_VALUE);
      anyValueNode.accept(this);
      return getValue();
   }

   @Override
   public void visit(ASTReferencedHasValue node) {
      OWLEntity property = getReferencedProperty(node);
      if (property != null) {
         if (property.isOWLDataProperty()) {
            Value value = getHasValue(node);
            if (value instanceof LiteralValue) {
               OWLLiteral literal = owlFactory.getOWLTypedLiteral((LiteralValue) value);
               classExpression = owlFactory.createOWLDataHasValue(property.asOWLDataProperty(), literal);
            } else if (value instanceof PlainLiteralValue) {
               OWLLiteral literal = owlFactory.getOWLPlainLiteral((PlainLiteralValue) value);
               classExpression = owlFactory.createOWLDataHasValue(property.asOWLDataProperty(), literal);
            } else if (value instanceof UntypedValue) {
               OWLLiteral literal = owlFactory.getOWLTypedLiteral(((UntypedValue) value).asLiteralValue());
               classExpression = owlFactory.createOWLDataHasValue(property.asOWLDataProperty(), literal);
            }
         } else if (property.isOWLObjectProperty()) {
            Value value = getHasValue(node);
            if (value instanceof IndividualName) {
               OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((IndividualName) value);
               classExpression = owlFactory.createOWLObjectHasValue(property.asOWLObjectProperty(), individual);
            } else if (value instanceof IndividualIri) {
               OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual((IndividualIri) value);
               classExpression = owlFactory.createOWLObjectHasValue(property.asOWLObjectProperty(), individual);
            } else if (value instanceof UntypedValue) {
               OWLNamedIndividual individual = owlFactory.getOWLNamedIndividual(((UntypedValue) value).asIndividualName());
               classExpression = owlFactory.createOWLObjectHasValue(property.asOWLObjectProperty(), individual);
            }
         }
      }
   }

   @Override
   public void visit(ASTPropertySomeValuesFrom node) {
      OWLEntity property = getDeclaredProperty(node);
      if (property != null) {
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataSomeValuesFrom(property.asOWLDataProperty(), datatype);
         } else if (property.isOWLObjectProperty()) {
            OWLClassExpression fillerExpression = getClassExpressionFiller(node);
            classExpression = owlFactory.createOWLObjectSomeValuesFrom(property.asOWLObjectProperty(), fillerExpression);
         }
      }
   }

   @Override
   public void visit(ASTReferencedSomeValuesFrom node) {
      OWLEntity property = getReferencedProperty(node);
      if (property != null) {
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataSomeValuesFrom(property.asOWLDataProperty(), datatype);
         } else if (property.isOWLObjectProperty()) {
            OWLClassExpression fillerExpression = getClassExpressionFiller(node);
            classExpression = owlFactory.createOWLObjectSomeValuesFrom(property.asOWLObjectProperty(), fillerExpression);
         }
      }
   }

   @Override
   public void visit(ASTPropertyAllValuesFrom node) {
      OWLEntity property = getDeclaredProperty(node);
      if (property != null) {
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataAllValuesFrom(property.asOWLDataProperty(), datatype);
         } else if (property.isOWLObjectProperty()) {
            OWLClassExpression fillerExpression = getClassExpressionFiller(node);
            classExpression = owlFactory.createOWLObjectAllValuesFrom(property.asOWLObjectProperty(), fillerExpression);
         }
      }
   }

   @Override
   public void visit(ASTReferencedAllValuesFrom node) {
      OWLEntity property = getReferencedProperty(node);
      if (property != null) {
         if (property.isOWLDataProperty()) {
            OWLDatatype datatype = getDatatypeFiller(node);
            classExpression = owlFactory.createOWLDataAllValuesFrom(property.asOWLDataProperty(), datatype);
         } else if (property.isOWLObjectProperty()) {
            OWLClassExpression fillerExpression = getClassExpressionFiller(node);
            classExpression = owlFactory.createOWLObjectAllValuesFrom(property.asOWLObjectProperty(), fillerExpression);
         }
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
      ASTClassExpressionCategory classExpressionNode = ParserUtils.getChild(node, NodeType.CLASS_EXPRESSION);
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

   private FillerNodeVisitor createNewFillerNodeVisitor() {
      return new FillerNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }

   private ClassExpressionNodeVisitor createNewClassExpressionNodeVisitor() {
      return new ClassExpressionNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }
}

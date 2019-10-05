package org.mm.parser;

import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationAssertion;
import org.mm.parser.node.ASTAnnotationProperty;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.ASTAnyValue;
import org.mm.parser.node.ASTArgument;
import org.mm.parser.node.ASTBooleanLiteral;
import org.mm.parser.node.ASTBuiltInFunction;
import org.mm.parser.node.ASTCardinalityValue;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTClassAssertion;
import org.mm.parser.node.ASTClassDeclaration;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTClassExpressionFiller;
import org.mm.parser.node.ASTClassFrame;
import org.mm.parser.node.ASTDataAllValuesFrom;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTDataSomeValuesFrom;
import org.mm.parser.node.ASTDatatype;
import org.mm.parser.node.ASTDifferentFrom;
import org.mm.parser.node.ASTEquivalentClasses;
import org.mm.parser.node.ASTFact;
import org.mm.parser.node.ASTFiller;
import org.mm.parser.node.ASTFloatLiteral;
import org.mm.parser.node.ASTIndividualDeclaration;
import org.mm.parser.node.ASTIndividualFrame;
import org.mm.parser.node.ASTIntegerLiteral;
import org.mm.parser.node.ASTIri;
import org.mm.parser.node.ASTLiteral;
import org.mm.parser.node.ASTName;
import org.mm.parser.node.ASTNamedIndividual;
import org.mm.parser.node.ASTObjectAllValuesFrom;
import org.mm.parser.node.ASTObjectComplement;
import org.mm.parser.node.ASTObjectIntersection;
import org.mm.parser.node.ASTObjectOneOf;
import org.mm.parser.node.ASTObjectProperty;
import org.mm.parser.node.ASTObjectSomeValuesFrom;
import org.mm.parser.node.ASTObjectUnion;
import org.mm.parser.node.ASTProperty;
import org.mm.parser.node.ASTPropertyAssertion;
import org.mm.parser.node.ASTPropertyExactCardinality;
import org.mm.parser.node.ASTPropertyFact;
import org.mm.parser.node.ASTPropertyHasValue;
import org.mm.parser.node.ASTPropertyMaxCardinality;
import org.mm.parser.node.ASTPropertyMinCardinality;
import org.mm.parser.node.ASTPropertyValue;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTReferenceNotation;
import org.mm.parser.node.ASTReferencedExactCardinality;
import org.mm.parser.node.ASTReferencedFact;
import org.mm.parser.node.ASTReferencedHasValue;
import org.mm.parser.node.ASTReferencedMaxCardinality;
import org.mm.parser.node.ASTReferencedMinCardinality;
import org.mm.parser.node.ASTReferencedProperty;
import org.mm.parser.node.ASTRestrictionCategory;
import org.mm.parser.node.ASTRuleExpression;
import org.mm.parser.node.ASTSameAs;
import org.mm.parser.node.ASTSourceSpecification;
import org.mm.parser.node.ASTStringLiteral;
import org.mm.parser.node.ASTSubclassOf;
import org.mm.parser.node.ASTTransformationRule;
import org.mm.parser.node.ASTValue;
import org.mm.parser.node.Node;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class NodeVisitorAdapter implements NodeVisitor {

   protected void handleDefault(Node node) {}

   @Override
   public void visit(ASTAnnotation node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTAnnotationAssertion node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTAnnotationProperty node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTAnnotationValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTArgument node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTBooleanLiteral node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTBuiltInFunction node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTClass node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTClassAssertion node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTClassDeclaration node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTClassExpressionCategory node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTClassExpressionFiller node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTClassFrame node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDataAllValuesFrom node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTPropertyExactCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTReferencedExactCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTFiller node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTPropertyHasValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTReferencedHasValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTPropertyMaxCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTReferencedMaxCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTPropertyMinCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTReferencedMinCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTCardinalityValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDataProperty node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDataSomeValuesFrom node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDifferentFrom node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTEquivalentClasses node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTFact node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTPropertyFact node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTPropertyValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTReferencedFact node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTFloatLiteral node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTIndividualDeclaration node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTIndividualFrame node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTIntegerLiteral node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDatatype node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTIri node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTLiteral node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTAnyValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTName node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTNamedIndividual node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectAllValuesFrom node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectComplement node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectIntersection node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectOneOf node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectProperty node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectSomeValuesFrom node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectUnion node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTReferencedProperty node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTProperty node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTPropertyAssertion node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTReference node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTReferenceNotation node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTRestrictionCategory node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTRuleExpression node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTSameAs node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTSourceSpecification node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTStringLiteral node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTSubclassOf node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTTransformationRule node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTValue node) {
      handleDefault(node);
   }
}

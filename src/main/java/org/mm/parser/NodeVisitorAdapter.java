package org.mm.parser;

import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationAssertion;
import org.mm.parser.node.ASTAnnotationProperty;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.ASTArgument;
import org.mm.parser.node.ASTBooleanLiteral;
import org.mm.parser.node.ASTBuiltInFunction;
import org.mm.parser.node.ASTCardinalityValue;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTClassAssertion;
import org.mm.parser.node.ASTClassDeclaration;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTClassFrame;
import org.mm.parser.node.ASTDataAllValuesFrom;
import org.mm.parser.node.ASTDataExactCardinality;
import org.mm.parser.node.ASTDataHasValue;
import org.mm.parser.node.ASTDataMaxCardinality;
import org.mm.parser.node.ASTDataMinCardinality;
import org.mm.parser.node.ASTDataProperty;
import org.mm.parser.node.ASTDataSomeValuesFrom;
import org.mm.parser.node.ASTDifferentFrom;
import org.mm.parser.node.ASTEquivalentClasses;
import org.mm.parser.node.ASTFact;
import org.mm.parser.node.ASTFloatLiteral;
import org.mm.parser.node.ASTIndividualDeclaration;
import org.mm.parser.node.ASTIndividualFrame;
import org.mm.parser.node.ASTIntegerLiteral;
import org.mm.parser.node.ASTIri;
import org.mm.parser.node.ASTLiteral;
import org.mm.parser.node.ASTLiteralValue;
import org.mm.parser.node.ASTName;
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
import org.mm.parser.node.ASTPropertyAssertion;
import org.mm.parser.node.ASTPropertyValue;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTReferenceNotation;
import org.mm.parser.node.ASTRestrictionCategory;
import org.mm.parser.node.ASTRuleExpression;
import org.mm.parser.node.ASTSameAs;
import org.mm.parser.node.ASTSourceSpecification;
import org.mm.parser.node.ASTStringLiteral;
import org.mm.parser.node.ASTSubclassOf;
import org.mm.parser.node.ASTTransformationRule;
import org.mm.parser.node.ASTUntypedExactCardinality;
import org.mm.parser.node.ASTUntypedHasValue;
import org.mm.parser.node.ASTUntypedMaxCardinality;
import org.mm.parser.node.ASTUntypedMinCardinality;
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
   public void visit(ASTClassFrame node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDataAllValuesFrom node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDataExactCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTUntypedExactCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDataHasValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTUntypedHasValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDataMaxCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTUntypedMaxCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTDataMinCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTCardinalityValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTUntypedMinCardinality node) {
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
   public void visit(ASTIri node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTLiteral node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTLiteralValue node) {
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
   public void visit(ASTObjectExactCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectHasValue node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectIntersection node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectMaxCardinality node) {
      handleDefault(node);
   }

   @Override
   public void visit(ASTObjectMinCardinality node) {
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
   public void visit(ASTObjectValue node) {
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
   public void visit(ASTPropertyValue node) {
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

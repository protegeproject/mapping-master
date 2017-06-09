package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.mm.directive.ReferenceDirectives;
import org.mm.parser.NodeType;
import org.mm.parser.NodeVisitorAdapter;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.ASTBooleanLiteral;
import org.mm.parser.node.ASTBuiltInFunctionPipe;
import org.mm.parser.node.ASTFloatLiteral;
import org.mm.parser.node.ASTIntegerLiteral;
import org.mm.parser.node.ASTIri;
import org.mm.parser.node.ASTLiteral;
import org.mm.parser.node.ASTLiteralValue;
import org.mm.parser.node.ASTName;
import org.mm.parser.node.ASTObjectValue;
import org.mm.parser.node.ASTPropertyValue;
import org.mm.parser.node.ASTQName;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTReferenceNotation;
import org.mm.parser.node.ASTStringLiteral;
import org.mm.parser.node.ASTValueCategory;
import org.mm.parser.node.Node;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ValueNodeVisitor extends NodeVisitorAdapter {

   private final ReferenceResolver referenceResolver;
   private final BuiltInFunctionHandler functionHandler;

   private Value<?> value;

   public ValueNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler functionHandler) {
      this.referenceResolver = checkNotNull(referenceResolver);
      this.functionHandler = checkNotNull(functionHandler);
   }

   public Value<?> getValue() {
      return value;
   }

   @Override
   public void visit(ASTValueCategory valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTAnnotationValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTPropertyValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTLiteralValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTObjectValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTReference referenceNode) {
      String resolvedValue = resolveReference(referenceNode);
      resolvedValue = applyFunction(resolvedValue, referenceNode);
      value = produceValue(resolvedValue, referenceNode);
   }

   private void resolveReference(ASTReference referenceNode) {
      ReferenceNotation referenceNotation = getReferenceNotation(referenceNode);
      ReferenceDirectives referenceDirectives = getReferenceDirectives(referenceNode);
      value = referenceResolver.resolve(referenceNotation, referenceDirectives);
   }

   private void applyFunction(ASTReference referenceNode, String inputValue) {
      FunctionPipe functionPipe = getFunctionPipe(referenceNode);
      FunctionPipeHandler functionPipeHandler = new FunctionPipeHandler(functionHandler);
      value = functionPipeHandler.evaluate(functionPipe, inputValue);
   }

   private ReferenceDirectives getReferenceDirectives(ASTReference referenceNode) {
      return referenceNode.getDirectives();
   }

   private ReferenceNotation getReferenceNotation(ASTReference referenceNode) {
      ASTReferenceNotation referenceNotationNode = ParserUtils.getChild(
            referenceNode,
            NodeType.REFERENCE_NOTATION);
      ReferenceNotation referenceNotation = referenceNotationNode.getReferenceNotation();
      return referenceNotation;
   }

   private FunctionPipe getFunctionPipe(ASTReference referenceNode) {
      FunctionPipe functionPipe = new FunctionPipe();
      for (ASTBuiltInFunctionPipe builtInFunctionPipeNode
            : ParserUtils.getChildren(referenceNode, NodeType.BUILTIN_FUNCTION_PIPE)) {
         BuiltInFunction bif = visitInnerBuiltInFunctionPipe(builtInFunctionPipeNode);
         functionPipe.add(bif);
      }
      return functionPipe;
   }

   private BuiltInFunction visitInnerBuiltInFunctionPipe(
         ASTBuiltInFunctionPipe builtInFunctionPipeNode) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * @deprecated use {@link visit(ASTQName)} instead. 
    */
   @Override
   @Deprecated
   public void visit(ASTName node) {
      String name = node.getValue();
      value = new QName(name);
   }

   @Override
   public void visit(ASTQName node) {
      String name = node.getValue();
      value = new QName(name);
   }

   @Override
   public void visit(ASTIri node) {
      String iri = node.getValue();
      value = new IriValue(iri);
   }

   @Override
   public void visit(ASTLiteral node) {
      Node literalNode = ParserUtils.getChild(node);
      literalNode.accept(this);
   }

   @Override
   public void visit(ASTIntegerLiteral node) {
      int literal = node.getLexicalValue();
      value = LiteralValue.createLiteral(literal);
   }

   @Override
   public void visit(ASTFloatLiteral node) {
      float literal = node.getLexicalValue();
      value = LiteralValue.createLiteral(literal);
   }

   @Override
   public void visit(ASTStringLiteral node) {
      String literal = node.getLexicalValue();
      value = LiteralValue.createLiteral(literal);
   }

   @Override
   public void visit(ASTBooleanLiteral node) {
      boolean literal = node.getLexicalValue();
      value = LiteralValue.createLiteral(literal);
   }
}

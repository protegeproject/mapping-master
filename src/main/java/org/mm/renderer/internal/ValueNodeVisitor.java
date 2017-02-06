package org.mm.renderer.internal;

import javax.annotation.Nonnull;

import org.mm.directive.ReferenceDirectives;
import org.mm.parser.NodeType;
import org.mm.parser.NodeVisitorAdapter;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.ASTBooleanLiteral;
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

   private final String sheetName;
   private final int column;
   private final int row;

   private Value<?> value;

   public ValueNodeVisitor(@Nonnull ReferenceResolver referenceResolver, @Nonnull String sheetName,
         int columnLabel, int rowIndex) {
      this.referenceResolver = referenceResolver;
      this.sheetName = sheetName;
      this.column = columnLabel;
      this.row = rowIndex;
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
      CellAddress cellAddress = getCellAddress(referenceNode);
      ReferenceDirectives referenceDirectives = getReferenceDirectives(referenceNode);
      value = referenceResolver.resolve(cellAddress, referenceDirectives);
   }

   private ReferenceDirectives getReferenceDirectives(ASTReference referenceNode) {
      return referenceNode.getDirectives();
   }

   private CellAddress getCellAddress(ASTReference referenceNode) {
      ASTReferenceNotation referenceNotationNode = ParserUtils.getChild(
            referenceNode,
            NodeType.REFERENCE_NOTATION);
      ReferenceNotation cellNotation = referenceNotationNode.getReferenceNotation();
      return applyParametersAndGetCellAddress(cellNotation, sheetName, column, row);
   }

   private CellAddress applyParametersAndGetCellAddress(ReferenceNotation cellNotation, String sheetName,
         int column, int row) {
      CellAddress cellAddress = cellNotation.apply(sheetName, column, row);
      return cellAddress;
   }

   /**
    * @deprecated use {@link visit(ASTQName)} instead. 
    */
   @Override
   @Deprecated
   public void visit(ASTName node) {
      String name = node.getValue();
      value = new EntityName(name);
   }

   @Override
   public void visit(ASTQName node) {
      String name = node.getValue();
      value = new EntityName(name);
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

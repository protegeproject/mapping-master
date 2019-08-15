package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import javax.annotation.Nonnull;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.NodeType;
import org.mm.parser.NodeVisitorAdapter;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTArgument;
import org.mm.parser.node.ASTBooleanLiteral;
import org.mm.parser.node.ASTBuiltInFunction;
import org.mm.parser.node.ASTFloatLiteral;
import org.mm.parser.node.ASTIntegerLiteral;
import org.mm.parser.node.ASTLiteral;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTReferenceNotation;
import org.mm.parser.node.ASTStringLiteral;
import org.mm.parser.node.Node;
import org.mm.renderer.CellCursor;
import com.google.common.collect.Lists;

public class BuiltInFunctionNodeVisitor extends NodeVisitorAdapter {

   private final CellCursor cellCursor;
   private final ReferenceResolver referenceResolver;

   private int functionType;
   private List<Argument> arguments = Lists.newArrayList();

   public BuiltInFunctionNodeVisitor(@Nonnull CellCursor cellCursor,
         @Nonnull ReferenceResolver referenceResolver) {
      this.cellCursor = checkNotNull(cellCursor);
      this.referenceResolver = checkNotNull(referenceResolver);
   }

   public BuiltInFunction getBuiltInFunction() {
      return new BuiltInFunction(functionType, arguments);
   }

   @Override
   public void visit(ASTBuiltInFunction builtInFunctionNode) {
      functionType = builtInFunctionNode.getFunctionType();
      for (ASTArgument argumentNode : ParserUtils.getChildren(builtInFunctionNode, NodeType.ARGUMENT)) {
         argumentNode.accept(this);
      }
   }

   @Override
   public void visit(ASTReference referenceNode) {
      String stringValue = resolveReference(referenceNode);
      arguments.add(LiteralValue.createLiteral(stringValue));
   }

   private String resolveReference(ASTReference referenceNode) {
      CellAddress cellAddress = getCellAddress(referenceNode);
      ReferenceDirectives directives = referenceNode.getDirectives();
      Value resolvedValue = referenceResolver.resolve(cellAddress, directives);
      return resolvedValue.getString();
   }

   private CellAddress getCellAddress(ASTReference referenceNode) {
      final ReferenceNotation referenceNotation = getReferenceNotation(referenceNode);
      ReferenceNotation.ColumnReference columnReference = referenceNotation.getColumnReference();
      ReferenceNotation.RowReference rowReference = referenceNotation.getRowReference();
      int columnNumber = columnReference.isWildcard() ? cellCursor.getColumn()
            : CellUtils.toColumnNumber(columnReference.getString());
      int rowNumber = rowReference.isWildcard() ? cellCursor.getRow()
            : CellUtils.toRowNumber(rowReference.getString());
      return new CellAddress(cellCursor.getSheetName(), columnNumber, rowNumber);
   }

   private ReferenceNotation getReferenceNotation(ASTReference referenceNode) {
      ASTReferenceNotation referenceNotationNode =
            ParserUtils.getChild(referenceNode, NodeType.REFERENCE_NOTATION);
      ReferenceNotation referenceNotation = referenceNotationNode.getReferenceNotation();
      return referenceNotation;
   }

   @Override
   public void visit(ASTLiteral node) {
      Node literalNode = ParserUtils.getChild(node);
      literalNode.accept(this);
   }

   @Override
   public void visit(ASTIntegerLiteral node) {
      int literal = node.getLexicalValue();
      arguments.add(LiteralValue.createLiteral(literal));
   }

   @Override
   public void visit(ASTFloatLiteral node) {
      float literal = node.getLexicalValue();
      arguments.add(LiteralValue.createLiteral(literal));
   }

   @Override
   public void visit(ASTStringLiteral node) {
      String literal = node.getLexicalValue();
      arguments.add(LiteralValue.createLiteral(literal));
   }

   @Override
   public void visit(ASTBooleanLiteral node) {
      boolean literal = node.getLexicalValue();
      arguments.add(LiteralValue.createLiteral(literal));
   }
}

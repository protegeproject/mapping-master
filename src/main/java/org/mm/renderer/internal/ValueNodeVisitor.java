package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.NodeType;
import org.mm.parser.NodeVisitorAdapter;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.ASTBooleanLiteral;
import org.mm.parser.node.ASTBuiltInFunction;
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
import org.mm.renderer.CellCursor;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ValueNodeVisitor extends NodeVisitorAdapter {

   private final ReferenceResolver referenceResolver;
   private final BuiltInFunctionHandler builtInFunctionHandler;

   private CellCursor cellCursor = CellCursor.getDefaultCursor();

   private Value value = EmptyValue.create();

   public ValueNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler) {
      this.referenceResolver = checkNotNull(referenceResolver);
      this.builtInFunctionHandler = checkNotNull(builtInFunctionHandler);
   }

   public void setCellCursor(@Nonnull CellCursor cellCursor) {
      this.cellCursor = cellCursor;
   }

   public Value getValue() {
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
      value = resolveReference(referenceNode);
      if (!(value instanceof EmptyValue) && referenceNode.hasBuiltInFunction()) {
         ASTBuiltInFunction builtInFunctionNode =
               ParserUtils.getChild(referenceNode, NodeType.BUILTIN_FUNCTION);
         BuiltInFunction function = getBuiltInFunction(builtInFunctionNode);
         value = applyFunction(function, value);
      }
   }

   private Value applyFunction(BuiltInFunction function, Value inputValue) {
      return builtInFunctionHandler.evaluate(function, inputValue);
   }

   private Value resolveReference(ASTReference referenceNode) {
      CellAddress cellAddress = getCellAddress(referenceNode);
      ReferenceDirectives directives = referenceNode.getDirectives();
      return referenceResolver.resolve(cellAddress, directives);
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

   private BuiltInFunction getBuiltInFunction(ASTBuiltInFunction builtInFunctionNode) {
      BuiltInFunctionNodeVisitor builtInFunctionNodeVisitor =
            new BuiltInFunctionNodeVisitor(cellCursor, referenceResolver);
      builtInFunctionNodeVisitor.visit(builtInFunctionNode);
      return builtInFunctionNodeVisitor.getBuiltInFunction();
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

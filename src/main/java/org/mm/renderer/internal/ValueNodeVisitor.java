package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mm.parser.MappingMasterParserConstants.MM_UNTYPED;
import static org.mm.parser.MappingMasterParserConstants.OWL_ANNOTATION_PROPERTY;
import static org.mm.parser.MappingMasterParserConstants.OWL_CLASS;
import static org.mm.parser.MappingMasterParserConstants.OWL_DATATYPE;
import static org.mm.parser.MappingMasterParserConstants.OWL_DATA_PROPERTY;
import static org.mm.parser.MappingMasterParserConstants.OWL_NAMED_INDIVIDUAL;
import static org.mm.parser.MappingMasterParserConstants.OWL_OBJECT_PROPERTY;
import javax.annotation.Nonnull;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.NodeType;
import org.mm.parser.NodeVisitorAdapter;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.ASTAnyValue;
import org.mm.parser.node.ASTBooleanLiteral;
import org.mm.parser.node.ASTBuiltInFunction;
import org.mm.parser.node.ASTCardinalityValue;
import org.mm.parser.node.ASTClassExpressionFiller;
import org.mm.parser.node.ASTFiller;
import org.mm.parser.node.ASTFloatLiteral;
import org.mm.parser.node.ASTIntegerLiteral;
import org.mm.parser.node.ASTIri;
import org.mm.parser.node.ASTLiteral;
import org.mm.parser.node.ASTName;
import org.mm.parser.node.ASTPropertyValue;
import org.mm.parser.node.ASTReference;
import org.mm.parser.node.ASTReferenceNotation;
import org.mm.parser.node.ASTStringLiteral;
import org.mm.parser.node.ASTValue;
import org.mm.parser.node.Node;
import org.mm.renderer.CellCursor;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ValueNodeVisitor extends NodeVisitorAdapter {

   protected final ReferenceResolver referenceResolver;
   protected final BuiltInFunctionHandler builtInFunctionHandler;
   protected final CellCursor cellCursor;

   private Value value = EmptyValue.create();

   public ValueNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull CellCursor cellCursor) {
      this.referenceResolver = checkNotNull(referenceResolver);
      this.builtInFunctionHandler = checkNotNull(builtInFunctionHandler);
      this.cellCursor = checkNotNull(cellCursor);
   }

   public Value getValue() {
      return value;
   }

   @Override
   public void visit(ASTAnyValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTAnnotationValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTCardinalityValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTPropertyValue valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTFiller valueNode) {
      Node valueTypeNode = ParserUtils.getChild(valueNode);
      valueTypeNode.accept(this);
   }

   @Override
   public void visit(ASTClassExpressionFiller fillerNode) {
      throw new RuntimeException("A data property restriction can't have a class expression filler");
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

   @Override
   public void visit(ASTName node) {
      String valueString = node.getValue();
      int type = node.getEntityType();
      switch (type) {
         case OWL_CLASS: value = ClassName.create(valueString); break;
         case OWL_DATA_PROPERTY: value = DataPropertyName.create(valueString); break;
         case OWL_OBJECT_PROPERTY: value = ObjectPropertyName.create(valueString); break;
         case OWL_ANNOTATION_PROPERTY: value = AnnotationPropertyName.create(valueString); break;
         case OWL_NAMED_INDIVIDUAL: value = IndividualName.create(valueString); break;
         case OWL_DATATYPE: value = DatatypeName.create(valueString); break;
         case MM_UNTYPED: value = UntypedPrefixedName.create(valueString); break;
      }
   }


   @Override
   public void visit(ASTIri node) {
      String iriString = node.getValue();
      int type = node.getEntityType();
      switch (type) {
         case OWL_CLASS: value = ClassIri.create(iriString); break;
         case OWL_DATA_PROPERTY: value = DataPropertyIri.create(iriString); break;
         case OWL_OBJECT_PROPERTY: value = ObjectPropertyIri.create(iriString); break;
         case OWL_ANNOTATION_PROPERTY: value = AnnotationPropertyIri.create(iriString); break;
         case OWL_NAMED_INDIVIDUAL: value = IndividualIri.create(iriString); break;
         case OWL_DATATYPE: value = DatatypeIri.create(iriString); break;
         case MM_UNTYPED: value = UntypedIri.create(iriString); break;
      }
   }

   @Override
   public void visit(ASTLiteral node) {
      Node literalNode = ParserUtils.getChild(node);
      literalNode.accept(this);
   }

   @Override
   public void visit(ASTIntegerLiteral node) {
      int literal = node.getLexicalValue();
      value = LiteralValue.create(literal, Datatypes.XSD_INTEGER);
   }

   @Override
   public void visit(ASTFloatLiteral node) {
      float literal = node.getLexicalValue();
      value = LiteralValue.create(literal, Datatypes.XSD_FLOAT);
   }

   @Override
   public void visit(ASTStringLiteral node) {
      String literal = node.getLexicalValue();
      value = LiteralValue.create(literal, Datatypes.XSD_STRING);
   }

   @Override
   public void visit(ASTBooleanLiteral node) {
      boolean literal = node.getLexicalValue();
      value = LiteralValue.create(literal, Datatypes.XSD_BOOLEAN);
   }
}

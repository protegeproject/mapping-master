package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import org.mm.parser.NodeType;
import org.mm.parser.NodeVisitorAdapter;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotationValue;
import org.mm.parser.node.ASTLiteralValue;
import org.mm.parser.node.ASTObjectValue;
import org.mm.parser.node.ASTPropertyValue;
import org.mm.parser.node.ASTValue;
import org.mm.parser.node.SimpleNode;
import org.mm.renderer.internal.Value;
import org.mm.renderer.internal.ValueNodeVisitor;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class AbstractNodeVisitor extends NodeVisitorAdapter { // XXX: Deprecated or do something

   protected final ValueNodeVisitor valueNodeVisitor;

   protected AbstractNodeVisitor(@Nonnull ValueNodeVisitor valueNodeVisitor) {
      this.valueNodeVisitor = checkNotNull(valueNodeVisitor);
   }

   protected Value getValue(SimpleNode node) {
      ASTValue valueNode = ParserUtils.getChild(node, NodeType.VALUE);
      valueNodeVisitor.visit(valueNode);
      return valueNodeVisitor.getValue();
   }

   protected Value getAnnotationValue(SimpleNode node) {
      ASTAnnotationValue valueNode = ParserUtils.getChild(node, NodeType.ANNOTATION_VALUE);
      valueNodeVisitor.visit(valueNode);
      return valueNodeVisitor.getValue();
   }

   protected Value getPropertyValue(SimpleNode node) {
      ASTPropertyValue valueNode = ParserUtils.getChild(node, NodeType.PROPERTY_VALUE);
      valueNodeVisitor.visit(valueNode);
      return valueNodeVisitor.getValue();
   }

   protected Value getLiteralValue(SimpleNode node) {
      ASTLiteralValue literalValueNode = ParserUtils.getChild(node, NodeType.LITERAL_VALUE);
      valueNodeVisitor.visit(literalValueNode);
      return valueNodeVisitor.getValue();
   }

   protected Value getObjectValue(SimpleNode node) {
      ASTObjectValue objectValueNode = ParserUtils.getChild(node, NodeType.OBJECT_VALUE);
      valueNodeVisitor.visit(objectValueNode);
      return valueNodeVisitor.getValue();
   }
}

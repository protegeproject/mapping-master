package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.mm.parser.MappingMasterParser;
import org.mm.parser.NodeType;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTClassFrame;
import org.mm.parser.node.ASTIndividualFrame;
import org.mm.parser.node.ASTRuleExpression;
import org.mm.parser.node.Node;
import org.mm.parser.node.SimpleNode;
import org.mm.renderer.Renderer;
import org.mm.renderer.RenderingContext;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OwlRenderer implements Renderer<Set<OWLAxiom>> {

   private final ReferenceResolver referenceResolver;
   private final OwlFactory owlFactory;

   public OwlRenderer(@Nonnull ReferenceResolver referenceResolver, @Nonnull OwlFactory owlFactory) {
      this.referenceResolver = checkNotNull(referenceResolver);
      this.owlFactory = checkNotNull(owlFactory);
   }

   @Override
   public Set<OWLAxiom> render(@Nonnull String ruleString, @Nonnull RenderingContext context) {
      final SimpleNode rootNode = parse(checkNotNull(ruleString));
      final Node frameNode = getFrameNode(rootNode);
      return performRendering(frameNode, checkNotNull(context));
   }

   private Set<OWLAxiom> performRendering(Node frameNode, RenderingContext context) {
      return iterateRenderingAndCollectOutputAxioms(frameNode,
            context.getSheetName(),
            context.getStartColumn(),
            context.getEndColumn(),
            context.getStartRow(),
            context.getEndRow());
   }

   private Set<OWLAxiom> iterateRenderingAndCollectOutputAxioms(Node frameNode, String sheetName,
         int startColumn, int endColumn, int startRow, int endRow) {
      Set<OWLAxiom> axioms = new HashSet<>();
      for (int column = startColumn; column <= endColumn; column++) {
         for (int row = startRow; row <= endRow; row++) {
            final ValueNodeVisitor valueNodeVisitor = createValueNodeVisitor(sheetName, column, row);
            if (frameNode instanceof ASTClassFrame) {
               axioms.addAll(visitClassFrameNode((ASTClassFrame) frameNode, valueNodeVisitor));
            } else if (frameNode instanceof ASTIndividualFrame) {
               axioms.addAll(visitIndividualFrameNode((ASTIndividualFrame) frameNode, valueNodeVisitor));
            }
         }
      }
      return axioms;
   }

   private Collection<OWLAxiom> visitClassFrameNode(final ASTClassFrame classFrameNode,
         final ValueNodeVisitor valueNodeVisitor) {
      ClassFrameNodeVisitor frameVisitor = createClassFrameNodeVisitor(valueNodeVisitor);
      frameVisitor.visit(classFrameNode);
      return frameVisitor.getAxioms();
   }

   private Collection<OWLAxiom> visitIndividualFrameNode(final ASTIndividualFrame individualFrameNode,
         final ValueNodeVisitor valueNodeVisitor) {
      IndividualFrameNodeVisitor frameVisitor = createIndividualFrameNodeVisitor(valueNodeVisitor);
      frameVisitor.visit(individualFrameNode);
      return frameVisitor.getAxioms();
   }

   private SimpleNode parse(String ruleString) {
      try {
         return parseRuleStringAndGetRootNode(ruleString);
      } catch (ParseException e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   private SimpleNode parseRuleStringAndGetRootNode(String ruleString) throws ParseException {
      final ByteArrayInputStream inputStream = new ByteArrayInputStream(ruleString.getBytes());
      MappingMasterParser parser = new MappingMasterParser(inputStream, -1);
      return parser.getTransformationRuleNode();
   }

   private Node getFrameNode(SimpleNode rootNode) {
      ASTRuleExpression expressionNode = ParserUtils.getChild(rootNode, NodeType.RULE_EXPRESSION);
      return ParserUtils.getChild(expressionNode);
   }

   private ValueNodeVisitor createValueNodeVisitor(String sheetName, int column, int row) {
      return new ValueNodeVisitor(referenceResolver, sheetName, column, row);
   }

   private ClassFrameNodeVisitor createClassFrameNodeVisitor(ValueNodeVisitor valueNodeVisitor) {
      return new ClassFrameNodeVisitor(owlFactory, valueNodeVisitor);
   }

   private IndividualFrameNodeVisitor createIndividualFrameNodeVisitor(ValueNodeVisitor valueNodeVisitor) {
      return new IndividualFrameNodeVisitor(owlFactory, valueNodeVisitor);
   }
}

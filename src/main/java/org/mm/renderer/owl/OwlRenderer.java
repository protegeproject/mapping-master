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
import org.mm.renderer.Workbook;
import org.mm.renderer.exception.IgnoreEmptyCellException;
import org.mm.renderer.exception.WarningEmptyCellException;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.ReferenceResolver;
import org.mm.renderer.internal.ValueNodeVisitor;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OwlRenderer implements Renderer<Set<OWLAxiom>> {

   private static final Logger logger = LoggerFactory.getLogger(OwlRenderer.class);

   private final OwlFactory owlFactory;

   public OwlRenderer(@Nonnull OwlFactory owlFactory) {
      this.owlFactory = checkNotNull(owlFactory);
   }

   @Override
   public Set<OWLAxiom> render(@Nonnull String transformationRule, @Nonnull Workbook workbook,
         @Nonnull RenderingContext context) {
      checkNotNull(transformationRule);
      checkNotNull(workbook);
      checkNotNull(context);
      final SimpleNode rootNode = parse(transformationRule);
      return performRendering(
            getFrameNode(rootNode),
            new ReferenceResolver(workbook, context),
            new BuiltInFunctionHandler(workbook, context),
            context);
   }

   private Set<OWLAxiom> performRendering(Node frameNode, ReferenceResolver referenceResolver,
         BuiltInFunctionHandler functionHandler, RenderingContext context) {
      Set<OWLAxiom> axioms = new HashSet<>();
      while (context.hasNextCell()) {
         ValueNodeVisitor valueNodeVisitor = new ValueNodeVisitor(referenceResolver, functionHandler);
         if (frameNode instanceof ASTClassFrame) {
            performClassFrameRendering((ASTClassFrame) frameNode, valueNodeVisitor, axioms);
         } else if (frameNode instanceof ASTIndividualFrame) {
            performIndividualFrameRendering((ASTIndividualFrame) frameNode, valueNodeVisitor, axioms);
         }
      }
      return axioms;
   }

   private void performClassFrameRendering(ASTClassFrame classFrame,
         ValueNodeVisitor valueNodeVisitor, final Set<OWLAxiom> collector) {
      try {
         collector.addAll(
               visitClassFrameNode(
                     classFrame,
                     createClassFrameNodeVisitor(valueNodeVisitor)));
      } catch (IgnoreEmptyCellException e) {
         // NO-OP: Ignore the exception
      } catch (WarningEmptyCellException e) {
         logger.warn(e.getMessage());
      }
   }

   private void performIndividualFrameRendering(ASTIndividualFrame individualFrame,
         ValueNodeVisitor valueNodeVisitor, final Set<OWLAxiom> collector) {
      try {
         collector.addAll(
               visitIndividualFrameNode(
                     individualFrame,
                     createIndividualFrameNodeVisitor(valueNodeVisitor)));
      } catch (IgnoreEmptyCellException e) {
         // NO-OP: Ignore the exception
      } catch (WarningEmptyCellException e) {
         logger.warn(e.getMessage());
      }
   }

   private Collection<OWLAxiom> visitClassFrameNode(ASTClassFrame classFrameNode,
         ClassFrameNodeVisitor classFrameVisitor) {
      classFrameVisitor.visit(classFrameNode);
      return classFrameVisitor.getAxioms();
   }

   private Collection<OWLAxiom> visitIndividualFrameNode(ASTIndividualFrame individualFrameNode,
         IndividualFrameNodeVisitor individualFrameVisitor) {
      individualFrameVisitor.visit(individualFrameNode);
      return individualFrameVisitor.getAxioms();
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

   private ClassFrameNodeVisitor createClassFrameNodeVisitor(ValueNodeVisitor valueNodeVisitor) {
      return new ClassFrameNodeVisitor(owlFactory, valueNodeVisitor);
   }

   private IndividualFrameNodeVisitor createIndividualFrameNodeVisitor(ValueNodeVisitor valueNodeVisitor) {
      return new IndividualFrameNodeVisitor(owlFactory, valueNodeVisitor);
   }
}

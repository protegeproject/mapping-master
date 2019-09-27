package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.ByteArrayInputStream;
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
import org.mm.renderer.CellCursor;
import org.mm.renderer.Renderer;
import org.mm.renderer.RenderingContext;
import org.mm.renderer.Workbook;
import org.mm.renderer.exception.IgnoreEmptyCellException;
import org.mm.renderer.exception.WarningEmptyCellException;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.ReferenceResolver;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Sets;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OwlRenderer implements Renderer<Set<OWLAxiom>> {

   private static final Logger logger = LoggerFactory.getLogger(OwlRenderer.class);

   @Override
   public Set<OWLAxiom> render(String transformationRule, Workbook workbook, RenderingContext context, OwlEntityResolver entityResolver) {
      final Node transformationRuleObject = parse(transformationRule);
      final ReferenceResolver referenceResolver = new ReferenceResolver(workbook);
      final BuiltInFunctionHandler builtInFunctionHandler = new BuiltInFunctionHandler();
      final OwlFactory owlFactory = new OwlFactory(entityResolver);
      return performRendering(transformationRuleObject, referenceResolver, builtInFunctionHandler, owlFactory, context);
   }

   private Set<OWLAxiom> performRendering(Node transformationRuleObject, ReferenceResolver referenceResolver,
         BuiltInFunctionHandler builtInFunctionHandler, OwlFactory owlFactory, RenderingContext context) {
      Set<OWLAxiom> axioms = Sets.newHashSet();
      RenderingContext.Iterator contextIterator = context.getIterator();
      while (contextIterator.next()) {
         CellCursor cellCursor = contextIterator.getCursor();
         try {
            if (transformationRuleObject instanceof ASTClassFrame) {
               ClassFrameNodeVisitor classFrameNodeVisitor = new ClassFrameNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
               classFrameNodeVisitor.visit((ASTClassFrame) transformationRuleObject);
               axioms.addAll(classFrameNodeVisitor.getAxioms());
            } else if (transformationRuleObject instanceof ASTIndividualFrame) {
               IndividualFrameNodeVisitor individualFrameNodeVisitor = new IndividualFrameNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
               individualFrameNodeVisitor.visit((ASTIndividualFrame) transformationRuleObject);
               axioms.addAll(individualFrameNodeVisitor.getAxioms());
            }
         } catch (IgnoreEmptyCellException | WarningEmptyCellException e) {
            logger.warn(e.getMessage());
         }
      }
      return axioms;
   }

   private Node parse(String ruleString) {
      try {
         SimpleNode rootNode = parseRuleStringAndGetRootNode(ruleString);
         ASTRuleExpression expressionNode = ParserUtils.getChild(rootNode, NodeType.RULE_EXPRESSION);
         return ParserUtils.getChild(expressionNode);
      } catch (ParseException e) {
         throw new RuntimeException(e.getMessage(), e);
      }
   }

   private SimpleNode parseRuleStringAndGetRootNode(String ruleString) throws ParseException {
      final ByteArrayInputStream inputStream = new ByteArrayInputStream(ruleString.getBytes());
      MappingMasterParser parser = new MappingMasterParser(inputStream, -1);
      return parser.getTransformationRuleNode();
   }
}

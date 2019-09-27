package org.mm.renderer.owl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.mm.parser.NodeType;
import org.mm.parser.ParserUtils;
import org.mm.parser.node.ASTAnnotation;
import org.mm.parser.node.ASTAnnotationAssertion;
import org.mm.parser.node.ASTClass;
import org.mm.parser.node.ASTClassDeclaration;
import org.mm.parser.node.ASTClassExpressionCategory;
import org.mm.parser.node.ASTClassFrame;
import org.mm.parser.node.ASTEquivalentClasses;
import org.mm.parser.node.ASTSubclassOf;
import org.mm.renderer.CellCursor;
import org.mm.renderer.internal.BuiltInFunctionHandler;
import org.mm.renderer.internal.ReferenceResolver;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ClassFrameNodeVisitor extends EntityNodeVisitor {

   private OWLClass subject;

   private Set<OWLAxiom> axioms = new HashSet<>();

   public ClassFrameNodeVisitor(@Nonnull ReferenceResolver referenceResolver,
         @Nonnull BuiltInFunctionHandler builtInFunctionHandler,
         @Nonnull OwlFactory owlFactory,
         @Nonnull CellCursor cellCursor) {
      super(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }

   public Collection<OWLAxiom> getAxioms() {
      return axioms;
   }

   @Override
   public void visit(ASTClassFrame node) {
      visitClassDeclarationNode(node);
      visitSubClassOfNode(node);
      visitEquivalentClassesNode(node);
      visitAnnotationAssertionNode(node);
   }

   @Override
   public void visit(ASTClassDeclaration node) {
      ASTClass classNode = ParserUtils.getChild(node, NodeType.CLASS);
      classNode.accept(this);
      subject = (OWLClass) getEntity();
      axioms.add(owlFactory.createOWLDeclarationAxiom(subject));
   }

   private void visitClassDeclarationNode(ASTClassFrame classSectionNode) {
      ASTClassDeclaration classDeclarationNode = ParserUtils.getChild(
            classSectionNode,
            NodeType.CLASS_DECLARATION);
      classDeclarationNode.accept(this);
   }

   private void visitSubClassOfNode(ASTClassFrame classSectionNode) {
      final List<ASTSubclassOf> subClassNodes = ParserUtils.getChildren(
            classSectionNode,
            NodeType.SUBCLASS_OF);
      for (ASTSubclassOf subClassNode : subClassNodes) {
         visitEachSubClassOfNode(subClassNode);
      }
   }

   private void visitEachSubClassOfNode(ASTSubclassOf subClassNode) {
      final List<ASTClassExpressionCategory> classExpressionNodes = ParserUtils.getChildren(
            subClassNode,
            NodeType.CLASS_EXPRESSION);
      for (ASTClassExpressionCategory classExpressionNode : classExpressionNodes) {
         visitEachClassExpressionNodeForSubClassOfAxiom(classExpressionNode);
      }
   }

   private void visitEachClassExpressionNodeForSubClassOfAxiom(
         ASTClassExpressionCategory classExpressionNode) {
      ClassExpressionNodeVisitor visitor = createNewClassExpressionNodeVisitor();
      visitor.visit(classExpressionNode);
      OWLClassExpression classExpression = visitor.getClassExpression();
      if (classExpression != null && subject != null) {
         axioms.add(owlFactory.createOWLSubClassOfAxiom(classExpression, subject));
      }
   }

   private void visitEquivalentClassesNode(ASTClassFrame classSectionNode) {
      final List<ASTEquivalentClasses> equivalentClassesNodes = ParserUtils.getChildren(
            classSectionNode,
            NodeType.EQUIVALENT_CLASSES);
      for (ASTEquivalentClasses equivalentClassesNode : equivalentClassesNodes) {
         visitEachEquivalentClassesNode(equivalentClassesNode);
      }
   }

   private void visitEachEquivalentClassesNode(ASTEquivalentClasses equivalentClassesNode) {
      final List<ASTClassExpressionCategory> classExpressionNodes = ParserUtils.getChildren(
            equivalentClassesNode,
            NodeType.CLASS_EXPRESSION);
      for (ASTClassExpressionCategory classExpressionNode : classExpressionNodes) {
         visitEachClassExpressionNodeForEquivalentClassesAxiom(classExpressionNode);
      }
   }

   private void visitEachClassExpressionNodeForEquivalentClassesAxiom(
         ASTClassExpressionCategory classExpressionNode) {
      ClassExpressionNodeVisitor visitor = createNewClassExpressionNodeVisitor();
      visitor.visit(classExpressionNode);
      OWLClassExpression classExpression = visitor.getClassExpression();
      if (classExpression != null && subject != null) {
         axioms.add(owlFactory.createOWLEquivalentClassesAxiom(classExpression, subject));
      }
   }

   private void visitAnnotationAssertionNode(ASTClassFrame classSectionNode) {
      final List<ASTAnnotationAssertion> annotationAssertionNodes = ParserUtils.getChildren(
            classSectionNode,
            NodeType.ANNOTATION_ASSERTION);
      for (ASTAnnotationAssertion annotationAssertionNode : annotationAssertionNodes) {
         visitEachAnnotationAssertionNode(annotationAssertionNode);
      }
   }

   private void visitEachAnnotationAssertionNode(ASTAnnotationAssertion annotationAssertionNode) {
      final List<ASTAnnotation> annotationNodes = ParserUtils.getChildren(
            annotationAssertionNode,
            NodeType.ANNOTATION);
      AnnotationNodeVisitor visitor = createNewAnnotationNodeVisitor();
      for (ASTAnnotation annotationNode : annotationNodes) {
         visitor.visit(annotationNode);
         OWLAnnotation annotation = visitor.getAnnotation();
         axioms.add(owlFactory.createOWLAnnotationAssertionAxiom(subject, annotation));
      }
   }

   private ClassExpressionNodeVisitor createNewClassExpressionNodeVisitor() {
      return new ClassExpressionNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }

   private AnnotationNodeVisitor createNewAnnotationNodeVisitor() {
      return new AnnotationNodeVisitor(referenceResolver, builtInFunctionHandler, owlFactory, cellCursor);
   }
}

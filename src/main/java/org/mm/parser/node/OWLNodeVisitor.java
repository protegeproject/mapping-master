package org.mm.parser.node;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface OWLNodeVisitor {

   void visit(LiteralNode node);

   void visit(OWLClassDeclarationNode node);

   void visit(OWLIndividualDeclarationNode node);

   void visit(OWLClassNode node);

   void visit(OWLClassExpressionNode node);

   void visit(OWLSubclassOfNode node);

   void visit(AnnotationFactNode node);

   void visit(FactNode node);

   void visit(TypeNode node);

   void visit(IRIRefNode node);

   void visit(NameNode node);

   void visit(OWLAllValuesFromNode node);

   void visit(OWLDataAllValuesFromNode node);

   void visit(OWLObjectAllValuesFromNode node);

   void visit(OWLSomeValuesFromNode node);

   void visit(OWLDataSomeValuesFromNode node);

   void visit(OWLObjectSomeValuesFromNode node);

   void visit(OWLSameAsNode node);

   void visit(OWLDifferentFromNode node);

   void visit(OWLEquivalentClassesNode node);

   void visit(OWLHasValueNode node);

   void visit(OWLIntersectionClassNode node);

   void visit(OWLMaxCardinalityNode node);

   void visit(OWLMinCardinalityNode node);

   void visit(OWLObjectOneOfNode node);

   void visit(OWLUnionClassNode node);

   void visit(OWLAnnotationValueNode node);

   void visit(OWLNamedIndividualNode node);

   void visit(OWLPropertyNode node);

   void visit(OWLPropertyAssertionNode node);

   void visit(OWLAnnotationPropertyNode node);

   void visit(OWLLiteralNode node);

   void visit(OWLRestrictionNode node);
}

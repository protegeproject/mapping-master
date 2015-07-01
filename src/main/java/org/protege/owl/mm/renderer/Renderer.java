package org.protege.owl.mm.renderer;

import org.protege.owl.mm.parser.node.AnnotationFactNode;
import org.protege.owl.mm.parser.node.EmptyDataValueSettingNode;
import org.protege.owl.mm.parser.node.EmptyLocationSettingNode;
import org.protege.owl.mm.parser.node.EmptyRDFIDSettingNode;
import org.protege.owl.mm.parser.node.EmptyRDFSLabelSettingNode;
import org.protege.owl.mm.parser.node.EntityTypeNode;
import org.protege.owl.mm.parser.node.ExpressionNode;
import org.protege.owl.mm.parser.node.FactNode;
import org.protege.owl.mm.parser.node.LiteralNode;
import org.protege.owl.mm.parser.node.MMDefaultEntityTypeNode;
import org.protege.owl.mm.parser.node.MMDefaultPropertyValueTypeNode;
import org.protege.owl.mm.parser.node.MMDefaultValueEncodingNode;
import org.protege.owl.mm.parser.node.NameNode;
import org.protege.owl.mm.parser.node.OWLAllValuesFromClassNode;
import org.protege.owl.mm.parser.node.OWLAllValuesFromDataTypeNode;
import org.protege.owl.mm.parser.node.OWLAllValuesFromNode;
import org.protege.owl.mm.parser.node.OWLCardinalityNode;
import org.protege.owl.mm.parser.node.OWLClassDeclarationNode;
import org.protege.owl.mm.parser.node.OWLClassExpressionNode;
import org.protege.owl.mm.parser.node.OWLClassOrRestrictionNode;
import org.protege.owl.mm.parser.node.OWLEnumeratedClassNode;
import org.protege.owl.mm.parser.node.OWLEquivalentToNode;
import org.protege.owl.mm.parser.node.OWLExpressionNode;
import org.protege.owl.mm.parser.node.OWLHasValueNode;
import org.protege.owl.mm.parser.node.OWLIndividualDeclarationNode;
import org.protege.owl.mm.parser.node.OWLIndividualNode;
import org.protege.owl.mm.parser.node.OWLIntersectionClassNode;
import org.protege.owl.mm.parser.node.OWLMaxCardinalityNode;
import org.protege.owl.mm.parser.node.OWLMinCardinalityNode;
import org.protege.owl.mm.parser.node.OWLNamedClassNode;
import org.protege.owl.mm.parser.node.OWLPropertyNode;
import org.protege.owl.mm.parser.node.OWLPropertyValueNode;
import org.protege.owl.mm.parser.node.OWLRestrictionNode;
import org.protege.owl.mm.parser.node.OWLSomeValuesFromClassNode;
import org.protege.owl.mm.parser.node.OWLSomeValuesFromDataTypeNode;
import org.protege.owl.mm.parser.node.OWLSomeValuesFromNode;
import org.protege.owl.mm.parser.node.OWLSubclassOfNode;
import org.protege.owl.mm.parser.node.OWLUnionClassNode;
import org.protege.owl.mm.parser.node.ReferenceNode;
import org.protege.owl.mm.parser.node.SameAsNode;
import org.protege.owl.mm.parser.node.ShiftSettingNode;
import org.protege.owl.mm.parser.node.StringOrReferenceNode;
import org.protege.owl.mm.parser.node.TypesNode;
import org.protege.owl.mm.parser.node.ValueEncodingNode;
import org.protege.owl.mm.parser.node.ValueExtractionFunctionNode;
import org.protege.owl.mm.parser.node.ValueSpecificationItemNode;

public interface Renderer
{
  Rendering renderExpression(ExpressionNode expressionNode) throws RendererException;

  Rendering renderMMDefaultEntityType(MMDefaultEntityTypeNode defaultEntityTypeNode) throws RendererException;

  Rendering renderMMDefaultValueEncoding(MMDefaultValueEncodingNode defaultValueEncodingNode) throws RendererException;

  Rendering renderMMDefaultPropertyValueType(MMDefaultPropertyValueTypeNode defaultPropertyValueTypeNode)
    throws RendererException;

  Rendering renderOWLExpression(OWLExpressionNode owlExpressionNode) throws RendererException;

  Rendering renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode) throws RendererException;

  Rendering renderOWLIndividualDeclaration(OWLIndividualDeclarationNode owlIndividualDeclarationNode)
    throws RendererException;

  Rendering renderOWLNamedClass(OWLNamedClassNode owlNamedClassNode) throws RendererException;

  Rendering renderOWLProperty(OWLPropertyNode owlPropertyNodeNode) throws RendererException;

  Rendering renderOWLIndividual(OWLIndividualNode owlIndividualNode) throws RendererException;

  Rendering renderOWLPropertyValue(OWLPropertyValueNode owlPropertyValueNode) throws RendererException;

  Rendering renderFact(FactNode factNode) throws RendererException;

  Rendering renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException;

  Rendering renderOWLEnumeratedClass(OWLEnumeratedClassNode owlEnumeratedClassNode) throws RendererException;

  Rendering renderOWLClassOrRestriction(OWLClassOrRestrictionNode owlClassOrRestrictionNode) throws RendererException;

  // OWL class expressions
  Rendering renderOWLClassExpression(OWLClassExpressionNode owlClassExpressionNode) throws RendererException;

  Rendering renderOWLUnionClass(OWLUnionClassNode owlUnionClassNode) throws RendererException;

  Rendering renderOWLIntersectionClass(OWLIntersectionClassNode owlIntersectionClassNode) throws RendererException;

  Rendering renderOWLRestriction(OWLRestrictionNode owlRestrictionNode) throws RendererException;

  Rendering renderOWLMaxCardinality(OWLMaxCardinalityNode owlMaxCardinalityNode) throws RendererException;

  Rendering renderOWLMinCardinality(OWLMinCardinalityNode owlMinCardinalityNode) throws RendererException;

  Rendering renderOWLCardinality(OWLCardinalityNode owlCardinalityNode) throws RendererException;

  Rendering renderOWLHasValue(OWLHasValueNode owlHasValueNode) throws RendererException;

  Rendering renderOWLAllValuesFrom(OWLAllValuesFromNode owlAllValuesFromNode) throws RendererException;

  Rendering renderOWLAllValuesFromDataType(OWLAllValuesFromDataTypeNode owlAllValuesFromDataTypeNode)
    throws RendererException;

  Rendering renderOWLAllValuesFromClass(OWLAllValuesFromClassNode owlAllValuesFromClassNode) throws RendererException;

  Rendering renderOWLSomeValuesFrom(OWLSomeValuesFromNode owlSomeValuesFromNode) throws RendererException;

  Rendering renderOWLSomeValuesFromDataType(OWLSomeValuesFromDataTypeNode owlSomeValuesFromDataTypeNode)
    throws RendererException;

  Rendering renderOWLSomeValuesFromClass(OWLSomeValuesFromClassNode owlSomeValuesFromClassNode)
    throws RendererException;

  Rendering renderName(NameNode nameNode) throws RendererException;

  Rendering renderLiteral(LiteralNode literalNode) throws RendererException;

  Rendering renderReference(ReferenceNode referenceNode) throws RendererException;

  Rendering renderEntityType(EntityTypeNode entityTypeNode) throws RendererException;

  Rendering renderValueEncoding(ValueEncodingNode valueEncodingNode) throws RendererException;

  Rendering renderValueSpecificationItem(ValueSpecificationItemNode valueSpecificationItemNode)
    throws RendererException;

  Rendering renderTypes(TypesNode definingTypesNode) throws RendererException;

  Rendering renderOWLEquivalentTo(OWLEquivalentToNode owlEquivalentToNode) throws RendererException;

  Rendering renderOWLSubclassOf(OWLSubclassOfNode owlSubclassOfNode) throws RendererException;

  Rendering renderValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode)
    throws RendererException;

  Rendering renderStringOrReference(StringOrReferenceNode stringOrReferenceNode) throws RendererException;

  Rendering renderSameAs(SameAsNode sameAs) throws RendererException;

  Rendering renderShiftSetting(ShiftSettingNode shiftSettingNode) throws RendererException;

  Rendering renderEmptyLocationSetting(EmptyLocationSettingNode emptyLocationSettingNode) throws RendererException;

  Rendering renderEmptyDataValueSetting(EmptyDataValueSettingNode emptyDataValueSettingNode) throws RendererException;

  Rendering renderEmptyRDFIDSetting(EmptyRDFIDSettingNode emptyRDFIDSettingNode) throws RendererException;

  Rendering renderEmptyRDFSLabelSetting(EmptyRDFSLabelSettingNode emptyRDFSLabelSettingNode) throws RendererException;
}

package org.mm.renderer;

import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.EmptyDataValueSettingNode;
import org.mm.parser.node.EmptyLocationSettingNode;
import org.mm.parser.node.EmptyRDFIDSettingNode;
import org.mm.parser.node.EmptyRDFSLabelSettingNode;
import org.mm.parser.node.EntityTypeNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.LiteralNode;
import org.mm.parser.node.MMDefaultEntityTypeNode;
import org.mm.parser.node.MMDefaultValueEncodingNode;
import org.mm.parser.node.OWLAllValuesFromClassNode;
import org.mm.parser.node.OWLAllValuesFromDataTypeNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLCardinalityNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIndividualNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedClassNode;
import org.mm.parser.node.OWLPropertyValueNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSomeValuesFromClassNode;
import org.mm.parser.node.OWLSomeValuesFromDataTypeNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.SameAsNode;
import org.mm.parser.node.ShiftSettingNode;
import org.mm.parser.node.StringOrReferenceNode;
import org.mm.parser.node.TypesNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMDefaultPropertyValueTypeNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLEnumeratedClassNode;
import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.ValueSpecificationItemNode;

import java.util.Optional;

public interface Renderer
{
  Optional<Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException;

	Optional<Rendering> renderMMDefaultEntityType(MMDefaultEntityTypeNode defaultEntityTypeNode) throws RendererException;

	Optional<Rendering> renderMMDefaultValueEncoding(MMDefaultValueEncodingNode defaultValueEncodingNode) throws RendererException;

  Optional<Rendering> renderMMDefaultPropertyValueType(MMDefaultPropertyValueTypeNode defaultPropertyValueTypeNode)
    throws RendererException;

  Optional<Rendering> renderMMExpression(MMExpressionNode MMExpressionNode) throws RendererException;

  Optional<Rendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode) throws RendererException;

  Optional<Rendering> renderOWLIndividualDeclaration(OWLIndividualDeclarationNode owlIndividualDeclarationNode)
    throws RendererException;

  Optional<Rendering> renderOWLNamedClass(OWLNamedClassNode owlNamedClassNode) throws RendererException;

  Optional<Rendering> renderOWLProperty(OWLPropertyNode owlPropertyNodeNode) throws RendererException;

  Optional<Rendering> renderOWLIndividual(OWLIndividualNode owlIndividualNode) throws RendererException;

  Optional<Rendering> renderOWLPropertyValue(OWLPropertyValueNode owlPropertyValueNode) throws RendererException;

  Optional<Rendering> renderFact(FactNode factNode) throws RendererException;

  Optional<Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException;

  Optional<Rendering> renderOWLEnumeratedClass(OWLEnumeratedClassNode owlEnumeratedClassNode) throws RendererException;

  Optional<Rendering> renderOWLClassOrRestriction(OWLClassExpressionNode owlClassExpressionNode) throws RendererException;

  // OWL class expressions
  Optional<Rendering> renderOWLClassExpression(OWLClassExpressionNode owlClassExpressionNode) throws RendererException;

  Optional<Rendering> renderOWLUnionClass(OWLUnionClassNode owlUnionClassNode) throws RendererException;

  Optional<Rendering> renderOWLIntersectionClass(OWLIntersectionClassNode owlIntersectionClassNode) throws RendererException;

  Optional<Rendering> renderOWLRestriction(OWLRestrictionNode owlRestrictionNode) throws RendererException;

  Optional<Rendering> renderOWLMaxCardinality(OWLMaxCardinalityNode owlMaxCardinalityNode) throws RendererException;

  Optional<Rendering> renderOWLMinCardinality(OWLMinCardinalityNode owlMinCardinalityNode) throws RendererException;

  Optional<Rendering> renderOWLCardinality(OWLCardinalityNode owlCardinalityNode) throws RendererException;

  Optional<Rendering> renderOWLHasValue(OWLHasValueNode owlHasValueNode) throws RendererException;

  Optional<Rendering> renderOWLAllValuesFrom(OWLAllValuesFromNode owlAllValuesFromNode) throws RendererException;

  Optional<Rendering> renderOWLAllValuesFromDataType(OWLAllValuesFromDataTypeNode owlAllValuesFromDataTypeNode)
    throws RendererException;

  Optional<Rendering> renderOWLAllValuesFromClass(OWLAllValuesFromClassNode owlAllValuesFromClassNode) throws RendererException;

  Optional<Rendering> renderOWLSomeValuesFrom(OWLSomeValuesFromNode owlSomeValuesFromNode) throws RendererException;

  Optional<Rendering> renderOWLSomeValuesFromDataType(OWLSomeValuesFromDataTypeNode owlSomeValuesFromDataTypeNode)
    throws RendererException;

  Optional<Rendering> renderOWLSomeValuesFromClass(OWLSomeValuesFromClassNode owlSomeValuesFromClassNode)
    throws RendererException;

  Optional<Rendering> renderName(NameNode nameNode) throws RendererException;

  Optional<Rendering> renderLiteral(LiteralNode literalNode) throws RendererException;

  Optional<Rendering> renderReference(ReferenceNode referenceNode) throws RendererException;

  Optional<Rendering> renderEntityType(EntityTypeNode entityTypeNode) throws RendererException;

  Optional<Rendering> renderValueEncoding(ValueEncodingNode valueEncodingNode) throws RendererException;

  Optional<Rendering> renderValueSpecificationItem(ValueSpecificationItemNode valueSpecificationItemNode)
    throws RendererException;

  Optional<Rendering> renderTypes(TypesNode definingTypesNode) throws RendererException;

  Optional<Rendering> renderOWLEquivalentTo(OWLClassEquivalentToNode owlClassEquivalentToNode) throws RendererException;

  Optional<Rendering> renderOWLSubclassOf(OWLSubclassOfNode owlSubclassOfNode) throws RendererException;

  Optional<Rendering> renderValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode)
    throws RendererException;

  Optional<Rendering> renderStringOrReference(StringOrReferenceNode stringOrReferenceNode) throws RendererException;

  Optional<Rendering> renderSameAs(SameAsNode sameAs) throws RendererException;

  Optional<Rendering> renderShiftSetting(ShiftSettingNode shiftSettingNode) throws RendererException;

  Optional<Rendering> renderEmptyLocationSetting(EmptyLocationSettingNode emptyLocationSettingNode) throws RendererException;

  Optional<Rendering> renderEmptyDataValueSetting(EmptyDataValueSettingNode emptyDataValueSettingNode) throws RendererException;

  Optional<Rendering> renderEmptyRDFIDSetting(EmptyRDFIDSettingNode emptyRDFIDSettingNode) throws RendererException;

  Optional<Rendering> renderEmptyRDFSLabelSetting(EmptyRDFSLabelSettingNode emptyRDFSLabelSettingNode) throws RendererException;
}

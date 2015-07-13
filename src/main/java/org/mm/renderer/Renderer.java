package org.mm.renderer;

import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.EmptyDataValueSettingNode;
import org.mm.parser.node.EmptyLocationSettingNode;
import org.mm.parser.node.EmptyRDFIDSettingNode;
import org.mm.parser.node.EmptyRDFSLabelSettingNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.LiteralNode;
import org.mm.parser.node.MMDefaultReferenceTypeNode;
import org.mm.parser.node.MMDefaultValueEncodingNode;
import org.mm.parser.node.OWLAllValuesFromClassNode;
import org.mm.parser.node.OWLAllValuesFromDataTypeNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLCardinalityNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIndividualNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedClassNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
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
	Optional<? extends Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException;

	Optional<? extends Rendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLIndividualDeclaration(
			OWLIndividualDeclarationNode owlIndividualDeclarationNode) throws RendererException;

	Optional<? extends Rendering> renderOWLClass(OWLNamedClassNode owlNamedClassNode) throws RendererException;

	Optional<? extends Rendering> renderOWLProperty(OWLPropertyNode owlPropertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLObjectProperty(OWLPropertyNode owlPropertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLDataProperty(OWLPropertyNode owlPropertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLAnnotationProperty(OWLPropertyNode owlPropertyNode) throws RendererException;

	Optional<? extends Rendering> renderOWLNamedIndividual(OWLIndividualNode owlNamedIndividualNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLPropertyAssertionObject(OWLPropertyAssertionObjectNode owlPropertyAssertionObjectNode)
			throws RendererException;

	Optional<? extends Rendering> renderFact(FactNode factNode) throws RendererException;

	Optional<? extends Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode) throws RendererException;

	Optional<? extends Rendering> renderOWLAnnotationValue(OWLAnnotationValueNode annotationValueNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLEnumeratedClass(OWLEnumeratedClassNode enumeratedClassNode)
			throws RendererException;

	// OWL class expressions
	Optional<? extends Rendering> renderOWLClassExpression(OWLClassExpressionNode classExpressionNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode) throws RendererException;

	Optional<? extends Rendering> renderOWLIntersectionClass(OWLIntersectionClassNode intersectionClassNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLRestriction(OWLRestrictionNode restrictionNode) throws RendererException;

	Optional<? extends Rendering> renderOWLMaxCardinality(OWLMaxCardinalityNode maxCardinalityNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLMinCardinality(OWLMinCardinalityNode minCardinalityNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLCardinality(OWLCardinalityNode cardinalityNode) throws RendererException;

	Optional<? extends Rendering> renderOWLHasValue(OWLHasValueNode hasValueNode) throws RendererException;

	Optional<? extends Rendering> renderOWLAllValuesFrom(OWLAllValuesFromNode allValuesFromNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLAllValuesFromDataType(
			OWLAllValuesFromDataTypeNode allValuesFromDataTypeNode) throws RendererException;

	Optional<? extends Rendering> renderOWLAllValuesFromClass(OWLAllValuesFromClassNode allValuesFromClassNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLSomeValuesFrom(OWLSomeValuesFromNode someValuesFromNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLSomeValuesFromDataType(
			OWLSomeValuesFromDataTypeNode someValuesFromDataTypeNode) throws RendererException;

	Optional<? extends Rendering> renderOWLSomeValuesFromClass(OWLSomeValuesFromClassNode someValuesFromClassNode)
			throws RendererException;

	Optional<? extends Rendering> renderName(NameNode nameNode) throws RendererException;

	Optional<? extends Rendering> renderLiteral(LiteralNode literalNode) throws RendererException;

	Optional<? extends Rendering> renderReference(ReferenceNode referenceNode) throws RendererException;

	Optional<? extends Rendering> renderValueEncoding(ValueEncodingNode valueEncodingNode) throws RendererException;

	Optional<? extends Rendering> renderValueSpecificationItem(ValueSpecificationItemNode valueSpecificationItemNode)
			throws RendererException;

	Optional<? extends Rendering> renderTypes(TypesNode definingTypesNode) throws RendererException;

	Optional<? extends Rendering> renderOWLEquivalentTo(OWLClassEquivalentToNode owlClassEquivalentToNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLSubclassOf(OWLSubclassOfNode owlSubclassOfNode) throws RendererException;

	Optional<? extends Rendering> renderValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode)
			throws RendererException;

	Optional<? extends Rendering> renderStringOrReference(StringOrReferenceNode stringOrReferenceNode)
			throws RendererException;

	Optional<? extends Rendering> renderSameAs(SameAsNode sameAs) throws RendererException;

	Optional<? extends Rendering> renderShiftSetting(ShiftSettingNode shiftSettingNode) throws RendererException;

	Optional<? extends Rendering> renderEmptyLocationSetting(EmptyLocationSettingNode emptyLocationSettingNode)
			throws RendererException;

	Optional<? extends Rendering> renderEmptyDataValueSetting(EmptyDataValueSettingNode emptyDataValueSettingNode)
			throws RendererException;

	Optional<? extends Rendering> renderEmptyRDFIDSetting(EmptyRDFIDSettingNode emptyRDFIDSettingNode)
			throws RendererException;

	Optional<? extends Rendering> renderEmptyRDFSLabelSetting(EmptyRDFSLabelSettingNode emptyRDFSLabelSettingNode)
			throws RendererException;

	Optional<? extends Rendering> renderMMDefaultReferenceType(MMDefaultReferenceTypeNode defaultReferenceTypeNode)
			throws RendererException;

	Optional<? extends Rendering> renderMMDefaultValueEncoding(MMDefaultValueEncodingNode defaultValueEncodingNode)
			throws RendererException;

	Optional<? extends Rendering> renderMMDefaultPropertyValueType(
			MMDefaultPropertyValueTypeNode defaultPropertyValueTypeNode) throws RendererException;

	Optional<? extends Rendering> renderMMExpression(MMExpressionNode MMExpressionNode) throws RendererException;
}

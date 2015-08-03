package org.mm.renderer.owlapi;

import java.util.Optional;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLDataAllValuesFromNode;
import org.mm.parser.node.OWLDataSomeValuesFromNode;
import org.mm.parser.node.OWLDifferentFromNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLExactCardinalityNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLObjectAllValuesFromNode;
import org.mm.parser.node.OWLObjectOneOfNode;
import org.mm.parser.node.OWLObjectSomeValuesFromNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSameAsNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.renderer.BaseReferenceRenderer;
import org.mm.renderer.CoreRenderer;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLClassExpressionRenderer;
import org.mm.renderer.OWLEntityRenderer;
import org.mm.renderer.OWLLiteralRenderer;
import org.mm.renderer.ReferenceRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.OWLLiteralRendering;
import org.mm.rendering.ReferenceRendering;
import org.mm.rendering.Rendering;
import org.mm.rendering.owlapi.OWLAnnotationPropertyRendering;
import org.mm.rendering.owlapi.OWLAnnotationValueRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLDataPropertyRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.mm.rendering.owlapi.OWLObjectPropertyRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

// TODO Implement methods

public class OWLAPIEntityRenderer extends BaseReferenceRenderer implements CoreRenderer, OWLEntityRenderer,
		OWLLiteralRenderer, OWLClassExpressionRenderer, MappingMasterParserConstants
{
	private OWLAPIObjectHandler handler;

	public OWLAPIEntityRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource) {
		super(dataSource);
		handler = new OWLAPIObjectHandler(ontology);
	}

	@Override public Optional<OWLClassRendering> renderOWLClass(OWLClassNode classNode) throws RendererException
	{
		if (classNode.hasNameNode()) {
			return renderNameForClassNode(classNode.getNameNode());
		} else if (classNode.hasReferenceNode()) {
			return renderReferenceForClassNode(classNode.getReferenceNode());
		} else
			throw new InternalRendererException("unknown child for node " + classNode.getNodeName());
	}

	private Optional<OWLClassRendering> renderNameForClassNode(NameNode nameNode) throws RendererException
	{
		OWLClass cls = handler.getOWLClass(nameNode.getName());
		return Optional.of(new OWLClassRendering(cls));
	}

	private Optional<OWLClassRendering> renderReferenceForClassNode(ReferenceNode referenceNode) throws RendererException
	{
		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
		if (referenceType.isUntyped()) {
			throw new RendererException("untyped reference " + referenceNode);
		}
		
		SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
		SpreadsheetLocation location = resolveLocation(sourceSpecificationNode);
		String resolvedReferenceValue = resolveReferenceValue(location, referenceNode);
		if (resolvedReferenceValue.isEmpty()) {
			switch (referenceNode.getActualEmptyLocationDirective()) {
				case MM_SKIP_IF_EMPTY_LOCATION:
					return Optional.empty();
				case MM_WARNING_IF_EMPTY_LOCATION:
					// TODO Warn in log files
					return Optional.empty();
			}
		}
		
		if (referenceType.isOWLEntity()) {
			String rdfID = getReferenceRDFID(resolvedReferenceValue, referenceNode);
			if (rdfID.isEmpty()) {
				throw new InternalRendererException("missing class identifier for reference " + referenceNode);
			}
			
			OWLClass cls = handler.getOWLClass(rdfID);
			return Optional.of(new OWLClassRendering(cls));
			
		}
		throw new InternalRendererException("unknown reference type " + referenceType + " for reference " + referenceNode);
	}

	@Override public Optional<OWLNamedIndividualRendering> renderOWLNamedIndividual(
			OWLNamedIndividualNode namedIndividualNode) throws RendererException
	{
		if (namedIndividualNode.hasNameNode()) {
			OWLNamedIndividual individual = null; // TODO Implement
			throw new InternalRendererException("not implemented");
			// return Optional.of(new OWLNamedIndividualRendering(individual));

		} else if (namedIndividualNode.hasReferenceNode()) {
			OWLNamedIndividual individual = null; // TODO Implement
			throw new InternalRendererException("not implemented");
			// return Optional.of(new OWLNamedIndividualRendering(individual));
		} else
			throw new InternalRendererException("unknown child for node " + namedIndividualNode.getNodeName());
	}

	@Override public Optional<? extends OWLPropertyRendering> renderOWLProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		/*
		 * MM assumes the default property node is a object property type.
		 */
		if (propertyNode.hasNameNode()) {
			return renderNameForObjectPropertyNode(propertyNode.getNameNode());
		} else if (propertyNode.hasReferenceNode()) {
			return renderReferenceForObjectPropertyNode(propertyNode.getReferenceNode());
		} else
			throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
	}

	@Override public Optional<OWLObjectPropertyRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		if (propertyNode.hasNameNode()) {
			return renderNameForObjectPropertyNode(propertyNode.getNameNode());
		} else if (propertyNode.hasReferenceNode()) {
			return renderReferenceForObjectPropertyNode(propertyNode.getReferenceNode());
		} else
			throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
	}

	private Optional<OWLObjectPropertyRendering> renderNameForObjectPropertyNode(NameNode nameNode) throws RendererException
	{
		OWLObjectProperty prop = handler.getOWLObjectProperty(nameNode.getName());
		return Optional.of(new OWLObjectPropertyRendering(prop));
	}

	private Optional<OWLObjectPropertyRendering> renderReferenceForObjectPropertyNode(ReferenceNode referenceNode) throws RendererException
	{
		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
		if (referenceType.isUntyped()) {
			throw new RendererException("untyped reference " + referenceNode);
		}
		
		SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
		SpreadsheetLocation location = resolveLocation(sourceSpecificationNode);
		String resolvedReferenceValue = resolveReferenceValue(location, referenceNode);
		if (resolvedReferenceValue.isEmpty()) {
			switch (referenceNode.getActualEmptyLocationDirective()) {
				case MM_SKIP_IF_EMPTY_LOCATION:
					return Optional.empty();
				case MM_WARNING_IF_EMPTY_LOCATION:
					// TODO Warn in log files
					return Optional.empty();
			}
		}
		
		if (referenceType.isOWLEntity()) {
			String rdfID = getReferenceRDFID(resolvedReferenceValue, referenceNode);
			if (rdfID.isEmpty()) {
				throw new InternalRendererException("missing class identifier for reference " + referenceNode);
			}
			
			OWLObjectProperty prop = handler.getOWLObjectProperty(rdfID);
			return Optional.of(new OWLObjectPropertyRendering(prop));
			
		}
		throw new InternalRendererException("unknown reference type " + referenceType + " for reference " + referenceNode);
	}

	@Override public Optional<OWLDataPropertyRendering> renderOWLDataProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		if (propertyNode.hasNameNode()) {
			return renderNameForDataPropertyNode(propertyNode.getNameNode());
		} else if (propertyNode.hasReferenceNode()) {
			return renderReferenceForDataPropertyNode(propertyNode.getReferenceNode());
		} else
			throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
	}

	private Optional<OWLDataPropertyRendering> renderNameForDataPropertyNode(NameNode nameNode) throws RendererException
	{
		OWLDataProperty prop = handler.getOWLDataProperty(nameNode.getName());
		return Optional.of(new OWLDataPropertyRendering(prop));
	}

	private Optional<OWLDataPropertyRendering> renderReferenceForDataPropertyNode(ReferenceNode referenceNode) throws RendererException
	{
		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
		if (referenceType.isUntyped()) {
			throw new RendererException("untyped reference " + referenceNode);
		}
		
		SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
		SpreadsheetLocation location = resolveLocation(sourceSpecificationNode);
		String resolvedReferenceValue = resolveReferenceValue(location, referenceNode);
		if (resolvedReferenceValue.isEmpty()) {
			switch (referenceNode.getActualEmptyLocationDirective()) {
				case MM_SKIP_IF_EMPTY_LOCATION:
					return Optional.empty();
				case MM_WARNING_IF_EMPTY_LOCATION:
					// TODO Warn in log files
					return Optional.empty();
			}
		}
		
		if (referenceType.isOWLEntity()) {
			String rdfID = getReferenceRDFID(resolvedReferenceValue, referenceNode);
			if (rdfID.isEmpty()) {
				throw new InternalRendererException("missing class identifier for reference " + referenceNode);
			}
			
			OWLDataProperty prop = handler.getOWLDataProperty(rdfID);
			return Optional.of(new OWLDataPropertyRendering(prop));
			
		}
		throw new InternalRendererException("unknown reference type " + referenceType + " for reference " + referenceNode);
	}

	@Override public Optional<OWLAnnotationPropertyRendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		if (propertyNode.hasNameNode()) {
			return renderNameForAnnotationPropertyNode(propertyNode.getNameNode());
		} else if (propertyNode.hasReferenceNode()) {
			return renderReferenceForAnnotationPropertyNode(propertyNode.getReferenceNode());
		} else
			throw new InternalRendererException("unknown child for node " + propertyNode.getNodeName());
	}

	private Optional<OWLAnnotationPropertyRendering> renderNameForAnnotationPropertyNode(NameNode nameNode)
			throws RendererException
	{
		OWLAnnotationProperty anno = handler.getOWLAnnotationProperty(nameNode.getName());
		return Optional.of(new OWLAnnotationPropertyRendering(anno));
	}

	private Optional<OWLAnnotationPropertyRendering> renderReferenceForAnnotationPropertyNode(ReferenceNode referenceNode)
			throws RendererException
	{
		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
		if (referenceType.isUntyped()) {
			throw new RendererException("untyped reference " + referenceNode);
		}
		
		SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
		SpreadsheetLocation location = resolveLocation(sourceSpecificationNode);
		String resolvedReferenceValue = resolveReferenceValue(location, referenceNode);
		if (resolvedReferenceValue.isEmpty()) {
			switch (referenceNode.getActualEmptyLocationDirective()) {
				case MM_SKIP_IF_EMPTY_LOCATION:
					return Optional.empty();
				case MM_WARNING_IF_EMPTY_LOCATION:
					// TODO Warn in log files
					return Optional.empty();
			}
		}
		
		if (referenceType.isOWLEntity()) {
			String rdfID = getReferenceRDFID(resolvedReferenceValue, referenceNode);
			if (rdfID.isEmpty()) {
				throw new InternalRendererException("missing class identifier for reference " + referenceNode);
			}
			
			OWLAnnotationProperty anno = handler.getOWLAnnotationProperty(rdfID);
			return Optional.of(new OWLAnnotationPropertyRendering(anno));
			
		}
		throw new InternalRendererException("unknown reference type " + referenceType + " for reference " + referenceNode);
	}

	@Override
	public Optional<? extends Rendering> renderOWLClassExpression(OWLClassExpressionNode classExpressionNode)
	        throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLIntersectionClass(OWLIntersectionClassNode intersectionClassNode)
	        throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLObjectOneOf(OWLObjectOneOfNode objectOneOfNode)
	        throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLEquivalentClasses(OWLClassNode declaredClassNode,
	        OWLEquivalentClassesNode equivalentClassesNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLRestriction(OWLRestrictionNode restrictionNode)
	        throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLObjectExactCardinality(OWLPropertyNode propertyNode,
	        OWLExactCardinalityNode cardinalityNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLDataExactCardinality(OWLPropertyNode propertyNode,
	        OWLExactCardinalityNode cardinalityNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLObjectMaxCardinality(OWLPropertyNode propertyNode,
	        OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLDataMaxCardinality(OWLPropertyNode propertyNode,
	        OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLObjectMinCardinality(OWLPropertyNode propertyNode,
	        OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLDataMinCardinality(OWLPropertyNode propertyNode,
	        OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLObjectHasValue(OWLPropertyNode propertyNode,
	        OWLHasValueNode hasValueNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLDataHasValue(OWLPropertyNode propertyNode,
	        OWLHasValueNode hasValueNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
	        OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
	        OWLObjectAllValuesFromNode objectAllValuesFromNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLDataSomeValuesFrom(OWLPropertyNode propertyNode,
	        OWLDataSomeValuesFromNode dataSomeValuesFromNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode propertyNode,
	        OWLObjectSomeValuesFromNode objectSomeValuesFromNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends OWLLiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderExpression(ExpressionNode expressionNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderMMExpression(MMExpressionNode MMExpressionNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLClassDeclaration(OWLClassDeclarationNode owlClassDeclarationNode)
	        throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLSubClassOf(OWLClassNode declaredClassNode,
	        OWLSubclassOfNode subclassOfNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLIndividualDeclaration(
	        OWLIndividualDeclarationNode owlIndividualDeclarationNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLSameAs(OWLSameAsNode sameAs) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLDifferentFrom(OWLDifferentFromNode differentFromNode)
	        throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderOWLPropertyAssertionObject(
	        OWLPropertyAssertionObjectNode propertyAssertionObjectNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<OWLAnnotationValueRendering> renderOWLAnnotationValue(OWLAnnotationValueNode annotationValueNode)
			throws RendererException
	{
		if (annotationValueNode.isName()) {
			return renderNameForAnnotationValueNode(annotationValueNode.getNameNode());
		} else if (annotationValueNode.isLiteral()) {
			return renderLiteralForAnnotationValueNode(annotationValueNode.getOWLLiteralNode());
		} else if (annotationValueNode.isReference()) {
			return renderReferenceForAnnotationValueNode(annotationValueNode.getReferenceNode());
		} else
			throw new InternalRendererException("unknown child for node " + annotationValueNode.getNodeName());
	}

	private Optional<OWLAnnotationValueRendering> renderNameForAnnotationValueNode(NameNode nameNode)
	{
		OWLAnnotationValue anno = handler.getQualifiedName(nameNode.getName());
		return Optional.of(new OWLAnnotationValueRendering(anno));
	}

	private Optional<OWLAnnotationValueRendering> renderLiteralForAnnotationValueNode(OWLLiteralNode literalNode)
			throws RendererException
	{
		OWLAnnotationValue annoValue;
		if (literalNode.isString()) {
			String value = literalNode.getStringLiteralNode().getValue();
			annoValue = handler.getOWLAnnotationValue(value);
		} else if (literalNode.isBoolean()) {
			boolean value = literalNode.getBooleanLiteralNode().getValue();
			annoValue = handler.getOWLAnnotationValue(value);
		} else if (literalNode.isInt()) {
			int value = literalNode.getIntLiteralNode().getValue();
			annoValue = handler.getOWLAnnotationValue(value);
		} else if (literalNode.isFloat()) {
			float value = literalNode.getFloatLiteralNode().getValue();
			annoValue = handler.getOWLAnnotationValue(value);
		} else {
			throw new InternalRendererException("unsupported datatype for node " + literalNode.getNodeName());
		}
		return Optional.of(new OWLAnnotationValueRendering(annoValue));
	}

	private Optional<OWLAnnotationValueRendering> renderReferenceForAnnotationValueNode(ReferenceNode referenceNode)
			throws RendererException
	{
		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();
		if (referenceType.isUntyped()) {
			throw new RendererException("untyped reference " + referenceNode);
		}
		
		SourceSpecificationNode sourceSpecificationNode = referenceNode.getSourceSpecificationNode();
		SpreadsheetLocation location = resolveLocation(sourceSpecificationNode);
		String resolvedReferenceValue = resolveReferenceValue(location, referenceNode);
		if (resolvedReferenceValue.isEmpty()) {
			switch (referenceNode.getActualEmptyLocationDirective()) {
				case MM_SKIP_IF_EMPTY_LOCATION:
					return Optional.empty();
				case MM_WARNING_IF_EMPTY_LOCATION:
					// TODO Warn in log files
					return Optional.empty();
			}
		}
		
		if (referenceType.isOWLLiteral()) {
			OWLAnnotationValue value = handler.getOWLAnnotationValue(resolvedReferenceValue);
			return Optional.of(new OWLAnnotationValueRendering(value));
			
		}
		throw new InternalRendererException("unknown reference type " + referenceType + " for reference " + referenceNode);
	}

	@Override
	public Optional<? extends Rendering> renderFact(FactNode factNode) throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode)
	        throws RendererException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReferenceRenderer getReferenceRenderer()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<? extends ReferenceRendering> renderReference(ReferenceNode referenceNode) throws RendererException
	{
		// NO-OP
		return null;
	}
}

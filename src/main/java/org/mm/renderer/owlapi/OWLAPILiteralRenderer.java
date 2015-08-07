package org.mm.renderer.owlapi;

import java.util.Optional;

import org.mm.core.ReferenceType;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLLiteralRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.owlapi.OWLAPILiteralRendering;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

public class OWLAPILiteralRenderer implements OWLLiteralRenderer
{
	private final OWLDataFactory owlDataFactory;

	public OWLAPILiteralRenderer(OWLDataFactory owlDataFactory)
	{
		this.owlDataFactory = owlDataFactory;
	}

	@Override public Optional<OWLAPILiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
	{
		return Optional.of(new OWLAPILiteralRendering(createOWLLiteral(literalNode)));
	}

	public OWLLiteral createOWLLiteral(OWLLiteralNode literalNode) throws RendererException
	{
		if (literalNode.isString()) {
			return this.owlDataFactory.getOWLLiteral(literalNode.getStringLiteralNode().getValue());
		} else if (literalNode.isInt()) {
			return this.owlDataFactory.getOWLLiteral(literalNode.getIntLiteralNode().getValue()+"", OWL2Datatype.XSD_INT);
		} else if (literalNode.isFloat()) {
			return this.owlDataFactory.getOWLLiteral(literalNode.getFloatLiteralNode().getValue());
		} else if (literalNode.isBoolean()) {
			return this.owlDataFactory.getOWLLiteral(literalNode.getBooleanLiteralNode().getValue());
		} else {
			throw new InternalRendererException("unsupported datatype for node " + literalNode);
		}
	}

	public OWLLiteral createOWLLiteral(String rawValue, ReferenceType referenceType)
		throws RendererException
	{
		if (referenceType.isXSDBoolean())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_BOOLEAN);
		else if (referenceType.isXSDString())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_STRING);
		else if (referenceType.isXSDByte())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_BYTE);
		else if (referenceType.isXSDShort())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_SHORT);
		else if (referenceType.isXSDInt())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_INT);
		else if (referenceType.isXSDLong())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_LONG);
		else if (referenceType.isXSDFloat())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_FLOAT);
		else if (referenceType.isXSDDouble())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_DOUBLE);
		else if (referenceType.isXSDDateTime())
			return this.owlDataFactory.getOWLLiteral(rawValue, OWL2Datatype.XSD_DATE_TIME);
		else if (referenceType.isXSDDate())
			return this.owlDataFactory.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.DATE.getIRI()));
		else if (referenceType.isXSDTime())
			return this.owlDataFactory.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.TIME.getIRI()));
		else if (referenceType.isXSDDuration())
			return this.owlDataFactory.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.DURATION.getIRI()));
		else
			throw new RendererException(
				"unknown type " + referenceType.getTypeName() + " for literal " + rawValue);
	}

	public OWLLiteral createOWLLiteral(String rawValue) throws RendererException
	{
		return this.owlDataFactory.getOWLLiteral(rawValue);
	}
}

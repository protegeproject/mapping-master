package org.mm.renderer.owlapi;

import org.mm.core.ReferenceType;
import org.mm.parser.node.OWLLiteralNode;
import org.mm.renderer.OWLLiteralRenderer;
import org.mm.renderer.RendererException;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import java.util.Optional;

public class OWLAPILiteralRenderer implements OWLLiteralRenderer
{
	private final OWLDataFactory owlDataFactory;

	public OWLAPILiteralRenderer(OWLDataFactory owlDataFactory)
	{
		this.owlDataFactory = owlDataFactory;
	}

	@Override public Optional<OWLAPILiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
	{
		if (literalNode.isBoolean())
			return Optional.of(
        new OWLAPILiteralRendering(this.owlDataFactory.getOWLLiteral(literalNode.getBooleanLiteralNode().getValue())));
		else if (literalNode.isInteger())
			return Optional.of(new OWLAPILiteralRendering(
					this.owlDataFactory.getOWLLiteral(literalNode.getIntLiteralNode().getValue())));
		else if (literalNode.isFloat())
			return Optional
					.of(new OWLAPILiteralRendering(this.owlDataFactory.getOWLLiteral(literalNode.getFloatLiteralNode().getValue())));
		else if (literalNode.isString())
			return Optional.of(new OWLAPILiteralRendering(
					this.owlDataFactory.getOWLLiteral(literalNode.getStringLiteralNode().getValue())));
		else
			throw new RendererException("unknown type for node " + literalNode.getNodeName());
	}

	public OWLLiteral createOWLLiteral(String rawValue, ReferenceType referenceType)
		throws RendererException
	{
		if (referenceType.isXSDBoolean())
			return this.owlDataFactory.getOWLLiteral(rawValue, this.owlDataFactory.getBooleanOWLDatatype());
		else if (referenceType.isXSDString())
			return this.owlDataFactory.getOWLLiteral(rawValue);
		else if (referenceType.isXSDByte())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.BYTE.getIRI()));
		else if (referenceType.isXSDShort())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.SHORT.getIRI()));
		else if (referenceType.isXSDInt())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.INT.getIRI()));
		else if (referenceType.isXSDLong())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.LONG.getIRI()));
		else if (referenceType.isXSDFloat())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.FLOAT.getIRI()));
		else if (referenceType.isXSDDouble())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.DOUBLE.getIRI()));
		else if (referenceType.isXSDDate())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.DATE.getIRI()));
		else if (referenceType.isXSDDateTime())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.DATE_TIME.getIRI()));
		else if (referenceType.isXSDTime())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.TIME.getIRI()));
		else if (referenceType.isXSDDuration())
			return this.owlDataFactory
				.getOWLLiteral(rawValue, this.owlDataFactory.getOWLDatatype(XSDVocabulary.DURATION.getIRI()));
		else
			throw new RendererException(
				"unknown type " + referenceType.getTypeName() + " for literal " + rawValue);
	}

	public OWLLiteral createOWLLiteral(String rawValue)
		throws RendererException
	{
			return this.owlDataFactory.getOWLLiteral(rawValue);
	}
}

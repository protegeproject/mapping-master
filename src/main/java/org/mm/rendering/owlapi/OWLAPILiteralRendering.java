package org.mm.rendering.owlapi;

import org.mm.core.OWLLiteralType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.rendering.OWLLiteralRendering;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

public class OWLAPILiteralRendering extends OWLAPIRendering implements OWLLiteralRendering, MappingMasterParserConstants
{
	private final OWLLiteralType literalType;
	private final OWLLiteral literal;

	public OWLAPILiteralRendering(OWLLiteral literal)
	{
		super();
		this.literal = literal;
		this.literalType = getOWLLiteralType(literal);
	}

	public OWLLiteral getOWLLiteral()
	{
		return this.literal;
	}

	@Override
	public OWLLiteralType getOWLLiteralType()
	{
		return this.literalType;
	}

	@Override
	public String getRawValue()
	{
		return this.literal.getLiteral();
	}

	private static OWLLiteralType getOWLLiteralType(OWLLiteral literal)
	{
		if (literal.getDatatype().isString())
			return new OWLLiteralType(XSD_STRING);
		else if (literal.getDatatype().isBoolean())
			return new OWLLiteralType(XSD_BOOLEAN);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.BYTE.getIRI()))
			return new OWLLiteralType(XSD_BYTE);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.SHORT.getIRI()))
			return new OWLLiteralType(XSD_SHORT);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.INT.getIRI()))
			return new OWLLiteralType(XSD_INT);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.LONG.getIRI()))
			return new OWLLiteralType(XSD_LONG);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.FLOAT.getIRI()))
			return new OWLLiteralType(XSD_FLOAT);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.DOUBLE.getIRI()))
			return new OWLLiteralType(XSD_DOUBLE);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.DATE.getIRI()))
			return new OWLLiteralType(XSD_DATE);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.TIME.getIRI()))
			return new OWLLiteralType(XSD_TIME);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.DATE_TIME.getIRI()))
			return new OWLLiteralType(XSD_DATETIME);
		else if (literal.getDatatype().getIRI().equals(XSDVocabulary.DURATION.getIRI()))
			return new OWLLiteralType(XSD_DURATION);
		else
			return new OWLLiteralType(RDF_XMLLITERAL);
	}
}

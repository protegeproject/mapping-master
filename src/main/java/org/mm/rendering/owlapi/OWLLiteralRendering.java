package org.mm.rendering.owlapi;

import org.mm.core.OWLLiteralType;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

public class OWLLiteralRendering extends OWLAnnotationValueRendering
{
   private final OWLLiteralType literalType;
   private final OWLLiteral literal;

   public OWLLiteralRendering(OWLLiteral literal)
   {
      super(literal);
      this.literal = literal;
      this.literalType = getOWLLiteralType(literal);
   }

   public OWLLiteral getOWLLiteral()
   {
      return this.literal;
   }

   @Override
   public String getRawValue()
   {
      return literal.getLiteral();
   }

   public OWLLiteralType getOWLLiteralType()
   {
      return this.literalType;
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
      else if (literal.getDatatype().getIRI().equals(XSDVocabulary.INTEGER.getIRI()))
         return new OWLLiteralType(XSD_INTEGER);
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
      else if (literal.getDatatype().isRDFPlainLiteral()) {
         return new OWLLiteralType(RDF_PLAINLITERAL);
      }
      throw new RuntimeException("Unsupported data type: " + literal.getDatatype());
   }
}

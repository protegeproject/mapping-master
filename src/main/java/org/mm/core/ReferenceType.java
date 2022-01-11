package org.mm.core;

import org.mm.parser.MappingMasterParserConstants;

public class ReferenceType implements MappingMasterParserConstants
{
   private final int type;

   public ReferenceType(int type)
   {
      this.type = type;
   }

   public String getTypeName()
   {
      return tokenImage[this.type].substring(1, tokenImage[this.type].length() - 1);
   }

   public int getType()
   {
      return this.type;
   }

   public boolean isUntyped()
   {
      return this.type == MM_UNTYPED;
   }

   public boolean isOWLClass()
   {
      return this.type == OWL_CLASS;
   }

   public boolean isOWLNamedIndividual()
   {
      return this.type == OWL_NAMED_INDIVIDUAL;
   }

   public boolean isOWLObjectProperty()
   {
      return this.type == OWL_OBJECT_PROPERTY;
   }

   public boolean isOWLDataProperty()
   {
      return this.type == OWL_DATA_PROPERTY;
   }

   public boolean isOWLAnnotationProperty()
   {
      return this.type == OWL_ANNOTATION_PROPERTY;
   }

   public boolean isOWLDatatype()
   {
      return this.type == OWL_DATATYPE;
   }

   public boolean isIRI()
   {
      return this.type == MM_IRI || this.type == OWL_IRI;
   }

   public boolean isEntityIRI()
   {
      return this.type == MM_ENTITY_IRI;
   }

   public boolean isOWLEntity()
   {
      return isOWLClass() || isOWLNamedIndividual() || isOWLObjectProperty() || isOWLDataProperty()
            || isOWLAnnotationProperty() || isOWLDatatype();
   }

   public boolean isOWLLiteral()
   {
      return isTypedLiteral() || isPlainLiteral();
   }

   public boolean isTypedLiteral()
   {
      return isXSDString() || isXSDBoolean() || isXSDDouble() || isXSDFloat() || isXSDLong() || isXSDInteger()
            || isXSDShort() || isXSDByte() || isXSDDecimal() || isXSDDateTime() || isXSDDate() || isXSDTime()
            || isXSDDuration() || isXSDAnyURI();
   }

   public boolean isPlainLiteral()
   {
      return isRDFPlainLiteral();
   }

   public boolean isXSDString()
   {
      return this.type == XSD_STRING;
   }

   public boolean isXSDDecimal()
   {
      return this.type == XSD_DECIMAL;
   }

   public boolean isXSDByte()
   {
      return this.type == XSD_BYTE;
   }

   public boolean isXSDShort()
   {
      return this.type == XSD_SHORT;
   }

   public boolean isXSDInteger()
   {
      return this.type == XSD_INTEGER;
   }

   public boolean isXSDLong()
   {
      return this.type == XSD_LONG;
   }

   public boolean isXSDFloat()
   {
      return this.type == XSD_FLOAT;
   }

   public boolean isXSDDouble()
   {
      return this.type == XSD_DOUBLE;
   }

   public boolean isXSDBoolean()
   {
      return this.type == XSD_BOOLEAN;
   }

   public boolean isXSDTime()
   {
      return this.type == XSD_TIME;
   }

   public boolean isXSDDateTime()
   {
      return this.type == XSD_DATETIME;
   }

   public boolean isXSDDate()
   {
      return this.type == XSD_DATE;
   }

   public boolean isXSDDuration()
   {
      return this.type == XSD_DURATION;
   }

   public boolean isXSDAnyURI()
   {
      return this.type == XSD_ANYURI;
   }

   public boolean isRDFPlainLiteral()
   {
      return this.type == RDF_PLAINLITERAL;
   }

   public boolean isQuotedOWLLiteral()
   {
      return isXSDString() || isXSDDateTime() || isXSDDate() || isXSDTime() || isXSDDuration() || isRDFPlainLiteral();
   }

   public String toString()
   {
      return getTypeName();
   }

   public boolean equals(Object obj)
   {
      if (this == obj) return true;
      if (obj == null || obj.getClass() != this.getClass()) return false;
      ReferenceType et = (ReferenceType) obj;
      return this.type == et.type;
   }

   public int hashCode()
   {
      int hash = 15;

      hash = hash + this.type;

      return hash;
   }
}

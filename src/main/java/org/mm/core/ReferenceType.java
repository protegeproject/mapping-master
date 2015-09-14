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

   public boolean isOWLEntity()
   {
      return isOWLClass() || isOWLNamedIndividual() || isOWLObjectProperty() || isOWLDataProperty()
            || isOWLAnnotationProperty() || isOWLDatatype();
   }

   public boolean isOWLLiteral()
   {
      return isXSDString() || isXSDByte() || isXSDShort() || isXSDFloat() || isXSDInt() || isXSDLong() || isXSDFloat()
            || isXSDDouble() || isXSDBoolean() || isXSDTime() || isXSDDate() || isXSDDateTime() | isXSDDuration();
   }

   public boolean isXSDString()
   {
      return this.type == XSD_STRING;
   }

   public boolean isXSDByte()
   {
      return this.type == XSD_BYTE;
   }

   public boolean isXSDShort()
   {
      return this.type == XSD_SHORT;
   }

   public boolean isXSDInt()
   {
      return this.type == XSD_INT;
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

   public boolean isQuotedOWLLiteral()
   {
      return !(isXSDBoolean() || isXSDByte() || isXSDShort() || isXSDInt() || isXSDFloat() || isXSDDouble());
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

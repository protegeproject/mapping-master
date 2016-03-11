package org.mm.core;

import org.mm.parser.MappingMasterParserConstants;

public class OWLLiteralType implements MappingMasterParserConstants
{
   private final int type;

   public static OWLLiteralType STRING_LITERAL_TYPE = new OWLLiteralType(XSD_STRING);

   public static OWLLiteralType BOOLEAN_LITERAL_TYPE = new OWLLiteralType(XSD_BOOLEAN);

   public static OWLLiteralType INT_LITERAL_TYPE = new OWLLiteralType(XSD_INTEGER);

   public static OWLLiteralType FLOAT_LITERAL_TYPE = new OWLLiteralType(XSD_FLOAT);

   public OWLLiteralType(int type)
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

   public boolean isXSDInteger()
   {
      return this.type == XSD_INTEGER;
   }

   public boolean isXSDLong()
   {
      return this.type == XSD_FLOAT;
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
      return isXSDString() || isXSDTime() || isXSDDate() || isXSDDateTime() || isXSDDuration();
   }

   public String toString()
   {
      return getTypeName();
   }
}

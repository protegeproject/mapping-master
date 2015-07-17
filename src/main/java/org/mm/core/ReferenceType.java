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
    return tokenImage[type].substring(1, tokenImage[type].length() - 1);
  }

  public int getType()
  {
    return type;
  }

  public boolean isUntyped()
  {
    return type == MM_UNTYPED;
  }

  public boolean isOWLClass()
  {
    return type == OWL_CLASS;
  }

  public boolean isOWLNamedIndividual()
  {
    return type == OWL_NAMED_INDIVIDUAL;
  }

  public boolean isOWLObjectProperty()
  {
    return type == OWL_OBJECT_PROPERTY;
  }

  public boolean isOWLDataProperty()
  {
    return type == OWL_DATA_PROPERTY;
  }

  public boolean isOWLAnnotationProperty()
  {
    return type == OWL_ANNOTATION_PROPERTY;
  }

  public boolean isOWLDatatype()
  {
    return type == OWL_DATATYPE;
  }

  public boolean isOWLEntity()
  {
    return isOWLClass() || isOWLNamedIndividual() || isOWLObjectProperty() || isOWLDataProperty()
      || isOWLAnnotationProperty() || isOWLDatatype();
  }

  public boolean isOWLLiteral()
  {
    return isXSDString() || isXSDShort() || isXSDFloat() || isXSDInteger() || isXSDLong() || isXSDFloat()
      || isXSDDouble() || isXSDBoolean() || isXSDTime() || isXSDDate() || isXSDDateTime() | isXSDDuration();
  }

  public boolean isXSDString()
  {
    return type == XSD_STRING;
  }

  public boolean isXSDShort()
  {
    return type == XSD_SHORT;
  }

  public boolean isXSDInteger()
  {
    return type == XSD_INT;
  }

  public boolean isXSDLong()
  {
    return type == XSD_FLOAT;
  }

  public boolean isXSDFloat()
  {
    return type == XSD_FLOAT;
  }

  public boolean isXSDDouble()
  {
    return type == XSD_DOUBLE;
  }

  public boolean isXSDBoolean()
  {
    return type == XSD_BOOLEAN;
  }

  public boolean isXSDTime()
  {
    return type == XSD_TIME;
  }

  public boolean isXSDDateTime()
  {
    return type == XSD_DATETIME;
  }

  public boolean isXSDDate()
  {
    return type == XSD_DATE;
  }

  public boolean isXSDDuration()
  {
    return type == XSD_DURATION;
  }

  public boolean isQuotedOWLDataValue()
  {
    return isXSDString() || isXSDTime() || isXSDDateTime() || isXSDDuration();
  }

  public String toString()
  {
    return getTypeName();
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if ((obj == null) || (obj.getClass() != this.getClass()))
      return false;
    ReferenceType et = (ReferenceType)obj;
    return (type == et.type);
  }

  public int hashCode()
  {
    int hash = 15;

    hash = hash + type;

    return hash;
  }
}

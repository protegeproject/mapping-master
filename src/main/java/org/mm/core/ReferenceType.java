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

  public boolean isTyped()
  {
    return !isUntyped();
  }

  public boolean isRDFSClass()
  {
    return type == RDFS_CLASS;
  }

  public boolean isRDFProperty()
  {
    return type == RDF_PROPERTY;
  }

  public boolean isOWLClass()
  {
    return type == OWL_CLASS;
  }

  public boolean isOWLNamedIndividual()
  {
    return type == OWL_NAMED_INDIVIDUAL;
  }

  public boolean isOWLLiteral()
  {
    return isXSDString() || isXSDFloat() || isXSDDouble() || isXSDInteger() || isXSDBoolean() || isXSDShort()
      || isXSDTime() | isXSDDateTime() | isXSDDuration();
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

  public boolean isXSDDuration()
  {
    return type == XSD_DURATION;
  }

  public boolean isQuotedOWLDataValue()
  {
    return isXSDString() || isXSDTime() || isXSDDateTime() || isXSDDuration();
  }

  public boolean isOWLObjectProperty()
  {
    return type == OWL_OBJECT_PROPERTY;
  }

  public boolean isOWLDataProperty()
  {
    return type == OWL_DATA_PROPERTY;
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

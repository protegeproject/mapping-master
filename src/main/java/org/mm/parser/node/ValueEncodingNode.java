package org.mm.parser.node;

import org.mm.parser.ASTValueEncoding;
import org.mm.parser.ASTValueSpecification;
import org.mm.parser.InternalParseException;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ValueEncodingNode implements MMNode, MappingMasterParserConstants
{
  private int encodingType;
  private ValueSpecificationNode valueSpecification;

  public ValueEncodingNode(ASTValueEncoding node) throws ParseException
  {
    encodingType = node.encodingType;

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "ValueSpecification")) {
        valueSpecification = new ValueSpecificationNode((ASTValueSpecification)child);
      } else
        throw new InternalParseException("invalid child node " + child.toString() + " for node " + getNodeName());
    }
  }

  public ValueEncodingNode(int defaultValueEncoding) { encodingType = defaultValueEncoding; }

  public boolean hasValueSpecification() { return valueSpecification != null; }

  public ValueSpecificationNode getValueSpecification() { return valueSpecification; }

  public int getEncodingType() { return encodingType; }

  public String getEncodingTypeName()
  {
    return tokenImage[encodingType].substring(1, tokenImage[encodingType].length() - 1);
  }

  public boolean useLocationEncoding() { return encodingType == MM_LOCATION; }

  public boolean hasLocationWithDuplicatesEncoding() { return encodingType == MM_LOCATION_WITH_DUPLICATES; }

  public boolean hasDataValueEncoding() { return encodingType == MM_DATA_VALUE; }

  public boolean hasRDFIDEncoding() { return encodingType == RDF_ID; }

  public boolean hasRDFSLabelEncoding() { return encodingType == RDFS_LABEL; }

  @Override public String getNodeName()
  {
    return "ValueEncoding";
  }

  public String toString()
  {
    String representation = getEncodingTypeName();

    if (hasValueSpecification())
      representation += valueSpecification.toString();

    return representation;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if ((obj == null) || (obj.getClass() != this.getClass()))
      return false;
    ValueEncodingNode ve = (ValueEncodingNode)obj;
    return (getEncodingType() == ve.getEncodingType() && (valueSpecification != null && ve.valueSpecification != null
      && valueSpecification.equals(ve.valueSpecification)));
  }

  public int hashCode()
  {
    int hash = 15;

    hash = hash + encodingType;
    hash = hash + (null == valueSpecification ? 0 : valueSpecification.hashCode());

    return hash;
  }
}

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
  private final int encodingType;
  private ValueSpecificationNode valueSpecificationNode;

  public ValueEncodingNode(ASTValueEncoding node) throws ParseException
  {
    this.encodingType = node.encodingType;

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "ValueSpecification")) {
        this.valueSpecificationNode = new ValueSpecificationNode((ASTValueSpecification)child);
      } else
        throw new InternalParseException("invalid child node " + child + " for node " + getNodeName());
    }
  }

  public ValueEncodingNode(int defaultValueEncoding) { this.encodingType = defaultValueEncoding; }

  public boolean hasValueSpecificationNode() { return this.valueSpecificationNode != null; }

  public ValueSpecificationNode getValueSpecificationNode() { return this.valueSpecificationNode; }

  public int getEncodingType() { return this.encodingType; }

  public String getEncodingTypeName()
  {
    return tokenImage[this.encodingType].substring(1, tokenImage[this.encodingType].length() - 1);
  }

  public boolean useLocationEncoding() { return this.encodingType == MM_LOCATION; }

  public boolean hasLocationWithDuplicatesEncoding() { return this.encodingType == MM_LOCATION_WITH_DUPLICATES; }

  public boolean hasDataValueEncoding() { return this.encodingType == MM_DATA_VALUE; }

  public boolean hasRDFIDEncoding() { return this.encodingType == RDF_ID; }

  public boolean hasRDFSLabelEncoding() { return this.encodingType == RDFS_LABEL; }

  @Override public String getNodeName()
  {
    return "ValueEncoding";
  }

  public String toString()
  {
    String representation = getEncodingTypeName();

    if (hasValueSpecificationNode())
      representation += this.valueSpecificationNode.toString();

    return representation;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    ValueEncodingNode ve = (ValueEncodingNode)obj;
    return getEncodingType() == ve.getEncodingType() && this.valueSpecificationNode != null
      && ve.valueSpecificationNode != null && this.valueSpecificationNode.equals(ve.valueSpecificationNode);
  }

  public int hashCode()
  {
    int hash = 15;

    hash = hash + this.encodingType;
    hash = hash + (null == this.valueSpecificationNode ? 0 : this.valueSpecificationNode.hashCode());

    return hash;
  }
}

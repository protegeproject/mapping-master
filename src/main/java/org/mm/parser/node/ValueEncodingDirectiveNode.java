package org.mm.parser.node;

import org.mm.parser.ASTValueEncoding;
import org.mm.parser.ASTValueSpecification;
import org.mm.parser.InternalParseException;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ValueEncodingDirectiveNode implements MMNode, MappingMasterParserConstants
{
  private final int valueEncodingType;
  private ValueSpecificationNode valueSpecificationNode;

  public ValueEncodingDirectiveNode(ASTValueEncoding node) throws ParseException
  {
    this.valueEncodingType = node.encodingType;

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "ValueSpecification")) {
        this.valueSpecificationNode = new ValueSpecificationNode((ASTValueSpecification)child);
      } else
        throw new InternalParseException("invalid child node " + child + " for node " + getNodeName());
    }
  }

  @Override public String getNodeName()
  {
    return "ValueEncodingDirective";
  }

  public ValueEncodingDirectiveNode(int defaultValueEncoding) { this.valueEncodingType = defaultValueEncoding; }

  public boolean hasValueSpecificationNode() { return this.valueSpecificationNode != null; }

  public ValueSpecificationNode getValueSpecificationNode() { return this.valueSpecificationNode; }

  public int getValueEncodingType() { return this.valueEncodingType; }

  public String getValueEncodingTypeName()
  {
    return tokenImage[this.valueEncodingType].substring(1, tokenImage[this.valueEncodingType].length() - 1);
  }

  public boolean useLocationEncoding() { return this.valueEncodingType == MM_LOCATION; }

  public boolean hasLocationWithDuplicatesEncoding() { return this.valueEncodingType == MM_LOCATION_WITH_DUPLICATES; }

  public boolean hasLiteralEncoding() { return this.valueEncodingType == MM_LITERAL; }

  public boolean hasRDFIDEncoding() { return this.valueEncodingType == RDF_ID; }

  public boolean hasRDFSLabelEncoding() { return this.valueEncodingType == RDFS_LABEL; }

  public String toString()
  {
    String representation = getValueEncodingTypeName();

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
    ValueEncodingDirectiveNode ve = (ValueEncodingDirectiveNode)obj;
    return getValueEncodingType() == ve.getValueEncodingType() && this.valueSpecificationNode != null
      && ve.valueSpecificationNode != null && this.valueSpecificationNode.equals(ve.valueSpecificationNode);
  }

  public int hashCode()
  {
    int hash = 15;

    hash = hash + this.valueEncodingType;
    hash = hash + (null == this.valueSpecificationNode ? 0 : this.valueSpecificationNode.hashCode());

    return hash;
  }
}

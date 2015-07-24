package org.mm.parser.node;

import org.mm.parser.ASTSourceSpecification;
import org.mm.parser.ParseException;

public class SourceSpecificationNode implements MMNode
{
  private final String source, location;
  private final String literal;

  public SourceSpecificationNode(ASTSourceSpecification node) throws ParseException
  {
    this.source = node.source;
    this.location = node.location;
    this.literal = node.literal;
  }

  public boolean hasSource() { return this.source != null; }

  public boolean hasLocation() { return this.location != null; }

  public boolean hasLiteral() { return this.literal != null; }

  public String getSource() { return this.source; }

  public String getLocation() { return this.location; }

  public String getLiteral() { return this.literal; }

  @Override public String getNodeName()
  {
    return "SourceSpecification";
  }

  public String toString()
  {
    String representation = "@";

    if (hasSource())
      representation += "'" + this.source + "'!";

    if (hasLocation())
      representation += this.location;
    else // literal
      representation += "\"" + this.literal + "\"";

    return representation;
  }
}

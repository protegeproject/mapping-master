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

  public boolean hasSource() { return source != null; }

  public boolean hasLocation() { return location != null; }

  public boolean hasLiteral() { return literal != null; }

  public String getSource() { return source; }

  public String getLocation() { return location; }

  public String getLiteral() { return literal; }

  @Override public String getNodeName()
  {
    return "SourceSpecification";
  }

  public String toString()
  {
    String representation = "@";

    if (hasSource())
      representation += "'" + source + "'!";

    if (hasLocation())
      representation += location;
    else // literal
      representation += "\"" + literal + "\"";

    return representation;
  }
}

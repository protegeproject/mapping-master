
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTSourceSpecification;
import org.protege.owl.mm.parser.ParseException;

public class SourceSpecificationNode
{
  private String source, location, literal;

  public SourceSpecificationNode(ASTSourceSpecification node) throws ParseException
  {
    source = node.source;
    location = node.location;
    literal = node.literal;
  }

  public boolean hasSource() { return source != null; }
  public boolean hasLocation() { return location != null; }
  public boolean hasLiteral() { return literal != null; }

  public String getSource() { return source; }
  public String getLocation() { return location; }
  public String getLiteral() { return literal; }

  public String toString() 
  { 
    String representation = "@";

    if (hasSource()) representation += "'" + source + "'!";

    if (hasLocation()) 
      representation += location;
    else // literal
      representation += "\"" + literal + "\"";

    return representation;
  }
}

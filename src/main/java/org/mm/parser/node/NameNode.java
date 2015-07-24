package org.mm.parser.node;

import org.mm.parser.ASTName;
import org.mm.parser.ParseException;

public class NameNode implements MMNode
{
  private final String name;
  private final boolean isQuotedName;

  public NameNode(ASTName node) throws ParseException
  {
    this.name = node.name;
    this.isQuotedName = node.isQuotedName;
  }

  public String getName()
  {
    return this.name;
  }

  public boolean isQuoted()
  {
    return this.isQuotedName;
  }

  @Override public String getNodeName()
  {
    return "Name";
  }

  public String toString()
  {
    if (this.isQuotedName)
      return "'" + this.name + "'";
    else
      return this.name;
  }
}

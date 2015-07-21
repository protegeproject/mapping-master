package org.mm.parser.node;

import org.mm.parser.ASTName;
import org.mm.parser.ParseException;

public class NameNode implements MMNode
{
  private final String name;
  private final boolean isQuotedName;

  public NameNode(ASTName node) throws ParseException
  {
    name = node.name;
    isQuotedName = node.isQuotedName;
  }

  public String getName()
  {
    return name;
  }

  public boolean isQuoted()
  {
    return isQuotedName;
  }

  @Override public String getNodeName()
  {
    return "Name";
  }

  public String toString()
  {
    if (isQuotedName)
      return "'" + name + "'";
    else
      return name;
  }
}

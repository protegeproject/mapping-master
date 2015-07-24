package org.mm.parser.node;

import org.mm.parser.ParseException;
import org.mm.parser.ASTPrefix;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;

public class PrefixNode implements MMNode, MappingMasterParserConstants
{
  private final String prefix;

  PrefixNode(ASTPrefix node) throws ParseException
  {
    this.prefix = node.prefix;
  }

  public String getPrefix() { return this.prefix; }

  @Override public String getNodeName()
  {
    return "Prefix";
  }

  public String toString()
  {
    return ParserUtil.getTokenName(MM_PREFIX) + "=\"" + this.prefix + "\"";
  }
}

package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTPrefix;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class PrefixNode implements MappingMasterParserConstants
{
  String prefix;

  PrefixNode(ASTPrefix node) throws ParseException
  {
    prefix = node.prefix;
  }

  public String getPrefix() { return prefix; }

  public String toString()
  {
    return ParserUtil.getTokenName(MM_PREFIX) + "=\"" + prefix + "\"";
  }
}


package org.mm.parser.node;

import org.mm.parser.ASTLanguage;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class LanguageNode implements MMNode, MappingMasterParserConstants
{
  private final String language;

  public LanguageNode(ASTLanguage node) throws ParseException { this.language = node.language; }

  public String getLanguage() { return this.language; }

  @Override public String getNodeName()
  {
    return "Language";
  }

  public String toString()
  { 
    String s = ParserUtil.getTokenName(XML_LANG);
    
    if (this.language == null || this.language.isEmpty()) s += "=" + ParserUtil.getTokenName(MM_NULL);
    else if ("*".equals(this.language)) s += "=*";
    else if ("+".equals(this.language)) s+= "!=" + ParserUtil.getTokenName(MM_NULL);
    else s+= "=\"" + this.language + "\"";
    
    return s;
  } 
}

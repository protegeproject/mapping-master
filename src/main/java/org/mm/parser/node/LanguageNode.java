
package org.mm.parser.node;

import org.mm.parser.ASTLanguage;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class LanguageNode implements MappingMasterParserConstants
{
  String language;

  public LanguageNode(ASTLanguage node) throws ParseException { language = node.language; }

  public String getLanguage() { return language; }

  public String toString() 
  { 
    String s = ParserUtil.getTokenName(XML_LANG);
    
    if (language == null || language.equals("")) s += "=" + ParserUtil.getTokenName(MM_NULL);
    else if (language.equals("*")) s += "=*";
    else if (language.equals("+")) s+= "!=" + ParserUtil.getTokenName(MM_NULL);
    else s+= "=\"" + language + "\"";
    
    return s;
  } 
}

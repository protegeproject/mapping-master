
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTLanguage;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

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

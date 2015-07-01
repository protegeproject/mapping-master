
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTIfNotExistsDirective;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class IfNotExistsDirectiveNode implements MappingMasterParserConstants
{
  private int ifNotExistsSetting;
  
  public IfNotExistsDirectiveNode(ASTIfNotExistsDirective node) throws ParseException
  {
    ifNotExistsSetting = node.ifNotExistsSetting;
  }
  
  public int getIfNotExistsSetting() { return ifNotExistsSetting; }
  
  public boolean isCreateIfNotExists() { return ifNotExistsSetting == MM_CREATE_IF_NOT_EXISTS; }
  public boolean isWarningIfNotExists() { return ifNotExistsSetting == MM_WARNING_IF_NOT_EXISTS; }
  public boolean isErrorIfNotExists() { return ifNotExistsSetting == MM_ERROR_IF_NOT_EXISTS; }
  public boolean isSkipIfNotExists() { return ifNotExistsSetting == MM_SKIP_IF_NOT_EXISTS; }
  
  public String toString() { return ParserUtil.getTokenName(ifNotExistsSetting); }
}

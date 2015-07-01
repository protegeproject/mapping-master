
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTIfExistsDirective;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class IfExistsDirectiveNode implements MappingMasterParserConstants
{
  private int ifExistsSetting;
  
  public IfExistsDirectiveNode(ASTIfExistsDirective node) throws ParseException
  {
    this.ifExistsSetting = node.ifExistsSetting;
  }
  
  public int getIfExistsSetting() { return ifExistsSetting; }
  
  public boolean isResolveIfExists() { return ifExistsSetting == MM_RESOLVE_IF_EXISTS; }
  public boolean isWarningIfExists() { return ifExistsSetting == MM_WARNING_IF_EXISTS; }
  public boolean isErrorIfExists() { return ifExistsSetting == MM_ERROR_IF_EXISTS; }
  public boolean isSkipIfExists() { return ifExistsSetting == MM_SKIP_IF_EXISTS; }
  
  public String toString() { return ParserUtil.getTokenName(ifExistsSetting); }
}

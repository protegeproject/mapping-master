package org.mm.parser.node;

import org.mm.parser.ASTIfExistsDirective;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class IfExistsDirectiveNode implements MappingMasterParserConstants
{
  private int ifExistsSetting;

  public IfExistsDirectiveNode(ASTIfExistsDirective node) throws ParseException
  {
    this.ifExistsSetting = node.ifExistsSetting;
  }

  public int getIfExistsSetting() { return ifExistsSetting; }

  public String getIfExistsSettingName() { return ParserUtil.getTokenName(ifExistsSetting); }

  public boolean isResolveIfExists() { return ifExistsSetting == MM_RESOLVE_IF_EXISTS; }

  public boolean isWarningIfExists() { return ifExistsSetting == MM_WARNING_IF_EXISTS; }

  public boolean isErrorIfExists() { return ifExistsSetting == MM_ERROR_IF_EXISTS; }

  public boolean isSkipIfExists() { return ifExistsSetting == MM_SKIP_IF_EXISTS; }

  public String toString() { return getIfExistsSettingName(); }
}

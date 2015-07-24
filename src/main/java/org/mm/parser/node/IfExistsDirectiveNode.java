package org.mm.parser.node;

import org.mm.parser.ASTIfExistsDirective;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class IfExistsDirectiveNode implements MMNode, MappingMasterParserConstants
{
  private final int ifExistsSetting;

  public IfExistsDirectiveNode(ASTIfExistsDirective node) throws ParseException
  {
    this.ifExistsSetting = node.ifExistsSetting;
  }

  public int getIfExistsSetting() { return this.ifExistsSetting; }

  public String getIfExistsSettingName() { return ParserUtil.getTokenName(this.ifExistsSetting); }

  public boolean isResolveIfExists() { return this.ifExistsSetting == MM_RESOLVE_IF_EXISTS; }

  public boolean isWarningIfExists() { return this.ifExistsSetting == MM_WARNING_IF_EXISTS; }

  public boolean isErrorIfExists() { return this.ifExistsSetting == MM_ERROR_IF_EXISTS; }

  public boolean isSkipIfExists() { return this.ifExistsSetting == MM_SKIP_IF_EXISTS; }

  @Override public String getNodeName()
  {
    return "IfExistsDirective";
  }

  public String toString() { return getIfExistsSettingName(); }
}

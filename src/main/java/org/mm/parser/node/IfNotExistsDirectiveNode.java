package org.mm.parser.node;

import org.mm.parser.ASTIfNotExistsDirective;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class IfNotExistsDirectiveNode implements MMNode, MappingMasterParserConstants
{
  private final int ifNotExistsSetting;

  public IfNotExistsDirectiveNode(ASTIfNotExistsDirective node) throws ParseException
  {
    this.ifNotExistsSetting = node.ifNotExistsSetting;
  }

  public int getIfNotExistsSetting() { return this.ifNotExistsSetting; }

  public String getIfNotExistsSettingName() { return ParserUtil.getTokenName(this.ifNotExistsSetting); }

  public boolean isCreateIfNotExists() { return this.ifNotExistsSetting == MM_CREATE_IF_NOT_EXISTS; }

  public boolean isWarningIfNotExists() { return this.ifNotExistsSetting == MM_WARNING_IF_NOT_EXISTS; }

  public boolean isErrorIfNotExists() { return this.ifNotExistsSetting == MM_ERROR_IF_NOT_EXISTS; }

  public boolean isSkipIfNotExists() { return this.ifNotExistsSetting == MM_SKIP_IF_NOT_EXISTS; }

  @Override public String getNodeName()
  {
    return "IfNotExistsDirective";
  }

  public String toString() { return getIfNotExistsSettingName(); }
}


package org.mm.parser.node;

import org.mm.parser.ASTEmptyDataValueSetting;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class EmptyDataValueDirectiveNode implements MMNode, MappingMasterParserConstants
{
  private final int emptyDataValueSetting;
  
  public EmptyDataValueDirectiveNode(ASTEmptyDataValueSetting node) throws ParseException
  {
    this.emptyDataValueSetting = node.emptyDataValueSetting;
  }
  
  public int getEmptyDataValueSetting() { return this.emptyDataValueSetting; }
  
  public boolean isErrorIfEmpty() { return this.emptyDataValueSetting == MM_ERROR_IF_EMPTY_DATA_VALUE; }
  public boolean isWarningIfEmpty() { return this.emptyDataValueSetting == MM_WARNING_IF_EMPTY_DATA_VALUE; }
  public boolean isSkipIfEmpty() { return this.emptyDataValueSetting == MM_SKIP_IF_EMPTY_DATA_VALUE; }
  public boolean isProcessIfEmpty() { return this.emptyDataValueSetting == MM_PROCESS_IF_EMPTY_DATA_VALUE; }

  @Override public String getNodeName()
  {
    return "EmptyDataValueDirective";
  }

  public String toString() { return ParserUtil.getTokenName(this.emptyDataValueSetting); }
}

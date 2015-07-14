
package org.mm.parser.node;

import org.mm.parser.ASTEmptyDataValueSetting;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class EmptyDataValueDirectiveNode implements MappingMasterParserConstants
{
  private int emptyDataValueSetting;
  
  public EmptyDataValueDirectiveNode(ASTEmptyDataValueSetting node) throws ParseException
  {
    emptyDataValueSetting = node.emptyDataValueSetting;
  }
  
  public int getEmptyDataValueSetting() { return emptyDataValueSetting; }
  
  public boolean isErrorIfEmpty() { return emptyDataValueSetting == MM_ERROR_IF_EMPTY_DATA_VALUE; }
  public boolean isWarningIfEmpty() { return emptyDataValueSetting == MM_WARNING_IF_EMPTY_DATA_VALUE; }
  public boolean isSkipIfEmpty() { return emptyDataValueSetting == MM_SKIP_IF_EMPTY_DATA_VALUE; }
  public boolean isProcessIfEmpty() { return emptyDataValueSetting == MM_PROCESS_IF_EMPTY_DATA_VALUE; }
  
  public String toString() { return ParserUtil.getTokenName(emptyDataValueSetting); }
}

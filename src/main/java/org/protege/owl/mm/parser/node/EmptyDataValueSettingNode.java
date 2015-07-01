
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTEmptyDataValueSetting;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class EmptyDataValueSettingNode implements MappingMasterParserConstants
{
  private int emptyDataValueSetting;
  
  public EmptyDataValueSettingNode(ASTEmptyDataValueSetting node) throws ParseException
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

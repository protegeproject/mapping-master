
package org.mm.parser.node;

import org.mm.parser.ASTEmptyLocationSetting;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class EmptyLocationSettingNode implements MappingMasterParserConstants
{
  private int emptyLocationSetting;
  
  public EmptyLocationSettingNode(ASTEmptyLocationSetting node) throws ParseException
  {
  	emptyLocationSetting = node.emptyLocationSetting;
  }
  
  public int getEmptyLocationSetting() { return emptyLocationSetting; }
  
  public boolean isErrorIfEmpty() { return emptyLocationSetting == MM_ERROR_IF_EMPTY_LOCATION; }
  public boolean isWarningIfEmpty() { return emptyLocationSetting == MM_WARNING_IF_EMPTY_LOCATION; }
  public boolean isSkipIfEmpty() { return emptyLocationSetting == MM_SKIP_IF_EMPTY_LOCATION; }
  public boolean isProcessIfEmpty() { return emptyLocationSetting == MM_PROCESS_IF_EMPTY_LOCATION; }
  
  public String toString() { return ParserUtil.getTokenName(emptyLocationSetting); }
}

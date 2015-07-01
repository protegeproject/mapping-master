
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTShiftSetting;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class ShiftSettingNode implements MappingMasterParserConstants
{
  private int shiftSetting;
  
  public ShiftSettingNode(ASTShiftSetting node) throws ParseException
  {
  	shiftSetting = node.shiftSetting;
  }
  
  public boolean isNoShift() { return shiftSetting == MM_NO_SHIFT; }
  public boolean isShiftLeft() { return shiftSetting == MM_SHIFT_LEFT; }
  public boolean isShiftRight() { return shiftSetting == MM_SHIFT_RIGHT; }
  public boolean isShiftUp() { return shiftSetting == MM_SHIFT_UP; }
  public boolean isShiftDown() { return shiftSetting == MM_SHIFT_DOWN; }
  
  public int getShiftSetting() { return shiftSetting; }
  
  public String toString() { return ParserUtil.getTokenName(shiftSetting); }
}

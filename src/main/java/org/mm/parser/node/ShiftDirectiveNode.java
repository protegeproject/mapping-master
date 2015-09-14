package org.mm.parser.node;

import org.mm.parser.ASTShiftSetting;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class ShiftDirectiveNode implements MMNode, MappingMasterParserConstants
{
   private final int shiftSetting;

   public ShiftDirectiveNode(ASTShiftSetting node) throws ParseException
   {
      this.shiftSetting = node.shiftSetting;
   }

   public boolean isNoShift()
   {
      return this.shiftSetting == MM_NO_SHIFT;
   }

   public boolean isShiftLeft()
   {
      return this.shiftSetting == MM_SHIFT_LEFT;
   }

   public boolean isShiftRight()
   {
      return this.shiftSetting == MM_SHIFT_RIGHT;
   }

   public boolean isShiftUp()
   {
      return this.shiftSetting == MM_SHIFT_UP;
   }

   public boolean isShiftDown()
   {
      return this.shiftSetting == MM_SHIFT_DOWN;
   }

   public int getShiftSetting()
   {
      return this.shiftSetting;
   }

   public String getShiftSettingName()
   {
      return ParserUtil.getTokenName(this.shiftSetting);
   }

   @Override
   public String getNodeName()
   {
      return "ShiftDirective";
   }

   public String toString()
   {
      return getShiftSettingName();
   }
}

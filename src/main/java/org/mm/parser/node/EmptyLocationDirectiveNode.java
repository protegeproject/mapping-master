package org.mm.parser.node;

import org.mm.parser.ASTEmptyLocationSetting;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class EmptyLocationDirectiveNode implements ReferenceDirectiveNode
{
   private final int emptyLocationSetting;

   public EmptyLocationDirectiveNode(ASTEmptyLocationSetting node) throws ParseException
   {
      this.emptyLocationSetting = node.emptyLocationSetting;
   }

   public int getEmptyLocationSetting()
   {
      return this.emptyLocationSetting;
   }

   public boolean isErrorIfEmpty()
   {
      return this.emptyLocationSetting == MM_ERROR_IF_EMPTY_LOCATION;
   }

   public boolean isWarningIfEmpty()
   {
      return this.emptyLocationSetting == MM_WARNING_IF_EMPTY_LOCATION;
   }

   public boolean isSkipIfEmpty()
   {
      return this.emptyLocationSetting == MM_SKIP_IF_EMPTY_LOCATION;
   }

   public boolean isProcessIfEmpty()
   {
      return this.emptyLocationSetting == MM_PROCESS_IF_EMPTY_LOCATION;
   }

   @Override
   public String getNodeName()
   {
      return "EmptyLocationDirective";
   }

   public String toString()
   {
      return ParserUtil.getTokenName(this.emptyLocationSetting);
   }
}

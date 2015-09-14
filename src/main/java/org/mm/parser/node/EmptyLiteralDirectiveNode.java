package org.mm.parser.node;

import org.mm.parser.ASTEmptyLiteralSetting;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class EmptyLiteralDirectiveNode implements MMNode, MappingMasterParserConstants
{
   private final int emptyLiteralSetting;

   public EmptyLiteralDirectiveNode(ASTEmptyLiteralSetting node) throws ParseException
   {
      this.emptyLiteralSetting = node.emptyLiteralSetting;
   }

   public int getEmptyLiteralSetting()
   {
      return this.emptyLiteralSetting;
   }

   public boolean isErrorIfEmpty()
   {
      return this.emptyLiteralSetting == MM_ERROR_IF_EMPTY_LITERAL;
   }

   public boolean isWarningIfEmpty()
   {
      return this.emptyLiteralSetting == MM_WARNING_IF_EMPTY_LITERAL;
   }

   public boolean isSkipIfEmpty()
   {
      return this.emptyLiteralSetting == MM_SKIP_IF_EMPTY_LITERAL;
   }

   public boolean isProcessIfEmpty()
   {
      return this.emptyLiteralSetting == MM_PROCESS_IF_EMPTY_LITERAL;
   }

   @Override
   public String getNodeName()
   {
      return "EmptyLiteralDirective";
   }

   public String toString()
   {
      return ParserUtil.getTokenName(this.emptyLiteralSetting);
   }
}

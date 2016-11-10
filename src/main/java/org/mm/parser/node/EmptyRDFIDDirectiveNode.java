package org.mm.parser.node;

import org.mm.parser.ASTEmptyRDFIDSetting;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class EmptyRDFIDDirectiveNode implements ReferenceDirectiveNode
{
   private final int emptyRDFIDSetting;

   public EmptyRDFIDDirectiveNode(ASTEmptyRDFIDSetting node) throws ParseException
   {
      this.emptyRDFIDSetting = node.emptyRDFIDSetting;
   }

   public int getEmptyRDFIDSetting()
   {
      return this.emptyRDFIDSetting;
   }

   public boolean isErrorIfEmpty()
   {
      return this.emptyRDFIDSetting == MM_ERROR_IF_EMPTY_ID;
   }

   public boolean isWarningIfEmpty()
   {
      return this.emptyRDFIDSetting == MM_WARNING_IF_EMPTY_ID;
   }

   public boolean isSkipIfEmpty()
   {
      return this.emptyRDFIDSetting == MM_SKIP_IF_EMPTY_ID;
   }

   public boolean isProcessIfEmpty()
   {
      return this.emptyRDFIDSetting == MM_PROCESS_IF_EMPTY_ID;
   }

   @Override
   public String getNodeName()
   {
      return "EmptyRDFIDDirective";
   }

   public String toString()
   {
      return ParserUtil.getTokenName(this.emptyRDFIDSetting);
   }
}

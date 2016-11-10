package org.mm.parser.node;

import org.mm.parser.ASTEmptyRDFSLabelSetting;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class EmptyRDFSLabelDirectiveNode implements ReferenceDirectiveNode
{
   private final int emptyRDFSLabelSetting;

   public EmptyRDFSLabelDirectiveNode(ASTEmptyRDFSLabelSetting node) throws ParseException
   {
      this.emptyRDFSLabelSetting = node.emptyRDFSLabelSetting;
   }

   public int getEmptyRDFSLabelSetting()
   {
      return this.emptyRDFSLabelSetting;
   }

   public String getEmptyRDFSLabelSettingName()
   {
      return ParserUtil.getTokenName(this.emptyRDFSLabelSetting);
   }

   public boolean isErrorIfEmpty()
   {
      return this.emptyRDFSLabelSetting == MM_ERROR_IF_EMPTY_LABEL;
   }

   public boolean isWarningIfEmpty()
   {
      return this.emptyRDFSLabelSetting == MM_WARNING_IF_EMPTY_LABEL;
   }

   public boolean isSkipIfEmpty()
   {
      return this.emptyRDFSLabelSetting == MM_SKIP_IF_EMPTY_LABEL;
   }

   public boolean isProcessIfEmpty()
   {
      return this.emptyRDFSLabelSetting == MM_PROCESS_IF_EMPTY_LABEL;
   }

   @Override
   public String getNodeName()
   {
      return "EmptyRDFSLabelDirective";
   }

   public String toString()
   {
      return getEmptyRDFSLabelSettingName();
   }
}

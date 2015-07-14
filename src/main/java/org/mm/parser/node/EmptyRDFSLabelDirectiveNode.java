package org.mm.parser.node;

import org.mm.parser.ASTEmptyRDFSLabelSetting;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class EmptyRDFSLabelDirectiveNode implements MappingMasterParserConstants
{
  private int emptyRDFSLabelSetting;

  public EmptyRDFSLabelDirectiveNode(ASTEmptyRDFSLabelSetting node) throws ParseException
  {
    emptyRDFSLabelSetting = node.emptyRDFSLabelSetting;
  }

  public int getEmptyRDFSLabelSetting() { return emptyRDFSLabelSetting; }

  public String getEmptyRDFSLabelSettingName() { return ParserUtil.getTokenName(emptyRDFSLabelSetting); }

  public boolean isErrorIfEmpty() { return emptyRDFSLabelSetting == MM_ERROR_IF_EMPTY_LABEL; }

  public boolean isWarningIfEmpty() { return emptyRDFSLabelSetting == MM_WARNING_IF_EMPTY_LABEL; }

  public boolean isSkipIfEmpty() { return emptyRDFSLabelSetting == MM_SKIP_IF_EMPTY_LABEL; }

  public boolean isProcessIfEmpty() { return emptyRDFSLabelSetting == MM_PROCESS_IF_EMPTY_LABEL; }

  public String toString() { return getEmptyRDFSLabelSettingName(); }
}

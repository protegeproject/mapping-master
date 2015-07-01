
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTEmptyRDFSLabelSetting;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class EmptyRDFSLabelSettingNode implements MappingMasterParserConstants
{
  private int emptyRDFSLabelSetting;
  
  public EmptyRDFSLabelSettingNode(ASTEmptyRDFSLabelSetting node) throws ParseException
  {
  	emptyRDFSLabelSetting = node.emptyRDFSLabelSetting;
  }
  
  public int getEmptyRDFSLabelSetting() { return emptyRDFSLabelSetting; }
  
  public boolean isErrorIfEmpty() { return emptyRDFSLabelSetting == MM_ERROR_IF_EMPTY_LABEL; }
  public boolean isWarningIfEmpty() { return emptyRDFSLabelSetting == MM_WARNING_IF_EMPTY_LABEL; }
  public boolean isSkipIfEmpty() { return emptyRDFSLabelSetting == MM_SKIP_IF_EMPTY_LABEL; }
  public boolean isProcessIfEmpty() { return emptyRDFSLabelSetting == MM_PROCESS_IF_EMPTY_LABEL; }
  
  public String toString() { return ParserUtil.getTokenName(emptyRDFSLabelSetting); }
}

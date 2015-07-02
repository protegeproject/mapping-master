
package org.mm.parser.node;

import org.mm.parser.ASTOWLSomeValuesFromDataType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLSomeValuesFromDataTypeNode implements MappingMasterParserConstants
{
  private int dataType;

  public OWLSomeValuesFromDataTypeNode(ASTOWLSomeValuesFromDataType node) throws ParseException
  {
    dataType = node.dataType;
  }

  public String getDataTypeName() { return tokenImage[dataType].substring(1, tokenImage[dataType].length() - 1); }

  public String toString() { return " SOME " + getDataTypeName(); }
}

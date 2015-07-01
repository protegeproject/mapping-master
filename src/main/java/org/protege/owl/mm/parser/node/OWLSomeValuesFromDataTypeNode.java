
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLSomeValuesFromDataType;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;

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

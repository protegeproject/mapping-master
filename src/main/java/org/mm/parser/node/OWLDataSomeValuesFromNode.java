
package org.mm.parser.node;

import org.mm.parser.ASTOWLDataSomeValuesFrom;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLDataSomeValuesFromNode implements MappingMasterParserConstants
{
  private int datatype;

  public OWLDataSomeValuesFromNode(ASTOWLDataSomeValuesFrom node) throws ParseException
  {
    datatype = node.datatype;
  }

  public String getDataTypeName() { return tokenImage[datatype].substring(1, tokenImage[datatype].length() - 1); }

  public String toString() { return " SOME " + getDataTypeName(); }
}

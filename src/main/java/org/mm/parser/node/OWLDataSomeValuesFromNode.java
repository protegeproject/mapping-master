
package org.mm.parser.node;

import org.mm.parser.ASTOWLDataSomeValuesFrom;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLDataSomeValuesFromNode implements MMNode, MappingMasterParserConstants
{
  private int datatype;

  public OWLDataSomeValuesFromNode(ASTOWLDataSomeValuesFrom node) throws ParseException
  {
    datatype = node.datatype;
  }

  public String getDatatypeName() { return tokenImage[datatype].substring(1, tokenImage[datatype].length() - 1); }

  @Override public String getNodeName()
  {
    return "OWLDataSomeValuesFrom";
  }

  public String toString() { return " SOME " + getDatatypeName(); }
}

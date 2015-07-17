package org.mm.parser.node;

import org.mm.parser.ASTOWLDataAllValuesFrom;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLDataAllValuesFromNode implements MMNode, MappingMasterParserConstants
{
  int datatype;

  OWLDataAllValuesFromNode(ASTOWLDataAllValuesFrom node) throws ParseException
  {
    datatype = node.datatype;
  }

  public String getDatatypeName()
  {
    return tokenImage[datatype].substring(1, tokenImage[datatype].length() - 1);
  }

  @Override public String getNodeName()
  {
    return "OWLDataAllValuesFrom";
  }

  public String toString()
  {
    return "ONLY " + getDatatypeName() + ")";
  }
}

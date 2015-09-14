package org.mm.parser.node;

import org.mm.parser.ASTOWLDataAllValuesFrom;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLDataAllValuesFromNode implements MMNode, MappingMasterParserConstants
{
   private final int datatype;

   OWLDataAllValuesFromNode(ASTOWLDataAllValuesFrom node) throws ParseException
   {
      this.datatype = node.datatype;
   }

   public String getDatatypeName()
   {
      return tokenImage[this.datatype].substring(1, tokenImage[this.datatype].length() - 1);
   }

   @Override
   public String getNodeName()
   {
      return "OWLDataAllValuesFrom";
   }

   public String toString()
   {
      return "ONLY " + getDatatypeName() + ")";
   }
}

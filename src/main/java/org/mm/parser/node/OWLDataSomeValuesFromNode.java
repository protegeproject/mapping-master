package org.mm.parser.node;

import org.mm.parser.ASTOWLDataSomeValuesFrom;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLDataSomeValuesFromNode implements MMNode, MappingMasterParserConstants
{
   private final int datatype;

   public OWLDataSomeValuesFromNode(ASTOWLDataSomeValuesFrom node) throws ParseException
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
      return "OWLDataSomeValuesFrom";
   }

   public String toString()
   {
      return " SOME " + getDatatypeName();
   }
}

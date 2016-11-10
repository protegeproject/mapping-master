package org.mm.parser.node;

import org.mm.parser.ASTOWLDataAllValuesFrom;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLDataAllValuesFromNode implements OWLNode, MappingMasterParserConstants // TODO: Rename to OWLDataAllValuesNode
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

   @Override
   public void accept(OWLNodeVisitor visitor) {
      visitor.visit(this);
   }

   public String toString()
   {
      return "ONLY " + getDatatypeName() + ")";
   }
}

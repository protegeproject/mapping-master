package org.mm.parser.node;

import org.mm.core.ReferenceType;
import org.mm.parser.ASTReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class ReferenceTypeNode implements MMNode, MappingMasterParserConstants // TODO: Rename to MMReferenceTypeNode
{
   private final ReferenceType referenceType;

   public ReferenceTypeNode(ASTReferenceType node) throws ParseException
   {
      this.referenceType = new ReferenceType(node.referenceType);
   }

   public ReferenceTypeNode(int type)
   {
      this.referenceType = new ReferenceType(type);
   }

   public ReferenceTypeNode(ReferenceType referenceType)
   {
      this.referenceType = referenceType;
   }

   public ReferenceType getReferenceType()
   {
      return this.referenceType;
   }

   @Override
   public String getNodeName()
   {
      return "ReferenceType";
   }

   public String toString()
   {
      return this.referenceType.toString();
   }
}

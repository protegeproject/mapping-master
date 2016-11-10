package org.mm.parser.node;

import org.mm.parser.ASTIRIEncoding;
import org.mm.parser.ParseException;

public class IRIEncodingDirectiveNode implements ReferenceDirectiveNode
{
   private final int valueEncodingType;

   public IRIEncodingDirectiveNode(ASTIRIEncoding node) throws ParseException
   {
      this.valueEncodingType = node.encodingType;
   }

   public IRIEncodingDirectiveNode(int defaultValueEncoding)
   {
      this.valueEncodingType = defaultValueEncoding;
   }

   @Override
   public String getNodeName()
   {
      return "IRIEncodingDirective";
   }

   public int getValueEncodingType()
   {
      return this.valueEncodingType;
   }

   public String getValueEncodingTypeName()
   {
      return tokenImage[this.valueEncodingType].substring(1, tokenImage[this.valueEncodingType].length() - 1);
   }

   public boolean useCamelCaseEncoding()
   {
      return this.valueEncodingType == MM_CAMELCASE_ENCODE;
   }

   public boolean useSnakeCaseEncoding()
   {
      return this.valueEncodingType == MM_SNAKECASE_ENCODE;
   }

   public boolean useUUIDEncoding()
   {
      return this.valueEncodingType == MM_UUID_ENCODE;
   }

   public boolean useHashEncoding()
   {
      return this.valueEncodingType == MM_HASH_ENCODE;
   }

   public String toString()
   {
      return getValueEncodingTypeName();
   }

   public int hashCode()
   {
      int hash = 15;
      hash = hash + this.valueEncodingType;
      return hash;
   }
}

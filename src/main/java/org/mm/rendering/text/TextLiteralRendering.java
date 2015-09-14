package org.mm.rendering.text;

import org.mm.core.OWLLiteralType;
import org.mm.rendering.OWLLiteralRendering;

public class TextLiteralRendering extends TextRendering implements OWLLiteralRendering
{
   private final OWLLiteralType literalType;

   public TextLiteralRendering(String rawValue, OWLLiteralType literalType)
   {
      super(rawValue);
      this.literalType = literalType;
   }

   public TextLiteralRendering(String value)
   {
      this(value, OWLLiteralType.STRING_LITERAL_TYPE);
   }

   public TextLiteralRendering(int value)
   {
      this("" + value, OWLLiteralType.INT_LITERAL_TYPE);
   }

   public TextLiteralRendering(boolean value)
   {
      this("" + value, OWLLiteralType.BOOLEAN_LITERAL_TYPE);
   }

   public TextLiteralRendering(float value)
   {
      this("" + value, OWLLiteralType.FLOAT_LITERAL_TYPE);
   }

   @Override
   public String getRawValue()
   {
      return getRendering();
   }

   @Override
   public OWLLiteralType getOWLLiteralType()
   {
      return this.literalType;
   }
}

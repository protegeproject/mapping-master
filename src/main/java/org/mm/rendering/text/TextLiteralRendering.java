package org.mm.rendering.text;

import org.mm.core.OWLLiteralType;
import org.mm.rendering.LiteralRendering;

public class TextLiteralRendering extends TextRendering implements LiteralRendering
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

   public boolean isQuoted()
   {
      return literalType.isQuotedOWLLiteral();
   }
}

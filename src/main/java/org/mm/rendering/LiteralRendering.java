package org.mm.rendering;

import org.mm.core.OWLLiteralType;

public interface LiteralRendering extends Rendering
{
   String getRawValue();

   OWLLiteralType getOWLLiteralType();
}

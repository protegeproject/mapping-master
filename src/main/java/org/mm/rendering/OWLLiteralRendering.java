package org.mm.rendering;

import org.mm.core.OWLLiteralType;

public interface OWLLiteralRendering extends Rendering
{
   String getRawValue();

   OWLLiteralType getOWLLiteralType();
}

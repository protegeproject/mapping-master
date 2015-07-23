package org.mm.rendering.text;

import org.mm.rendering.Rendering;

public class TextRendering implements Rendering
{
  private final String textRepresentation;

  public TextRendering(String textRepresentation)
  {
    this.textRepresentation = textRepresentation;
  }

  public String getTextRendering()
  {
    if (textRepresentation.startsWith("\"")) // TODO Hack
      return textRepresentation.substring(1, textRepresentation.length() - 1);
    else
      return textRepresentation;
  }

  @Override public String toString()
  {
    return getTextRendering();
  }
}

package org.mm.rendering;

public class TextRendering implements Rendering
{
  private final String textRendering;

  public TextRendering(String textRendering)
  {
    this.textRendering = textRendering;
  }

  public String getTextRendering()
  {
    if (textRendering.startsWith("\"")) // TODO Hack
      return textRendering.substring(1, textRendering.length() - 1);
    else
      return textRendering;
  }

  @Override public String toString()
  {
    return getTextRendering();
  }
}

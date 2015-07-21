package org.mm.renderer;

public class TextRendering implements Rendering
{
  private String rendering;

  public TextRendering()
  {
    this.rendering = "";
  }

  public TextRendering(String initialTextRendering)
  {
    rendering = initialTextRendering;
  }

  public boolean nothingRendered()
  {
    return rendering.equals("") || rendering.equals("\"\"");
  } // TODO: last clause is hack

  public void addText(String text)
  {
    this.rendering = rendering.concat(text);
  }

  public String getTextRendering()
  {
    stripDoubleQuotesIfNecessary(); // TODO: hack

    return this.rendering;
  }

  @Override public String toString()
  {
    return getTextRendering();
  }

  private void stripDoubleQuotesIfNecessary()
  {
    if (rendering.startsWith("\""))
      rendering = rendering.substring(1, rendering.length() - 1);
  }
}

package org.mm.renderer;

public class Rendering
{
  private String textRendering;
  private StringBuffer loggingText;

  public Rendering()
  {
    textRendering = "";
    loggingText = new StringBuffer();
  }

  public Rendering(String initialTextRendering)
  {
    textRendering = initialTextRendering;
  }

  public boolean nothingRendered()
  {
    return textRendering.equals("") || textRendering.equals("\"\"");
  } // TODO: last clause is hack

  public void addText(String text)
  {
    this.textRendering = textRendering.concat(text);
  }

  public void setTextRendering(String textRendering)
  {
    this.textRendering = textRendering;
  }

  // TODO Use real logger
  public void log(String loggingText)
  {
    this.loggingText.append(loggingText);
    System.err.print(loggingText);
  }

  public void logLine(String loggingText)
  {
    log(loggingText + "\n");
  }

  public String getTextRendering()
  {
    stripDoubleQuotesIfNecessary(); // TODO: hack

    return this.textRendering;
  }

  public void addLoggingTextNewLine()
  {
    log("\n");
  }

  public String getLoggingText()
  {
    return loggingText.toString();
  }

  private void stripDoubleQuotesIfNecessary()
  {
    if (textRendering.startsWith("\""))
      textRendering = textRendering.substring(1, textRendering.length() - 1);
  }

  @Override public String toString()
  {
    return getTextRendering();
  }
}

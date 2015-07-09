package org.mm.renderer;

public class Rendering
{
  private String finalRendering;
  private StringBuffer loggingText;

  public Rendering()
  {
    finalRendering = "";
    loggingText = new StringBuffer();
  }

  public Rendering(String initialTextRendering)
  {
    finalRendering = initialTextRendering;
  }

  public boolean nothingRendered()
  {
    return finalRendering.equals("") || finalRendering.equals("\"\"");
  } // TODO: last clause is hack

  public void addText(String text)
  {
    this.finalRendering = finalRendering.concat(text);
  }

  public void setTextRendering(String textRendering)
  {
    this.finalRendering = textRendering;
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

  public String getRendering()
  {
    stripDoubleQuotesIfNecessary(); // TODO: hack

    return finalRendering;
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
    if (finalRendering.startsWith("\""))
      finalRendering = finalRendering.substring(1, finalRendering.length() - 1);
  }

  @Override public String toString()
  {
    return getRendering();
  }
}

package org.mm.renderer;

public interface Rendering
{
  void addText(String text);

  String getTextRendering();

  void log(String loggingText);

  void logLine(String loggingText);

  void addLoggingTextNewLine();

  String getLoggingText();
}

package org.mm.renderer;

public interface Rendering
{
  void log(String loggingText);

  void logLine(String loggingText);

  void addLoggingTextNewLine();

  String getLoggingText();
}

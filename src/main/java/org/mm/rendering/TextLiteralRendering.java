package org.mm.rendering;

public class TextLiteralRendering extends TextRendering implements OWLLiteralRendering
{
  private final String rawValue;

  public TextLiteralRendering(String rawValue)
  {
    super(rawValue);
    this.rawValue = rawValue;
  }

  @Override public String getRawValue()
  {
    return this.rawValue;
  }

  @Override public boolean isString()
  {
    return true; // TODO
  }

  @Override public boolean isBoolean()
  {
    return false;  // TODO
  }

  @Override public boolean isByte()
  {
    return false;  // TODO
  }

  @Override public boolean isShort()
  {
    return false;  // TODO
  }

  @Override public boolean isInt()
  {
    return false;  // TODO
  }

  @Override public boolean isLong()
  {
    return false;  // TODO
  }

  @Override public boolean isFloat()
  {
    return false;  // TODO
  }

  @Override public boolean isDouble()
  {
    return false;  // TODO
  }

  @Override public boolean isDate()
  {
    return false;   // TODO
  }

  @Override public boolean isTime()
  {
    return false;  // TODO
  }

  @Override public boolean isDateTime()
  {
    return false;  // TODO
  }

  @Override public boolean isDuration()
  {
    return false;  // TODO
  }
}

package org.mm.rendering;

public interface OWLLiteralRendering extends Rendering
{
  String getRawValue();

  boolean isString();

  boolean isBoolean();

  boolean isByte();

  boolean isShort();

  boolean isInt();

  boolean isLong();

  boolean isFloat();

  boolean isDouble();

  boolean isDate();

  boolean isTime();

  boolean isDateTime();

  boolean isDuration();
}

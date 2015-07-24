package org.mm.parser.node;

import org.mm.parser.ASTIntegerLiteral;
import org.mm.parser.ParseException;

public class IntegerLiteralNode implements MMNode
{
  private final int value;

  IntegerLiteralNode(ASTIntegerLiteral node) throws ParseException
  {
    this.value = node.value;
  }

  public int getValue() { return this.value; }

  @Override public String getNodeName()
  {
    return "IntegerLiteral";
  }

  public String toString() { return "" + this.value; }
}

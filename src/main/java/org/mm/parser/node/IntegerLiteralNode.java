package org.mm.parser.node;

import org.mm.parser.ASTIntegerLiteral;
import org.mm.parser.ParseException;

public class IntegerLiteralNode implements MMNode
{
  int value;

  IntegerLiteralNode(ASTIntegerLiteral node) throws ParseException
  {
    value = node.value;
  }

  public int getValue() { return value; }

  @Override public String getNodeName()
  {
    return "IntegerLiteral";
  }

  public String toString() { return "" + value; }
}

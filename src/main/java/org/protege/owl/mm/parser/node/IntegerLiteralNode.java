package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTIntegerLiteral;
import org.protege.owl.mm.parser.ParseException;

public class IntegerLiteralNode
{
  int value;

  IntegerLiteralNode(ASTIntegerLiteral node) throws ParseException
  {
    value = node.value;
  }

  public int getValue() { return value; }

  public String toString() { return "" + value; }
}

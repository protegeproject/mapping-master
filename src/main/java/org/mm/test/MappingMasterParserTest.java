package org.mm.test;

import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.node.ExpressionNode;
import org.mm.renderer.DefaultRenderer;
import org.mm.parser.SimpleNode;
import org.mm.renderer.Renderer;

import java.io.ByteArrayInputStream;

public class MappingMasterParserTest
{
  public static void main(String args[])
  {
    if (args.length != 1)
      Usage();

    String expressionText = args[0];

    try {
      MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expressionText.getBytes()));
      Renderer renderer = new DefaultRenderer();

      SimpleNode expressionNode = parser.expression();
      ExpressionNode expression = new ExpressionNode((ASTExpression)expressionNode);

      expressionNode.dump(" ");

      System.err.println("expression.toString()      : " + expression.toString());
      System.err.println("renderer.render(expression): " + renderer.renderExpression(expression));

    } catch (ParseException e) {
      System.err.println("ParseException: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void Usage()
  {
    System.err.println("Usage: MappingMasterParserTest <Expression>");
    System.exit(-1);
  }
}
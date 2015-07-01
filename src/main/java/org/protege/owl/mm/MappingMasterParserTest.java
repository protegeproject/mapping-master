package org.protege.owl.mm;

import org.protege.owl.mm.parser.ASTExpression;
import org.protege.owl.mm.parser.node.ExpressionNode;
import org.protege.owl.mm.parser.MappingMasterParser;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.SimpleNode;
import org.protege.owl.mm.renderer.DefaultRenderer;
import org.protege.owl.mm.renderer.Renderer;

import java.io.ByteArrayInputStream;

public class MappingMasterParserTest
{
  public static void main(String args[])
  {
    MappingMasterParser parser;
    Renderer renderer = new DefaultRenderer();
    SimpleNode expressionNode;
    ExpressionNode expression;
    String expressionText;

    if (args.length != 1)
      Usage();

    expressionText = args[0];

    try {
      parser = new MappingMasterParser(new ByteArrayInputStream(expressionText.getBytes()));

      expressionNode = parser.expression();
      expression = new ExpressionNode((ASTExpression)expressionNode);

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
    System.err.println("Usage: org.protege.owl.mm.MappingMasterParserTest <Expression>");
    System.exit(-1);
  }
}
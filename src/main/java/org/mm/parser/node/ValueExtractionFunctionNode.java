package org.mm.parser.node;

import org.mm.parser.ASTValueExtractionFunction;
import org.mm.parser.ASTValueExtractionFunctionArgument;
import org.mm.parser.InternalParseException;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class ValueExtractionFunctionNode implements MMNode, MappingMasterParserConstants
{
  private final int functionID;
  private final List<ValueExtractionFunctionArgumentNode> argumentNodes;

  public ValueExtractionFunctionNode(ASTValueExtractionFunction node) throws ParseException
  {
    this.functionID = node.functionID;

    this.argumentNodes = new ArrayList<>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "ValueExtractionFunctionArgument")) {
        ValueExtractionFunctionArgumentNode valueExtractionFunctionArgument = new ValueExtractionFunctionArgumentNode(
          (ASTValueExtractionFunctionArgument)child);
        this.argumentNodes.add(valueExtractionFunctionArgument);
      } else
        throw new InternalParseException(
          "ValueExtractionFunction node expecting ValueExtractionFunctionArgument child, got " + child);
    }
  }

  public int getFunctionID() { return this.functionID; }

  public String getFunctionName() { return tokenImage[this.functionID].substring(1, tokenImage[this.functionID].length() - 1); }

  public boolean hasArguments() { return !this.argumentNodes.isEmpty(); }

  public List<ValueExtractionFunctionArgumentNode> getArgumentNodes() { return this.argumentNodes; }

  @Override public String getNodeName()
  {
    return "ValueExtractionFunction";
  }

  public String toString()
  {
    String representation = getFunctionName();

    if (hasArguments()) {
      boolean isFirst = true;
      representation += "(";
      for (ValueExtractionFunctionArgumentNode argument : this.argumentNodes) {
        if (!isFirst)
          representation += " ";
        representation += argument.toString();
        isFirst = false;
      }
      representation += ")";
    }
    return representation;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    ValueExtractionFunctionNode vef = (ValueExtractionFunctionNode)obj;
    return this.functionID == vef.functionID && this.argumentNodes != null && vef.argumentNodes != null
      && this.argumentNodes.equals(vef.argumentNodes);
  }

  public int hashCode()
  {
    int hash = 25;

    hash = hash + this.functionID;
    hash = hash + (null == this.argumentNodes ? 0 : this.argumentNodes.hashCode());

    return hash;
  }
} 

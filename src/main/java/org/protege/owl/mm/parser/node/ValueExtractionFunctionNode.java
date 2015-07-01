package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTStringOrReference;
import org.protege.owl.mm.parser.ASTValueExtractionFunction;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class ValueExtractionFunctionNode implements MappingMasterParserConstants
{
  private int functionID;
  private List<StringOrReferenceNode> argumentNodes;

  public ValueExtractionFunctionNode(ASTValueExtractionFunction node) throws ParseException
  {
    functionID = node.functionID;

    argumentNodes = new ArrayList<StringOrReferenceNode>();

    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);

      if (ParserUtil.hasName(child, "StringOrReference")) {
        StringOrReferenceNode stringOrReference = new StringOrReferenceNode((ASTStringOrReference)child);
        argumentNodes.add(stringOrReference);
      } else
        throw new InternalParseException(
          "ValueExtractionFunction node expecting StringOrReference child, got " + child.toString());
    }
  }

  public int getFunctionID() { return functionID; }

  public String getFunctionName() { return tokenImage[functionID].substring(1, tokenImage[functionID].length() - 1); }

  public boolean hasArguments() { return !argumentNodes.isEmpty(); }

  public List<StringOrReferenceNode> getArgumentNodes() { return argumentNodes; }

  public String toString()
  {
    String representation = getFunctionName();

    if (hasArguments()) {
      boolean isFirst = true;
      representation += "(";
      for (StringOrReferenceNode argument : argumentNodes) {
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
    if ((obj == null) || (obj.getClass() != this.getClass()))
      return false;
    ValueExtractionFunctionNode vef = (ValueExtractionFunctionNode)obj;
    return (functionID == vef.functionID && (argumentNodes != null && vef.argumentNodes != null && argumentNodes
      .equals(vef.argumentNodes)));
  }

  public int hashCode()
  {
    int hash = 25;

    hash = hash + functionID;
    hash = hash + (null == argumentNodes ? 0 : argumentNodes.hashCode());

    return hash;
  }
} 


package org.mm.parser.node;

import org.mm.parser.ASTStringLiteral;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class StringLiteralNode implements StringNode, MappingMasterParserConstants
{
	String value;

	public StringLiteralNode(ASTStringLiteral node) throws ParseException
	{
		value = node.value;
	}

	public String getNodeName()
	{
		return "StringLiteral";
	}

	public String getValue()
	{
		return value;
	}

	public String toString()
	{
		return "\"" + value + "\"";
	}
}

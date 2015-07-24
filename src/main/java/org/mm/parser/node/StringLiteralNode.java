
package org.mm.parser.node;

import org.mm.parser.ASTStringLiteral;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class StringLiteralNode implements StringNode, MappingMasterParserConstants
{
	private final String value;

	public StringLiteralNode(ASTStringLiteral node) throws ParseException
	{
		this.value = node.value;
	}

	public String getNodeName()
	{
		return "StringLiteral";
	}

	public String getValue()
	{
		return this.value;
	}

	public String toString()
	{
		return "\"" + this.value + "\"";
	}
}

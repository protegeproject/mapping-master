
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTStringLiteral;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;

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

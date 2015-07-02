
package org.mm.parser.node;

import org.mm.parser.ASTFloatLiteral;
import org.mm.parser.ParseException;

public class FloatLiteralNode implements MMNode
{
	float value;

	public FloatLiteralNode(ASTFloatLiteral node) throws ParseException
	{
		value = node.value;
	}

	public float getValue()
	{
		return value;
	}

	public String getNodeName()
	{
		return "FloatLiteral";
	}

	public String toString()
	{
		return "" + value;
	}

}

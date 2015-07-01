
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTBooleanLiteral;
import org.protege.owl.mm.parser.ParseException;

public class BooleanLiteralNode implements MMNode
{
	boolean value;

	public BooleanLiteralNode(ASTBooleanLiteral node) throws ParseException
	{
		value = node.value;
	}

	public boolean getValue()
	{
		return value;
	}

	public String getNodeName()
	{
		return "BooleanLiteral";
	}

	public String toString()
	{
		return "" + value;
	}

} 

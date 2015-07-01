package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTBooleanLiteral;
import org.protege.owl.mm.parser.ASTFloatLiteral;
import org.protege.owl.mm.parser.ASTIntegerLiteral;
import org.protege.owl.mm.parser.ASTLiteral;
import org.protege.owl.mm.parser.ASTStringLiteral;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class LiteralNode
{
	private IntegerLiteralNode integerLiteralNode = null;
	private FloatLiteralNode floatLiteralNode = null;
	private StringLiteralNode stringLiteralNode = null;
	private BooleanLiteralNode booleanLiteralNode = null;

	public LiteralNode(ASTLiteral node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one child of Literal node");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "IntegerLiteralNode"))
				integerLiteralNode = new IntegerLiteralNode((ASTIntegerLiteral)child);
			else if (ParserUtil.hasName(child, "FloatLiteral"))
				floatLiteralNode = new FloatLiteralNode((ASTFloatLiteral)child);
			else if (ParserUtil.hasName(child, "StringLiteral"))
				stringLiteralNode = new StringLiteralNode((ASTStringLiteral)child);
			else if (ParserUtil.hasName(child, "BooleanLiteral"))
				booleanLiteralNode = new BooleanLiteralNode((ASTBooleanLiteral)child);
			else
				throw new InternalParseException("unexpected child node " + child.toString() + " for Literal node");
		}
	}

	public boolean isInteger()
	{
		return integerLiteralNode != null;
	}
	
	public boolean isFloat()
	{
		return floatLiteralNode != null;
	}
	
	public boolean isString()
	{
		return stringLiteralNode != null;
	}
	
	public boolean isBoolean()
	{
		return booleanLiteralNode != null;
	}

	public IntegerLiteralNode getIntegerLiteralNode()
	{
		return integerLiteralNode;
	}
	
	public FloatLiteralNode getFloatLiteralNode()
	{
		return floatLiteralNode;
	}
	
	public StringLiteralNode getStringLiteralNode()
	{
		return stringLiteralNode;
	}
	
	public BooleanLiteralNode getBooleanLiteralNode()
	{
		return booleanLiteralNode;
	}

	public String toString()
	{
		if (isInteger())
			return integerLiteralNode.toString();
		else if (isFloat())
			return floatLiteralNode.toString();
		else if (isString())
			return stringLiteralNode.toString();
		else if (isBoolean())
			return booleanLiteralNode.toString();
		else
			return "";
	}
}

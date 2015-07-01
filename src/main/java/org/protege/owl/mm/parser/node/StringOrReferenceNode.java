
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTReference;
import org.protege.owl.mm.parser.ASTStringLiteral;
import org.protege.owl.mm.parser.ASTStringOrReference;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class StringOrReferenceNode
{
	private ReferenceNode referenceNode = null;
	private StringLiteralNode stringLiteralNode = null;

	public StringOrReferenceNode(ASTStringOrReference node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one child node for StringOrReference node");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "StringLiteral"))
				stringLiteralNode = new StringLiteralNode((ASTStringLiteral)child);
			else if (ParserUtil.hasName(child, "Reference"))
				referenceNode = new ReferenceNode((ASTReference)child);
			else
				throw new InternalParseException("unexpected child node " + child.toString() + " for StringOrReference node");
		}
	}

	public ReferenceNode getReferenceNode()
	{
		return referenceNode;
	}

	public StringLiteralNode getStringLiteralNode()
	{
		return stringLiteralNode;
	}

	public boolean isString()
	{
		return stringLiteralNode != null;
	}

	public boolean isReference()
	{
		return referenceNode != null;
	}

	public String toString()
	{
		if (isString())
			return stringLiteralNode.toString();
		else if (isReference())
			return referenceNode.toString();
		else
			return "";
	}
}

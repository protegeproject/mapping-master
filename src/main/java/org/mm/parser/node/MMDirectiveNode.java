
package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultPropertyValueType;
import org.mm.parser.ASTMMDefaultReferenceType;
import org.mm.parser.ASTMMDefaultValueEncoding;
import org.mm.parser.ASTMMDirective;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class MMDirectiveNode
{
	private MMDefaultValueEncodingNode defaultValueEncodingNode = null;
	private MMDefaultReferenceTypeNode defaultReferenceTypeNode = null;
	private MMDefaultPropertyValueTypeNode defaultPropertyValueTypeNode = null;

	public MMDirectiveNode(ASTMMDirective node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "MMDefaultValueEncoding")) {
				defaultValueEncodingNode = new MMDefaultValueEncodingNode((ASTMMDefaultValueEncoding)child);
			} else if (ParserUtil.hasName(child, "MMDefaultReferenceType")) {
				defaultReferenceTypeNode = new MMDefaultReferenceTypeNode((ASTMMDefaultReferenceType)child);
			} else if (ParserUtil.hasName(child, "MMDefaultPropertyValueType")) {
				defaultPropertyValueTypeNode = new MMDefaultPropertyValueTypeNode((ASTMMDefaultPropertyValueType)child);
			} else
				throw new InternalParseException("invalid child node " + child.toString() + " to MMExpression");
		}
	}

	public MMDefaultValueEncodingNode getDefaultValueEncodingNode()
	{
		return defaultValueEncodingNode;
	}

	public MMDefaultReferenceTypeNode getDefaultReferenceTypeNode()
	{
		return defaultReferenceTypeNode;
	}

	public MMDefaultPropertyValueTypeNode getDefaultPropertyValueTypeNode()
	{
		return defaultPropertyValueTypeNode;
	}

	public boolean hasDefaultValueEncoding()
	{
		return defaultValueEncodingNode != null;
	}

	public boolean hasDefaultReferenceType()
	{
		return defaultReferenceTypeNode != null;
	}

	public boolean hasDefaultPropertyValueType()
	{
		return defaultPropertyValueTypeNode != null;
	}

	public String toString()
	{
		String representation = "";

		if (hasDefaultValueEncoding())
			representation += defaultValueEncodingNode.toString();
		if (hasDefaultReferenceType())
			representation += defaultReferenceTypeNode.toString();
		if (hasDefaultPropertyValueType())
			representation += defaultPropertyValueTypeNode.toString();

		return representation;
	}

}

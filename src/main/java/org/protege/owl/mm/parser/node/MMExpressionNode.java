
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTMMDefaultEntityType;
import org.protege.owl.mm.parser.ASTMMDefaultPropertyValueType;
import org.protege.owl.mm.parser.ASTMMDefaultValueEncoding;
import org.protege.owl.mm.parser.ASTMMExpression;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class MMExpressionNode
{
	private MMDefaultValueEncodingNode defaultValueEncodingNode = null;
	private MMDefaultEntityTypeNode defaultEntityTypeNode = null;
	private MMDefaultPropertyValueTypeNode defaultPropertyValueTypeNode = null;

	public MMExpressionNode(ASTMMExpression node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "MMDefaultValueEncoding")) {
				defaultValueEncodingNode = new MMDefaultValueEncodingNode((ASTMMDefaultValueEncoding)child);
			} else if (ParserUtil.hasName(child, "MMDefaultEntityType")) {
				defaultEntityTypeNode = new MMDefaultEntityTypeNode((ASTMMDefaultEntityType)child);
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

	public MMDefaultEntityTypeNode getDefaultEntityTypeNode()
	{
		return defaultEntityTypeNode;
	}

	public MMDefaultPropertyValueTypeNode getDefaultPropertyValueTypeNode()
	{
		return defaultPropertyValueTypeNode;
	}

	public boolean hasDefaultValueEncoding()
	{
		return defaultValueEncodingNode != null;
	}

	public boolean hasDefaultEntityType()
	{
		return defaultEntityTypeNode != null;
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
		if (hasDefaultEntityType())
			representation += defaultEntityTypeNode.toString();
		if (hasDefaultPropertyValueType())
			representation += defaultPropertyValueTypeNode.toString();

		return representation;
	}

}

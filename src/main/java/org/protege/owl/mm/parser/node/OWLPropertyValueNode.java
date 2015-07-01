
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTLiteral;
import org.protege.owl.mm.parser.ASTName;
import org.protege.owl.mm.parser.ASTOWLPropertyValue;
import org.protege.owl.mm.parser.ASTReference;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class OWLPropertyValueNode implements MMNode
{
	private ReferenceNode referenceNode = null;
	private NameNode nameNode = null;
	private LiteralNode literalNode = null;

	public OWLPropertyValueNode(ASTOWLPropertyValue node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one child of OWLPropertyValue node");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "Reference"))
				referenceNode = new ReferenceNode((ASTReference)child);
			else if (ParserUtil.hasName(child, "Name"))
				nameNode = new NameNode((ASTName)child);
			else if (ParserUtil.hasName(child, "Literal"))
				literalNode = new LiteralNode((ASTLiteral)child);
			else
				throw new InternalParseException("unexpected child node " + child.toString() + " for OWLPropertyValue node");
		}
	}

	public ReferenceNode getReferenceNode()
	{
		return referenceNode;
	}

	public NameNode getNameNode()
	{
		return nameNode;
	}

	public LiteralNode getLiteralNode()
	{
		return literalNode;
	}

	public boolean isReference()
	{
		return referenceNode != null;
	}

	public boolean isName()
	{
		return nameNode != null;
	}

	public boolean isLiteral()
	{
		return literalNode != null;
	}

	public String getNodeName()
	{
		return "OWLPropertyValue";
	}

	public String toString()
	{
		if (isReference())
			return referenceNode.toString();
		else if (isName())
			return nameNode.toString();
		else if (isLiteral())
			return literalNode.toString();
		else
			return "";
	}
}

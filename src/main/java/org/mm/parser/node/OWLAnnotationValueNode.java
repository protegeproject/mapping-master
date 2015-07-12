package org.mm.parser.node;

import org.mm.parser.ASTLiteral;
import org.mm.parser.ASTOWLAnnotationValue;
import org.mm.parser.ParseException;
import org.mm.parser.ASTName;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

public class OWLAnnotationValueNode implements MMNode
{
	private ReferenceNode referenceNode = null;
	private NameNode nameNode = null;
	private LiteralNode literalNode = null;

	public OWLAnnotationValueNode(ASTOWLAnnotationValue node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one child of OWLAnnotationValue node");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "Reference"))
				referenceNode = new ReferenceNode((ASTReference)child);
			else if (ParserUtil.hasName(child, "Name"))
				nameNode = new NameNode((ASTName)child);
			else if (ParserUtil.hasName(child, "Literal"))
				literalNode = new LiteralNode((ASTLiteral)child);
			else
				throw new InternalParseException(
						"unexpected child node " + child.toString() + " for OWLPropertyAnnotationValue node");
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
		return "OWLAnnotationValue";
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


package org.mm.parser.node;

import org.mm.parser.ASTName;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.ASTReference;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLPropertyNode implements TypeNode
{
	private ReferenceNode referenceNode;
	private NameNode nameNode;

	public OWLPropertyNode(ASTOWLProperty node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one child node for node " + getNodeName());
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "Name"))
				nameNode = new NameNode((ASTName)child);
			else if (ParserUtil.hasName(child, "Reference"))
				referenceNode = new ReferenceNode((ASTReference)child);
			else
				throw new InternalParseException("unexpected child node " + child.toString() + " for " + getNodeName());
		}
	}

	public String getNodeName()
	{
		return "OWLProperty";
	}

	public ReferenceNode getReferenceNode()
	{
		return referenceNode;
	}

	public NameNode getNameNode()
	{
		return nameNode;
	}

	public boolean hasNameNode()
	{
		return nameNode != null;
	}

	public boolean hasReferenceNode()
	{
		return referenceNode != null;
	}

	@Override public boolean isOWLClassNode() { return false; }

	@Override public boolean isOWLPropertyNode() { return false; }

	@Override public boolean isReferenceNode() { return false; }

	public String toString()
	{
		if (hasNameNode())
			return nameNode.toString();
		else if (hasReferenceNode())
			return referenceNode.toString();
		else
			return "";
	}
}

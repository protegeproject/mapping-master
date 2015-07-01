
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTName;
import org.protege.owl.mm.parser.ASTOWLProperty;
import org.protege.owl.mm.parser.ASTReference;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class OWLPropertyNode implements MMNode
{
	private ReferenceNode referenceNode = null;
	private NameNode nameNode = null;

	public OWLPropertyNode(ASTOWLProperty node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one child node for OWLPropertyNode");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "Name"))
				nameNode = new NameNode((ASTName)child);
			else if (ParserUtil.hasName(child, "Reference"))
				referenceNode = new ReferenceNode((ASTReference)child);
			else
				throw new InternalParseException("unexpected child node " + child.toString() + " for OWLPropertyNode");
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

	public boolean isName()
	{
		return nameNode != null;
	}

	public boolean isReference()
	{
		return referenceNode != null;
	}

	public String getNodeName()
	{
		return "OWLProperty";
	}

	public String toString()
	{
		if (isName())
			return nameNode.toString();
		else if (isReference())
			return referenceNode.toString();
		else
			return "";
	}
}

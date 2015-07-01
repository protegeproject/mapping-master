
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTName;
import org.protege.owl.mm.parser.ASTOWLIndividual;
import org.protege.owl.mm.parser.ASTReference;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class OWLIndividualNode implements MMNode
{
	private ReferenceNode referenceNode = null;
	private NameNode nameNode = null;

	public OWLIndividualNode(ASTOWLIndividual node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one child node of OWLIndividual node");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "Name"))
				nameNode = new NameNode((ASTName)child);
			else if (ParserUtil.hasName(child, "Reference"))
				referenceNode = new ReferenceNode((ASTReference)child);
			else
				throw new InternalParseException("unexpected child node " + child.toString() + " for OWLIndividual node");
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
		return "OWLIndividual";
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

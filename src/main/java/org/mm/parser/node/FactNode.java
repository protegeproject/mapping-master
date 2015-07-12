package org.mm.parser.node;

import org.mm.parser.ASTFact;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.ASTOWLPropertyAssertionObject;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class FactNode
{
	private OWLPropertyNode owlPropertyNode;
	private OWLPropertyAssertionObjectNode owlPropertyAssertionObjectNode;

	public FactNode(ASTFact node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLProperty"))
				owlPropertyNode = new OWLPropertyNode((ASTOWLProperty)child);
			else if (ParserUtil.hasName(child, "OWLPropertyAssertionObject"))
				owlPropertyAssertionObjectNode = new OWLPropertyAssertionObjectNode((ASTOWLPropertyAssertionObject)child);
			else
				throw new InternalParseException("unexpect child node " + child.toString() + " for Fact");
		}
	}

	public OWLPropertyNode getOWLPropertyNode()
	{
		return owlPropertyNode;
	}

	public OWLPropertyAssertionObjectNode getOWLPropertyAssertionObjectNode()
	{
		return owlPropertyAssertionObjectNode;
	}

	public String toString()
	{
		return owlPropertyNode.toString() + " " + owlPropertyAssertionObjectNode.toString();
	}
}

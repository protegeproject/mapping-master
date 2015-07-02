
package org.mm.parser.node;

import org.mm.parser.ASTFact;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.ASTOWLPropertyValue;
import org.mm.parser.InternalParseException;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;
import org.mm.parser.Node;

public class FactNode
{
	private OWLPropertyNode owlPropertyNode;
	private OWLPropertyValueNode owlPropertyValueNode;

	public FactNode(ASTFact node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLProperty"))
				owlPropertyNode = new OWLPropertyNode((ASTOWLProperty)child);
			else if (ParserUtil.hasName(child, "OWLPropertyValue"))
				owlPropertyValueNode = new OWLPropertyValueNode((ASTOWLPropertyValue)child);
			else
				throw new InternalParseException("unexpect child node " + child.toString() + " for Fact");
		}
	}

	public OWLPropertyNode getOWLPropertyNode()
	{
		return owlPropertyNode;
	}

	public OWLPropertyValueNode getOWLPropertyValueNode()
	{
		return owlPropertyValueNode;
	}

	public String toString()
	{
		return owlPropertyNode.toString() + " " + owlPropertyValueNode.toString();
	}
}

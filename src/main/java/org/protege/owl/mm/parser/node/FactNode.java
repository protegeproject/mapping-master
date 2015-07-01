
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTFact;
import org.protege.owl.mm.parser.ASTOWLProperty;
import org.protege.owl.mm.parser.ASTOWLPropertyValue;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

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

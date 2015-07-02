
package org.mm.parser.node;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTOWLPropertyValue;
import org.mm.parser.ParseException;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

public class AnnotationFactNode
{
	private OWLPropertyNode owlPropertyNode;
	private OWLPropertyValueNode owlPropertyValueNode;

	public AnnotationFactNode(ASTAnnotationFact node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLProperty"))
				owlPropertyNode = new OWLPropertyNode((ASTOWLProperty)child);
			else if (ParserUtil.hasName(child, "OWLPropertyValue"))
				owlPropertyValueNode = new OWLPropertyValueNode((ASTOWLPropertyValue)child);
			else
				throw new InternalParseException("unexpect child node " + child.toString() + " for AnnotationFact");
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

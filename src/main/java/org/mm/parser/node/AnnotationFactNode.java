package org.mm.parser.node;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTOWLAnnotationValue;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class AnnotationFactNode
{
	private OWLPropertyNode owlPropertyNode;
	private OWLAnnotationValueNode owlAnnotationValueNode;

	public AnnotationFactNode(ASTAnnotationFact node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLProperty"))
				owlPropertyNode = new OWLPropertyNode((ASTOWLProperty)child);
			else if (ParserUtil.hasName(child, "OWLAnnotationValue"))
				owlAnnotationValueNode = new OWLAnnotationValueNode((ASTOWLAnnotationValue)child);
			else
				throw new InternalParseException("unexpected child node " + child.toString() + " for AnnotationFact");
		}
	}

	public OWLPropertyNode getOWLPropertyNode()
	{
		return owlPropertyNode;
	}

	public OWLAnnotationValueNode getOWLAnnotationValueNode()
	{
		return this.owlAnnotationValueNode;
	}

	public String toString()
	{
		return owlPropertyNode.toString() + " " + owlAnnotationValueNode.toString();
	}
}

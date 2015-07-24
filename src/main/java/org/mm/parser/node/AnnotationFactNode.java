package org.mm.parser.node;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTOWLAnnotationValue;
import org.mm.parser.ASTOWLProperty;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class AnnotationFactNode implements MMNode
{
	private OWLPropertyNode owlPropertyNode;
	private OWLAnnotationValueNode owlAnnotationValueNode;

	public AnnotationFactNode(ASTAnnotationFact node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLProperty"))
				this.owlPropertyNode = new OWLPropertyNode((ASTOWLProperty)child);
			else if (ParserUtil.hasName(child, "OWLAnnotationValue"))
				this.owlAnnotationValueNode = new OWLAnnotationValueNode((ASTOWLAnnotationValue)child);
			else
				throw new InternalParseException("unexpected child node " + child + " for " + getNodeName());
		}
	}

	public OWLPropertyNode getOWLPropertyNode()
	{
		return this.owlPropertyNode;
	}

	public OWLAnnotationValueNode getOWLAnnotationValueNode()
	{
		return this.owlAnnotationValueNode;
	}

	@Override public String getNodeName()
	{
		return "AnnotationFact";
	}

	public String toString()
	{
		return this.owlPropertyNode + " " + this.owlAnnotationValueNode;
	}
}

package org.mm.parser.node;

import org.mm.parser.ASTOWLObjectAllValuesFrom;
import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ParseException;
import org.mm.parser.ASTOWLNamedClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

public class OWLObjectAllValuesFromNode
{
	private OWLClassExpressionNode classExpressionNode;
	private OWLNamedClassNode namedClassNode;

	public OWLObjectAllValuesFromNode(ASTOWLObjectAllValuesFrom node) throws ParseException
	{
		Node child = node.jjtGetChild(0);
		if (ParserUtil.hasName(child, "OWLClassExpression"))
			classExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression)child);
		else if (ParserUtil.hasName(child, "OWLNamedClass"))
			namedClassNode = new OWLNamedClassNode((ASTOWLNamedClass)child);
		else
			throw new InternalParseException("OWLObjectAllValuesFrom node expecting OWLClassExpression child, got " + child.toString());
	}

	public boolean hasOWLClassExpression()
	{
		return classExpressionNode != null;
	}
	public boolean hasOWLNamedClass()
	{
		return namedClassNode != null;
	}

	public OWLClassExpressionNode getOWLClassExpressionNode()
	{
		return classExpressionNode;
	}
	public OWLNamedClassNode getOWLNamedClassNode()
	{
		return namedClassNode;
	}

	public String toString()
	{
		String representation = "ONLY ";

		if (hasOWLClassExpression())
			representation += classExpressionNode.toString();
		else if (hasOWLNamedClass())
			representation += namedClassNode.toString();

		representation += ")";

		return representation;
	}
}

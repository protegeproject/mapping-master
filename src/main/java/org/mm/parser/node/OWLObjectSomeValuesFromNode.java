package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLObjectSomeValuesFrom;
import org.mm.parser.ASTOWLObjectSomeValuesFrom;
import org.mm.parser.ASTOWLNamedClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLObjectSomeValuesFromNode
{
	private OWLClassExpressionNode classExpressionNode = null;
	private OWLNamedClassNode namedClassNode = null;

	public OWLObjectSomeValuesFromNode(ASTOWLObjectSomeValuesFrom node) throws ParseException
	{
		Node child = node.jjtGetChild(0);
		if (ParserUtil.hasName(child, "OWLClassExpression"))
			classExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression)child);
		else if (ParserUtil.hasName(child, "OWLNamedClass"))
			namedClassNode = new OWLNamedClassNode((ASTOWLNamedClass)child);
		else
			throw new InternalParseException("OWLObjectSomeValuesFrom node expecting OWLNamedCLass or OWLClassExpression child, got " + child.toString());
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
		String representation = "SOME ";

		if (hasOWLClassExpression())
			representation += classExpressionNode.toString();
		else if (hasOWLNamedClass())
			representation += namedClassNode.toString();

		return representation;
	}
}

package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLSomeValuesFromClass;
import org.mm.parser.ASTOWLNamedClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLSomeValuesFromClassNode
{
	private OWLClassExpressionNode owlClassExpressionNode = null;
	private OWLNamedClassNode owlNamedClassNode = null;

	public OWLSomeValuesFromClassNode(ASTOWLSomeValuesFromClass node) throws ParseException
	{
		Node child = node.jjtGetChild(0);
		if (ParserUtil.hasName(child, "OWLClassExpression"))
			owlClassExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression)child);
		else if (ParserUtil.hasName(child, "OWLNamedClass"))
			owlNamedClassNode = new OWLNamedClassNode((ASTOWLNamedClass)child);
		else
			throw new InternalParseException("OWLSomeValuesFromClass node expecting OWLNamedCLass or OWLClassExpression child, got " + child.toString());
	}

	public boolean hasOWLClassExpression()
	{
		return owlClassExpressionNode != null;
	}

	public boolean hasOWLNamedClass()
	{
		return owlNamedClassNode != null;
	}

	public OWLClassExpressionNode getOWLClassExpressionNode()
	{
		return owlClassExpressionNode;
	}

	public OWLNamedClassNode getOWLNamedClassNode()
	{
		return owlNamedClassNode;
	}

	public String toString()
	{
		String representation = "SOME ";

		if (hasOWLClassExpression())
			representation += owlClassExpressionNode.toString();
		else if (hasOWLNamedClass())
			representation += owlNamedClassNode.toString();

		return representation;
	}
}

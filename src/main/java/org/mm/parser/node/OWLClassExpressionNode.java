
package org.mm.parser.node;

import org.mm.parser.ParseException;
import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLUnionClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

public class OWLClassExpressionNode implements TypeNode
{
	private OWLUnionClassNode owlUnionClassNode;

	public OWLClassExpressionNode(ASTOWLClassExpression node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one OWLUnionClass child of OWLClassExpression node");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "OWLUnionClass"))
				owlUnionClassNode = new OWLUnionClassNode((ASTOWLUnionClass)child);
			else
				throw new InternalParseException("OWLClassExpression node expecting OWLUnionClass child, got " + child.toString());
		}
	}

	public String getNodeName()
	{
		return "OWLClassExpression";
	}

	public OWLUnionClassNode getOWLUnionClassNode()
	{
		return owlUnionClassNode;
	}

	public String toString()
	{
		return owlUnionClassNode.toString();
	}

}

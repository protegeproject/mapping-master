
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLClassExpression;
import org.protege.owl.mm.parser.ASTOWLUnionClass;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

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

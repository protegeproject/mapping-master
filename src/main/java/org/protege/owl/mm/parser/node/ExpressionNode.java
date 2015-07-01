
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTExpression;
import org.protege.owl.mm.parser.ASTMMExpression;
import org.protege.owl.mm.parser.ASTOWLExpression;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class ExpressionNode
{
	private MMExpressionNode mmExpressionNode = null;
	private OWLExpressionNode owlExpressionNode = null;

	public ExpressionNode(ASTExpression node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "MMExpression")) {
				mmExpressionNode = new MMExpressionNode((ASTMMExpression)child);
			} else if (ParserUtil.hasName(child, "OWLExpression")) {
				owlExpressionNode = new OWLExpressionNode((ASTOWLExpression)child);
			} else
				throw new InternalParseException("invalid child node " + child.toString() + " to Expression");
		} 
	}

	public MMExpressionNode getMMExpressionNode()
	{
		return mmExpressionNode;
	}

	public OWLExpressionNode getOWLExpressionNode()
	{
		return owlExpressionNode;
	}

	public boolean hasMMExpression()
	{
		return mmExpressionNode != null;
	}

	public boolean hasOWLExpression()
	{
		return owlExpressionNode != null;
	}

	public String toString()
	{
		String representation = "";

		if (hasMMExpression())
			representation += mmExpressionNode.toString();
		if (hasOWLExpression())
			representation += owlExpressionNode.toString();

		return representation;
	}

}

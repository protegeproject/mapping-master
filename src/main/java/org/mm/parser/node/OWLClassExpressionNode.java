
package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLEnumeratedClass;
import org.mm.parser.ASTOWLNamedClass;
import org.mm.parser.ASTOWLRestriction;
import org.mm.parser.ASTOWLUnionClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLClassExpressionNode implements MMNode
{
	private OWLNamedClassNode namedClassNode;
	private OWLEnumeratedClassNode enumeratedClassNode;
	private OWLUnionClassNode unionClassNode;
	private OWLRestrictionNode restrictionNode;
	private boolean isNegated;

	public OWLClassExpressionNode(ASTOWLClassExpression node) throws ParseException
	{
		isNegated = node.isNegated;

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLEnumeratedClass"))
				enumeratedClassNode = new OWLEnumeratedClassNode((ASTOWLEnumeratedClass)child);
			else if (ParserUtil.hasName(child, "OWLUnionClass"))
				unionClassNode = new OWLUnionClassNode((ASTOWLUnionClass)child);
			else if (ParserUtil.hasName(child, "OWLRestriction"))
				restrictionNode = new OWLRestrictionNode((ASTOWLRestriction)child);
			else if (ParserUtil.hasName(child, "OWLNamedClass"))
				namedClassNode = new OWLNamedClassNode((ASTOWLNamedClass)child);
			else
				throw new InternalParseException("invalid child node " + child.toString() + " for OWLClassExpression");
		}
	}

	@Override public String getNodeName()
	{
		return "OWLClassExpression";
	}

	public OWLEnumeratedClassNode getOWLEnumeratedClassNode()
	{
		return enumeratedClassNode;
	}

	public OWLUnionClassNode getOWLUnionClassNode()
	{
		return unionClassNode;
	}

	public OWLRestrictionNode getOWLRestrictionNode()
	{
		return restrictionNode;
	}

	public OWLNamedClassNode getOWLNamedClassNode()
	{
		return namedClassNode;
	}

	public boolean getIsNegated()
	{
		return isNegated;
	}

	public boolean hasOWLEnumeratedClass()
	{
		return enumeratedClassNode != null;
	}

	public boolean hasOWLUnionClass()
	{
		return unionClassNode != null;
	}

	public boolean hasOWLRestriction()
	{
		return restrictionNode != null;
	}

	public boolean hasOWLNamedClass()
	{
		return namedClassNode != null;
	}

	public String toString()
	{
		String representation = "";

		if (isNegated)
			representation += "NOT ";

		if (enumeratedClassNode != null)
			representation += enumeratedClassNode.toString();
		else if (unionClassNode != null)
			representation += unionClassNode.toString();
		else if (restrictionNode != null)
			representation += restrictionNode.toString();
		else if (namedClassNode != null)
			representation += namedClassNode.toString();

		return representation;
	}
}

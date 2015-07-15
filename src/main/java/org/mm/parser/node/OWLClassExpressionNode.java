
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

public class OWLClassExpressionNode implements TypeNode
{
	private OWLNamedClassNode owlNamedClassNode = null;
	private OWLEnumeratedClassNode owlEnumeratedClassNode = null;
	private OWLUnionClassNode owlUnionClassNode = null;
	private OWLRestrictionNode owlRestrictionNode = null;
	private boolean isNegated;

	public OWLClassExpressionNode(ASTOWLClassExpression node) throws ParseException
	{
		isNegated = node.isNegated;

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLEnumeratedClass"))
				owlEnumeratedClassNode = new OWLEnumeratedClassNode((ASTOWLEnumeratedClass)child);
			else if (ParserUtil.hasName(child, "OWLUnionClass"))
				owlUnionClassNode = new OWLUnionClassNode((ASTOWLUnionClass)child);
			else if (ParserUtil.hasName(child, "OWLRestriction"))
				owlRestrictionNode = new OWLRestrictionNode((ASTOWLRestriction)child);
			else if (ParserUtil.hasName(child, "OWLNamedClass"))
				owlNamedClassNode = new OWLNamedClassNode((ASTOWLNamedClass)child);
			else
				throw new InternalParseException("invalid child node " + child.toString() + " for OWLClassOrRestriction");
		} 

	}

	public OWLEnumeratedClassNode getOWLEnumeratedClassNode()
	{
		return owlEnumeratedClassNode;
	}

	public OWLUnionClassNode getOWLUnionClassNode()
	{
		return owlUnionClassNode;
	}

	public OWLRestrictionNode getOWLRestrictionNode()
	{
		return owlRestrictionNode;
	}

	public OWLNamedClassNode getOWLNamedClassNode()
	{
		return owlNamedClassNode;
	}

	public boolean getIsNegated()
	{
		return isNegated;
	}

	public boolean hasOWLEnumeratedClass()
	{
		return owlEnumeratedClassNode != null;
	}

	public boolean hasOWLUnionClass()
	{
		return owlUnionClassNode != null;
	}

	public boolean hasOWLRestriction()
	{
		return owlRestrictionNode != null;
	}

	public boolean hasOWLNamedClass()
	{
		return owlNamedClassNode != null;
	}

	public String getNodeName()
	{
		return "OWLClassExpression";
	}

	@Override public boolean isOWLClassExpressionNode() { return true; }

	@Override public boolean isReferenceNode() { return false; }

	public String toString()
	{
		String representation = "";

		if (isNegated)
			representation += "NOT ";

		if (owlEnumeratedClassNode != null)
			representation += owlEnumeratedClassNode.toString();
		else if (owlUnionClassNode != null)
			representation += owlUnionClassNode.toString();
		else if (owlRestrictionNode != null)
			representation += owlRestrictionNode.toString();
		else if (owlNamedClassNode != null)
			representation += owlNamedClassNode.toString();

		return representation;
	}
}

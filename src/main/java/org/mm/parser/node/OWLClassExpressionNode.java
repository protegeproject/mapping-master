
package org.mm.parser.node;

import org.mm.parser.ASTOWLClassExpression;
import org.mm.parser.ASTOWLObjectOneOf;
import org.mm.parser.ASTOWLClass;
import org.mm.parser.ASTOWLRestriction;
import org.mm.parser.ASTOWLUnionClass;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLClassExpressionNode implements MMNode
{
	private OWLClassNode classNode;
	private OWLObjectOneOfNode objectOneOfNode;
	private OWLUnionClassNode unionClassNode;
	private OWLRestrictionNode restrictionNode;
	private boolean isNegated;

	public OWLClassExpressionNode(ASTOWLClassExpression node) throws ParseException
	{
		this.isNegated = node.isNegated;

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLObjectOneOf"))
				this.objectOneOfNode = new OWLObjectOneOfNode((ASTOWLObjectOneOf)child);
			else if (ParserUtil.hasName(child, "OWLUnionClass"))
				this.unionClassNode = new OWLUnionClassNode((ASTOWLUnionClass)child);
			else if (ParserUtil.hasName(child, "OWLRestriction"))
				this.restrictionNode = new OWLRestrictionNode((ASTOWLRestriction)child);
			else if (ParserUtil.hasName(child, "OWLClass"))
				this.classNode = new OWLClassNode((ASTOWLClass)child);
			else
				throw new InternalParseException("invalid child node " + child.toString() + " for node " + getNodeName());
		}
	}

	@Override public String getNodeName()
	{
		return "OWLClassExpression";
	}

	public OWLObjectOneOfNode getOWLObjectOneOfNode()
	{
		return this.objectOneOfNode;
	}

	public OWLUnionClassNode getOWLUnionClassNode()
	{
		return this.unionClassNode;
	}

	public OWLRestrictionNode getOWLRestrictionNode()
	{
		return restrictionNode;
	}

	public OWLClassNode getOWLClassNode()
	{
		return classNode;
	}

	public boolean getIsNegated()
	{
		return isNegated;
	}

	public boolean hasOWLObjectOneOfNode()
	{
		return objectOneOfNode != null;
	}

	public boolean hasOWLUnionClassNode()
	{
		return unionClassNode != null;
	}

	public boolean hasOWLRestrictionNode()
	{
		return restrictionNode != null;
	}

	public boolean hasOWLClassNode()
	{
		return classNode != null;
	}

	public String toString()
	{
		String representation = "";

		if (isNegated)
			representation += "NOT ";

		if (objectOneOfNode != null)
			representation += objectOneOfNode.toString();
		else if (unionClassNode != null)
			representation += unionClassNode.toString();
		else if (restrictionNode != null)
			representation += restrictionNode.toString();
		else if (classNode != null)
			representation += classNode.toString();

		return representation;
	}
}

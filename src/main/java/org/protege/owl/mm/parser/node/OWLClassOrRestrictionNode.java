
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLClassOrRestriction;
import org.protege.owl.mm.parser.ASTOWLEnumeratedClass;
import org.protege.owl.mm.parser.ASTOWLNamedClass;
import org.protege.owl.mm.parser.ASTOWLRestriction;
import org.protege.owl.mm.parser.ASTOWLUnionClass;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class OWLClassOrRestrictionNode
{
	private OWLNamedClassNode owlNamedClassNode = null;
	private OWLEnumeratedClassNode owlEnumeratedClassNode = null;
	private OWLUnionClassNode owlUnionClassNode = null;
	private OWLRestrictionNode owlRestrictionNode = null;
	private boolean isNegated;

	public OWLClassOrRestrictionNode(ASTOWLClassOrRestriction node) throws ParseException
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

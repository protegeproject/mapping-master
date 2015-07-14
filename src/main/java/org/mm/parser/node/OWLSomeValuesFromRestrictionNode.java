package org.mm.parser.node;

import org.mm.parser.ASTOWLDataSomeValuesFrom;
import org.mm.parser.ASTOWLSomeValuesFromRestriction;
import org.mm.parser.ASTOWLObjectSomeValuesFrom;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLSomeValuesFromRestrictionNode
{
	private OWLDataSomeValuesFromNode owlDataSomeValuesFromNode = null;
	private OWLObjectSomeValuesFromNode owlObjectSomeValuesFromNode = null;

	public OWLSomeValuesFromRestrictionNode(ASTOWLSomeValuesFromRestriction node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLDataSomeValuesFrom"))
				owlDataSomeValuesFromNode = new OWLDataSomeValuesFromNode((ASTOWLDataSomeValuesFrom)child);
			else if (ParserUtil.hasName(child, "OWLSomeValuesFromClass"))
				owlObjectSomeValuesFromNode = new OWLObjectSomeValuesFromNode((ASTOWLObjectSomeValuesFrom)child);
			else
				throw new InternalParseException("invalid child node " + child.toString() + " for OWLSomeValuesFrom");
		} 
	}

	public OWLDataSomeValuesFromNode getOWLSomeValuesFromDataTypeNode()
	{
		return owlDataSomeValuesFromNode;
	}
	
	public OWLObjectSomeValuesFromNode getOWLSomeValuesFromClassNode()
	{
		return owlObjectSomeValuesFromNode;
	}

	public boolean hasOWLSomeValuesFromDataType()
	{
		return owlDataSomeValuesFromNode != null;
	}
	
	public boolean hasOWLSomeValuesFromClass()
	{
		return owlObjectSomeValuesFromNode != null;
	}

	public String toString()
	{
		String representation = "";

		if (owlDataSomeValuesFromNode != null)
			representation += owlDataSomeValuesFromNode.toString();
		else if (owlObjectSomeValuesFromNode != null)
			representation += owlObjectSomeValuesFromNode.toString();

		return representation;
	}
}

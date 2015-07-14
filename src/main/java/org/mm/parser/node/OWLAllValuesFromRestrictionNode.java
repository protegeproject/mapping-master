package org.mm.parser.node;

import org.mm.parser.ASTOWLAllValuesFromClass;
import org.mm.parser.ASTOWLAllValuesFromDataType;
import org.mm.parser.ASTOWLAllValuesFromRestriction;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLAllValuesFromRestrictionNode
{
	private OWLAllValuesFromDataTypeNode owlAllValuesFromDataTypeNode = null;
	private OWLAllValuesFromClassNode owlAllValuesFromClassNode = null;

	public OWLAllValuesFromRestrictionNode(ASTOWLAllValuesFromRestriction node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLAllValuesFromDataType"))
				owlAllValuesFromDataTypeNode = new OWLAllValuesFromDataTypeNode((ASTOWLAllValuesFromDataType)child);
			else if (ParserUtil.hasName(child, "OWLAllValuesFromClass"))
				owlAllValuesFromClassNode = new OWLAllValuesFromClassNode((ASTOWLAllValuesFromClass)child);
			else
				throw new InternalParseException("invalid child node " + child.toString() + " for OWLAllValuesFrom");
		}
	}

	public OWLAllValuesFromDataTypeNode getOWLAllValuesFromDataTypeNode()
	{
		return owlAllValuesFromDataTypeNode;
	}

	public OWLAllValuesFromClassNode getOWLAllValuesFromClassNode()
	{
		return owlAllValuesFromClassNode;
	}

	public boolean hasOWLAllValuesFromDataType()
	{
		return owlAllValuesFromDataTypeNode != null;
	}

	public boolean hasOWLAllValuesFromClass()
	{
		return owlAllValuesFromClassNode != null;
	}

	public String toString()
	{
		String representation = "";

		if (owlAllValuesFromDataTypeNode != null)
			representation += owlAllValuesFromDataTypeNode.toString();
		else if (owlAllValuesFromClassNode != null)
			representation += owlAllValuesFromClassNode.toString();

		return representation;
	}
}

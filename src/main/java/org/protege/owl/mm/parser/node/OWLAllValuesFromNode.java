
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLAllValuesFrom;
import org.protege.owl.mm.parser.ASTOWLAllValuesFromClass;
import org.protege.owl.mm.parser.ASTOWLAllValuesFromDataType;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class OWLAllValuesFromNode
{
	private OWLAllValuesFromDataTypeNode owlAllValuesFromDataTypeNode = null;
	private OWLAllValuesFromClassNode owlAllValuesFromClassNode = null;

	public OWLAllValuesFromNode(ASTOWLAllValuesFrom node) throws ParseException
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

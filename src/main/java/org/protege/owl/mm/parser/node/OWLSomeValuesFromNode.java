package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLSomeValuesFrom;
import org.protege.owl.mm.parser.ASTOWLSomeValuesFromClass;
import org.protege.owl.mm.parser.ASTOWLSomeValuesFromDataType;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class OWLSomeValuesFromNode
{
	private OWLSomeValuesFromDataTypeNode owlSomeValuesFromDataTypeNode = null;
	private OWLSomeValuesFromClassNode owlSomeValuesFromClassNode = null;

	public OWLSomeValuesFromNode(ASTOWLSomeValuesFrom node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLSomeValuesFromDataType"))
				owlSomeValuesFromDataTypeNode = new OWLSomeValuesFromDataTypeNode((ASTOWLSomeValuesFromDataType)child);
			else if (ParserUtil.hasName(child, "OWLSomeValuesFromClass"))
				owlSomeValuesFromClassNode = new OWLSomeValuesFromClassNode((ASTOWLSomeValuesFromClass)child);
			else
				throw new InternalParseException("invalid child node " + child.toString() + " for OWLSomeValuesFrom");
		} 
	}

	public OWLSomeValuesFromDataTypeNode getOWLSomeValuesFromDataTypeNode()
	{
		return owlSomeValuesFromDataTypeNode;
	}
	
	public OWLSomeValuesFromClassNode getOWLSomeValuesFromClassNode()
	{
		return owlSomeValuesFromClassNode;
	}

	public boolean hasOWLSomeValuesFromDataType()
	{
		return owlSomeValuesFromDataTypeNode != null;
	}
	
	public boolean hasOWLSomeValuesFromClass()
	{
		return owlSomeValuesFromClassNode != null;
	}

	public String toString()
	{
		String representation = "";

		if (owlSomeValuesFromDataTypeNode != null)
			representation += owlSomeValuesFromDataTypeNode.toString();
		else if (owlSomeValuesFromClassNode != null)
			representation += owlSomeValuesFromClassNode.toString();

		return representation;
	}
}

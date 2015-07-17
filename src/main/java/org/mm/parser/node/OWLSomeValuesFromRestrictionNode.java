package org.mm.parser.node;

import org.mm.parser.ASTOWLDataSomeValuesFrom;
import org.mm.parser.ASTOWLSomeValuesFromRestriction;
import org.mm.parser.ASTOWLObjectSomeValuesFrom;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLSomeValuesFromRestrictionNode implements MMNode
{
	private OWLDataSomeValuesFromNode dataSomeValuesFromNode;
	private OWLObjectSomeValuesFromNode objectSomeValuesFromNode;

	public OWLSomeValuesFromRestrictionNode(ASTOWLSomeValuesFromRestriction node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLDataSomeValuesFrom"))
				dataSomeValuesFromNode = new OWLDataSomeValuesFromNode((ASTOWLDataSomeValuesFrom)child);
			else if (ParserUtil.hasName(child, "OWLObjectSomeValuesFrom"))
				objectSomeValuesFromNode = new OWLObjectSomeValuesFromNode((ASTOWLObjectSomeValuesFrom)child);
			else
				throw new InternalParseException("invalid child node " + child.toString() + " for OWLSomeValuesFrom");
		} 
	}

	public OWLDataSomeValuesFromNode getOWLDataSomeValuesFromNode()
	{
		return dataSomeValuesFromNode;
	}
	
	public OWLObjectSomeValuesFromNode getOWLObjectSomeValuesFromNode()
	{
		return objectSomeValuesFromNode;
	}

	public boolean hasOWLDataSomeValuesFromNode()
	{
		return dataSomeValuesFromNode != null;
	}
	
	public boolean hasOWLObjectSomeValuesFrom()
	{
		return objectSomeValuesFromNode != null;
	}

	@Override public String getNodeName()
	{
		return "OWLSomeValuesFromRestriction";
	}

	public String toString()
	{
		String representation = "";

		if (dataSomeValuesFromNode != null)
			representation += dataSomeValuesFromNode.toString();
		else if (objectSomeValuesFromNode != null)
			representation += objectSomeValuesFromNode.toString();

		return representation;
	}
}

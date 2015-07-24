package org.mm.parser.node;

import org.mm.parser.ASTOWLDataSomeValuesFrom;
import org.mm.parser.ASTOWLSomeValuesFromRestriction;
import org.mm.parser.ASTOWLObjectSomeValuesFrom;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

public class OWLSomeValuesFromNode implements MMNode
{
	private OWLDataSomeValuesFromNode dataSomeValuesFromNode;
	private OWLObjectSomeValuesFromNode objectSomeValuesFromNode;

	public OWLSomeValuesFromNode(ASTOWLSomeValuesFromRestriction node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLDataSomeValuesFrom"))
				this.dataSomeValuesFromNode = new OWLDataSomeValuesFromNode((ASTOWLDataSomeValuesFrom)child);
			else if (ParserUtil.hasName(child, "OWLObjectSomeValuesFrom"))
				this.objectSomeValuesFromNode = new OWLObjectSomeValuesFromNode((ASTOWLObjectSomeValuesFrom)child);
			else
				throw new InternalParseException("invalid child node " + child + " for OWLSomeValuesFrom");
		} 
	}

	public OWLDataSomeValuesFromNode getOWLDataSomeValuesFromNode()
	{
		return this.dataSomeValuesFromNode;
	}
	
	public OWLObjectSomeValuesFromNode getOWLObjectSomeValuesFromNode()
	{
		return this.objectSomeValuesFromNode;
	}

	public boolean hasOWLDataSomeValuesFromNode()
	{
		return this.dataSomeValuesFromNode != null;
	}
	
	public boolean hasOWLObjectSomeValuesFrom()
	{
		return this.objectSomeValuesFromNode != null;
	}

	@Override public String getNodeName()
	{
		return "OWLSomeValuesFromRestriction";
	}

	public String toString()
	{
		String representation = "";

		if (this.dataSomeValuesFromNode != null)
			representation += this.dataSomeValuesFromNode.toString();
		else if (this.objectSomeValuesFromNode != null)
			representation += this.objectSomeValuesFromNode.toString();

		return representation;
	}
}


package org.mm.parser.node;

import org.mm.parser.ASTOWLMaxCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLMaxCardinalityNode implements MMNode
{
	private int cardinality;

	public OWLMaxCardinalityNode(ASTOWLMaxCardinalityRestriction node) throws ParseException
	{
		cardinality = node.cardinality;
	}

	public int getCardinality()
	{
		return cardinality;
	}

	@Override public String getNodeName()
	{
		return "OWLMaxCardinalityRestriction";
	}

	public String toString()
	{
		return "MAX " + cardinality;
	}
}

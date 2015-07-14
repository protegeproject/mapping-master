
package org.mm.parser.node;

import org.mm.parser.ASTOWLMaxCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLMaxCardinalityRestrictionNode
{
	private int cardinality;

	public OWLMaxCardinalityRestrictionNode(ASTOWLMaxCardinalityRestriction node) throws ParseException
	{
		cardinality = node.cardinality;
	}

	public int getCardinality()
	{
		return cardinality;
	}

	public String toString()
	{
		return "MAX " + cardinality;
	}
}


package org.mm.parser.node;

import org.mm.parser.ASTOWLMaxCardinality;
import org.mm.parser.ParseException;

public class OWLMaxCardinalityNode
{
	private int cardinality;

	public OWLMaxCardinalityNode(ASTOWLMaxCardinality node) throws ParseException
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


package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLMaxCardinality;
import org.protege.owl.mm.parser.ParseException;

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

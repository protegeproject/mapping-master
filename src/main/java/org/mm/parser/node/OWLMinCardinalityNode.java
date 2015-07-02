
package org.mm.parser.node;

import org.mm.parser.ParseException;
import org.mm.parser.ASTOWLMinCardinality;

public class OWLMinCardinalityNode
{
	private int cardinality;

	OWLMinCardinalityNode(ASTOWLMinCardinality node) throws ParseException
	{
		cardinality = node.cardinality;
	}

	public int getCardinality()
	{
		return cardinality;
	}

	public String toString()
	{
		return "MIN " + cardinality;
	}
}

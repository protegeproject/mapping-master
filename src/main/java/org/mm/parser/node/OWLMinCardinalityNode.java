
package org.mm.parser.node;

import org.mm.parser.ASTOWLMinCardinalityRestriction;
import org.mm.parser.ParseException;

public class OWLMinCardinalityNode implements  MMNode
{
	private int cardinality;

	OWLMinCardinalityNode(ASTOWLMinCardinalityRestriction node) throws ParseException
	{
		cardinality = node.cardinality;
	}

	public int getCardinality()
	{
		return cardinality;
	}

	@Override public String getNodeName()
	{
		return "OWLMinCardinaliyRestriction";
	}

	public String toString()
	{
		return "MIN " + cardinality;
	}
}

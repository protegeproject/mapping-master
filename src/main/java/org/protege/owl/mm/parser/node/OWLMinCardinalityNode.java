
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLMinCardinality;
import org.protege.owl.mm.parser.ParseException;

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

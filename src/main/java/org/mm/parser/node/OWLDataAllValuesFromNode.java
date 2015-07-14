package org.mm.parser.node;

import org.mm.parser.ASTOWLDataAllValuesFrom;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLDataAllValuesFromNode implements MappingMasterParserConstants
{
	int datatype;

	OWLDataAllValuesFromNode(ASTOWLDataAllValuesFrom node) throws ParseException
	{
		datatype = node.datatype;
	}

	public String getDataTypeName()
	{
		return tokenImage[datatype].substring(1, tokenImage[datatype].length() - 1);
	}

	public String toString()
	{
		return "ONLY " + getDataTypeName() + ")";
	}
}

package org.mm.parser.node;

import org.mm.parser.ASTOWLAllValuesFromDataType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class OWLAllValuesFromDataTypeNode implements MappingMasterParserConstants
{
	int dataType;

	OWLAllValuesFromDataTypeNode(ASTOWLAllValuesFromDataType node) throws ParseException
	{
		dataType = node.dataType;
	}

	public String getDataTypeName()
	{
		return tokenImage[dataType].substring(1, tokenImage[dataType].length() - 1);
	}

	public String toString()
	{
		return "ONLY " + getDataTypeName() + ")";
	}
}

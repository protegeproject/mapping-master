package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLAllValuesFromDataType;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;

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

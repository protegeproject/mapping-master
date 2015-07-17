
package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultDatatypePropertyValueType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class MMDefaultDatatypePropertyValueTypeNode implements MMNode, MappingMasterParserConstants
{
	private int defaultType;

	public MMDefaultDatatypePropertyValueTypeNode(ASTMMDefaultDatatypePropertyValueType node) throws ParseException
	{
		this.defaultType = node.defaultType;
	}

	public int getType()
	{
		return defaultType;
	}

	public String getTypeName()
	{
		return tokenImage[defaultType].substring(1, tokenImage[defaultType].length() - 1);
	}

	@Override public String getNodeName()
	{
		return "MMDefaultPropertyValueType";
	}

	public String toString()
	{
		return "MM:DefaultPropertyValueType " + getTypeName();
	}

}

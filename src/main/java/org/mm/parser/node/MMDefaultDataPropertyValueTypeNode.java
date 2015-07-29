
package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultDatatypePropertyValueType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class MMDefaultDataPropertyValueTypeNode implements MMNode, MappingMasterParserConstants
{
	private final int defaultDataPropertyValueType;

	public MMDefaultDataPropertyValueTypeNode(ASTMMDefaultDatatypePropertyValueType node) throws ParseException
	{
		this.defaultDataPropertyValueType = node.defaultType;
	}

	public int getType()
	{
		return this.defaultDataPropertyValueType;
	}

	public String getTypeName()
	{
		return tokenImage[this.defaultDataPropertyValueType].substring(1, tokenImage[this.defaultDataPropertyValueType].length() - 1);
	}

	@Override public String getNodeName()
	{
		return "MMDefaultDataPropertyValueType";
	}

	public String toString()
	{
		return "MM:DefaultDataPropertyValueType " + getTypeName();
	}
}

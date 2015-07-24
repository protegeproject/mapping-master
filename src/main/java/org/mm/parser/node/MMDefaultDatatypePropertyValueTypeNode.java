
package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultDatatypePropertyValueType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class MMDefaultDatatypePropertyValueTypeNode implements MMNode, MappingMasterParserConstants
{
	private final  int defaultType;

	public MMDefaultDatatypePropertyValueTypeNode(ASTMMDefaultDatatypePropertyValueType node) throws ParseException
	{
		this.defaultType = node.defaultType;
	}

	public int getType()
	{
		return this.defaultType;
	}

	public String getTypeName()
	{
		return tokenImage[this.defaultType].substring(1, tokenImage[this.defaultType].length() - 1);
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

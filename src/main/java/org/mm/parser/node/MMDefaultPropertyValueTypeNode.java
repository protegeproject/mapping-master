
package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultPropertyValueType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class MMDefaultPropertyValueTypeNode implements MappingMasterParserConstants
{
	private int defaultType;

	public MMDefaultPropertyValueTypeNode(ASTMMDefaultPropertyValueType node) throws ParseException
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

	public String toString()
	{
		return "MM:DefaultPropertyValueType " + getTypeName();
	}

}

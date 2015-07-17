
package org.mm.parser.node;

import org.mm.parser.ASTDefaultDataValue;
import org.mm.parser.ParseException;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;

public class DefaultDataValueNode implements MMNode, MappingMasterParserConstants
{
	private String defaultDataValue;

	public DefaultDataValueNode(ASTDefaultDataValue node) throws ParseException
	{
		this.defaultDataValue = node.defaultDataValue;
	}

	public String getDefaultDataValue()
	{
		return defaultDataValue;
	}

	@Override public String getNodeName()
	{
		return "DefaultDataValue";
	}

	public String toString()
	{
		return ParserUtil.getTokenName(MM_DEFAULT_DATA_VALUE) + "=\"" + defaultDataValue + "\"";
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		DefaultDataValueNode dv = (DefaultDataValueNode)obj;
		return defaultDataValue != null && dv.defaultDataValue != null && defaultDataValue.equals(dv.defaultDataValue);
	}

	public int hashCode()
	{
		int hash = 15;

		hash = hash + (null == defaultDataValue ? 0 : defaultDataValue.hashCode());

		return hash;
	}
}

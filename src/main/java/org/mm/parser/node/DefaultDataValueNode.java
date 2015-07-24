
package org.mm.parser.node;

import org.mm.parser.ASTDefaultDataValue;
import org.mm.parser.ParseException;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParserUtil;

public class DefaultDataValueNode implements MMNode, MappingMasterParserConstants
{
	private final String defaultDataValue;

	public DefaultDataValueNode(ASTDefaultDataValue node) throws ParseException
	{
		this.defaultDataValue = node.defaultDataValue;
	}

	public String getDefaultDataValue()
	{
		return this.defaultDataValue;
	}

	@Override public String getNodeName()
	{
		return "DefaultDataValue";
	}

	public String toString()
	{
		return ParserUtil.getTokenName(MM_DEFAULT_DATA_VALUE) + "=\"" + this.defaultDataValue + "\"";
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		DefaultDataValueNode dv = (DefaultDataValueNode)obj;
		return this.defaultDataValue != null && dv.defaultDataValue != null && this.defaultDataValue
			.equals(dv.defaultDataValue);
	}

	public int hashCode()
	{
		int hash = 15;

		hash = hash + (null == this.defaultDataValue ? 0 : this.defaultDataValue.hashCode());

		return hash;
	}
}

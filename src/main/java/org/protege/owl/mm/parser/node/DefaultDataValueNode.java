
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTDefaultDataValue;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class DefaultDataValueNode implements MappingMasterParserConstants
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


package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTMMDefaultPropertyValueType;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;

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

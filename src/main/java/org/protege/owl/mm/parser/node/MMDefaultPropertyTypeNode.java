
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTMMDefaultPropertyType;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;

public class MMDefaultPropertyTypeNode implements MappingMasterParserConstants
{
	private int defaultType;

	public MMDefaultPropertyTypeNode(ASTMMDefaultPropertyType node) throws ParseException
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
		return "MM:DefaultPropertyType " + getTypeName();
	}
}


package org.mm.parser.node;

import org.mm.parser.ASTMMDefaultValueEncoding;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class MMDefaultValueEncodingNode implements MMNode, MappingMasterParserConstants
{
	private final int encodingType;

	public MMDefaultValueEncodingNode(ASTMMDefaultValueEncoding node) throws ParseException
	{
		this.encodingType = node.encodingType;
	}

	public int getEncodingType()
	{
		return this.encodingType;
	}

	public String getEncodingTypeName()
	{
		return tokenImage[this.encodingType].substring(1, tokenImage[this.encodingType].length() - 1);
	}

	@Override public String getNodeName()
	{
		return "MMDefaultValueEncoding";
	}

	public String toString()
	{
		return "MM:DefaultValueEncoding " + getEncodingTypeName();
	}
}

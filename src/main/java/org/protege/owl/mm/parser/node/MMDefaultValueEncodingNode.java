
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTMMDefaultValueEncoding;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;

public class MMDefaultValueEncodingNode implements MappingMasterParserConstants
{
	private int encodingType;

	public MMDefaultValueEncodingNode(ASTMMDefaultValueEncoding node) throws ParseException
	{
		encodingType = node.encodingType;
	}

	public int getEncodingType()
	{
		return encodingType;
	}

	public String getEncodingTypeName()
	{
		return tokenImage[encodingType].substring(1, tokenImage[encodingType].length() - 1);
	}

	public String toString()
	{
		return "MM:DefaultValueEncoding " + getEncodingTypeName();
	}
}


package org.mm.parser.node;

import org.mm.core.ReferenceType;
import org.mm.parser.ASTReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class ReferenceTypeNode implements MMNode, MappingMasterParserConstants
{
	private ReferenceType referenceType;

	public ReferenceTypeNode(ASTReferenceType node) throws ParseException
	{
		referenceType = new ReferenceType(node.referenceType);
	}

	public ReferenceTypeNode(int type)
	{
		referenceType = new ReferenceType(type);
	}

	public ReferenceTypeNode(ReferenceType referenceType)
	{
		this.referenceType = referenceType;
	}

	public ReferenceType getReferenceType()
	{
		return referenceType;
	}

	@Override public String getNodeName()
	{
		return "ReferenceType";
	}

	public String toString()
	{
		return referenceType.toString();
	}
}

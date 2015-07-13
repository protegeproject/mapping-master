
package org.mm.parser.node;

import org.mm.core.ReferenceType;
import org.mm.parser.ASTEntityType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class EntityTypeNode implements MappingMasterParserConstants
{
	private ReferenceType entityType;

	public EntityTypeNode(ASTEntityType node) throws ParseException
	{
		entityType = new ReferenceType(node.entityType);
	}

	public EntityTypeNode(int type)
	{
		entityType = new ReferenceType(type);
	}

	public EntityTypeNode(ReferenceType entityType)
	{
		this.entityType = entityType;
	}

	public ReferenceType getEntityType()
	{
		return entityType;
	}
	
	public String toString()
	{
		return entityType.toString();
	}
}

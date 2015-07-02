
package org.mm.parser.node;

import org.mm.core.OWLEntityType;
import org.mm.parser.ASTEntityType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;

public class EntityTypeNode implements MappingMasterParserConstants
{
	private OWLEntityType entityType;

	public EntityTypeNode(ASTEntityType node) throws ParseException
	{
		entityType = new OWLEntityType(node.entityType);
	}

	public EntityTypeNode(int type)
	{
		entityType = new OWLEntityType(type);
	}

	public EntityTypeNode(OWLEntityType entityType)
	{
		this.entityType = entityType;
	}

	public OWLEntityType getEntityType()
	{
		return entityType;
	}
	
	public String toString()
	{
		return entityType.toString();
	}
}

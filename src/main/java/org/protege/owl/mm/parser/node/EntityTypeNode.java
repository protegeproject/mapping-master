
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.OWLEntityType;
import org.protege.owl.mm.parser.ASTEntityType;
import org.protege.owl.mm.parser.MappingMasterParserConstants;
import org.protege.owl.mm.parser.ParseException;

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

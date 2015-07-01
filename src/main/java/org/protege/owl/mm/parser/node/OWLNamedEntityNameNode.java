
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLNamedEntityName;

public class OWLNamedEntityNameNode
{
	private String entityName;

	public OWLNamedEntityNameNode(ASTOWLNamedEntityName node)
	{
		entityName = node.entityName;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public String toString()
	{
		return entityName;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		OWLNamedEntityNameNode nename = (OWLNamedEntityNameNode)obj;
		return (entityName != null && nename.entityName != null && entityName.equals(nename.entityName));
	}

	public int hashCode()
	{
		int hash = 39;

		hash = hash + (null == entityName ? 0 : entityName.hashCode());

		return hash;
	}
}

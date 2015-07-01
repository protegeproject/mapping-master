
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.OWLEntityType;
import org.protege.owl.mm.parser.ASTEntityType;
import org.protege.owl.mm.parser.ASTMMDefaultEntityType;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;
import org.protege.owl.mm.parser.node.EntityTypeNode;

public class MMDefaultEntityTypeNode
{
	private EntityTypeNode entityTypeNode;

	public MMDefaultEntityTypeNode(ASTMMDefaultEntityType node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one OWLEntityType child of MMDefaultEntityType node");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "OWLEntityType"))
				entityTypeNode = new EntityTypeNode((ASTEntityType)child);
			else
				throw new InternalParseException("MMDefaultEntityType node expecting OWLEntityType child, got " + child.toString());
		}
	}

	public OWLEntityType getEntityType()
	{
		return entityTypeNode.getEntityType();
	}

	public String toString()
	{
		return "MM:DefaultEntityType " + entityTypeNode.toString();
	}
}


package org.mm.parser.node;

import org.mm.core.ReferenceType;
import org.mm.parser.ASTEntityType;
import org.mm.parser.ASTMMDefaultEntityType;
import org.mm.parser.ParseException;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

public class MMDefaultEntityTypeNode
{
	private EntityTypeNode entityTypeNode;

	public MMDefaultEntityTypeNode(ASTMMDefaultEntityType node) throws ParseException
	{
		if (node.jjtGetNumChildren() != 1)
			throw new InternalParseException("expecting one ReferenceType child of MMDefaultEntityType node");
		else {
			Node child = node.jjtGetChild(0);
			if (ParserUtil.hasName(child, "ReferenceType"))
				entityTypeNode = new EntityTypeNode((ASTEntityType)child);
			else
				throw new InternalParseException("MMDefaultEntityType node expecting ReferenceType child, got " + child.toString());
		}
	}

	public ReferenceType getEntityType()
	{
		return entityTypeNode.getEntityType();
	}

	public String toString()
	{
		return "MM:DefaultEntityType " + entityTypeNode.toString();
	}
}

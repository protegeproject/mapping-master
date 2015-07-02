package org.mm.parser.node;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.ParseException;
import org.mm.parser.ASTValueSpecification;
import org.mm.parser.ASTValueSpecificationItem;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class ValueSpecificationNode implements MappingMasterParserConstants
{
	private List<ValueSpecificationItemNode> valueSpecificationItemNodes = new ArrayList<ValueSpecificationItemNode>();

	public ValueSpecificationNode(ASTValueSpecification node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "ValueSpecificationItem")) {
				ValueSpecificationItemNode valueSpecificationItem = new ValueSpecificationItemNode((ASTValueSpecificationItem)child);
				valueSpecificationItemNodes.add(valueSpecificationItem);
			} else
				throw new InternalParseException("invalid child node " + child.toString() + " for ValueSpecification");
		}

		if (valueSpecificationItemNodes.isEmpty())
			throw new ParseException("ValueSpecification node must have at least one child");
	}

	public int getNumberOfValueSpecificationItems()
	{
		return valueSpecificationItemNodes.size();
	}
	
	public List<ValueSpecificationItemNode> getValueSpecificationItemNodes()
	{
		return valueSpecificationItemNodes;
	}

	public String toString()
	{
		String representation = "";

		if (!valueSpecificationItemNodes.isEmpty()) {
			boolean isFirst = true;

			representation += "=";

			if (valueSpecificationItemNodes.size() > 1)
				representation += "(";

			for (ValueSpecificationItemNode valueSpecificationItem : valueSpecificationItemNodes) {
				if (!isFirst)
					representation += ", ";
				representation += valueSpecificationItem.toString();
				isFirst = false;
			}

			if (valueSpecificationItemNodes.size() > 1)
				representation += ")";
		} 

		return representation;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		ValueSpecificationNode ve = (ValueSpecificationNode)obj;
		return (valueSpecificationItemNodes != null && ve.valueSpecificationItemNodes != null && valueSpecificationItemNodes.equals(ve.valueSpecificationItemNodes));
	}

	public int hashCode()
	{
		int hash = 15;

		hash = hash + (null == valueSpecificationItemNodes ? 0 : valueSpecificationItemNodes.hashCode());

		return hash;
	}
}

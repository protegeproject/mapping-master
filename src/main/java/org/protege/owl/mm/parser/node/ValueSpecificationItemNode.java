package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTReference;
import org.protege.owl.mm.parser.ASTValueExtractionFunction;
import org.protege.owl.mm.parser.ASTValueSpecificationItem;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class ValueSpecificationItemNode
{
	private String stringLiteral = null;
	private ReferenceNode referenceNode = null;
	private ValueExtractionFunctionNode valueExtractionFunctionNode = null;
	private String capturingExpression = null;

	public ValueSpecificationItemNode(ASTValueSpecificationItem node) throws ParseException
	{
		stringLiteral = node.stringLiteral;
		capturingExpression = node.captureExpression;

		if (node.jjtGetNumChildren() != 0) {
			Node child = node.jjtGetChild(0);

			if (ParserUtil.hasName(child, "Reference"))
				referenceNode = new ReferenceNode((ASTReference)child);
			else if (ParserUtil.hasName(child, "ValueExtractionFunction"))
				valueExtractionFunctionNode = new ValueExtractionFunctionNode((ASTValueExtractionFunction)child);
			else
				throw new InternalParseException("invalid child node " + child.toString() + " for ValueSpecificationItem");
		}
	}

	public boolean hasStringLiteral()
	{
		return stringLiteral != null;
	}
	
	public String getStringLiteral()
	{
		return stringLiteral;
	}

	public boolean hasReference()
	{
		return referenceNode != null;
	}
	
	public ReferenceNode getReferenceNode()
	{
		return referenceNode;
	}

	public boolean hasValueExtractionFunction()
	{
		return valueExtractionFunctionNode != null;
	}
	
	public ValueExtractionFunctionNode getValueExtractionFunctionNode()
	{
		return valueExtractionFunctionNode;
	}

	public boolean hasCapturingExpression()
	{
		return capturingExpression != null;
	}
	
	public String getCapturingExpression()
	{
		return capturingExpression;
	}

	public String toString()
	{
		String representation = "";

		if (hasStringLiteral())
			representation = "\"" + stringLiteral + "\"";
		else if (hasReference())
			representation = referenceNode.toString();
		else if (hasValueExtractionFunction())
			representation = valueExtractionFunctionNode.toString();
		else if (hasCapturingExpression())
			representation = "[\"" + capturingExpression + "\"]";

		return representation;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		ValueSpecificationItemNode vs = (ValueSpecificationItemNode)obj;
		return (stringLiteral != null && vs.stringLiteral != null && stringLiteral.equals(vs.stringLiteral))
				&& (referenceNode != null && vs.referenceNode != null && referenceNode.equals(vs.referenceNode))
				&& (valueExtractionFunctionNode != null && vs.valueExtractionFunctionNode != null && valueExtractionFunctionNode.equals(vs.valueExtractionFunctionNode))
				&& (capturingExpression != null && vs.capturingExpression != null && capturingExpression.equals(vs.capturingExpression));
	}

	public int hashCode()
	{
		int hash = 15;

		hash = hash + (null == stringLiteral ? 0 : stringLiteral.hashCode());
		hash = hash + (null == referenceNode ? 0 : referenceNode.hashCode());
		hash = hash + (null == valueExtractionFunctionNode ? 0 : valueExtractionFunctionNode.hashCode());
		hash = hash + (null == capturingExpression ? 0 : capturingExpression.hashCode());

		return hash;
	}
}


package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLClassExpression;
import org.protege.owl.mm.parser.ASTOWLEquivalentTo;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

import java.util.List;
import java.util.ArrayList;

public class OWLEquivalentToNode
{
	private List<OWLClassExpressionNode> owlClassExpressionNodes;

	public OWLEquivalentToNode(ASTOWLEquivalentTo node) throws ParseException
	{
		owlClassExpressionNodes = new ArrayList<OWLClassExpressionNode>();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLClassExpression")) {
				OWLClassExpressionNode owlClassExpression = new OWLClassExpressionNode((ASTOWLClassExpression)child);
				owlClassExpressionNodes.add(owlClassExpression);
			} else
				throw new InternalParseException("OWLEquivalentTo node expecting OWLClassExpression child, got " + child.toString());
		}
	}

	public List<OWLClassExpressionNode> getClassExpressionNodes()
	{
		return owlClassExpressionNodes;
	}

	public String toString()
	{
		String representation = " EquivalentTo: ";

		if (owlClassExpressionNodes.size() == 1)
			representation += owlClassExpressionNodes.get(0).toString();
		else {
			boolean isFirst = true;

			for (OWLClassExpressionNode owlClassExpression : owlClassExpressionNodes) {
				if (!isFirst)
					representation += ", ";
				representation += owlClassExpression.toString();
				isFirst = false;
			}
		}

		return representation;
	}
}

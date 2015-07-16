
package org.mm.parser.node;

import org.mm.parser.ParseException;
import org.mm.parser.ASTOWLNamedIndividual;
import org.mm.parser.ASTSameAs;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class SameAsNode
{
	private List<OWLNamedIndividualNode> namedIndividualNodes;

	public SameAsNode(ASTSameAs node) throws ParseException
	{
		namedIndividualNodes = new ArrayList<>();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLNamedIndividual")) {
				OWLNamedIndividualNode owlIndividual = new OWLNamedIndividualNode((ASTOWLNamedIndividual)child);
				namedIndividualNodes.add(owlIndividual);
			} else
				throw new InternalParseException("SameAs node expecting OWLNamedIndividual child, got " + child.toString());
		} // for

	}

	public List<OWLNamedIndividualNode> getIndividualNodes()
	{
		return namedIndividualNodes;
	}

	public String toString()
	{
		String representation = " SameAs: ";

		if (namedIndividualNodes.size() == 1)
			representation += namedIndividualNodes.get(0).toString();
		else {
			boolean isFirst = true;

			for (OWLNamedIndividualNode namedIndividualNode : namedIndividualNodes) {
				if (!isFirst)
					representation += ", ";
				representation += namedIndividualNode.toString();
				isFirst = false;
			}
		}
		return representation;
	}

}

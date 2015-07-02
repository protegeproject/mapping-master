
package org.mm.parser.node;

import org.mm.parser.ParseException;
import org.mm.parser.ASTOWLIndividual;
import org.mm.parser.ASTSameAs;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class SameAsNode
{
	private List<OWLIndividualNode> owlIndividualNodes;

	public SameAsNode(ASTSameAs node) throws ParseException
	{
		owlIndividualNodes = new ArrayList<OWLIndividualNode>();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLIndividual")) {
				OWLIndividualNode owlIndividual = new OWLIndividualNode((ASTOWLIndividual)child);
				owlIndividualNodes.add(owlIndividual);
			} else
				throw new InternalParseException("SameAs node expecting OWLIndividual child, got " + child.toString());
		} // for

	}

	public List<OWLIndividualNode> getIndividualNodes()
	{
		return owlIndividualNodes;
	}

	public String toString()
	{
		String representation = " SameAs: ";

		if (owlIndividualNodes.size() == 1)
			representation += owlIndividualNodes.get(0).toString();
		else {
			boolean isFirst = true;

			for (OWLIndividualNode owlIndividual : owlIndividualNodes) {
				if (!isFirst)
					representation += ", ";
				representation += owlIndividual.toString();
				isFirst = false;
			} // for
		} // if

		return representation;
	}

}

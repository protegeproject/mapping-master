package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLIntersectionClass;
import org.protege.owl.mm.parser.ASTOWLUnionClass;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

import java.util.List;
import java.util.ArrayList;

public class OWLUnionClassNode
{
	private List<OWLIntersectionClassNode> owlIntersectionClasseNodes;

	public OWLUnionClassNode(ASTOWLUnionClass node) throws ParseException
	{
		owlIntersectionClasseNodes = new ArrayList<OWLIntersectionClassNode>();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLIntersectionClass")) {
				OWLIntersectionClassNode owlIntersectionClass = new OWLIntersectionClassNode((ASTOWLIntersectionClass)child);
				owlIntersectionClasseNodes.add(owlIntersectionClass);
			} else
				throw new InternalParseException("OWLUnionClass node expecting OWLIntersectionClass child, got " + child.toString());
		} 
	}

	public List<OWLIntersectionClassNode> getOWLIntersectionClasseNodes()
	{
		return owlIntersectionClasseNodes;
	}

	public String toString()
	{
		String representation = "";

		if (owlIntersectionClasseNodes.size() == 1)
			representation = owlIntersectionClasseNodes.get(0).toString();
		else {
			boolean isFirst = true;

			representation += "(";
			for (OWLIntersectionClassNode owlIntersectionClass : owlIntersectionClasseNodes) {
				if (!isFirst)
					representation += " OR ";
				representation += owlIntersectionClass.toString();
				isFirst = false;
			}
			representation += ")";
		}
		return representation;
	}
}

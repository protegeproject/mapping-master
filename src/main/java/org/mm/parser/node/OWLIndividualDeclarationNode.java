
package org.mm.parser.node;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTOWLDifferentFrom;
import org.mm.parser.ASTOWLIndividualDeclaration;
import org.mm.parser.ParseException;
import org.mm.parser.ASTFact;
import org.mm.parser.ASTOWLNamedIndividual;
import org.mm.parser.ASTOWLSameAs;
import org.mm.parser.ASTTypes;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLIndividualDeclarationNode
{
	private OWLNamedIndividualNode namedIndividualNode;
	private List<FactNode> factNodes;
	private List<AnnotationFactNode> annotationNodes;
	private TypesNode typesNode = null;
	private OWLSameAsNode OWLSameAsNode = null;
	private OWLDifferentFromNode OWLDifferentFromNode;

	public OWLIndividualDeclarationNode(ASTOWLIndividualDeclaration node) throws ParseException
	{
		factNodes = new ArrayList<>();
		annotationNodes = new ArrayList<>();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLNamedIndividual"))
				namedIndividualNode = new OWLNamedIndividualNode((ASTOWLNamedIndividual)child);
			else if (ParserUtil.hasName(child, "Fact")) {
				FactNode fact = new FactNode((ASTFact)child);
				factNodes.add(fact);
			} else if (ParserUtil.hasName(child, "AnnotationFact")) {
				AnnotationFactNode fact = new AnnotationFactNode((ASTAnnotationFact)child);
				annotationNodes.add(fact);
			} else if (ParserUtil.hasName(child, "Types")) {
				typesNode = new TypesNode((ASTTypes)child);
			} else if (ParserUtil.hasName(child, "OWLSameAs")) {
				OWLSameAsNode = new OWLSameAsNode((ASTOWLSameAs)child);
			} else if (ParserUtil.hasName(child, "OWLDifferentFrom")) {
				OWLDifferentFromNode = new OWLDifferentFromNode((ASTOWLDifferentFrom)child);
			} else
				throw new InternalParseException("unexpect child node " + child.toString() + " for OWLIndividualDeclaration");
		}
	}

	public boolean hasFacts()
	{
		return !factNodes.isEmpty();
	}

	public boolean hasAnnotations()
	{
		return !annotationNodes.isEmpty();
	}

	public boolean hasTypes()
	{
		return typesNode != null;
	}

	public boolean hasSameAs()
	{
		return OWLSameAsNode != null;
	}

	public boolean hasDifferentFrom()
	{
		return OWLDifferentFromNode != null;
	}

	public OWLNamedIndividualNode getOWLIndividualNode()
	{
		return namedIndividualNode;
	}

	public List<FactNode> getFactNodes()
	{
		return factNodes;
	}

	public List<AnnotationFactNode> getAnnotationNodes()
	{
		return annotationNodes;
	}

	public TypesNode getTypesNode()
	{
		return typesNode;
	}

	public OWLSameAsNode getOWLSameAsNode()
	{
		return OWLSameAsNode;
	}

	public OWLDifferentFromNode getOWLDifferentFromNode()
	{
		return OWLDifferentFromNode;
	}

	public String toString()
	{
		String representation = "Individual: " + namedIndividualNode.toString();
		boolean isFirst = true;

		if (hasFacts()) {
			representation += " Facts: ";
			for (FactNode fact : factNodes) {
				if (!isFirst)
					representation += ", ";
				representation += fact.toString();
				isFirst = false;
			}
		} 

		if (hasTypes()) {
			representation += " Types: ";
			representation += typesNode.toString();
		}

		isFirst = true;
		if (hasAnnotations()) {
			representation += " Annotations: ";
			for (AnnotationFactNode annotationFact : annotationNodes) {
				if (!isFirst)
					representation += ", ";
				representation += annotationFact.toString();
				isFirst = false;
			}
		}

		if (hasSameAs())
			representation += OWLSameAsNode.toString();
		if (hasDifferentFrom())
			representation += OWLDifferentFromNode.toString();

		return representation;
	}
}

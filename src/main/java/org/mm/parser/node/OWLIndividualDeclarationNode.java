
package org.mm.parser.node;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTDifferentFrom;
import org.mm.parser.ASTOWLIndividualDeclaration;
import org.mm.parser.ParseException;
import org.mm.parser.ASTFact;
import org.mm.parser.ASTOWLIndividual;
import org.mm.parser.ASTSameAs;
import org.mm.parser.ASTTypes;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLIndividualDeclarationNode
{
	private OWLIndividualNode individualNode;
	private List<FactNode> factNodes;
	private List<AnnotationFactNode> annotationNodes;
	private TypesNode typesNode = null;
	private SameAsNode sameAsNode = null;
	private DifferentFromNode differentFromNode;

	public OWLIndividualDeclarationNode(ASTOWLIndividualDeclaration node) throws ParseException
	{
		factNodes = new ArrayList<>();
		annotationNodes = new ArrayList<>();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLIndividual"))
				individualNode = new OWLIndividualNode((ASTOWLIndividual)child);
			else if (ParserUtil.hasName(child, "Fact")) {
				FactNode fact = new FactNode((ASTFact)child);
				factNodes.add(fact);
			} else if (ParserUtil.hasName(child, "AnnotationFact")) {
				AnnotationFactNode fact = new AnnotationFactNode((ASTAnnotationFact)child);
				annotationNodes.add(fact);
			} else if (ParserUtil.hasName(child, "Types")) {
				typesNode = new TypesNode((ASTTypes)child);
			} else if (ParserUtil.hasName(child, "SameAs")) {
				sameAsNode = new SameAsNode((ASTSameAs)child);
			} else if (ParserUtil.hasName(child, "DifferentFrom")) {
				differentFromNode = new DifferentFromNode((ASTDifferentFrom)child);
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
		return sameAsNode != null;
	}

	public boolean hasDifferentFrom()
	{
		return differentFromNode != null;
	}

	public OWLIndividualNode getOWLIndividualNode()
	{
		return individualNode;
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

	public SameAsNode getSameAsNode()
	{
		return sameAsNode;
	}

	public DifferentFromNode getDifferentFromNode()
	{
		return differentFromNode;
	}

	public String toString()
	{
		String representation = "Individual: " + individualNode.toString();
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
			representation += sameAsNode.toString();
		if (hasDifferentFrom())
			representation += differentFromNode.toString();

		return representation;
	}
}

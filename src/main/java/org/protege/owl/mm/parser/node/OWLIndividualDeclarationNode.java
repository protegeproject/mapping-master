
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTAnnotationFact;
import org.protege.owl.mm.parser.ASTDifferentFrom;
import org.protege.owl.mm.parser.ASTFact;
import org.protege.owl.mm.parser.ASTOWLIndividual;
import org.protege.owl.mm.parser.ASTOWLIndividualDeclaration;
import org.protege.owl.mm.parser.ASTSameAs;
import org.protege.owl.mm.parser.ASTTypes;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLIndividualDeclarationNode
{
	private OWLIndividualNode owlIndividualNode;
	private List<FactNode> factNodes;
	private List<AnnotationFactNode> annotationNodes;
	private TypesNode typeNodes = null;
	private SameAsNode sameAsNode = null;
	private DifferentFromNode differentFromNode = null;

	public OWLIndividualDeclarationNode(ASTOWLIndividualDeclaration node) throws ParseException
	{
		factNodes = new ArrayList<FactNode>();
		annotationNodes = new ArrayList<AnnotationFactNode>();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLIndividual"))
				owlIndividualNode = new OWLIndividualNode((ASTOWLIndividual)child);
			else if (ParserUtil.hasName(child, "Fact")) {
				FactNode fact = new FactNode((ASTFact)child);
				factNodes.add(fact);
			} else if (ParserUtil.hasName(child, "AnnotationFact")) {
				AnnotationFactNode fact = new AnnotationFactNode((ASTAnnotationFact)child);
				annotationNodes.add(fact);
			} else if (ParserUtil.hasName(child, "Types")) {
				typeNodes = new TypesNode((ASTTypes)child);
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
		return typeNodes != null;
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
		return owlIndividualNode;
	}

	public List<FactNode> getFactNodes()
	{
		return factNodes;
	}

	public List<AnnotationFactNode> getAnnotationNodes()
	{
		return annotationNodes;
	}

	public TypesNode getTypeNodes()
	{
		return typeNodes;
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
		String representation = "Individual: " + owlIndividualNode.toString();
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
			representation += typeNodes.toString();
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

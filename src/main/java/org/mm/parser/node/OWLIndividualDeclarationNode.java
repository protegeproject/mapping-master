
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

public class OWLIndividualDeclarationNode implements MMNode
{
	private OWLNamedIndividualNode namedIndividualNode;
	private final List<FactNode> factNodes;
	private final List<AnnotationFactNode> annotationNodes;
	private TypesNode typesNode ;
	private OWLSameAsNode sameAsNode;
	private OWLDifferentFromNode differentFromNode;

	public OWLIndividualDeclarationNode(ASTOWLIndividualDeclaration node) throws ParseException
	{
		this.factNodes = new ArrayList<>();
		this.annotationNodes = new ArrayList<>();

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLNamedIndividual"))
				this.namedIndividualNode = new OWLNamedIndividualNode((ASTOWLNamedIndividual)child);
			else if (ParserUtil.hasName(child, "Fact")) {
				FactNode fact = new FactNode((ASTFact)child);
				this.factNodes.add(fact);
			} else if (ParserUtil.hasName(child, "AnnotationFact")) {
				AnnotationFactNode fact = new AnnotationFactNode((ASTAnnotationFact)child);
				this.annotationNodes.add(fact);
			} else if (ParserUtil.hasName(child, "Types")) {
				this.typesNode = new TypesNode((ASTTypes)child);
			} else if (ParserUtil.hasName(child, "OWLSameAs")) {
				this.sameAsNode = new OWLSameAsNode((ASTOWLSameAs)child);
			} else if (ParserUtil.hasName(child, "OWLDifferentFrom")) {
				this.differentFromNode = new OWLDifferentFromNode((ASTOWLDifferentFrom)child);
			} else
				throw new InternalParseException("unexpected child node " + child + " for node " + getNodeName());
		}
	}

	public boolean hasFacts()
	{
		return !this.factNodes.isEmpty();
	}

	public boolean hasAnnotations()
	{
		return !this.annotationNodes.isEmpty();
	}

	public boolean hasTypes()
	{
		return this.typesNode != null;
	}

	public boolean hasSameAs()
	{
		return this.sameAsNode != null;
	}

	public boolean hasDifferentFrom()
	{
		return this.differentFromNode != null;
	}

	public OWLNamedIndividualNode getOWLIndividualNode()
	{
		return this.namedIndividualNode;
	}

	public List<FactNode> getFactNodes()
	{
		return this.factNodes;
	}

	public List<AnnotationFactNode> getAnnotationNodes()
	{
		return this.annotationNodes;
	}

	public TypesNode getTypesNode()
	{
		return this.typesNode;
	}

	public OWLSameAsNode getOWLSameAsNode()
	{
		return this.sameAsNode;
	}

	public OWLDifferentFromNode getOWLDifferentFromNode()
	{
		return this.differentFromNode;
	}

	@Override public String getNodeName()
	{
		return "OWLIndividualDeclaration";
	}

	public String toString()
	{
		String representation = "Individual: " + this.namedIndividualNode;
		boolean isFirst = true;

		if (hasFacts()) {
			representation += " Facts: ";
			for (FactNode fact : this.factNodes) {
				if (!isFirst)
					representation += ", ";
				representation += fact.toString();
				isFirst = false;
			}
		} 

		if (hasTypes()) {
			representation += " Types: ";
			representation += this.typesNode.toString();
		}

		isFirst = true;
		if (hasAnnotations()) {
			representation += " Annotations: ";
			for (AnnotationFactNode annotationFact : this.annotationNodes) {
				if (!isFirst)
					representation += ", ";
				representation += annotationFact.toString();
				isFirst = false;
			}
		}

		if (hasSameAs())
			representation += this.sameAsNode.toString();
		if (hasDifferentFrom())
			representation += this.differentFromNode.toString();

		return representation;
	}
}

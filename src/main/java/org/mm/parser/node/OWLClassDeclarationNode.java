
package org.mm.parser.node;

import org.mm.parser.ASTAnnotationFact;
import org.mm.parser.ASTOWLClassDeclaration;
import org.mm.parser.ASTOWLClassEquivalentTo;
import org.mm.parser.ASTOWLNamedClass;
import org.mm.parser.ASTOWLSubclassOf;
import org.mm.parser.InternalParseException;
import org.mm.parser.Node;
import org.mm.parser.ParseException;
import org.mm.parser.ParserUtil;

import java.util.ArrayList;
import java.util.List;

public class OWLClassDeclarationNode
{
	private OWLNamedClassNode owlNamedClass;
	private List<OWLClassEquivalentToNode> equivalentToNodes = new ArrayList<OWLClassEquivalentToNode>();
	private List<OWLSubclassOfNode> subclassOfNodes = new ArrayList<OWLSubclassOfNode>();
	private List<AnnotationFactNode> annotationFactNodes = new ArrayList<AnnotationFactNode>();

	OWLClassDeclarationNode(ASTOWLClassDeclaration node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);
			if (ParserUtil.hasName(child, "OWLNamedClass")) {
				owlNamedClass = new OWLNamedClassNode((ASTOWLNamedClass)child);
			} else if (ParserUtil.hasName(child, "OWLEquivalentTo")) {
				equivalentToNodes.add(new OWLClassEquivalentToNode((ASTOWLClassEquivalentTo)child));
			} else if (ParserUtil.hasName(child, "OWLSubclassOf")) {
				subclassOfNodes.add(new OWLSubclassOfNode((ASTOWLSubclassOf)child));
			} else if (ParserUtil.hasName(child, "AnnotationFact")) {
				AnnotationFactNode fact = new AnnotationFactNode((ASTAnnotationFact)child);
				annotationFactNodes.add(fact);
			} else
				throw new InternalParseException("unkown child " + child.toString() + " to OWLClassDeclaration node");
		}
	}

	public OWLNamedClassNode getOWLNamedClassNode()
	{
		return owlNamedClass;
	}

	public List<OWLClassEquivalentToNode> getEquivalentToNodes()
	{
		return equivalentToNodes;
	}

	public List<OWLSubclassOfNode> getSubclassOfNodes()
	{
		return subclassOfNodes;
	}

	public List<AnnotationFactNode> getAnnotationFactNodes()
	{
		return annotationFactNodes;
	}

	public boolean hasEquivalentTo()
	{
		return !equivalentToNodes.isEmpty();
	}

	public boolean hasSubclassOf()
	{
		return !subclassOfNodes.isEmpty();
	}

	public boolean hasAnnotations()
	{
		return !annotationFactNodes.isEmpty();
	}

	public String toString()
	{
		String representation = "Class: " + owlNamedClass.toString();
		boolean isFirst = true;

		if (hasSubclassOf()) {
			representation += " SubclassOf: ";
			for (OWLSubclassOfNode subclassOf : subclassOfNodes) {
				representation += subclassOf.toString();
			}
		}

		if (hasEquivalentTo()) {
			representation += " EquivalentTo: ";
			for (OWLClassEquivalentToNode equivalentTo : equivalentToNodes) {
				representation += equivalentTo.toString();
			}
		}

		isFirst = true;
		if (hasAnnotations()) {
			representation += " Annotations: ";
			for (AnnotationFactNode fact : annotationFactNodes) {
				if (!isFirst)
					representation += ", ";
				representation += fact.toString();
				isFirst = false;
			}
		}

		return representation;
	}

}

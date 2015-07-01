
package org.protege.owl.mm.parser.node;

import org.protege.owl.mm.parser.ASTOWLClassDeclaration;
import org.protege.owl.mm.parser.ASTOWLClassExpression;
import org.protege.owl.mm.parser.ASTOWLExpression;
import org.protege.owl.mm.parser.ASTOWLIndividualDeclaration;
import org.protege.owl.mm.parser.InternalParseException;
import org.protege.owl.mm.parser.Node;
import org.protege.owl.mm.parser.ParseException;
import org.protege.owl.mm.parser.ParserUtil;

public class OWLExpressionNode implements MMNode
{
	private OWLClassDeclarationNode owlClassDeclarationNode;
	private OWLIndividualDeclarationNode owlIndividualDeclarationNode;
	private OWLClassExpressionNode owlClassExpressionNode;

	public OWLExpressionNode(ASTOWLExpression node) throws ParseException
	{
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			Node child = node.jjtGetChild(i);

			if (ParserUtil.hasName(child, "OWLClassDeclaration")) {
				owlClassDeclarationNode = new OWLClassDeclarationNode((ASTOWLClassDeclaration)child);
			} else if (ParserUtil.hasName(child, "OWLIndividualDeclaration")) {
				owlIndividualDeclarationNode = new OWLIndividualDeclarationNode((ASTOWLIndividualDeclaration)child);
			} else if (ParserUtil.hasName(child, "OWLClassExpression")) {
				owlClassExpressionNode = new OWLClassExpressionNode((ASTOWLClassExpression)child);
			} else
				throw new InternalParseException("invalid child node " + child.toString() + " to OWLExpression");
		}
	}

	public OWLClassDeclarationNode getOWLClassDeclarationNode()
	{
		return owlClassDeclarationNode;
	}

	public OWLIndividualDeclarationNode getOWLIndividualDeclarationNode()
	{
		return owlIndividualDeclarationNode;
	}

	public OWLClassExpressionNode getOWLClassExpressionNode()
	{
		return owlClassExpressionNode;
	}

	public boolean hasOWLClassDeclaration()
	{
		return owlClassDeclarationNode != null;
	}

	public boolean hasOWLIndividualDeclaration()
	{
		return owlIndividualDeclarationNode != null;
	}

	public boolean hasOWLClassExpression()
	{
		return owlClassExpressionNode != null;
	}

	public String getNodeName()
	{
		return "OWLClassExpression";
	}

	public String toString()
	{
		String representation = "";

		if (hasOWLClassDeclaration())
			representation += owlClassDeclarationNode.toString();
		if (hasOWLIndividualDeclaration())
			representation += owlIndividualDeclarationNode.toString();
		if (hasOWLClassExpression())
			representation += owlClassExpressionNode.toString();

		return representation;
	}

}

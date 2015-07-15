package org.mm.parser.node;

/**
 * A TypeNode has subclasses OWLClassExpressionNode and ReferenceNode
 *
 * @see OWLClassExpressionNode
 * @see ReferenceNode
 */
public interface TypeNode extends MMNode
{
	boolean isOWLClassExpressionNode();

	boolean isReferenceNode();
}

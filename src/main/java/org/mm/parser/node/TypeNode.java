package org.mm.parser.node;

/**
 * A TypeNode has subclasses OWLClassNode and ReferenceNode
 *
 * @see OWLNamedClassNode
 * @see OWLPropertyNode
 * @see ReferenceNode
 */
public interface TypeNode extends MMNode
{
	boolean isOWLClassNode();

	boolean isOWLPropertyNode();

	boolean isReferenceNode();
}

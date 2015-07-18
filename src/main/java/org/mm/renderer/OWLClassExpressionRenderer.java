package org.mm.renderer;

import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLUnionClassNode;

import java.util.Optional;

public interface OWLClassExpressionRenderer
{
	Optional<? extends Rendering> renderOWLClassExpression(OWLClassExpressionNode classExpressionNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode) throws RendererException;

	Optional<? extends Rendering> renderOWLIntersectionClass(OWLIntersectionClassNode intersectionClassNode)
			throws RendererException;

	Optional<? extends Rendering> renderOWLClassEquivalentTo(OWLClassEquivalentToNode classEquivalentToNode)
			throws RendererException;
}

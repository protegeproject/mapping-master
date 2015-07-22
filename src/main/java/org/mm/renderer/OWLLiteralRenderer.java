package org.mm.renderer;

import org.mm.parser.node.OWLLiteralNode;
import org.mm.rendering.Rendering;

import java.util.Optional;

public interface OWLLiteralRenderer
{
	Optional<? extends Rendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException;
}

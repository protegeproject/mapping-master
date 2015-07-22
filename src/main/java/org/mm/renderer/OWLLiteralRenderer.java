package org.mm.renderer;

import org.mm.parser.node.OWLLiteralNode;
import org.mm.rendering.OWLLiteralRendering;

import java.util.Optional;

public interface OWLLiteralRenderer
{
	Optional<? extends OWLLiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException;
}

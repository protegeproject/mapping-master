package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.OWLLiteralNode;
import org.mm.rendering.Rendering;

public interface LiteralRenderer
{
   Optional<? extends Rendering> renderOWLLiteral(OWLLiteralNode node) throws RendererException;
}

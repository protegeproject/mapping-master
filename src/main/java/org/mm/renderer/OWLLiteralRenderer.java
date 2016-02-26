package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.OWLLiteralNode;
import org.mm.rendering.Rendering;

public interface OWLLiteralRenderer
{
   Optional<? extends Rendering> renderOWLLiteral(OWLLiteralNode node) throws RendererException;
}

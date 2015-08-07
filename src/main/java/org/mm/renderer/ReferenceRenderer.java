package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.ReferenceNode;
import org.mm.rendering.Rendering;

public interface ReferenceRenderer
{
	Optional<? extends Rendering> renderReference(ReferenceNode node) throws RendererException;
}

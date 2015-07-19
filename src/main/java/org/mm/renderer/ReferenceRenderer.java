package org.mm.renderer;

import org.mm.parser.node.ReferenceNode;

import java.util.Optional;

public interface ReferenceRenderer
{
	Optional<? extends Rendering> renderReference(ReferenceNode referenceNode) throws RendererException;
}

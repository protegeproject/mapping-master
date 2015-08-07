package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.ReferenceNode;
import org.mm.rendering.Rendering;

public interface ReferenceRenderer
{
	public Optional<? extends Rendering> renderReference(ReferenceNode referenceNode) throws RendererException;
}

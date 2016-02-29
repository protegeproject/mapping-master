package org.mm.renderer.owlapi.delegator;

import java.util.Optional;

import org.mm.parser.node.TypeNode;
import org.mm.renderer.RendererException;
import org.mm.renderer.owlapi.OWLObjectFactory;

public interface RendererDelegator<T>
{
   Optional<T> render(TypeNode typeNode, OWLObjectFactory objectFactory) throws RendererException;
}

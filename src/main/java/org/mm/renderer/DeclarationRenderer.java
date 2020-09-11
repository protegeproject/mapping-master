package org.mm.renderer;

import java.util.Optional;

import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLObjectPropertyDeclarationNode;
import org.mm.parser.node.OWLObjectPropertyNode;
import org.mm.rendering.Rendering;

public interface DeclarationRenderer
{
   Optional<? extends Rendering> renderOWLClassDeclaration(OWLClassDeclarationNode node) throws RendererException;

   Optional<? extends Rendering> renderOWLIndividualDeclaration(OWLIndividualDeclarationNode node)
         throws RendererException;
   
   Optional<? extends Rendering> renderOWLObjectPropertyDeclaration(OWLObjectPropertyDeclarationNode node)
         throws RendererException;
}

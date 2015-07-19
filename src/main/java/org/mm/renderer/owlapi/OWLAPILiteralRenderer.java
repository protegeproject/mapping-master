package org.mm.renderer.owlapi;

import org.mm.parser.node.OWLLiteralNode;
import org.mm.renderer.OWLLiteralRenderer;
import org.mm.renderer.RendererException;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Optional;

public class OWLAPILiteralRenderer implements OWLLiteralRenderer
{
	private final OWLDataFactory owlDataFactory;

	public OWLAPILiteralRenderer(OWLDataFactory owlDataFactory)
	{
		this.owlDataFactory = owlDataFactory;
	}

	@Override public Optional<OWLLiteralRendering> renderOWLLiteral(OWLLiteralNode literalNode) throws RendererException
	{
		if (literalNode.isBoolean())
			return Optional.of(new OWLLiteralRendering(
					this.owlDataFactory.getOWLLiteral(literalNode.getBooleanLiteralNode().getValue())));
		else if (literalNode.isInteger())
			return Optional.of(new OWLLiteralRendering(
					this.owlDataFactory.getOWLLiteral(literalNode.getIntegerLiteralNode().getValue())));
		else if (literalNode.isFloat())
			return Optional
					.of(new OWLLiteralRendering(this.owlDataFactory.getOWLLiteral(literalNode.getFloatLiteralNode().getValue())));
		else if (literalNode.isString())
			return Optional.of(new OWLLiteralRendering(
					this.owlDataFactory.getOWLLiteral(literalNode.getStringLiteralNode().getValue())));
		else
			throw new RendererException("unknown OWL literal property value " + literalNode.toString());
	}
}

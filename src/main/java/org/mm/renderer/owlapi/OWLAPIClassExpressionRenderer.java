package org.mm.renderer.owlapi;

import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.renderer.OWLClassExpressionRenderer;
import org.mm.renderer.RendererException;
import org.mm.renderer.Rendering;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class OWLAPIClassExpressionRenderer implements OWLClassExpressionRenderer
{
	private final OWLOntology ontology;
	private final OWLDataFactory owlDataFactory;
	private final OWLAPIEntityRenderer entityRenderer;
	private final OWLAPIRestrictionRenderer restrictionRenderer;

	public OWLAPIClassExpressionRenderer(OWLOntology ontology, OWLAPIEntityRenderer entityRenderer,
			OWLAPIRestrictionRenderer restrictionRenderer)
	{
		this.ontology = ontology;
		this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		this.entityRenderer = entityRenderer;
		this.restrictionRenderer = restrictionRenderer;
	}

	@Override public Optional<OWLClassExpressionRendering> renderOWLClassExpression(
			OWLClassExpressionNode classExpressionNode) throws RendererException
	{
		Optional<? extends OWLClassExpressionRendering> classExpressionRendering;

		if (classExpressionNode.hasOWLUnionClassNode())
			classExpressionRendering = renderOWLUnionClass(classExpressionNode.getOWLUnionClassNode());
		else if (classExpressionNode.hasOWLRestrictionNode())
			classExpressionRendering = restrictionRenderer.renderOWLRestriction(classExpressionNode.getOWLRestrictionNode());
		else if (classExpressionNode.hasOWLClassNode())
			classExpressionRendering = entityRenderer.renderOWLClass(classExpressionNode.getOWLClassNode());
		else
			throw new RendererException("unknown child for node " + classExpressionNode.getNodeName());

		if (classExpressionRendering.isPresent()) {
			OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();

			if (classExpressionNode.getIsNegated()) {
				OWLObjectComplementOf restriction = this.owlDataFactory.getOWLObjectComplementOf(classExpression);
				return Optional.of(new OWLClassExpressionRendering(restriction));
			} else
				return Optional.of(new OWLClassExpressionRendering(classExpression));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLClassExpressionRendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode)
			throws RendererException
	{
		Set<OWLClassExpression> classExpressions = new HashSet<>();

		for (OWLIntersectionClassNode intersectionClassNode : unionClassNode.getOWLIntersectionClassNodes()) {
			Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLIntersectionClass(
					intersectionClassNode);
			if (classExpressionRendering.isPresent()) {
				OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
				classExpressions.add(classExpression);
			}
		}

		if (!classExpressions.isEmpty()) {
			OWLObjectUnionOf restriction = this.owlDataFactory.getOWLObjectUnionOf(classExpressions);

			return Optional.of(new OWLClassExpressionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLClassExpressionRendering> renderOWLIntersectionClass(
			OWLIntersectionClassNode intersectionClassNode) throws RendererException
	{
		Set<OWLClassExpression> classExpressions = new HashSet<>();

		for (OWLClassExpressionNode classExpressionNode : intersectionClassNode.getOWLClassExpressionNodes()) {
			Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(classExpressionNode);
			if (classExpressionRendering.isPresent()) {
				OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
				classExpressions.add(classExpression);
			}
		}

		if (!classExpressions.isEmpty()) {
			OWLObjectIntersectionOf restriction = this.owlDataFactory.getOWLObjectIntersectionOf(classExpressions);
			OWLClassExpressionRendering classExpressionRendering = new OWLClassExpressionRendering(restriction);
			return Optional.of(classExpressionRendering);
		} else
			return Optional.empty();
	}

	@Override public Optional<? extends Rendering> renderOWLClassEquivalentTo(
			OWLClassEquivalentToNode classEquivalentToNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}
}

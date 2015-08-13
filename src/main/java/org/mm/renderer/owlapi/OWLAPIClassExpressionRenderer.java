package org.mm.renderer.owlapi;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLDataAllValuesFromNode;
import org.mm.parser.node.OWLDataSomeValuesFromNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLExactCardinalityNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLObjectAllValuesFromNode;
import org.mm.parser.node.OWLObjectOneOfNode;
import org.mm.parser.node.OWLObjectSomeValuesFromNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.OWLClassExpressionRenderer;
import org.mm.renderer.RendererException;
import org.mm.rendering.owlapi.OWLAPILiteralRendering;
import org.mm.rendering.owlapi.OWLAPIReferenceRendering;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.rendering.owlapi.OWLClassExpressionRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.mm.rendering.owlapi.OWLRestrictionRendering;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

public class OWLAPIClassExpressionRenderer implements OWLClassExpressionRenderer
{
	private final OWLAPIEntityRenderer entityRenderer;
	private final OWLAPIReferenceRenderer referenceRenderer;
	private final OWLAPILiteralRenderer literalRenderer;
	private final OWLAPIObjectHandler handler;

	public OWLAPIClassExpressionRenderer(OWLOntology ontology, OWLAPIEntityRenderer entityRenderer)
	{
		this.entityRenderer = entityRenderer;
		
		literalRenderer = new OWLAPILiteralRenderer(ontology);
		handler = new OWLAPIObjectHandler(ontology);
		referenceRenderer = entityRenderer.getReferenceRenderer();
	}

	@Override public Optional<OWLClassExpressionRendering> renderOWLClassExpression(
			OWLClassExpressionNode classExpressionNode) throws RendererException
	{
		Optional<? extends OWLClassExpressionRendering> classExpressionRendering;

		if (classExpressionNode.hasOWLUnionClassNode())
			classExpressionRendering = renderOWLUnionClass(classExpressionNode.getOWLUnionClassNode());
		else if (classExpressionNode.hasOWLRestrictionNode())
			classExpressionRendering = renderOWLRestriction(classExpressionNode.getOWLRestrictionNode());
		else if (classExpressionNode.hasOWLClassNode())
			classExpressionRendering = this.entityRenderer.renderOWLClass(classExpressionNode.getOWLClassNode());
		else
			throw new InternalRendererException("unknown child for node " + classExpressionNode.getNodeName());

		if (classExpressionRendering.isPresent()) {
			OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();

			if (classExpressionNode.getIsNegated()) {
				OWLObjectComplementOf restriction = handler.getOWLObjectComplementOf(classExpression);
				return Optional.of(new OWLClassExpressionRendering(restriction));
			} else
				return Optional.of(new OWLClassExpressionRendering(classExpression));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLClassExpressionRendering> renderOWLUnionClass(OWLUnionClassNode unionNode)
			throws RendererException
	{
		Set<OWLClassExpression> classExpressions = new HashSet<>();

		for (OWLIntersectionClassNode intersectionNode : unionNode.getOWLIntersectionClassNodes()) {
			Optional<OWLClassExpressionRendering> rendering = renderOWLIntersectionClass(intersectionNode);
			if (rendering.isPresent()) {
				OWLClassExpression classExpression = rendering.get().getOWLClassExpression();
				classExpressions.add(classExpression);
			}
		}

		if (classExpressions.size() == 1) {
			return Optional.of(new OWLClassExpressionRendering(classExpressions.iterator().next()));
		}
		else if (classExpressions.size() > 1) {
			OWLObjectUnionOf restriction = handler.getOWLObjectUnionOf(classExpressions);
			return Optional.of(new OWLClassExpressionRendering(restriction));
		}
		return Optional.empty();
	}

	@Override public Optional<OWLClassExpressionRendering> renderOWLObjectOneOf(OWLObjectOneOfNode objectOneOfNode)
			throws RendererException
	{
		Set<OWLNamedIndividual> namedIndividuals = new HashSet<>();

		for (OWLNamedIndividualNode namedIndividualNode : objectOneOfNode.getOWLNamedIndividualNodes()) {
			Optional<OWLNamedIndividualRendering> namedIndividualRendering = this.entityRenderer
					.renderOWLNamedIndividual(namedIndividualNode);
			if (namedIndividualRendering.isPresent()) {
				OWLNamedIndividual namedIndividual = namedIndividualRendering.get().getOWLNamedIndividual();
				namedIndividuals.add(namedIndividual);
			}
		}

		if (!namedIndividuals.isEmpty()) {
			OWLObjectOneOf objectOneOf = handler.getOWLObjectOneOf(namedIndividuals);
			OWLClassExpressionRendering classExpressionRendering = new OWLClassExpressionRendering(objectOneOf);
			return Optional.of(classExpressionRendering);
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLClassExpressionRendering> renderOWLIntersectionClass(
			OWLIntersectionClassNode intersectionNode) throws RendererException
	{
		Set<OWLClassExpression> classExpressions = new HashSet<>();

		for (OWLClassExpressionNode classExpressionNode : intersectionNode.getOWLClassExpressionNodes()) {
			Optional<OWLClassExpressionRendering> rendering = renderOWLClassExpression(classExpressionNode);
			if (rendering.isPresent()) {
				OWLClassExpression classExpression = rendering.get().getOWLClassExpression();
				classExpressions.add(classExpression);
			}
		}

		if (classExpressions.size() == 1) {
			return Optional.of(new OWLClassExpressionRendering(classExpressions.iterator().next()));
		} else if (classExpressions.size() > 1) {
			OWLObjectIntersectionOf restriction = handler.getOWLObjectIntersectionOf(classExpressions);
			OWLClassExpressionRendering classExpressionRendering = new OWLClassExpressionRendering(restriction);
			return Optional.of(classExpressionRendering);
		}
		return Optional.empty();
	}

	@Override public Optional<OWLAPIRendering> renderOWLEquivalentClasses(OWLClassNode declaredClassNode,
			OWLEquivalentClassesNode equivalentClassesNode) throws RendererException
	{
		Optional<OWLClassRendering> declaredClassRendering = this.entityRenderer.renderOWLClass(declaredClassNode);

		if (declaredClassRendering.isPresent()) {
			OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
			Set<OWLClassExpression> classExpressions = new HashSet<>();

			for (OWLClassExpressionNode classExpressionNode : equivalentClassesNode.getClassExpressionNodes()) {
				Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(classExpressionNode);
				if (classExpressionRendering.isPresent()) {
					classExpressions.add(classExpressionRendering.get().getOWLClassExpression());
				}
			}

			if (!classExpressions.isEmpty()) {
				classExpressions.add(declaredClass);
				OWLEquivalentClassesAxiom axiom = handler.getOWLEquivalentClassesAxiom(classExpressions);
				OWLAPIRendering rendering = new OWLAPIRendering(axiom);
				return Optional.of(rendering);
			} else
				return Optional.empty();
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLRestriction(OWLRestrictionNode restrictionNode)
			throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer
				.renderOWLProperty(restrictionNode.getOWLPropertyNode());

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.handler.isOWLDataProperty(property)) { // data property restrictions
				OWLDataProperty dataProperty = handler.getOWLDataProperty(property.getIRI());
				if (restrictionNode.isOWLMinCardinality()) {
					OWLMinCardinalityNode dataMinCardinalityNode = restrictionNode.getOWLMinCardinalityNode();
					return renderOWLDataMinCardinality(restrictionNode.getOWLPropertyNode(), dataMinCardinalityNode);

				} else if (restrictionNode.isOWLMaxCardinality()) {
					OWLMaxCardinalityNode dataMaxCardinalityNode = restrictionNode.getOWLMaxCardinalityNode();
					return renderOWLDataMaxCardinality(restrictionNode.getOWLPropertyNode(), dataMaxCardinalityNode);
				} else if (restrictionNode.isOWLExactCardinality()) {
					OWLExactCardinalityNode dataExactCardinalityNode = restrictionNode.getOWLExactCardinalityNode();
					return renderOWLDataExactCardinality(restrictionNode.getOWLPropertyNode(), dataExactCardinalityNode);
				} else if (restrictionNode.isOWLHasValue()) { // data has value restriction
					OWLHasValueNode hasValueNode = restrictionNode.getOWLHasValueNode();
					return renderOWLDataHasValue(restrictionNode.getOWLPropertyNode(), hasValueNode);
				} else if (restrictionNode.isOWLAllValuesFrom()) { // data all values from restriction
					OWLAllValuesFromNode allValuesFromNode = restrictionNode.getOWLAllValuesFromNode();

					if (!allValuesFromNode.hasOWLDataAllValuesFromNode())
						throw new RendererException("expecting datatype for data all values data restriction " + restrictionNode);

					OWLDataAllValuesFromNode dataAllValuesFromNode = allValuesFromNode.getOWLDataAllValuesFromNode();

					return renderOWLDataAllValuesFrom(restrictionNode.getOWLPropertyNode(), dataAllValuesFromNode);
				} else if (restrictionNode.isOWLSomeValuesFrom()) {
					OWLSomeValuesFromNode someValuesFromNode = restrictionNode.getOWLSomeValuesFromNode();

					if (!someValuesFromNode.hasOWLDataSomeValuesFromNode())
						throw new RendererException("expecting literal for some values data restriction " + restrictionNode);

					OWLDataSomeValuesFromNode dataSomeValuesFromNode = someValuesFromNode.getOWLDataSomeValuesFromNode();
					OWLDatatype datatype = this.handler.getOWLDatatype(dataSomeValuesFromNode.getDatatypeName());
					OWLDataSomeValuesFrom restriction = handler.getOWLDataSomeValuesFrom(dataProperty, datatype);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else
					return Optional.empty();
			} else { // Object property restrictions
				if (restrictionNode.isOWLMinCardinality()) {
					OWLMinCardinalityNode objectMinCardinalityNode = restrictionNode.getOWLMinCardinalityNode();
					return renderOWLObjectMinCardinality(restrictionNode.getOWLPropertyNode(), objectMinCardinalityNode);
				} else if (restrictionNode.isOWLMaxCardinality()) {
					OWLMaxCardinalityNode objectMaxCardinalityNode = restrictionNode.getOWLMaxCardinalityNode();
					return renderOWLObjectMaxCardinality(restrictionNode.getOWLPropertyNode(), objectMaxCardinalityNode);
				} else if (restrictionNode.isOWLExactCardinality()) {
					OWLExactCardinalityNode objectExactCardinalityNode = restrictionNode.getOWLExactCardinalityNode();
					return renderOWLObjectExactCardinality(restrictionNode.getOWLPropertyNode(), objectExactCardinalityNode);
				} else if (restrictionNode.isOWLHasValue()) {
					OWLHasValueNode objectHasValueNode = restrictionNode.getOWLHasValueNode();

					return renderOWLObjectHasValue(restrictionNode.getOWLPropertyNode(), objectHasValueNode);
				} else if (restrictionNode.isOWLAllValuesFrom()) { // Object all values from restriction
					OWLAllValuesFromNode allValuesFromNode = restrictionNode.getOWLAllValuesFromNode();

					if (allValuesFromNode.hasOWLDataAllValuesFromNode())
						throw new RendererException("expecting class for all values object restriction " + restrictionNode);

					return renderOWLObjectAllValuesFrom(restrictionNode.getOWLPropertyNode(),
							allValuesFromNode.getObjectOWLAllValuesFromNode());

				} else if (restrictionNode.isOWLSomeValuesFrom()) {
					OWLSomeValuesFromNode someValuesFromNode = restrictionNode.getOWLSomeValuesFromNode();
					if (someValuesFromNode.hasOWLDataSomeValuesFromNode())
						throw new RendererException("expecting class for object some values from restriction " + restrictionNode);

					return renderOWLObjectSomeValuesFrom(restrictionNode.getOWLPropertyNode(),
							someValuesFromNode.getOWLObjectSomeValuesFromNode());
				} else
					return Optional.empty();
			}
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectExactCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode exactCardinalityNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is object property
			OWLObjectProperty objectProperty = handler.getOWLObjectProperty(propertyIRI);
			int cardinality = exactCardinalityNode.getCardinality();
			OWLObjectExactCardinality restriction = handler.getOWLObjectExactCardinality(cardinality, objectProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataExactCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode exactCardinalityNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is data property
			OWLDataProperty dataProperty = handler.getOWLDataProperty(propertyIRI);
			int cardinality = exactCardinalityNode.getCardinality();
			OWLDataExactCardinality restriction = handler.getOWLDataExactCardinality(cardinality, dataProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is object property
			OWLObjectProperty objectProperty = handler.getOWLObjectProperty(propertyIRI);
			int cardinality = maxCardinalityNode.getCardinality();
			OWLObjectMaxCardinality restriction = handler.getOWLObjectMaxCardinality(cardinality, objectProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is data property
			OWLDataProperty dataProperty = handler.getOWLDataProperty(propertyIRI);
			int cardinality = maxCardinalityNode.getCardinality();
			OWLDataMaxCardinality restriction = handler.getOWLDataMaxCardinality(cardinality, dataProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is object property
			OWLObjectProperty objectProperty = handler.getOWLObjectProperty(propertyIRI);
			int cardinality = minCardinalityNode.getCardinality();
			OWLObjectMinCardinality restriction = handler.getOWLObjectMinCardinality(cardinality, objectProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is data property
			OWLDataProperty dataProperty = handler.getOWLDataProperty(propertyIRI);
			int cardinality = minCardinalityNode.getCardinality();
			OWLDataMinCardinality restriction = handler.getOWLDataMinCardinality(cardinality, dataProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectHasValue(OWLPropertyNode propertyNode,
			OWLHasValueNode objectHasValueNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.handler.isOWLObjectProperty(property)) {
				OWLObjectProperty objectProperty = handler.getOWLObjectProperty(property.getIRI());

				if (objectHasValueNode.hasNameNone()) {
					NameNode nameNode = objectHasValueNode.getNameNode();
					String shortName = nameNode.getName();
					if (this.handler.isOWLNamedIndividual(shortName)) {
						OWLNamedIndividual individual = this.handler.getOWLNamedIndividual(shortName);
						OWLObjectHasValue objectHasValueRestriction = handler.getOWLObjectHasValue(objectProperty, individual);
						return Optional.of(new OWLRestrictionRendering(objectHasValueRestriction));
					} else
						throw new RendererException(
								"value " + shortName + " supplied for object has value restriction is not an OWL named individual");
				} else if (objectHasValueNode.hasReferenceNode()) {
					ReferenceNode referenceNode = objectHasValueNode.getReferenceNode();
					Optional<OWLAPIReferenceRendering> referenceRendering = this.referenceRenderer.renderReference(referenceNode);
					if (referenceRendering.isPresent()) {
						if (referenceRendering.get().isOWLNamedIndividual()) {
							OWLNamedIndividual individual = referenceRendering.get().getOWLEntity().get().asOWLNamedIndividual();
							OWLObjectHasValue objectHasValueRestriction = handler.getOWLObjectHasValue(objectProperty, individual);
							return Optional.of(new OWLRestrictionRendering(objectHasValueRestriction));
						} else
							throw new RendererException(
									"reference value " + referenceNode + " for object has value is not an OWL named individual");
					} else
						return Optional.empty();
				} else
					throw new InternalRendererException("unknown child node for node " + objectHasValueNode.getNodeName());
			}
			throw new RendererException(
					"property " + property.getIRI() + " in  object has values restriction is not an object property");
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataHasValue(OWLPropertyNode propertyNode,
			OWLHasValueNode hasValueNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.handler.isOWLDataProperty(property)) {
				OWLDataProperty dataProperty = handler.getOWLDataProperty(property.getIRI());
				if (hasValueNode.hasLiteralNode()) {
					Optional<OWLAPILiteralRendering> literalRendering = this.literalRenderer
							.renderOWLLiteral(hasValueNode.getOWLLiteralNode());
					if (literalRendering.isPresent()) {
						OWLLiteral literal = literalRendering.get().getOWLLiteral();
						OWLDataHasValue dataHasValue = handler.getOWLDataHasValue(dataProperty, literal);
						return Optional.of(new OWLRestrictionRendering(dataHasValue));
					} else
						return Optional.empty();
				} else if (hasValueNode.hasReferenceNode()) {
					Optional<OWLAPIReferenceRendering> referenceRendering = this.referenceRenderer
							.renderReference(hasValueNode.getReferenceNode());
					if (referenceRendering.isPresent()) {
						if (referenceRendering.get().isOWLLiteral()) {
							OWLLiteral literal = referenceRendering.get().getOWLLiteral().get();
							OWLDataHasValue dataHasValue = handler.getOWLDataHasValue(dataProperty, literal);
							return Optional.of(new OWLRestrictionRendering(dataHasValue));
						} else
							throw new RendererException("expecting literal reference for data has value restriction " + hasValueNode);
					} else
						return Optional.empty();
				} else
					throw new RendererException("expecting literal or reference for data has value restriction " + hasValueNode);
			} else
				throw new RendererException(
						"property " + property.getIRI() + " in  data has values restriction is not a data property");
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectSomeValuesFromNode objectSomeValuesFromNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.handler.isOWLObjectProperty(property)) {
				OWLObjectProperty objectProperty = handler.getOWLObjectProperty(property.getIRI());

				if (objectSomeValuesFromNode.hasOWLClassExpressionNode()) {
					Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(
							objectSomeValuesFromNode.getOWLClassExpressionNode());
					if (classExpressionRendering.isPresent()) {
						OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
						OWLObjectSomeValuesFrom objectSomeValuesFromRestriction =
								handler.getOWLObjectSomeValuesFrom(objectProperty, classExpression);

						return Optional.of(new OWLRestrictionRendering(objectSomeValuesFromRestriction));
					} else
						return Optional.empty();
				} else if (objectSomeValuesFromNode.hasOWLClassNode()) {
					Optional<OWLClassRendering> classRendering = this.entityRenderer
							.renderOWLClass(objectSomeValuesFromNode.getOWLClassNode());
					if (classRendering.isPresent()) {
						OWLClassExpression cls = classRendering.get().getOWLClass();
						OWLObjectSomeValuesFrom objectSomeValuesFromRestriction =
								handler.getOWLObjectSomeValuesFrom(objectProperty, cls);
						return Optional.of(new OWLRestrictionRendering(objectSomeValuesFromRestriction));
					} else
						return Optional.empty();
				} else
					throw new InternalRendererException("unknown child node for node " + objectSomeValuesFromNode.getNodeName());
			} else
				throw new RendererException(
						"property " + property.getIRI() + " in object some values from restriction is not an object property");
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataSomeValuesFrom(OWLPropertyNode propertyNode,
			OWLDataSomeValuesFromNode dataSomeValuesFromNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.handler.isOWLDataProperty(property)) {
				OWLDataProperty dataProperty = handler.getOWLDataProperty(property.getIRI());
				String datatypeName = dataSomeValuesFromNode.getDatatypeName();
				OWLDatatype datatype = this.handler.getOWLDatatype(datatypeName);
				OWLDataSomeValuesFrom dataSomeValuesFrom = handler.getOWLDataSomeValuesFrom(dataProperty, datatype);
				return Optional.of(new OWLRestrictionRendering(dataSomeValuesFrom));
			} else
				throw new RendererException(
						"property " + property.getIRI() + " in object some values from restriction is not a data property");
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectAllValuesFromNode onlyNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (handler.isOWLObjectProperty(property)) {
				OWLObjectProperty op = handler.getOWLObjectProperty(property.getIRI());
				if (onlyNode.hasOWLClassExpression()) {
					Optional<OWLClassExpressionRendering> rendering = renderOWLClassExpression(onlyNode.getOWLClassExpressionNode());
					if (rendering.isPresent()) {
						OWLClassExpression ce = rendering.get().getOWLClassExpression();
						OWLObjectAllValuesFrom objectAllValuesFromRestriction = handler.getOWLObjectAllValuesFrom(op, ce);
						return Optional.of(new OWLRestrictionRendering(objectAllValuesFromRestriction));
					}
				} else if (onlyNode.hasOWLClass()) {
					Optional<OWLClassRendering> rendering = entityRenderer.renderOWLClass(onlyNode.getOWLClassNode());
					if (rendering.isPresent()) {
						OWLClassExpression ce = rendering.get().getOWLClass();
						OWLObjectAllValuesFrom objectAllValuesFromRestriction = handler.getOWLObjectAllValuesFrom(op, ce);
						return Optional.of(new OWLRestrictionRendering(objectAllValuesFromRestriction));
					}
				} else if (onlyNode.hasOWLObjectOneOfNode()) {
					Optional<OWLClassExpressionRendering> rendering = renderOWLObjectOneOf(onlyNode.getOWLObjectOneOfNode());
					if (rendering.isPresent()) {
						OWLClassExpression ce = rendering.get().getOWLClassExpression();
						OWLObjectAllValuesFrom objectAllValuesFromRestriction = handler.getOWLObjectAllValuesFrom(op, ce);
						return Optional.of(new OWLRestrictionRendering(objectAllValuesFromRestriction));
					}
				}
			} else {
				throw new RendererException(
						"property " + property.getIRI() + " in object all values from restriction is not an object property");
			}
		}
		return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
			OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException
	{
		Optional<? extends OWLPropertyRendering> propertyRendering = this.entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.handler.isOWLDataProperty(property)) {
				OWLDataProperty dataProperty = handler.getOWLDataProperty(property.getIRI());
				OWLDatatype datatype = this.handler.getOWLDatatype(dataAllValuesFromNode.getDatatypeName());
				OWLDataAllValuesFrom restriction = handler.getOWLDataAllValuesFrom(dataProperty, datatype);

				return Optional.of(new OWLRestrictionRendering(restriction));
			} else
				throw new RendererException(
						"property " + property.getIRI() + " in data all values from restriction is not a data property");
		} else
			return Optional.empty();
	}
}

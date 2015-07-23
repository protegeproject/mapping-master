package org.mm.renderer.owlapi;

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
import org.semanticweb.owlapi.model.OWLDataFactory;
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
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class OWLAPIClassExpressionRenderer implements OWLClassExpressionRenderer
{
	private final OWLOntology ontology;
	private final OWLDataFactory owlDataFactory;
	private final OWLAPIEntityRenderer entityRenderer;
	private final OWLAPIReferenceRenderer referenceRenderer;
	private final OWLAPILiteralRenderer literalRenderer;
	private final OWLAPIObjectHandler owlObjectHandler;

	public OWLAPIClassExpressionRenderer(OWLOntology ontology, OWLAPIEntityRenderer entityRenderer,
			OWLAPIReferenceRenderer referenceRenderer, OWLAPILiteralRenderer literalRenderer)
	{
		this.ontology = ontology;
		this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		this.referenceRenderer = referenceRenderer;
		this.literalRenderer = literalRenderer;
		this.entityRenderer = entityRenderer;
		this.owlObjectHandler = new OWLAPIObjectHandler(ontology);
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
			classExpressionRendering = entityRenderer.renderOWLClass(classExpressionNode.getOWLClassNode());
		else
			throw new InternalRendererException("unknown child for node " + classExpressionNode.getNodeName());

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

	@Override public Optional<OWLClassExpressionRendering> renderOWLObjectOneOf(OWLObjectOneOfNode objectOneOfNode)
			throws RendererException
	{
		Set<OWLNamedIndividual> namedIndividuals = new HashSet<>();

		for (OWLNamedIndividualNode namedIndividualNode : objectOneOfNode.getOWLNamedIndividualNodes()) {
			Optional<OWLNamedIndividualRendering> namedIndividualRendering = entityRenderer
					.renderOWLNamedIndividual(namedIndividualNode);
			if (namedIndividualRendering.isPresent()) {
				OWLNamedIndividual namedIndividual = namedIndividualRendering.get().getOWLNamedIndividual();
				namedIndividuals.add(namedIndividual);
			}
		}

		if (!namedIndividuals.isEmpty()) {
			OWLObjectOneOf objectOneOf = this.owlDataFactory.getOWLObjectOneOf(namedIndividuals);
			OWLClassExpressionRendering classExpressionRendering = new OWLClassExpressionRendering(objectOneOf);
			return Optional.of(classExpressionRendering);
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

	@Override public Optional<OWLAPIRendering> renderOWLEquivalentClasses(OWLClassNode declaredClassNode,
			OWLEquivalentClassesNode equivalentClassesNode) throws RendererException
	{
		Optional<OWLClassRendering> declaredClassRendering = entityRenderer.renderOWLClass(declaredClassNode);

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
				OWLEquivalentClassesAxiom axiom = this.owlDataFactory.getOWLEquivalentClassesAxiom(classExpressions);
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
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer
				.renderOWLProperty(restrictionNode.getOWLPropertyNode());

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			if (this.owlObjectHandler.isOWLDataProperty(propertyIRI)) { // data property restrictions
				OWLDataProperty dataProperty = this.owlDataFactory.getOWLDataProperty(propertyIRI);
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
						throw new RendererException("expecting data value for some values data restriction " + restrictionNode);

					OWLDataSomeValuesFromNode dataSomeValuesFromNode = someValuesFromNode.getOWLDataSomeValuesFromNode();
					OWLDatatype datatype = this.owlObjectHandler.getOWLDatatype(dataSomeValuesFromNode.getDatatypeName());
					OWLDataSomeValuesFrom restriction = this.owlDataFactory.getOWLDataSomeValuesFrom(dataProperty, datatype);

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

					return renderOWLDataSomeValuesFrom(restrictionNode.getOWLPropertyNode(),
							someValuesFromNode.getOWLDataSomeValuesFromNode());
				} else
					return Optional.empty();
			}
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectExactCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode exactCardinalityNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is object property
			OWLObjectProperty objectProperty = this.owlDataFactory.getOWLObjectProperty(propertyIRI);
			int cardinality = exactCardinalityNode.getCardinality();
			OWLObjectExactCardinality restriction = this.owlDataFactory
					.getOWLObjectExactCardinality(cardinality, objectProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataExactCardinality(OWLPropertyNode propertyNode,
			OWLExactCardinalityNode exactCardinalityNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is data property
			OWLDataProperty dataProperty = this.owlDataFactory.getOWLDataProperty(propertyIRI);
			int cardinality = exactCardinalityNode.getCardinality();
			OWLDataExactCardinality restriction = this.owlDataFactory.getOWLDataExactCardinality(cardinality, dataProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is object property
			OWLObjectProperty objectProperty = this.owlDataFactory.getOWLObjectProperty(propertyIRI);
			int cardinality = maxCardinalityNode.getCardinality();
			OWLObjectMinCardinality restriction = this.owlDataFactory.getOWLObjectMinCardinality(cardinality, objectProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataMaxCardinality(OWLPropertyNode propertyNode,
			OWLMaxCardinalityNode maxCardinalityNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is data property
			OWLDataProperty dataProperty = this.owlDataFactory.getOWLDataProperty(propertyIRI);
			int cardinality = maxCardinalityNode.getCardinality();
			OWLDataMaxCardinality restriction = this.owlDataFactory.getOWLDataMaxCardinality(cardinality, dataProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is object property
			OWLObjectProperty objectProperty = this.owlDataFactory.getOWLObjectProperty(propertyIRI);
			int cardinality = minCardinalityNode.getCardinality();
			OWLObjectMinCardinality restriction = this.owlDataFactory.getOWLObjectMinCardinality(cardinality, objectProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataMinCardinality(OWLPropertyNode propertyNode,
			OWLMinCardinalityNode minCardinalityNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			// TODO Check if is data property
			OWLDataProperty dataProperty = this.owlDataFactory.getOWLDataProperty(propertyIRI);
			int cardinality = minCardinalityNode.getCardinality();
			OWLDataMinCardinality restriction = this.owlDataFactory.getOWLDataMinCardinality(cardinality, dataProperty);

			return Optional.of(new OWLRestrictionRendering(restriction));
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectHasValue(OWLPropertyNode propertyNode,
			OWLHasValueNode objectHasValueNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.owlObjectHandler.isOWLObjectProperty(property)) {
				OWLObjectProperty objectProperty = this.owlDataFactory.getOWLObjectProperty(property.getIRI());

				if (objectHasValueNode.hasNameNone()) {
					NameNode nameNode = objectHasValueNode.getNameNode();
					String shortName = nameNode.getName();
					if (this.owlObjectHandler.isOWLDataProperty(shortName)) {
						OWLNamedIndividual individual = this.owlObjectHandler.getOWLNamedIndividual(shortName);
						OWLObjectHasValue objectHasValueRestriction = this.owlDataFactory
								.getOWLObjectHasValue(objectProperty, individual);
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
							OWLObjectHasValue objectHasValueRestriction = this.owlDataFactory
									.getOWLObjectHasValue(objectProperty, individual);
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
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.owlObjectHandler.isOWLDataProperty(property)) {
				OWLDataProperty dataProperty = this.owlDataFactory.getOWLDataProperty(property.getIRI());
				if (hasValueNode.hasLiteralNode()) {
					Optional<OWLAPILiteralRendering> literalRendering = literalRenderer
							.renderOWLLiteral(hasValueNode.getOWLLiteralNode());
					if (literalRendering.isPresent()) {
						OWLLiteral literal = literalRendering.get().getOWLLiteral();
						OWLDataHasValue dataHasValue = this.owlDataFactory.getOWLDataHasValue(dataProperty, literal);
						return Optional.of(new OWLRestrictionRendering(dataHasValue));
					} else
						return Optional.empty();
				} else if (hasValueNode.hasReferenceNode()) {
					Optional<OWLAPIReferenceRendering> referenceRendering = referenceRenderer
							.renderReference(hasValueNode.getReferenceNode());
					if (referenceRendering.isPresent()) {
						if (referenceRendering.get().isOWLLiteral()) {
							OWLLiteral literal = referenceRendering.get().getOWLLiteral().get();
							OWLDataHasValue dataHasValue = this.owlDataFactory.getOWLDataHasValue(dataProperty, literal);
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
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.owlObjectHandler.isOWLObjectProperty(property)) {
				OWLObjectProperty objectProperty = this.owlDataFactory.getOWLObjectProperty(property.getIRI());

				if (objectSomeValuesFromNode.hasOWLClassExpressionNode()) {
					Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(
							objectSomeValuesFromNode.getOWLClassExpressionNode());
					if (classExpressionRendering.isPresent()) {
						OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
						OWLObjectSomeValuesFrom objectSomeValuesFromRestriction = this.owlDataFactory
								.getOWLObjectSomeValuesFrom(objectProperty, classExpression);

						return Optional.of(new OWLRestrictionRendering(objectSomeValuesFromRestriction));
					} else
						return Optional.empty();
				} else if (objectSomeValuesFromNode.hasOWLClassNode()) {
					Optional<OWLClassRendering> classRendering = entityRenderer
							.renderOWLClass(objectSomeValuesFromNode.getOWLClassNode());
					if (classRendering.isPresent()) {
						OWLClassExpression cls = classRendering.get().getOWLClass();
						OWLObjectSomeValuesFrom objectSomeValuesFromRestriction = this.owlDataFactory
								.getOWLObjectSomeValuesFrom(objectProperty, cls);
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
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.owlObjectHandler.isOWLDataProperty(property)) {
				OWLDataProperty dataProperty = this.owlDataFactory.getOWLDataProperty(property.getIRI());
				String datatypeName = dataSomeValuesFromNode.getDatatypeName();
				OWLDatatype datatype = this.owlObjectHandler.getOWLDatatype(datatypeName);
				OWLDataSomeValuesFrom dataSomeValuesFrom = this.owlDataFactory.getOWLDataSomeValuesFrom(dataProperty, datatype);
				return Optional.of(new OWLRestrictionRendering(dataSomeValuesFrom));
			} else
				throw new RendererException(
						"property " + property.getIRI() + " in object some values from restriction is not an object property");
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLObjectAllValuesFrom(OWLPropertyNode propertyNode,
			OWLObjectAllValuesFromNode objectAllValuesFromNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.owlObjectHandler.isOWLObjectProperty(property)) {
				OWLObjectProperty objectProperty = this.owlDataFactory.getOWLObjectProperty(property.getIRI());

				if (objectAllValuesFromNode.hasOWLClassExpression()) {
					Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(
							objectAllValuesFromNode.getOWLClassExpressionNode());
					if (classExpressionRendering.isPresent()) {
						OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
						OWLObjectAllValuesFrom objectAllValuesFromRestriction = this.owlDataFactory
								.getOWLObjectAllValuesFrom(objectProperty, classExpression);
						return Optional.of(new OWLRestrictionRendering(objectAllValuesFromRestriction));
					} else
						return Optional.empty();
				} else {
					Optional<OWLClassRendering> classRendering = entityRenderer
							.renderOWLClass(objectAllValuesFromNode.getOWLClassNode());

					if (classRendering.isPresent()) {
						OWLClassExpression classExpression = classRendering.get().getOWLClass();
						OWLObjectAllValuesFrom objectAllValuesFromRestriction = this.owlDataFactory
								.getOWLObjectAllValuesFrom(objectProperty, classExpression);
						return Optional.of(new OWLRestrictionRendering(objectAllValuesFromRestriction));
					} else
						return Optional.empty();
				}
			} else
				throw new RendererException(
						"property " + property.getIRI() + " in object all values from restriction is not an object property");
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLRestrictionRendering> renderOWLDataAllValuesFrom(OWLPropertyNode propertyNode,
			OWLDataAllValuesFromNode dataAllValuesFromNode) throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = entityRenderer.renderOWLProperty(propertyNode);

		if (propertyRendering.isPresent()) {
			OWLProperty property = propertyRendering.get().getOWLProperty();
			if (this.owlObjectHandler.isOWLDataProperty(property)) {
				OWLDataProperty dataProperty = this.owlDataFactory.getOWLDataProperty(property.getIRI());
				OWLDatatype datatype = this.owlObjectHandler.getOWLDatatype(dataAllValuesFromNode.getDatatypeName());
				OWLDataAllValuesFrom restriction = this.owlDataFactory.getOWLDataAllValuesFrom(dataProperty, datatype);

				return Optional.of(new OWLRestrictionRendering(restriction));
			} else
				throw new RendererException(
						"property " + property.getIRI() + " in data all values from restriction is not a data property");
		} else
			return Optional.empty();
	}
}

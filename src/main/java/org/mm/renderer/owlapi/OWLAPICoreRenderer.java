package org.mm.renderer.owlapi;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLClassNode;
import org.mm.parser.node.OWLDifferentFromNode;
import org.mm.parser.node.OWLEquivalentClassesNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLSameAsNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.renderer.CoreRenderer;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.RendererException;
import org.mm.rendering.Rendering;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.rendering.owlapi.OWLAnnotationPropertyRendering;
import org.mm.rendering.owlapi.OWLAnnotationValueRendering;
import org.mm.rendering.owlapi.OWLClassExpressionRendering;
import org.mm.rendering.owlapi.OWLClassRendering;
import org.mm.rendering.owlapi.OWLNamedIndividualRendering;
import org.mm.rendering.owlapi.OWLPropertyAssertionObjectRendering;
import org.mm.rendering.owlapi.OWLPropertyRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OWLAPICoreRenderer implements CoreRenderer, MappingMasterParserConstants
{
	public static final int NameEncodings[] = { MM_LOCATION, MM_LITERAL, RDF_ID, RDFS_LABEL };
	public static final int ReferenceValueTypes[] = { OWL_CLASS, OWL_NAMED_INDIVIDUAL, OWL_OBJECT_PROPERTY,
			OWL_DATA_PROPERTY, XSD_INT, XSD_STRING, XSD_FLOAT, XSD_DOUBLE, XSD_SHORT, XSD_BOOLEAN, XSD_TIME, XSD_DATETIME,
			XSD_DURATION };
	public static final int PropertyTypes[] = { OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY };
	public static final int PropertyValueTypes[] = ReferenceValueTypes;
	public static final int DataPropertyValueTypes[] = { XSD_STRING, XSD_BYTE, XSD_SHORT, XSD_INT, XSD_FLOAT, XSD_DOUBLE,
			XSD_BOOLEAN, XSD_TIME, XSD_DATETIME, XSD_DATE, XSD_DURATION };

	private final OWLDataFactory owlDataFactory;
	private final OWLAPIObjectHandler owlObjectHandler;
	private final OWLAPIEntityRenderer entityRenderer;
	private final OWLAPILiteralRenderer literalRenderer;
	private final OWLAPIClassExpressionRenderer classExpressionRenderer;
	private final OWLAPIReferenceRenderer referenceRenderer;

	public OWLAPICoreRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource)
	{
		this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		this.owlObjectHandler = new OWLAPIObjectHandler(ontology);

		this.entityRenderer = new OWLAPIEntityRenderer(ontology, dataSource);
		this.literalRenderer = new OWLAPILiteralRenderer(ontology.getOWLOntologyManager().getOWLDataFactory());
		this.referenceRenderer = new OWLAPIReferenceRenderer(ontology, dataSource, this.entityRenderer,
				this.literalRenderer);
		this.classExpressionRenderer = new OWLAPIClassExpressionRenderer(ontology, this.entityRenderer,
				this.referenceRenderer, this.literalRenderer);
	}

	public Optional<OWLAPIRendering> renderExpression(ExpressionNode expressionNode) throws RendererException
	{
		if (expressionNode.hasMMExpression())
			return renderMMExpression(expressionNode.getMMExpressionNode());
		else
			throw new InternalRendererException("unknown child for node " + expressionNode.getNodeName());
	}

	@Override public Optional<OWLAPIRendering> renderMMExpression(MMExpressionNode mmExpressionNode)
			throws RendererException
	{
		if (mmExpressionNode.hasOWLClassDeclaration())
			return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
		else if (mmExpressionNode.hasOWLIndividualDeclaration())
			return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
		else
			throw new InternalRendererException("unknown child for node: " + mmExpressionNode.getNodeName());
	}

	// TODO Refactor - too long
	@Override public Optional<OWLAPIRendering> renderOWLClassDeclaration(OWLClassDeclarationNode classDeclarationNode)
			throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		// logLine("=====================OWLClassDeclaration================================");
		// logLine("MappingMaster DSL expression: " + classDeclarationNode);

		Optional<OWLClassRendering> declaredClassRendering = this.entityRenderer.renderOWLClass(classDeclarationNode.getOWLClassNode());
		if (!declaredClassRendering.isPresent()) {
			//rendering.logLine("processReference: skipping OWL class declaration because of missing class");
			return Optional.empty();
		}
		
		OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
		
		if (classDeclarationNode.hasOWLSubclassOfNodes()) {
			for (OWLSubclassOfNode subclassOfNode : classDeclarationNode.getOWLSubclassOfNodes()) {
				for (OWLClassExpressionNode classExpressionNode : subclassOfNode.getClassExpressionNodes()) {
					Optional<OWLClassExpressionRendering> classExpressionRendering = classExpressionRenderer.renderOWLClassExpression(classExpressionNode);
					if (!classExpressionRendering.isPresent()) {
						//logLine(
						//  "processReference: skipping subclass declaration [" + subclassOfNode + "] because of missing class");
					} else {
						OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
						OWLSubClassOfAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(classExpression, declaredClass);
						axioms.add(axiom);
					}
				}
			}
		}
		else {
			OWLDeclarationAxiom declaredAxiom = owlDataFactory.getOWLDeclarationAxiom(declaredClass);
			axioms.add(declaredAxiom);
		}

		if (classDeclarationNode.hasOWLEquivalentClassesNode()) {
			for (OWLEquivalentClassesNode equivalentClassesNode : classDeclarationNode.getOWLEquivalentClassesNodes()) {
				for (OWLClassExpressionNode classExpressionNode : equivalentClassesNode.getClassExpressionNodes()) {
					Optional<OWLClassExpressionRendering> classExpressionRendering = this.classExpressionRenderer
							.renderOWLClassExpression(classExpressionNode);
					if (!classExpressionRendering.isPresent()) {
						//logLine("processReference: skipping equivalent declaration [" + equivalentClassesNode
						//  + "] because of missing class");
					} else {
						OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
						OWLEquivalentClassesAxiom axiom = owlDataFactory.getOWLEquivalentClassesAxiom(classExpression, declaredClass);
						axioms.add(axiom);
					}
				}
			}
		}

		if (classDeclarationNode.hasAnnotationFactNodes()) {
			for (AnnotationFactNode annotationFactNode : classDeclarationNode.getAnnotationFactNodes()) {
				Optional<OWLAnnotationPropertyRendering> propertyRendering = this.entityRenderer
						.renderOWLAnnotationProperty(annotationFactNode.getOWLPropertyNode());
				OWLAnnotationValueNode annotationValueNode = annotationFactNode.getOWLAnnotationValueNode();
				Optional<OWLAnnotationValueRendering> annotationValueRendering = renderOWLAnnotationValue(
						annotationValueNode);

				if (!propertyRendering.isPresent()) {
					//logLine("processReference: skipping OWL annotation clause [" + annotationFactNode
					//  + "] because of missing property name");
					continue;
				}

				if (!annotationValueRendering.isPresent()) {
					// logLine("processReference: skipping OWL annotation clause [" + annotationFactNode
					//  + "] because of missing property value");
					continue;
				}

				OWLAnnotationProperty property = propertyRendering.get().getOWLAnnotationProperty();
				OWLAnnotationValue annotationValue = annotationValueRendering.get().getOWLAnnotationValue();
				OWLAnnotationAssertionAxiom axiom = owlDataFactory.getOWLAnnotationAssertionAxiom(property, declaredClass.getIRI(), annotationValue);
				axioms.add(axiom);
			}
		}
		return Optional.of(new OWLAPIRendering(axioms));
	}

	@Override public Optional<OWLAPIRendering> renderOWLIndividualDeclaration(
			OWLIndividualDeclarationNode individualDeclarationNode) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		// logLine("=====================OWLIndividualDeclaration================================");
		// logLine("MappingMaster DSL expression: " + individualDeclarationNode);

		Optional<OWLNamedIndividualRendering> declaredIndividualRendering = this.entityRenderer
				.renderOWLNamedIndividual(individualDeclarationNode.getOWLIndividualNode());
		if (!declaredIndividualRendering.isPresent()) {
			// logLine("Skipping OWL individual declaration because of missing individual name");
		} else {
			OWLNamedIndividual declaredIndividual = declaredIndividualRendering.get().getOWLNamedIndividual();

			if (individualDeclarationNode.hasFacts()) { // We have a Facts: clause
				List<FactNode> factNodes = individualDeclarationNode.getFactNodes();
				Set<OWLAxiom> factsAxioms = processFactsClause(declaredIndividualRendering, factNodes);
				axioms.addAll(factsAxioms);
			}

			if (individualDeclarationNode.hasAnnotations()) { // We have an Annotations: clause
				List<AnnotationFactNode> annotationFactNodes = individualDeclarationNode.getAnnotationNodes();
				Set<OWLAxiom> annotationsAxioms = processAnnotationClause(declaredIndividualRendering, annotationFactNodes);
				axioms.addAll(annotationsAxioms);
			}

			if (individualDeclarationNode.hasSameAs()) { // We have a SameAs: clause
				Set<OWLAxiom> sameAsAxioms = processSameAsClause(individualDeclarationNode, declaredIndividualRendering);
				axioms.addAll(sameAsAxioms);
			}

			if (individualDeclarationNode.hasDifferentFrom()) { // We have a DifferentFrom: clause
				Set<OWLAxiom> differentFromAxioms = processDifferentFromClause(individualDeclarationNode,
						declaredIndividualRendering);
				axioms.addAll(differentFromAxioms);
			}

			if (individualDeclarationNode.hasTypes()) { // We have a Types: clause
				Set<OWLAxiom> typesAxioms = this.referenceRenderer
						.processTypesClause(declaredIndividual, individualDeclarationNode.getTypesNode().getTypeNodes());
				axioms.addAll(typesAxioms);
			}
			// TODO Implement individual declaration axioms
		}
		if (!axioms.isEmpty())
			return Optional.of(new OWLAPIRendering(axioms));
		else
			return Optional.empty();
	}

	@Override public Optional<OWLAPIRendering> renderOWLSubClassOf(OWLClassNode declaredClassNode,
			OWLSubclassOfNode subclassOfNode) throws RendererException
	{
		Optional<OWLClassRendering> declaredClassRendering = this.entityRenderer.renderOWLClass(declaredClassNode);

		if (declaredClassRendering.isPresent()) {
			OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
			Set<OWLAxiom> axioms = new HashSet<>();
			for (OWLClassExpressionNode classExpressionNode : subclassOfNode.getClassExpressionNodes()) {
				Optional<OWLClassExpressionRendering> subClassRendering = this.classExpressionRenderer
						.renderOWLClassExpression(classExpressionNode);
				if (subClassRendering.isPresent()) {
					OWLClassExpression subClass = subClassRendering.get().getOWLClassExpression();
					OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(declaredClass, subClass);
					axioms.add(axiom);
				}
			}
			if (!axioms.isEmpty())
				return Optional.of(new OWLAPIRendering(axioms));
			else
				return Optional.empty();
		} else
			return Optional.empty();
	}

	@Override public Optional<OWLPropertyAssertionObjectRendering> renderOWLPropertyAssertionObject(
			OWLPropertyAssertionObjectNode propertyAssertionObjectNode) throws RendererException
	{
		if (propertyAssertionObjectNode.isName()) {
			String name = propertyAssertionObjectNode.getNameNode().getName();
			// TODO Implement
			throw new InternalRendererException("not implemented");
			//return Optional.empty();
		} else if (propertyAssertionObjectNode.isLiteral()) {
			throw new InternalRendererException("not implemented");
			// return Optional.empty(); // TODO Implement
		} else if (propertyAssertionObjectNode.isReference()) {
			throw new InternalRendererException("not implemented");
			// return Optional.empty(); // TODO Implement
		} else
			throw new InternalRendererException("unknown child node for node " + propertyAssertionObjectNode.getNodeName());
	}

	@Override public Optional<OWLAnnotationValueRendering> renderOWLAnnotationValue(
			OWLAnnotationValueNode annotationValueNode) throws RendererException
	{
		throw new InternalRendererException("not implemented"); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLSameAs(OWLSameAsNode sameAsNode) throws RendererException
	{
		throw new InternalRendererException("not implemented");  // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLDifferentFrom(OWLDifferentFromNode differentFromNode)
			throws RendererException
	{
		throw new InternalRendererException("not implemented"); // TODO
	}

	@Override public Optional<? extends Rendering> renderFact(FactNode factNode) throws RendererException
	{
		throw new InternalRendererException("not implemented"); // TODO
	}

	@Override public Optional<? extends Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode)
			throws RendererException
	{
		throw new InternalRendererException("not implemented"); // TODO
	}

	@Override public void setDataSource(SpreadSheetDataSource dataSource)
	{
		this.referenceRenderer.setDataSource(dataSource);
	}

	@Override public OWLAPIReferenceRenderer getReferenceRenderer()
	{
		return this.referenceRenderer;
	}

	/**
	 * Create same individual axioms for the declared individual.
	 */
	private Set<OWLAxiom> processSameAsClause(OWLIndividualDeclarationNode individualDeclarationNode,
			Optional<OWLNamedIndividualRendering> declaredIndividualRendering) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (declaredIndividualRendering.isPresent()) {
			OWLNamedIndividual individual1 = declaredIndividualRendering.get().getOWLNamedIndividual();

			for (OWLNamedIndividualNode sameAsIndividualNode : individualDeclarationNode.getOWLSameAsNode()
					.getIndividualNodes()) {
				Optional<OWLNamedIndividualRendering> sameAsIndividualRendering = this.entityRenderer
						.renderOWLNamedIndividual(sameAsIndividualNode);
				if (sameAsIndividualRendering.isPresent()) {
					OWLNamedIndividual individual2 = sameAsIndividualRendering.get().getOWLNamedIndividual();
					OWLSameIndividualAxiom axiom = this.owlDataFactory.getOWLSameIndividualAxiom(individual1, individual2);
					axioms.add(axiom);
				}
			}
		}
		return axioms;
	}

	/**
	 * Create different individuals axioms for the declared individual.
	 */
	private Set<OWLAxiom> processDifferentFromClause(OWLIndividualDeclarationNode individualDeclarationNode,
			Optional<OWLNamedIndividualRendering> declaredIndividualRendering) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (declaredIndividualRendering.isPresent()) {
			OWLNamedIndividual individual1 = declaredIndividualRendering.get().getOWLNamedIndividual();
			for (OWLNamedIndividualNode differentFromIndividualNode : individualDeclarationNode.getOWLDifferentFromNode()
					.getNamedIndividualNodes()) {
				Optional<OWLNamedIndividualRendering> differentFromIndividualsRendering = this.entityRenderer
						.renderOWLNamedIndividual(differentFromIndividualNode);
				if (differentFromIndividualsRendering.isPresent()) {
					OWLNamedIndividual individual2 = differentFromIndividualsRendering.get().getOWLNamedIndividual();
					OWLDifferentIndividualsAxiom axiom = this.owlDataFactory
							.getOWLDifferentIndividualsAxiom(individual1, individual2);

					axioms.add(axiom);
				}
			}
		}
		return axioms;
	}

	/**
	 * Create annotation assertion axioms for the declared individual.
	 */
	private Set<OWLAxiom> processAnnotationClause(Optional<OWLNamedIndividualRendering> declaredIndividualRendering,
			List<AnnotationFactNode> annotationFactNodes) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (declaredIndividualRendering.isPresent()) {

			for (AnnotationFactNode annotationFact : annotationFactNodes) {
				Optional<OWLPropertyRendering> propertyRendering = this.entityRenderer
						.renderOWLProperty(annotationFact.getOWLPropertyNode());

				if (!propertyRendering.isPresent()) {
					// logLine("Skipping OWL annotation clause [" + annotationFact + "] because of missing property name");
					continue;
				}

				OWLAnnotationValueNode annotationValueNode = annotationFact.getOWLAnnotationValueNode();
				Optional<OWLAnnotationValueRendering> annotationValueRendering = renderOWLAnnotationValue(annotationValueNode);

				if (!annotationValueRendering.isPresent()) {
					// logLine("Skipping OWL annotation clause [" + annotationFact + "] because of missing annotation value");
					continue;
				}

				OWLNamedIndividual individual = declaredIndividualRendering.get().getOWLNamedIndividual();
				// TODO Check that it is an annotation property
				OWLAnnotationProperty property = (OWLAnnotationProperty)propertyRendering.get().getOWLProperty();
				OWLAnnotationValue annotationValue = annotationValueRendering.get().getOWLAnnotationValue();

				if (annotationValueNode.isReference()) { // We have an object property so tell the reference
					ReferenceNode referenceNode = annotationValueNode.getReferenceNode();
					if (!referenceNode.hasExplicitlySpecifiedReferenceType() && this.owlObjectHandler
							.isOWLObjectProperty(property))
						referenceNode.updateReferenceType(OWL_NAMED_INDIVIDUAL);
				}

				OWLAnnotationAssertionAxiom axiom = this.owlDataFactory
						.getOWLAnnotationAssertionAxiom(property, individual.getIRI(), annotationValue);
				axioms.add(axiom);
			}
		}
		return axioms;
	}

	/**
	 * Create property assertion axioms for the declared individual.
	 */
	private Set<OWLAxiom> processFactsClause(Optional<OWLNamedIndividualRendering> subjectIndividualRendering,
			List<FactNode> factNodes) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (subjectIndividualRendering.isPresent()) {
			OWLIndividual subjectIndividual = subjectIndividualRendering.get().getOWLNamedIndividual();
			for (FactNode factNode : factNodes) {
				Optional<OWLPropertyRendering> propertyRendering = this.entityRenderer
						.renderOWLProperty(factNode.getOWLPropertyNode());

				if (!propertyRendering.isPresent()) {
					// logLine("Skipping OWL fact declaration clause [" + factNode + "] because of missing property name");
					continue;
				}

				OWLPropertyAssertionObjectNode propertyAssertionObjectNode = factNode.getOWLPropertyAssertionObjectNode();
				Optional<OWLPropertyAssertionObjectRendering> propertyAssertionObjectRendering = renderOWLPropertyAssertionObject(
						propertyAssertionObjectNode);

				if (!propertyAssertionObjectRendering.isPresent()) {
					// logLine("Skipping OWL fact declaration clause [" + factNode + "] because of missing property value");
					continue;
				}

				OWLProperty property = propertyRendering.get().getOWLProperty();
				OWLPropertyAssertionObject propertyAssertionObject = propertyAssertionObjectRendering.get()
						.getOWLPropertyAssertionObject();

				if (propertyAssertionObjectNode.isReference()) { // We have an object property so tell the reference the type
					ReferenceNode reference = propertyAssertionObjectNode.getReferenceNode();

					if (!reference.hasExplicitlySpecifiedReferenceType() && this.owlObjectHandler.isOWLObjectProperty(property))
						reference.updateReferenceType(OWL_NAMED_INDIVIDUAL);
				}

				if (this.owlObjectHandler.isOWLObjectProperty(property)) {
					OWLObjectProperty objectProperty = (OWLObjectProperty)property;
					// TODO Check this cast
					OWLIndividual objectIndividual = (OWLIndividual)propertyAssertionObject;

					OWLObjectPropertyAssertionAxiom axiom = this.owlDataFactory
							.getOWLObjectPropertyAssertionAxiom(objectProperty, subjectIndividual, objectIndividual);
					axioms.add(axiom);
				} else if (this.owlObjectHandler.isOWLDataProperty(property)) {
					OWLDataProperty dataProperty = (OWLDataProperty)property;
					// TODO Check this cast
					OWLLiteral literal = (OWLLiteral)propertyAssertionObject;

					OWLDataPropertyAssertionAxiom axiom = this.owlDataFactory
							.getOWLDataPropertyAssertionAxiom(dataProperty, subjectIndividual, literal);
					axioms.add(axiom);
				} else {
					// logLine(
					//  "Skipping OWL fact declaration clause [" + factNode + "] because property is an annotation property");
					continue;
				}
			}
		}
		return axioms;
	}
}

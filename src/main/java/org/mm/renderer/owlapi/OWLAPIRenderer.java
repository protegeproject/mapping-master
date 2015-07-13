// TODO Needs to be seriously refactored. Way too long.

package org.mm.renderer.owlapi;

import org.mm.core.ReferenceType;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.AnnotationFactNode;
import org.mm.parser.node.EmptyDataValueSettingNode;
import org.mm.parser.node.EmptyLocationSettingNode;
import org.mm.parser.node.EmptyRDFIDSettingNode;
import org.mm.parser.node.EmptyRDFSLabelSettingNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.FactNode;
import org.mm.parser.node.LiteralNode;
import org.mm.parser.node.MMDefaultReferenceTypeNode;
import org.mm.parser.node.MMDefaultPropertyValueTypeNode;
import org.mm.parser.node.MMDefaultValueEncodingNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.NameNode;
import org.mm.parser.node.OWLAllValuesFromClassNode;
import org.mm.parser.node.OWLAllValuesFromDataTypeNode;
import org.mm.parser.node.OWLAllValuesFromNode;
import org.mm.parser.node.OWLAnnotationValueNode;
import org.mm.parser.node.OWLCardinalityNode;
import org.mm.parser.node.OWLClassDeclarationNode;
import org.mm.parser.node.OWLClassEquivalentToNode;
import org.mm.parser.node.OWLClassExpressionNode;
import org.mm.parser.node.OWLEnumeratedClassNode;
import org.mm.parser.node.OWLHasValueNode;
import org.mm.parser.node.OWLIndividualDeclarationNode;
import org.mm.parser.node.OWLIndividualNode;
import org.mm.parser.node.OWLIntersectionClassNode;
import org.mm.parser.node.OWLMaxCardinalityNode;
import org.mm.parser.node.OWLMinCardinalityNode;
import org.mm.parser.node.OWLNamedClassNode;
import org.mm.parser.node.OWLPropertyAssertionObjectNode;
import org.mm.parser.node.OWLPropertyNode;
import org.mm.parser.node.OWLRestrictionNode;
import org.mm.parser.node.OWLSomeValuesFromClassNode;
import org.mm.parser.node.OWLSomeValuesFromDataTypeNode;
import org.mm.parser.node.OWLSomeValuesFromNode;
import org.mm.parser.node.OWLSubclassOfNode;
import org.mm.parser.node.OWLUnionClassNode;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SameAsNode;
import org.mm.parser.node.ShiftSettingNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.parser.node.StringOrReferenceNode;
import org.mm.parser.node.TypeNode;
import org.mm.parser.node.TypesNode;
import org.mm.parser.node.ValueEncodingNode;
import org.mm.parser.node.ValueExtractionFunctionNode;
import org.mm.parser.node.ValueSpecificationItemNode;
import org.mm.parser.node.ValueSpecificationNode;
import org.mm.renderer.Renderer;
import org.mm.renderer.RendererException;
import org.mm.renderer.Rendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
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
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class OWLAPIRenderer implements Renderer, MappingMasterParserConstants
{
	public static int NameEncodings[] = { MM_LOCATION, MM_DATA_VALUE, RDF_ID, RDFS_LABEL };
	public static int ReferenceValueTypes[] = { OWL_CLASS, OWL_THING, OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY, XSD_INT,
			XSD_STRING, XSD_FLOAT, XSD_DOUBLE, XSD_SHORT, XSD_BOOLEAN, XSD_TIME, XSD_DATETIME, XSD_DURATION };
	public static int PropertyTypes[] = { OWL_OBJECT_PROPERTY, OWL_DATA_PROPERTY };
	public static int PropertyValueTypes[] = ReferenceValueTypes;
	public static int DataPropertyValueTypes[] = { XSD_INT, XSD_STRING, XSD_FLOAT, XSD_DOUBLE, XSD_SHORT, XSD_BOOLEAN,
			XSD_TIME, XSD_DATETIME, XSD_DURATION };

	// Configuration options
	public int defaultValueEncoding = RDFS_LABEL;
	public int defaultReferenceType = OWL_CLASS;
	public int defaultOWLPropertyType = OWL_OBJECT_PROPERTY;
	public int defaultOWLPropertyAssertionObjectType = XSD_STRING;
	public int defaultOWLDataPropertyValueType = XSD_STRING;

	public int defaultEmptyLocationDirective = MM_PROCESS_IF_EMPTY_LOCATION;
	public int defaultEmptyRDFIDDirective = MM_PROCESS_IF_EMPTY_ID;
	public int defaultEmptyRDFSLabelDirective = MM_PROCESS_IF_EMPTY_LABEL;
	public int defaultIfExistsDirective = MM_RESOLVE_IF_EXISTS;
	public int defaultIfNotExistsDirective = MM_CREATE_IF_NOT_EXISTS;

	private final OWLOntology ontology;
	private final OWLDataFactory owlDataFactory;
	private final OWLAPIObjectHandler owlObjectHandler;
	private SpreadSheetDataSource dataSource;

	public OWLAPIRenderer(OWLOntology ontology, SpreadSheetDataSource dataSource)
	{
		this.ontology = ontology;
		this.owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		this.owlObjectHandler = new OWLAPIObjectHandler(ontology);
		this.dataSource = dataSource;
	}

	public void reset()
	{
		owlObjectHandler.reset();
	}

	public void setDataSource(SpreadSheetDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override public Optional<OWLAPIRendering> renderMMExpression(MMExpressionNode mmExpressionNode)
			throws RendererException
	{
		if (mmExpressionNode.hasOWLClassDeclaration())
			return renderOWLClassDeclaration(mmExpressionNode.getOWLClassDeclarationNode());
		else if (mmExpressionNode.hasOWLIndividualDeclaration())
			return renderOWLIndividualDeclaration(mmExpressionNode.getOWLIndividualDeclarationNode());
		else
			throw new RendererException("unknown expression: " + mmExpressionNode);
	}

	@Override public Optional<OWLAPIRendering> renderOWLClassDeclaration(OWLClassDeclarationNode classDeclarationNode)
			throws RendererException
	{
		OWLAPIRendering rendering = new OWLAPIRendering();

		rendering.logLine("=====================OWLClassDeclaration================================");
		rendering.logLine("MappingMaster DSL expression: " + classDeclarationNode);
		if (dataSource.hasCurrentLocation())
			rendering.logLine("********************Current location: " + dataSource.getCurrentLocation() + "*************");

		Optional<OWLClassRendering> declaredClassRendering = renderOWLClass(classDeclarationNode.getOWLNamedClassNode());

		if (!declaredClassRendering.isPresent()) {
			rendering.logLine("processReference: skipping OWL class declaration because of missing class");
		} else {
			if (classDeclarationNode.hasSubclassOf()) {
				for (OWLSubclassOfNode subclassOfNode : classDeclarationNode.getSubclassOfNodes()) {
					for (OWLClassExpressionNode classExpressionNode : subclassOfNode.getClassExpressionNodes()) {
						Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(
								classExpressionNode);
						if (!classExpressionRendering.isPresent())
							rendering.logLine(
									"processReference: skipping subclass declaration [" + subclassOfNode + "] because of missing class");
						else {
							OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
							OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
							OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(classExpression, declaredClass);
							rendering.addOWLAxiom(axiom);
						}
					}
				}
			}

			if (classDeclarationNode.hasEquivalentTo()) {
				for (OWLClassEquivalentToNode equivalentToNode : classDeclarationNode.getEquivalentToNodes()) {
					for (OWLClassExpressionNode classExpressionNode : equivalentToNode.getClassExpressionNodes()) {
						Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(
								classExpressionNode);
						if (!classExpressionRendering.isPresent())
							rendering.logLine("processReference: skipping eqivalent declaration [" + equivalentToNode
									+ "] because of missing class");
						else {
							OWLClass declaredClass = declaredClassRendering.get().getOWLClass();
							OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
							OWLEquivalentClassesAxiom axiom = owlDataFactory
									.getOWLEquivalentClassesAxiom(classExpression, declaredClass);
							rendering.addOWLAxiom(axiom);
						}
					}
				}
			}

			if (classDeclarationNode.hasAnnotations()) {
				for (AnnotationFactNode annotationFactNode : classDeclarationNode.getAnnotationFactNodes()) {
					Optional<OWLAnnotationPropertyRendering> propertyRendering = renderOWLAnnotationProperty(
							annotationFactNode.getOWLPropertyNode());
					OWLAnnotationValueNode annotationValueNode = annotationFactNode.getOWLAnnotationValueNode();
					Optional<OWLAnnotationValueRendering> annotationValueRendering = renderOWLAnnotationValue(
							annotationValueNode);

					if (!propertyRendering.isPresent()) {
						rendering.logLine("processReference: skipping OWL annotation clause [" + annotationFactNode
								+ "] because of missing property name");
						continue;
					}

					if (!annotationValueRendering.isPresent()) {
						rendering.logLine("processReference: skipping OWL annotation clause [" + annotationFactNode
								+ "] because of missing property value");
						continue;
					}

					OWLAnnotationProperty property = propertyRendering.get().getOWLAnnotationProperty();
					OWLAnnotationValue annotationValue = annotationValueRendering.get().getOWLAnnotationValue();
					OWLClass cls = declaredClassRendering.get().getOWLClass();
					OWLAnnotationAssertionAxiom axiom = this.owlDataFactory
							.getOWLAnnotationAssertionAxiom(property, cls.getIRI(), annotationValue);
					rendering.addOWLAxiom(axiom);
				}
			}
		}
		return Optional.of(rendering);
	}

	@Override public Optional<OWLAPIRendering> renderOWLIndividualDeclaration(
			OWLIndividualDeclarationNode individualDeclarationNode) throws RendererException
	{
		OWLAPIRendering individualDeclarationRendering = new OWLAPIRendering();

		individualDeclarationRendering
				.logLine("=====================OWLIndividualDeclaration================================");
		individualDeclarationRendering.logLine("MappingMaster DSL expression: " + individualDeclarationNode);
		if (dataSource.hasCurrentLocation())
			individualDeclarationRendering
					.logLine("********************Current location: " + dataSource.getCurrentLocation() + "*************");

		Optional<OWLNamedIndividualRendering> declaredIndividualRendering = renderOWLNamedIndividual(
				individualDeclarationNode.getOWLIndividualNode());
		if (!declaredIndividualRendering.isPresent()) {
			individualDeclarationRendering.logLine("Skipping OWL individual declaration because of missing individual name");
		} else {
			if (individualDeclarationNode.hasFacts()) { // We have a Facts: clause
				List<FactNode> factNodes = individualDeclarationNode.getFactNodes();
				Set<OWLAxiom> axioms = processFactsClause(individualDeclarationRendering, declaredIndividualRendering,
						factNodes);
				individualDeclarationRendering.addOWLAxioms(axioms);
			}

			if (individualDeclarationNode.hasAnnotations()) { // We have an Annotations: clause
				List<AnnotationFactNode> annotationFactNodes = individualDeclarationNode.getAnnotationNodes();
				Set<OWLAxiom> axioms = processAnnotationClause(individualDeclarationRendering, declaredIndividualRendering,
						annotationFactNodes);
				individualDeclarationRendering.addOWLAxioms(axioms);
			}

			if (individualDeclarationNode.hasSameAs()) { // We have a SameAs: clause
				Set<OWLAxiom> axioms = processSameAsClause(individualDeclarationNode, declaredIndividualRendering);
				individualDeclarationRendering.addOWLAxioms(axioms);
			}

			if (individualDeclarationNode.hasDifferentFrom()) { // We have a DifferentFrom: clause
				Set<OWLAxiom> axioms = processDifferentFromClause(individualDeclarationNode, declaredIndividualRendering);
				individualDeclarationRendering.addOWLAxioms(axioms);
			}

			if (individualDeclarationNode.hasTypes()) { // We have a Types: clause
				Set<OWLAxiom> axioms = processTypesClause(individualDeclarationNode, individualDeclarationRendering,
						declaredIndividualRendering.get());
				individualDeclarationRendering.addOWLAxioms(axioms);
			}
			// TODO individual declaration axioms
		}
		return Optional.of(individualDeclarationRendering);
	}

	@Override public Optional<OWLPropertyAssertionObjectRendering> renderOWLPropertyAssertionObject(
			OWLPropertyAssertionObjectNode propertyAssertionObject)
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLMaxCardinality(OWLMaxCardinalityNode maxCardinalityNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLMinCardinality(OWLMinCardinalityNode minCardinalityNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLCardinality(OWLCardinalityNode cardinalityNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<OWLClassExpressionRendering> renderOWLUnionClass(OWLUnionClassNode unionClassNode)
			throws RendererException
	{
		Set<OWLClassExpression> classExpressions = new HashSet<>();

		for (OWLIntersectionClassNode intersectionClassNode : unionClassNode.getOWLIntersectionClasseNodes()) {
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

	@Override public Optional<OWLClassExpressionRendering> renderOWLClassExpression(
			OWLClassExpressionNode classExpressionNode) throws RendererException
	{
		Optional<? extends OWLClassExpressionRendering> classExpressionRendering;

		if (classExpressionNode.hasOWLEnumeratedClass())
			classExpressionRendering = renderOWLEnumeratedClass(classExpressionNode.getOWLEnumeratedClassNode());
		else if (classExpressionNode.hasOWLUnionClass())
			classExpressionRendering = renderOWLUnionClass(classExpressionNode.getOWLUnionClassNode());
		else if (classExpressionNode.hasOWLRestriction())
			classExpressionRendering = renderOWLRestriction(classExpressionNode.getOWLRestrictionNode());
		else if (classExpressionNode.hasOWLNamedClass())
			classExpressionRendering = renderOWLClass(classExpressionNode.getOWLNamedClassNode());
		else
			throw new RendererException("unknown OWLClassOrRestriction node " + classExpressionNode);

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

	@Override public Optional<OWLClassExpressionRendering> renderOWLEnumeratedClass(
			OWLEnumeratedClassNode enumeratedClassNode) throws RendererException
	{
		OWLClassExpression classExpression = null; // TODO

		return Optional.of(new OWLClassExpressionRendering(classExpression));
	}

	@Override public Optional<OWLNamedIndividualRendering> renderOWLNamedIndividual(OWLIndividualNode namedIndividualNode)
			throws RendererException
	{
		OWLNamedIndividual individual = null; // TODO

		return Optional.of(new OWLNamedIndividualRendering(individual));
	}

	@Override public Optional<OWLPropertyRendering> renderOWLProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		OWLProperty property = null; // TODO

		return Optional.of(new OWLPropertyRendering(property));
	}

	@Override public Optional<OWLObjectPropertyRendering> renderOWLObjectProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		OWLObjectProperty property = null; // TODO

		return Optional.of(new OWLObjectPropertyRendering(property));
	}

	@Override public Optional<OWLDataPropertyRendering> renderOWLDataProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		OWLDataProperty property = null; // TODO

		return Optional.of(new OWLDataPropertyRendering(property));
	}

	@Override public Optional<OWLAnnotationPropertyRendering> renderOWLAnnotationProperty(OWLPropertyNode propertyNode)
			throws RendererException
	{
		OWLAnnotationProperty property = null; // TODO

		return Optional.of(new OWLAnnotationPropertyRendering(property));
	}

	@Override public Optional<OWLClassRendering> renderOWLClass(OWLNamedClassNode classNode) throws RendererException
	{
		OWLClass cls = null; // TODO
		return Optional.of(new OWLClassRendering(cls));
	}

	@Override public Optional<OWLAnnotationValueRendering> renderOWLAnnotationValue(
			OWLAnnotationValueNode annotationValueNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	// TODO Separate into data and object restrictions
	@Override public Optional<OWLRestrictionRendering> renderOWLRestriction(OWLRestrictionNode restrictionNode)
			throws RendererException
	{
		Optional<OWLPropertyRendering> propertyRendering = renderOWLProperty(restrictionNode.getOWLPropertyNode());

		if (propertyRendering.isPresent()) {
			IRI propertyIRI = propertyRendering.get().getOWLProperty().getIRI();
			if (this.owlObjectHandler.isOWLDataProperty(propertyIRI)) {
				OWLDataProperty property = this.owlObjectHandler.getOWLDataProperty(propertyIRI);
				if (restrictionNode.hasOWLMinCardinality()) {
					int cardinality = restrictionNode.getOWLMinCardinalityNode().getCardinality();
					OWLDataMinCardinality restriction = this.owlDataFactory.getOWLDataMinCardinality(cardinality, property);
					return Optional.of(new OWLRestrictionRendering(restriction));
				} else if (restrictionNode.hasOWLMaxCardinality()) {
					int cardinality = restrictionNode.getOWLMaxCardinalityNode().getCardinality();
					OWLDataMaxCardinality restriction = this.owlDataFactory.getOWLDataMaxCardinality(cardinality, property);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else if (restrictionNode.hasOWLCardinality()) {
					int cardinality = restrictionNode.getOWLCardinalityNode().getCardinality();
					OWLDataExactCardinality restriction = this.owlDataFactory.getOWLDataExactCardinality(cardinality, property);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else if (restrictionNode.hasOWLHasValue()) {
					OWLPropertyAssertionObjectNode propertyValueNode = restrictionNode.getOWLHasValueNode()
							.getOWLPropertyAssertionObjectNode();
					if (!propertyValueNode.isLiteral())
						throw new RendererException("expecting data value for data has value restriction " + restrictionNode);
					OWLLiteral literal = getOWLLiteral(propertyValueNode);
					OWLDataHasValue restriction = this.owlDataFactory.getOWLDataHasValue(property, literal);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else if (restrictionNode.hasOWLAllValuesFrom()) {
					OWLAllValuesFromNode allValuesFromNode = restrictionNode.getOWLAllValuesFromNode();
					if (!allValuesFromNode.hasOWLAllValuesFromDataType())
						throw new RendererException("expecting data value for all values data restriction " + restrictionNode);
					OWLDatatype datatype = this.owlObjectHandler
							.getOWLDatatype(allValuesFromNode.getOWLAllValuesFromDataTypeNode().getDataTypeName());
					OWLDataAllValuesFrom restriction = this.owlDataFactory.getOWLDataAllValuesFrom(property, datatype);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else if (restrictionNode.hasOWLSomeValuesFrom()) {
					OWLSomeValuesFromNode someValuesFromNode = restrictionNode.getOWLSomeValuesFromNode();
					if (!someValuesFromNode.hasOWLSomeValuesFromDataType())
						throw new RendererException("expecting data value for some values data restriction " + restrictionNode);
					OWLDatatype datatype = this.owlObjectHandler
							.getOWLDatatype(someValuesFromNode.getOWLSomeValuesFromDataTypeNode().getDataTypeName());
					OWLDataSomeValuesFrom restriction = this.owlDataFactory.getOWLDataSomeValuesFrom(property, datatype);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else
					return Optional.empty();
			} else { // Object property
				OWLObjectProperty property = this.owlObjectHandler.getOWLObjectProperty(propertyIRI);
				if (restrictionNode.hasOWLMinCardinality()) {
					int cardinality = restrictionNode.getOWLMinCardinalityNode().getCardinality();
					OWLObjectMinCardinality restriction = this.owlDataFactory.getOWLObjectMinCardinality(cardinality, property);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else if (restrictionNode.hasOWLMaxCardinality()) {
					int cardinality = restrictionNode.getOWLMaxCardinalityNode().getCardinality();
					OWLObjectMaxCardinality restriction = this.owlDataFactory.getOWLObjectMaxCardinality(cardinality, property);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else if (restrictionNode.hasOWLCardinality()) {
					int cardinality = restrictionNode.getOWLCardinalityNode().getCardinality();
					OWLObjectExactCardinality restriction = this.owlDataFactory
							.getOWLObjectExactCardinality(cardinality, property);

					return Optional.of(new OWLRestrictionRendering(restriction));
				} else if (restrictionNode.hasOWLHasValue()) {
					OWLPropertyAssertionObjectNode propertyAssertionObjectNode = restrictionNode.getOWLHasValueNode()
							.getOWLPropertyAssertionObjectNode();
					if (propertyAssertionObjectNode.isLiteral())
						throw new RendererException("expecting class for object has value restriction " + restrictionNode);
					Optional<OWLPropertyAssertionObjectRendering> propertyAssertionObjectRendering = renderOWLPropertyAssertionObject(
							propertyAssertionObjectNode);
					if (propertyAssertionObjectRendering.isPresent()) {
						OWLPropertyAssertionObject propertyAssertionObject = propertyAssertionObjectRendering.get()
								.getOWLPropertyAssertionObject();
						OWLObjectHasValue restriction = this.owlDataFactory.getOWLObjectHasValue(property, propertyAssertionObject);

						return Optional.of(new OWLRestrictionRendering(restriction));
					} else
						return Optional.empty();
				} else if (restrictionNode.hasOWLAllValuesFrom()) {
					OWLAllValuesFromNode allValuesFromNode = restrictionNode.getOWLAllValuesFromNode();
					if (allValuesFromNode.hasOWLAllValuesFromDataType())
						throw new RendererException("expecting class for all values object restriction " + restrictionNode);

					if (allValuesFromNode.getOWLAllValuesFromClassNode().hasOWLClassExpression()) {
						Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(
								allValuesFromNode.getOWLAllValuesFromClassNode().getOWLClassExpressionNode());
						if (classExpressionRendering.isPresent()) {
							OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
							OWLObjectAllValuesFrom restriction = this.owlDataFactory
									.getOWLObjectAllValuesFrom(property, classExpression);
							return Optional.of(new OWLRestrictionRendering(restriction));
						} else
							return Optional.empty();
					} else {
						Optional<OWLClassRendering> classRendering = renderOWLClass(
								allValuesFromNode.getOWLAllValuesFromClassNode().getOWLNamedClassNode());

						if (classRendering.isPresent()) {
							OWLClassExpression cls = classRendering.get().getOWLClass();
							OWLObjectAllValuesFrom restriction = this.owlDataFactory.getOWLObjectAllValuesFrom(property, cls);
							return Optional.of(new OWLRestrictionRendering(restriction));
						} else
							return Optional.empty();
					}
				} else if (restrictionNode.hasOWLSomeValuesFrom()) {
					OWLSomeValuesFromNode someValuesFromNode = restrictionNode.getOWLSomeValuesFromNode();
					if (someValuesFromNode.hasOWLSomeValuesFromDataType())
						throw new RendererException("expecting class for object some values  restriction " + restrictionNode);

					if (someValuesFromNode.getOWLSomeValuesFromClassNode().hasOWLClassExpression()) {
						Optional<OWLClassExpressionRendering> classExpressionRendering = renderOWLClassExpression(
								someValuesFromNode.getOWLSomeValuesFromClassNode().getOWLClassExpressionNode());
						if (classExpressionRendering.isPresent()) {
							OWLClassExpression classExpression = classExpressionRendering.get().getOWLClassExpression();
							OWLObjectSomeValuesFrom restriction = this.owlDataFactory
									.getOWLObjectSomeValuesFrom(property, classExpression);

							return Optional.of(new OWLRestrictionRendering(restriction));
						} else
							return Optional.empty();
					} else {
						Optional<OWLClassRendering> classRendering = renderOWLClass(
								someValuesFromNode.getOWLSomeValuesFromClassNode().getOWLNamedClassNode());
						if (classRendering.isPresent()) {
							OWLClassExpression cls = classRendering.get().getOWLClass();
							OWLObjectSomeValuesFrom restriction = this.owlDataFactory.getOWLObjectSomeValuesFrom(property, cls);
							return Optional.of(new OWLRestrictionRendering(restriction));
						} else
							return Optional.empty();
					}
				} else
					return Optional.empty();
			}
		} else
			return Optional.empty();
	}

	// TODO Too long. Clean up.
	@Override public Optional<ReferenceRendering> renderReference(ReferenceNode referenceNode) throws RendererException
	{
		SpreadsheetLocation location = getLocation(referenceNode.getSourceSpecificationNode());
		String defaultNamespace = getReferenceNamespace(referenceNode);
		String language = getReferenceLanguage(referenceNode);
		ReferenceRendering rendering = new ReferenceRendering();

		rendering.logLine("<<<<<<<<<<<<<<<<<<<< Rendering reference [" + referenceNode + "] <<<<<<<<<<<<<<<<<<<<");

		String locationValue = processLocationValue(location, referenceNode, rendering);

		if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_SKIP_IF_EMPTY_LOCATION)
			return Optional.empty();

		ReferenceType referenceType = referenceNode.getReferenceTypeNode().getReferenceType();

		if (referenceType.isUntyped())
			throw new RendererException("untyped entity for reference " + referenceNode);

		if (referenceType.isOWLLiteral()) { // OWL literal
			String processedOWLDataValue = processOWLDataValue(location, locationValue, referenceType, referenceNode, rendering);

			if (processedOWLDataValue.equals("")
					&& referenceNode.getActualEmptyDataValueDirective() == MM_SKIP_IF_EMPTY_DATA_VALUE)
				return Optional.empty();

			rendering.addText(processedOWLDataValue);
		} else { // OWL entity
			String rdfID = processRDFIDValue(locationValue, referenceNode, rendering);
			String rdfsLabelText = processRDFSLabelText(locationValue, referenceNode, rendering);
			OWLEntity entity = this.owlObjectHandler
					.createOrResolveOWLEntity(location, locationValue, referenceType, rdfID, rdfsLabelText, defaultNamespace,
							language, referenceNode.getReferenceDirectives());
			addDefiningTypes(referenceType, entity, referenceNode);
			rendering.addText(rdfID);
		}

		if (!rendering.nothingRendered())
			rendering.logLine(">>>>>>>>>>>>>>>>>>>> Reference [" + referenceNode.toString() + "] rendered as " + referenceNode
					.getReferenceTypeNode() + " " + rendering.getTextRendering() + " >>>>>>>>>>>>>>>>>>>>");
		else
			rendering
					.logLine(">>>>>>>>>>>>>>>>>>>> Reference [" + referenceNode + "] - nothing rendered >>>>>>>>>>>>>>>>>>>>");

		return Optional.of(rendering);
	}

	private Set<OWLAxiom> processTypesClause(OWLIndividualDeclarationNode individualDeclarationNode,
			Rendering individualDeclarationRendering, Rendering declaredIndividualRendering) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		for (TypeNode typeNode : individualDeclarationNode.getTypeNodes().getTypeNodes()) {
			Optional<OWLAPIRendering> typeRendering = renderType(typeNode);

			if (!typeRendering.isPresent()) {
				individualDeclarationRendering.logLine(
						"processReference: skipping OWL type declaration clause [" + typeNode + "] for individual "
								+ declaredIndividualRendering + " because of missing type");
				continue;
			}
			OWLNamedIndividual individual = this.owlObjectHandler.getOWLNamedIndividual(individualShortName);
			OWLClass cls = this.owlObjectHandler.getOWLClass(classShortName);
			OWLClassAssertionAxiom axiom = this.owlDataFactory.getOWLClassAssertionAxiom(cls, individual);

			axioms.add(axiom);
		}

		return axioms;
	}

	private Set<OWLAxiom> processSameAsClause(OWLIndividualDeclarationNode individualDeclarationNode,
			Optional<OWLNamedIndividualRendering> declaredIndividualRendering) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (declaredIndividualRendering.isPresent()) {
			OWLNamedIndividual individual1 = declaredIndividualRendering.get().getOWLNamedIndividual();

			for (OWLIndividualNode sameAsIndividualNode : individualDeclarationNode.getSameAsNode().getIndividualNodes()) {
				Optional<OWLNamedIndividualRendering> sameAsIndividualRendering = renderOWLNamedIndividual(
						sameAsIndividualNode);
				if (sameAsIndividualRendering.isPresent()) {
					OWLNamedIndividual individual2 = sameAsIndividualRendering.get().getOWLNamedIndividual();
					OWLSameIndividualAxiom axiom = owlDataFactory.getOWLSameIndividualAxiom(individual1, individual2);
					axioms.add(axiom);
				}
			}
		}
		return axioms;
	}

	private Set<OWLAxiom> processDifferentFromClause(OWLIndividualDeclarationNode individualDeclarationNode,
			Optional<OWLNamedIndividualRendering> declaredIndividualRendering) throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (declaredIndividualRendering.isPresent()) {
			OWLNamedIndividual individual1 = declaredIndividualRendering.get().getOWLNamedIndividual();
			for (OWLIndividualNode differentFromIndividualNode : individualDeclarationNode.getDifferentFromNode()
					.getIndividualNodes()) {
				Optional<OWLNamedIndividualRendering> differentFromIndividualsRendering = renderOWLNamedIndividual(
						differentFromIndividualNode);
				if (differentFromIndividualsRendering.isPresent()) {
					OWLNamedIndividual individual2 = differentFromIndividualsRendering.get().getOWLNamedIndividual();
					OWLDifferentIndividualsAxiom axiom = owlDataFactory.getOWLDifferentIndividualsAxiom(individual1, individual2);

					axioms.add(axiom);
				}
			}
		}
		return axioms;
	}

	private Set<OWLAxiom> processAnnotationClause(Rendering individualDeclarationRendering,
			Optional<OWLNamedIndividualRendering> declaredIndividualRendering, List<AnnotationFactNode> annotationFactNodes)
			throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (declaredIndividualRendering.isPresent()) {

			for (AnnotationFactNode annotationFact : annotationFactNodes) {
				Optional<OWLPropertyRendering> propertyRendering = renderOWLProperty(annotationFact.getOWLPropertyNode());

				if (!propertyRendering.isPresent()) {
					individualDeclarationRendering
							.logLine("Skipping OWL annotation clause [" + annotationFact + "] because of missing property name");
					continue;
				}

				OWLAnnotationValueNode annotationValueNode = annotationFact.getOWLAnnotationValueNode();
				Optional<OWLAnnotationValueRendering> annotationValueRendering = renderOWLAnnotationValue(annotationValueNode);

				if (!annotationValueRendering.isPresent()) {
					individualDeclarationRendering
							.logLine("Skipping OWL annotation clause [" + annotationFact + "] because of missing annotation value");
					continue;
				}

				OWLNamedIndividual individual = declaredIndividualRendering.get().getOWLNamedIndividual();
				OWLProperty property = propertyRendering.get().getOWLProperty();
				OWLAnnotationValue annotationValue = annotationValueRendering.get().getOWLAnnotationValue();

				if (annotationValueNode.isReference()) { // We have an object property so tell the reference
					ReferenceNode referenceNode = annotationValueNode.getReferenceNode();
					if (!referenceNode.hasExplicitlySpecifiedReferenceType() && this.owlObjectHandler.isOWLObjectProperty(property))
						referenceNode.updateReferenceType(OWL_THING);
				}

				OWLAnnotationAssertionAxiom axiom = this.owlDataFactory
						.getOWLAnnotationAssertionAxiom(property, individual, annotationValue);
				axioms.add(axiom);
			}
		}
		return axioms;
	}

	private Set<OWLAxiom> processFactsClause(Rendering individualDeclarationRendering,
			Optional<OWLNamedIndividualRendering> declaredIndividualRendering, List<FactNode> factNodes)
			throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (declaredIndividualRendering.isPresent()) {
			for (FactNode factNode : factNodes) {
				Optional<OWLPropertyRendering> propertyRendering = renderOWLProperty(factNode.getOWLPropertyNode());

				if (!propertyRendering.isPresent()) {
					individualDeclarationRendering
							.logLine("Skipping OWL fact declaration clause [" + factNode + "] because of missing property name");
					continue;
				}

				OWLPropertyAssertionObjectNode propertyAssertionObjectNode = factNode.getOWLPropertyAssertionObjectNode();
				Optional<OWLPropertyAssertionObjectRendering> propertyAssertionObjectRendering = renderOWLPropertyAssertionObject(
						propertyAssertionObjectNode);

				if (!propertyAssertionObjectRendering.isPresent()) {
					individualDeclarationRendering
							.logLine("Skipping OWL fact declaration clause [" + factNode + "] because of missing property value");
					continue;
				}

				OWLProperty property = propertyRendering.get().getOWLProperty();
				OWLPropertyAssertionObject propertyAssertionObject = propertyAssertionObjectRendering.get()
						.getOWLPropertyAssertionObject();

				if (propertyAssertionObjectNode.isReference()) { // We have an object property so tell the reference the type
					ReferenceNode reference = propertyAssertionObjectNode.getReferenceNode();

					if (!reference.hasExplicitlySpecifiedReferenceType() && this.owlObjectHandler.isOWLObjectProperty(property))
						reference.updateReferenceType(OWL_THING);
				}

				if (this.owlObjectHandler.isOWLObjectProperty(property)) {
					OWLObjectProperty objectProperty = (OWLObjectProperty)property;

					OWLObjectPropertyAssertionAxiom axiom = this.owlDataFactory
							.getOWLObjectPropertyAssertionAxiom(property, individual, propertyAssertionObject);
					axioms.add(axiom);
				} else if (this.owlObjectHandler.isOWLDataProperty(property)) {
					OWLDataProperty dataProperty = (OWLDataProperty)property;

					OWLDataPropertyAssertionAxiom axiom = this.owlDataFactory
							.getOWLDataPropertyAssertionAxiom(dataProperty, individual, propertyAssertionObject);
					axioms.add(axiom);
				} else {
					individualDeclarationRendering.logLine(
							"Skipping OWL fact declaration clause [" + factNode + "] because property is an annotation property");
					continue;
				}
			}
		}
		return axioms;
	}

	private Set<OWLAxiom> addDefiningTypes(ReferenceType referenceType, OWLEntity entity, ReferenceNode referenceNode)
			throws RendererException
	{
		Set<OWLAxiom> axioms = new HashSet<>();

		if (referenceNode.hasExplicitlySpecifiedTypes()) {
			for (TypeNode typeNode : referenceNode.getTypesNode().getTypeNodes()) {
				Rendering typeRendering = renderType(typeNode);
				if (!typeRendering.nothingRendered()) {
					String typeTextRendering = typeRendering.getTextRendering();
					if (referenceType.isOWLClass()) {
						if (!entity.isOWLClass())
							throw new RendererException(
									"expecting class for type in reference " + referenceNode + " for " + entity + ", got " + entity
											.getClass().getCanonicalName());
						String classExpressionID = typeTextRendering;
						OWLClassExpression classExpression = this.owlObjectHandler.getOWLClassExpression(classExpressionID);
						OWLSubClassOfAxiom axiom = this.owlDataFactory.getOWLSubClassOfAxiom(classExpression, entity.asOWLClass());
						axioms.add(axiom);
					} else if (referenceType.isOWLIndividual()) {
						if (!entity.isOWLNamedIndividual())
							throw new RendererException(
									"expecting individual for type in reference " + referenceNode + " for " + entity + ", got " + entity
											.getClass().getCanonicalName());
						String classExpressionID = typeTextRendering;
						OWLClassExpression classExpression = this.owlObjectHandler.getOWLClass(classExpressionID);
						OWLClassAssertionAxiom axiom = this.owlDataFactory
								.getOWLClassAssertionAxiom(classExpression, entity.asOWLNamedIndividual());
						axioms.add(axiom);
					} else if (referenceType.isOWLObjectProperty()) {
						if (!entity.isOWLObjectProperty())
							throw new RendererException(
									"expecting object property for type in reference " + referenceNode + " for " + entity);
						String propertyShortName = typeTextRendering;
						OWLObjectProperty property = this.owlObjectHandler.getOWLObjectProperty(propertyShortName);
						OWLSubObjectPropertyOfAxiom axiom = owlDataFactory
								.getOWLSubObjectPropertyOfAxiom(property, entity.asOWLObjectProperty());
						axioms.add(axiom);
					} else if (referenceType.isOWLDataProperty()) {
						if (!entity.isOWLDataProperty())
							throw new RendererException(
									"expecting data property for type in reference " + referenceNode + " for " + entity);
						String propertyShortName = typeTextRendering;
						OWLDataProperty property = this.owlObjectHandler.getOWLDataProperty(propertyShortName);
						OWLSubDataPropertyOfAxiom axiom = owlDataFactory
								.getOWLSubDataPropertyOfAxiom(property, entity.asOWLDataProperty());
						axioms.add(axiom);
					} else
						throw new RendererException("invalid entity type " + referenceType);
				}
			}
		}
		return axioms;
	}

	// TODO Tentative. Need a more principled way of finding and invoking functions. What about calls to Excel?

	private String processValueExtractionFunction(ValueExtractionFunctionNode valueExtractionFunctionNode, String value)
			throws RendererException
	{
		List<String> arguments = new ArrayList<>();
		String functionName = valueExtractionFunctionNode.getFunctionName();
		boolean hasExplicitArguments = valueExtractionFunctionNode.hasArguments();
		String processedValue;

		if (valueExtractionFunctionNode.hasArguments()) {
			for (StringOrReferenceNode argumentNode : valueExtractionFunctionNode.getArgumentNodes()) {
				Rendering argumentRendering = renderStringOrReference(argumentNode);
				arguments.add(argumentRendering.getTextRendering());
			}
		}

		switch (valueExtractionFunctionNode.getFunctionID()) {
		case MM_TO_UPPER_CASE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException("function " + functionName + " expecting one argument, got " + arguments.size());
				processedValue = arguments.get(0).toUpperCase();
			} else
				processedValue = value.toUpperCase();
			break;
		case MM_TO_LOWER_CASE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedValue = arguments.get(0).toLowerCase();
			} else
				processedValue = value.toLowerCase();
			break;
		case MM_TRIM:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedValue = arguments.get(0).trim();
			} else
				processedValue = value.trim();
			break;
		case MM_REVERSE:
			if (hasExplicitArguments) {
				if (arguments.size() != 1)
					throw new RendererException(
							"function " + functionName + " expecting only one argument, got " + arguments.size());
				processedValue = reverse(arguments.get(0));
			} else
				processedValue = reverse(value);
			break;
		case MM_CAPTURING:
			if (arguments.size() == 1) {
				processedValue = processCapturingExpression(value, arguments.get(0));
			} else if (arguments.size() == 2) {
				processedValue = processCapturingExpression(arguments.get(0), arguments.get(1));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_PREPEND:
			if (arguments.size() == 1) {
				processedValue = arguments.get(0) + value;
			} else if (arguments.size() == 2) {
				processedValue = arguments.get(0) + arguments.get(1);
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_APPEND:
			if (arguments.size() == 1) {
				processedValue = value + arguments.get(0);
			} else if (arguments.size() == 2) {
				processedValue = arguments.get(0) + arguments.get(1);
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE:
			if (arguments.size() == 2) {
				processedValue = value.replace(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedValue = arguments.get(0).replace(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE_ALL:
			if (arguments.size() == 2) {
				processedValue = value.replaceAll(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedValue = arguments.get(0).replaceAll(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		case MM_REPLACE_FIRST:
			if (arguments.size() == 2) {
				processedValue = value.replaceFirst(arguments.get(0), arguments.get(1));
			} else if (arguments.size() == 3) {
				processedValue = arguments.get(0).replaceFirst(arguments.get(1), arguments.get(2));
			} else
				throw new RendererException(
						"function " + functionName + " expecting two or three arguments, got " + arguments.size());
			break;
		default:
			throw new RendererException("unknown mapping function " + valueExtractionFunctionNode.getFunctionName() + ")");
		}
		return processedValue;
	}

	private OWLLiteral getOWLLiteral(OWLPropertyAssertionObjectNode propertyValueNode) throws RendererException
	{
		LiteralNode literalNode = propertyValueNode.getLiteralNode();

		if (literalNode.isBoolean())
			return this.owlDataFactory.getOWLLiteral(literalNode.getBooleanLiteralNode().getValue());
		else if (literalNode.isInteger())
			return this.owlDataFactory.getOWLLiteral(literalNode.getIntegerLiteralNode().getValue());
		else if (literalNode.isFloat())
			return this.owlDataFactory.getOWLLiteral(literalNode.getFloatLiteralNode().getValue());
		else if (literalNode.isString())
			return this.owlDataFactory.getOWLLiteral(literalNode.getStringLiteralNode().getValue());
		else
			throw new RendererException("unknown OWL literal property value " + propertyValueNode.toString());
	}

	private SpreadsheetLocation getLocation(SourceSpecificationNode sourceSpecificationNode) throws RendererException
	{
		if (sourceSpecificationNode.hasLiteral())
			return null;
		else
			return dataSource.resolveLocation(sourceSpecificationNode);
	}

	private String processLocationValue(SpreadsheetLocation location, ReferenceNode referenceNode, Rendering rendering)
			throws RendererException
	{
		String locationValue;
		SourceSpecificationNode sourceSpecification = referenceNode.getSourceSpecificationNode();

		if (sourceSpecification.hasLiteral()) {
			// Reference is a literal, e.g., @"Person", @"http://a.com#Person"
			locationValue = sourceSpecification.getLiteral();
			rendering.log("processReference: literal");
		} else { // Reference to data source location

			rendering.log("--processReference: specified location " + location);
			locationValue = dataSource.getLocationValue(location, referenceNode); // Deals with shifting

			if ((locationValue == null || locationValue.equals("")))
				locationValue = referenceNode.getActualDefaultLocationValue();

			if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_ERROR_IF_EMPTY_LOCATION)
				throw new RendererException("empty location " + location + " in reference " + referenceNode);

			if (locationValue.equals("") && referenceNode.getActualEmptyLocationDirective() == MM_WARNING_IF_EMPTY_LOCATION)
				rendering.logLine("processReference: WARNING: empty location " + location + " in reference " + referenceNode);

			rendering.log(", location value [" + locationValue + "], entity type " + referenceNode.getReferenceTypeNode());
		}

		if (!referenceNode.getReferenceTypeNode().getReferenceType().isOWLLiteral()) {
			rendering.log(", namespace " + getReferenceNamespace(referenceNode));
			String language = getReferenceLanguage(referenceNode);
			displayLanguage(language, rendering);
			rendering.log(", valueEncoding");
			for (ValueEncodingNode valueEncoding : referenceNode.getValueEncodingNodes())
				rendering.log(" " + valueEncoding);
		}

		if (!sourceSpecification.hasLiteral()) { // Determine if originally specified location has been shifted
			if (referenceNode.getActualShiftDirective() != MM_NO_SHIFT && referenceNode.hasShiftedLocation()) {
				location = referenceNode.getShiftedLocation();
				rendering.log(", shifted location " + referenceNode.getShiftedLocation());
			}
		}
		rendering.logLine("");

		return locationValue;
	}

	private String processRDFIDValue(String locationValue, ReferenceNode referenceNode, Rendering rendering)
			throws RendererException
	{
		String rdfIDValue;

		if (referenceNode.hasRDFIDValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedRDFIDValueEncoding())
				rdfIDValue = processValueEncoding(locationValue, referenceNode.getRDFIDValueEncodingNode(), referenceNode);
			else if (referenceNode.hasValueExtractionFunction())
				rdfIDValue = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(), locationValue);
			else
				rdfIDValue = locationValue;
		} else
			rdfIDValue = "";

		if (rdfIDValue.equals("") && !referenceNode.getActualDefaultRDFID().equals(""))
			rdfIDValue = referenceNode.getActualDefaultRDFID();

		if (rdfIDValue.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_ID)
			throw new RendererException("empty RDF ID in reference " + referenceNode);

		if (rdfIDValue.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_ID)
			rendering.logLine("processReference: WARNING: empty RDF ID in reference");

		return rdfIDValue;
	}

	private String processRDFSLabelText(String locationValue, ReferenceNode referenceNode, Rendering rendering)
			throws RendererException
	{
		String rdfsLabelText;

		if (referenceNode.hasRDFSLabelValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedRDFSLabelValueEncoding())
				rdfsLabelText = processValueEncoding(locationValue, referenceNode.getRDFSLabelValueEncodingNode(),
						referenceNode);
			else if (referenceNode.hasValueExtractionFunction())
				rdfsLabelText = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(), locationValue);
			else
				rdfsLabelText = locationValue;
		} else
			rdfsLabelText = "";

		if (rdfsLabelText.equals("") && !referenceNode.getActualDefaultRDFSLabel().equals(""))
			rdfsLabelText = referenceNode.getActualDefaultRDFSLabel();

		if (rdfsLabelText.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_ERROR_IF_EMPTY_LABEL)
			throw new RendererException("empty RDFS label in reference " + referenceNode);

		if (rdfsLabelText.equals("") && referenceNode.getActualEmptyRDFSLabelDirective() == MM_WARNING_IF_EMPTY_LABEL)
			rendering.logLine("processReference: WARNING: empty RDFS label in reference");

		return rdfsLabelText;
	}

	private String processOWLDataValue(SpreadsheetLocation location, String locationValue, ReferenceType referenceType,
			ReferenceNode referenceNode, Rendering rendering) throws RendererException
	{
		String processedLocationValue = locationValue.replace("\"", "\\\"");
		String dataValue;

		if (referenceNode.hasLiteralValueEncoding()) {
			if (referenceNode.hasExplicitlySpecifiedLiteralValueEncoding())
				dataValue = processValueEncoding(processedLocationValue, referenceNode.getLiteralValueEncodingNode(),
						referenceNode);
			else if (referenceNode.hasValueExtractionFunction())
				dataValue = processValueExtractionFunction(referenceNode.getValueExtractionFunctionNode(),
						processedLocationValue);
			else
				dataValue = processedLocationValue;
		} else
			dataValue = "";

		if (dataValue.equals("") && !referenceNode.getActualDefaultDataValue().equals(""))
			dataValue = referenceNode.getActualDefaultDataValue();

		if (dataValue.equals("") && referenceNode.getActualEmptyDataValueDirective() == MM_ERROR_IF_EMPTY_DATA_VALUE)
			throw new RendererException("empty data value in reference " + referenceNode + " at location " + location);

		if (dataValue.equals("") && referenceNode.getActualEmptyDataValueDirective() == MM_WARNING_IF_EMPTY_DATA_VALUE)
			rendering.logLine(
					"processReference: WARNING: empty data value in reference " + referenceNode + " at location " + location);

		if (referenceType.isQuotedOWLDataValue())
			dataValue = "\"" + dataValue + "\"";

		return dataValue;
	}

	private void displayLanguage(String language, Rendering rendering)
	{
		String display = "";

		if (language != null) {
			display += ", xml:lang";
			if (language.equals(""))
				display += "=mm:null";
			else if (!language.equals("*"))
				display += "=*";
			else if (language.equals("+"))
				display += "!=mm:null";
			else
				display += language;
		}
		rendering.log(display);
	}

	private String getReferenceNamespace(ReferenceNode referenceNode) throws RendererException
	{
		String namespace;

		// A reference will not have both a prefix and a namespace specified
		if (referenceNode.hasExplicitlySpecifiedPrefix()) {
			String prefix = referenceNode.getPrefixNode().getPrefix();
			namespace = getNamespaceForPrefix(prefix);
			if (namespace == null)
				throw new RendererException("unknown prefix " + prefix + " specified in reference " + referenceNode);
		} else if (referenceNode.hasExplicitlySpecifiedNamespace()) {
			namespace = referenceNode.getNamespaceNode().getNamespace();
		} else {
			if (!hasDefaultNamespace())
				throw new RendererException(
						"ontology has no default namespace and no namespace specified by reference " + referenceNode);

			namespace = getDefaultNamespace();
		}

		return namespace;
	}

	private String getReferenceLanguage(ReferenceNode referenceNode) throws RendererException
	{
		if (referenceNode.hasExplicitlySpecifiedLanguage())
			return referenceNode.getActualLanguage();
		else
			return getDefaultLanguage(); // Which might be null or empty
	}

	private String processValueEncoding(String value, ValueEncodingNode valueEncodingNode, ReferenceNode referenceNode)
			throws RendererException
	{
		String processedValue = "";

		if (valueEncodingNode != null) {
			if (valueEncodingNode.hasValueSpecification())
				processedValue = processValueSpecification(value, valueEncodingNode.getValueSpecification(), referenceNode);
			else
				processedValue = value;
		} else
			processedValue = value;

		return processedValue;
	}

	private String processValueSpecification(String value, ValueSpecificationNode valueSpecificationNode,
			ReferenceNode referenceNode) throws RendererException
	{
		String processedValue = "";

		for (ValueSpecificationItemNode valueSpecificationItemNode : valueSpecificationNode
				.getValueSpecificationItemNodes()) {
			if (valueSpecificationItemNode.hasStringLiteral())
				processedValue += valueSpecificationItemNode.getStringLiteral();
			else if (valueSpecificationItemNode.hasReference()) {
				ReferenceNode valueSpecificationItemReferenceNode = valueSpecificationItemNode.getReferenceNode();
				valueSpecificationItemReferenceNode.setDefaultShiftSetting(referenceNode.getActualShiftDirective());
				Optional<ReferenceRendering> referenceRendering = renderReference(valueSpecificationItemReferenceNode);
				if (valueSpecificationItemReferenceNode.getReferenceTypeNode().getReferenceType().isQuotedOWLDataValue()
						&& !referenceTextRendering.equals("") && referenceTextRendering.startsWith("\""))
					processedValue += referenceTextRendering.substring(1, referenceTextRendering.length() - 1); // Strip quotes
					// from quoted
					// renderings
				else
					processedValue += referenceTextRendering;
			} else if (valueSpecificationItemNode.hasValueExtractionFunction()) {
				ValueExtractionFunctionNode valueExtractionFunction = valueSpecificationItemNode
						.getValueExtractionFunctionNode();
				processedValue += processValueExtractionFunction(valueExtractionFunction, value);
			} else if (valueSpecificationItemNode.hasCapturingExpression() && value != null) {
				String capturingExpression = valueSpecificationItemNode.getCapturingExpression();
				processedValue += processCapturingExpression(value, capturingExpression);
			}
		}
		return processedValue;
	}

	private String processCapturingExpression(String locationValue, String capturingExpression) throws RendererException
	{
		String result = "";

		try {
			Pattern p = Pattern.compile(capturingExpression); // Pull the value out of the location
			Matcher m = p.matcher(locationValue);
			boolean matchFound = m.find();
			if (matchFound) {
				for (int groupIndex = 1; groupIndex <= m.groupCount(); groupIndex++)
					result += (m.group(groupIndex));
			}
		} catch (PatternSyntaxException e) {
			throw new RendererException("invalid capturing expression: " + capturingExpression + ": " + e.getMessage());
		}
		return result;
	}

	private String reverse(String source)
	{
		int i, len = source.length();
		StringBuffer dest = new StringBuffer(len);

		for (i = (len - 1); i >= 0; i--)
			dest.append(source.charAt(i));

		return dest.toString();
	}

	private String getDefaultNamespace()
	{
		return ""; // TODO
	}

	private String getDefaultLanguage()
	{
		return ""; // TODO
	}

	private String getNamespaceForPrefix(String prefix)
	{
		return null; // TODO
	}

	private boolean hasDefaultNamespace()
	{
		return false; // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLAllValuesFrom(OWLAllValuesFromNode allValuesFromNode)
			throws RendererException
	{
		return Optional.empty();
	}

	@Override public Optional<? extends Rendering> renderValueExtractionFunction(
			ValueExtractionFunctionNode valueExtractionFunctionNode) throws RendererException
	{
		return Optional.empty();
	}

	@Override public Optional<? extends Rendering> renderFact(FactNode factNode) throws RendererException
	{
		return Optional.empty();
	}

	@Override public Optional<? extends Rendering> renderMMDefaultPropertyValueType(
			MMDefaultPropertyValueTypeNode defaultPropertyValueTypeNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderSameAs(SameAsNode sameAs) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLAllValuesFromDataType(
			OWLAllValuesFromDataTypeNode allValuesFromDataTypeNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderAnnotationFact(AnnotationFactNode annotationFactNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLEquivalentTo(
			OWLClassEquivalentToNode owlClassEquivalentToNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLHasValue(OWLHasValueNode hasValueNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLSomeValuesFrom(OWLSomeValuesFromNode someValuesFromNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderValueSpecificationItem(
			ValueSpecificationItemNode valueSpecificationItemNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderMMDefaultValueEncoding(
			MMDefaultValueEncodingNode defaultValueEncodingNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderName(NameNode nameNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderExpression(ExpressionNode expressionNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLSubclassOf(OWLSubclassOfNode owlSubclassOfNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderTypes(TypesNode definingTypesNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLAllValuesFromClass(
			OWLAllValuesFromClassNode allValuesFromClassNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderEmptyLocationSetting(
			EmptyLocationSettingNode emptyLocationSettingNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderEmptyDataValueSetting(
			EmptyDataValueSettingNode emptyDataValueSettingNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderEmptyRDFIDSetting(EmptyRDFIDSettingNode emptyRDFIDSettingNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderEmptyRDFSLabelSetting(
			EmptyRDFSLabelSettingNode emptyRDFSLabelSettingNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderMMDefaultReferenceType(
		MMDefaultReferenceTypeNode defaultReferenceTypeNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderStringOrReference(StringOrReferenceNode stringOrReferenceNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLSomeValuesFromClass(
			OWLSomeValuesFromClassNode someValuesFromClassNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderOWLSomeValuesFromDataType(
			OWLSomeValuesFromDataTypeNode someValuesFromDataTypeNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderShiftSetting(ShiftSettingNode shiftSettingNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderValueEncoding(ValueEncodingNode valueEncodingNode)
			throws RendererException
	{
		return Optional.empty(); // TODO
	}

	@Override public Optional<? extends Rendering> renderLiteral(LiteralNode literalNode) throws RendererException
	{
		return Optional.empty(); // TODO
	}


}

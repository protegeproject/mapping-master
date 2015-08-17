package org.mm.renderer.owlapi;

import java.util.Set;

import org.mm.renderer.RendererException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
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
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
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
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

class OWLAPIObjectHandler
{
	private final OWLOntology ontology;
	
	private final OWLDataFactory owlDataFactory;

	private final PrefixManager prefixManager = new DefaultPrefixManager();

	public OWLAPIObjectHandler(OWLOntology ontology)
	{
		this.ontology = ontology;
		owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		
		// Assemble the prefix manager for the given ontology
		OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
		if (format.isPrefixOWLOntologyFormat()) {
			prefixManager.copyPrefixesFrom(format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap());
		}
	}

	public IRI getQualifiedName(String shortName)
	{
		return prefixManager.getIRI(shortName);
	}

	public OWLDeclarationAxiom getOWLDeclarationAxiom(OWLEntity entity)
	{
		return owlDataFactory.getOWLDeclarationAxiom(entity);
	}

	public OWLClass getOWLClass(String shortName)
	{
		return getOWLClass(getQualifiedName(shortName));
	}

	public OWLClass getOWLClass(String namespace, String localName)
	{
		return getOWLClass(IRI.create(namespace, localName));
	}

	public OWLClass getOWLClass(IRI iri)
	{
		return owlDataFactory.getOWLClass(iri);
	}

	public OWLNamedIndividual getOWLNamedIndividual(String shortName) throws RendererException
	{
		return getOWLNamedIndividual(getQualifiedName(shortName));
	}

	public OWLNamedIndividual getOWLNamedIndividual(String namespace, String localName)
	{
		return getOWLNamedIndividual(IRI.create(namespace, localName));
	}

	public OWLNamedIndividual getOWLNamedIndividual(IRI iri)
	{
		return owlDataFactory.getOWLNamedIndividual(iri);
	}

	public OWLObjectProperty getOWLObjectProperty(String shortName)
	{
		return getOWLObjectProperty(getQualifiedName(shortName));
	}

	public OWLObjectProperty getOWLObjectProperty(String namespace, String localName)
	{
		return getOWLObjectProperty(IRI.create(namespace, localName));
	}

	public OWLObjectProperty getOWLObjectProperty(IRI iri)
	{
		return owlDataFactory.getOWLObjectProperty(iri);
	}

	public OWLDataProperty getOWLDataProperty(String shortName)
	{
		return getOWLDataProperty(getQualifiedName(shortName));
	}

	public OWLDataProperty getOWLDataProperty(String namespace, String localName)
	{
		return getOWLDataProperty(IRI.create(namespace, localName));
	}

	public OWLDataProperty getOWLDataProperty(IRI iri)
	{
		return owlDataFactory.getOWLDataProperty(iri);
	}

	public OWLAnnotationProperty getOWLAnnotationProperty(String shortName)
	{
		return getOWLAnnotationProperty(getQualifiedName(shortName));
	}

	public OWLAnnotationProperty getOWLAnnotationProperty(String namespace, String localName)
	{
		return getOWLAnnotationProperty(IRI.create(namespace, localName));
	}

	public OWLAnnotationProperty getOWLAnnotationProperty(IRI iri)
	{
		return owlDataFactory.getOWLAnnotationProperty(iri);
	}

	public OWLAnnotationValue getOWLAnnotationValue(String value)
	{
		return owlDataFactory.getOWLLiteral(value);
	}

	public OWLAnnotationValue getOWLAnnotationValue(float value)
	{
		return owlDataFactory.getOWLLiteral(value);
	}

	public OWLAnnotationValue getOWLAnnotationValue(int value)
	{
		return owlDataFactory.getOWLLiteral(value+"", OWL2Datatype.XSD_INT);
	}

	public OWLAnnotationValue getOWLAnnotationValue(boolean value)
	{
		return owlDataFactory.getOWLLiteral(value);
	}

	public OWLDatatype getOWLDatatype(String shortName)
	{
		return owlDataFactory.getOWLDatatype(getQualifiedName(shortName));
	}

	public OWLLiteral getOWLLiteralString(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_STRING);
	}

	public OWLLiteral getOWLLiteralBoolean(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_BOOLEAN);
	}

	public OWLLiteral getOWLLiteralDouble(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_DOUBLE);
	}

	public OWLLiteral getOWLLiteralFloat(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_FLOAT);
	}

	public OWLLiteral getOWLLiteralLong(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_LONG);
	}

	public OWLLiteral getOWLLiteralInteger(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_INT);
	}

	public OWLLiteral getOWLLiteralShort(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_SHORT);
	}

	public OWLLiteral getOWLLiteralByte(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_BYTE);
	}

	public OWLLiteral getOWLLiteralDateTime(String value)
	{
		return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_DATE_TIME);
	}

	public OWLLiteral getOWLLiteralDate(String value)
	{
		return owlDataFactory.getOWLLiteral(value, owlDataFactory.getOWLDatatype(XSDVocabulary.DATE.getIRI()));
	}

	public OWLLiteral getOWLLiteralTime(String value)
	{
		return owlDataFactory.getOWLLiteral(value, owlDataFactory.getOWLDatatype(XSDVocabulary.TIME.getIRI()));
	}

	public OWLLiteral getOWLLiteralDuration(String value)
	{
		return owlDataFactory.getOWLLiteral(value, owlDataFactory.getOWLDatatype(XSDVocabulary.DURATION.getIRI()));
	}

	public OWLObjectComplementOf getOWLObjectComplementOf(OWLClassExpression ce)
	{
		return owlDataFactory.getOWLObjectComplementOf(ce);
	}

	public OWLObjectIntersectionOf getOWLObjectIntersectionOf(Set<OWLClassExpression> ces)
	{
		return owlDataFactory.getOWLObjectIntersectionOf(ces);
	}

	public OWLObjectUnionOf getOWLObjectUnionOf(Set<OWLClassExpression> ces)
	{
		return owlDataFactory.getOWLObjectUnionOf(ces);
	}

	public OWLObjectOneOf getOWLObjectOneOf(Set<OWLNamedIndividual> inds)
	{
		return owlDataFactory.getOWLObjectOneOf(inds);
	}

	public OWLSubClassOfAxiom getOWLSubClassOfAxiom(OWLClassExpression child, OWLClassExpression parent)
	{
		return owlDataFactory.getOWLSubClassOfAxiom(child, parent);
	}

	public OWLSubObjectPropertyOfAxiom getOWLSubObjectPropertyOfAxiom(OWLObjectPropertyExpression child, OWLObjectPropertyExpression parent)
	{
		return owlDataFactory.getOWLSubObjectPropertyOfAxiom(child, parent);
	}

	public OWLSubDataPropertyOfAxiom getOWLSubDataPropertyOfAxiom(OWLDataPropertyExpression child, OWLDataPropertyExpression parent)
	{
		return owlDataFactory.getOWLSubDataPropertyOfAxiom(child, parent);
	}

	public OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(Set<OWLClassExpression> ces)
	{
		return owlDataFactory.getOWLEquivalentClassesAxiom(ces);
	}

	public OWLEquivalentClassesAxiom getOWLEquivalentClassesAxiom(OWLClassExpression ce1, OWLClassExpression ce2)
	{
		return owlDataFactory.getOWLEquivalentClassesAxiom(ce1, ce2);
	}

	public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(OWLAnnotationProperty ap, OWLEntity entity, OWLLiteral value)
	{
		return getOWLAnnotationAssertionAxiom(ap, entity.getIRI(), value);
	}

	public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(OWLAnnotationProperty ap, OWLAnnotationSubject as, OWLAnnotationValue value)
	{
		return owlDataFactory.getOWLAnnotationAssertionAxiom(ap, as, value);
	}

	public OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLClassExpression ce, OWLNamedIndividual ind)
	{
		return owlDataFactory.getOWLClassAssertionAxiom(ce, ind);
	}

	public OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(OWLObjectProperty op, OWLIndividual source, OWLIndividual target)
	{
		return owlDataFactory.getOWLObjectPropertyAssertionAxiom(op, source, target);
	}

	public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLDataProperty dp, OWLIndividual source, OWLLiteral target)
	{
		return owlDataFactory.getOWLDataPropertyAssertionAxiom(dp, source, target);
	}

	public OWLSameIndividualAxiom getOWLSameIndividualAxiom(OWLIndividual ind1, OWLIndividual ind2)
	{
		return owlDataFactory.getOWLSameIndividualAxiom(ind1, ind2);
	}

	public OWLDifferentIndividualsAxiom getOWLDifferentIndividualsAxiom(OWLIndividual ind1, OWLIndividual ind2)
	{
		return owlDataFactory.getOWLDifferentIndividualsAxiom(ind1, ind2);
	}

	public OWLObjectAllValuesFrom getOWLObjectAllValuesFrom(OWLObjectProperty op, OWLClassExpression ce)
	{
		return owlDataFactory.getOWLObjectAllValuesFrom(op, ce);
	}

	public OWLObjectSomeValuesFrom getOWLObjectSomeValuesFrom(OWLObjectProperty op, OWLClassExpression ce)
	{
		return owlDataFactory.getOWLObjectSomeValuesFrom(op, ce);
	}

	public OWLDataAllValuesFrom getOWLDataAllValuesFrom(OWLDataProperty dp, OWLDatatype dt)
	{
		return owlDataFactory.getOWLDataAllValuesFrom(dp, dt);
	}

	public OWLDataSomeValuesFrom getOWLDataSomeValuesFrom(OWLDataProperty dp, OWLDatatype dt)
	{
		return owlDataFactory.getOWLDataSomeValuesFrom(dp, dt);
	}

	public OWLObjectExactCardinality getOWLObjectExactCardinality(int cardinality, OWLObjectProperty op)
	{
		return owlDataFactory.getOWLObjectExactCardinality(cardinality, op);
	}

	public OWLDataExactCardinality getOWLDataExactCardinality(int cardinality, OWLDataProperty dp)
	{
		return owlDataFactory.getOWLDataExactCardinality(cardinality, dp);
	}

	public OWLObjectMaxCardinality getOWLObjectMaxCardinality(int cardinality, OWLObjectProperty op)
	{
		return owlDataFactory.getOWLObjectMaxCardinality(cardinality, op);
	}

	public OWLDataMaxCardinality getOWLDataMaxCardinality(int cardinality, OWLDataProperty dp)
	{
		return owlDataFactory.getOWLDataMaxCardinality(cardinality, dp);
	}

	public OWLObjectMinCardinality getOWLObjectMinCardinality(int cardinality, OWLObjectProperty op)
	{
		return owlDataFactory.getOWLObjectMinCardinality(cardinality, op);
	}

	public OWLDataMinCardinality getOWLDataMinCardinality(int cardinality, OWLDataProperty dp)
	{
		return owlDataFactory.getOWLDataMinCardinality(cardinality, dp);
	}

	public OWLObjectHasValue getOWLObjectHasValue(OWLObjectProperty op, OWLNamedIndividual ind)
	{
		return owlDataFactory.getOWLObjectHasValue(op, ind);
	}

	public OWLDataHasValue getOWLDataHasValue(OWLDataProperty dp, OWLLiteral lit)
	{
		return owlDataFactory.getOWLDataHasValue(dp, lit);
	}

	public OWLAxiom getLabelAnnotationAxiom(OWLEntity owlEntity, String label, String language)
	{
		OWLLiteral value = owlDataFactory.getOWLLiteral(label, language);
		OWLAnnotation labelAnno = owlDataFactory.getOWLAnnotation(owlDataFactory.getRDFSLabel(), value);
		return owlDataFactory.getOWLAnnotationAssertionAxiom(owlEntity.getIRI(), labelAnno);
	}

	public boolean isOWLEntity(IRI iri)
	{
		return this.ontology.containsEntityInSignature(iri);
	}

	public boolean isOWLEntity(String shortName)
	{
		return isOWLEntity(getQualifiedName(shortName));
	}

	public boolean isOWLClass(IRI iri)
	{
		return this.ontology.containsClassInSignature(iri);
	}

	public boolean isOWLClass(String shortName)
	{
		return isOWLClass(getQualifiedName(shortName));
	}

	public boolean isOWLNamedIndividual(IRI iri)
	{
		return this.ontology.containsIndividualInSignature(iri);
	}

	public boolean isOWLNamedIndividual(String shortName)
	{
		return isOWLNamedIndividual(getQualifiedName(shortName));
	}

	public boolean isOWLObjectProperty(IRI iri)
	{
		return this.ontology.containsObjectPropertyInSignature(iri);
	}

	public boolean isOWLObjectProperty(String shortName)
	{
		return isOWLObjectProperty(getQualifiedName(shortName));
	}

	public boolean isOWLDataProperty(IRI iri)
	{
		return this.ontology.containsDataPropertyInSignature(iri);
	}

	public boolean isOWLDataProperty(String shortName)
	{
		return isOWLDataProperty(getQualifiedName(shortName));
	}

	public boolean isOWLAnnotationProperty(IRI iri)
	{
		return this.ontology.containsAnnotationPropertyInSignature(iri);
	}

	public boolean isOWLAnnotationProperty(String shortName)
	{
		return isOWLAnnotationProperty(getQualifiedName(shortName));
	}

	public boolean isOWLDatatype(IRI iri)
	{
		return this.ontology.containsDatatypeInSignature(iri);
	}

	public boolean isOWLDatatype(String shortName)
	{
		return isOWLDatatype(getQualifiedName(shortName));
	}

	public boolean isOWLObjectProperty(OWLProperty property)
	{
		return this.ontology.containsObjectPropertyInSignature(property.getIRI());
	}

	public boolean isOWLDataProperty(OWLProperty property)
	{
		return this.ontology.containsDataPropertyInSignature(property.getIRI());
	}

	public boolean isOWLAnnotationProperty(OWLAnnotationProperty property)
	{
		return this.ontology.containsAnnotationPropertyInSignature(property.getIRI());
	}
	public Set<OWLEntity> getOWLEntityWithRDFSLabel(String labelText)
	{
		/*
		 * The current implementation treats labelText the same as the OWL entity ID.
		 * TODO: Redo the implementation
		 */
		return this.ontology.getEntitiesInSignature(IRI.create(labelText));
	}

	public Set<OWLEntity> getOWLEntityWithRDFSLabelAndAtLeastOneLanguage(String labelText)
	{
		/*
		 * The current implementation treats labelText the same as the OWL entity ID.
		 * TODO: Redo the implementation
		 */
		return this.ontology.getEntitiesInSignature(IRI.create(labelText));
	}

	public Set<OWLEntity> getOWLEntityWithRDFSLabelAndLanguage(String labelText, String language)
	{
		/*
		 * The current implementation treats labelText the same as the OWL entity ID.
		 * TODO: Redo the implementation
		 */
		return this.ontology.getEntitiesInSignature(IRI.create(labelText));
	}

	public String getNamespaceForPrefix(String prefix) throws RendererException
	{
		IRI iri = prefixManager.getIRI(prefix);
		if (iri != null) {
			return iri.toString();
		}
		throw new RendererException("Namespace for prefix '" + prefix + "' cannot be found!");
	}
}

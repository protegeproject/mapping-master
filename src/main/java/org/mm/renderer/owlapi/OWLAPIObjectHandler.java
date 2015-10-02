package org.mm.renderer.owlapi;

import java.util.Map;
import java.util.Set;

import org.mm.parser.MappingMasterParserConstants;
import org.mm.renderer.LabelToEntityMapper;
import org.mm.renderer.NameUtil;
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
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

class OWLAPIObjectHandler implements MappingMasterParserConstants
{
   private final OWLOntology ontology;

   private final OWLDataFactory owlDataFactory;

   private final LabelToEntityMapper labelToEntityMapper;

   private final DefaultPrefixManager prefixManager = new DefaultPrefixManager();

   public OWLAPIObjectHandler(OWLOntology ontology)
   {
      this.ontology = ontology;

      labelToEntityMapper = new LabelToEntityMapper(ontology);

      owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

      // Assemble the prefix manager for the given ontology
      OWLOntologyFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
      if (format.isPrefixOWLOntologyFormat()) {
         Map<String, String> prefixMap = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
         for (String prefixName : prefixMap.keySet()) {
            prefixManager.setPrefix(prefixName, prefixMap.get(prefixName));
         }
      }

      // Make sure the default prefix is set
      if (prefixManager.getDefaultPrefix() == null) {
         IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
         if (ontologyIRI != null) {
            String iri = ontologyIRI.toString();
            if (!iri.endsWith("/") || !iri.endsWith("#")) {
               iri += "/";
            }
            prefixManager.setDefaultPrefix(iri);
         }
      }
   }

   public String getDefaultPrefix()
   {
      return prefixManager.getDefaultPrefix();
   }

   public IRI getQualifiedName(String shortName)
   {
      return prefixManager.getIRI(shortName);
   }

   public OWLDeclarationAxiom getOWLDeclarationAxiom(OWLEntity entity)
   {
      return owlDataFactory.getOWLDeclarationAxiom(entity);
   }

   public OWLClass getOWLClass(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return getOWLClass(IRI.create(givenName));
      } else {
         return getOWLClass(getQualifiedName(givenName));
      }
   }

   public OWLClass getOWLClass(String prefix, String localName)
   {
      return getOWLClass(IRI.create(prefix, localName));
   }

   public OWLClass getOWLClass(IRI iri)
   {
      return owlDataFactory.getOWLClass(iri);
   }

   public OWLNamedIndividual getOWLNamedIndividual(String givenName) throws RendererException
   {
      if (NameUtil.isValidIriString(givenName)) {
         return getOWLNamedIndividual(IRI.create(givenName));
      } else {
         return getOWLNamedIndividual(getQualifiedName(givenName));
      }
   }

   public OWLNamedIndividual getOWLNamedIndividual(String prefix, String localName)
   {
      return getOWLNamedIndividual(IRI.create(prefix, localName));
   }

   public OWLNamedIndividual getOWLNamedIndividual(IRI iri)
   {
      return owlDataFactory.getOWLNamedIndividual(iri);
   }

   public OWLObjectProperty getOWLObjectProperty(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return getOWLObjectProperty(IRI.create(givenName));
      } else {
         return getOWLObjectProperty(getQualifiedName(givenName));
      }
   }

   public OWLObjectProperty getOWLObjectProperty(String prefix, String localName)
   {
      return getOWLObjectProperty(IRI.create(prefix, localName));
   }

   public OWLObjectProperty getOWLObjectProperty(IRI iri)
   {
      return owlDataFactory.getOWLObjectProperty(iri);
   }

   public OWLDataProperty getOWLDataProperty(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return getOWLDataProperty(IRI.create(givenName));
      } else {
         return getOWLDataProperty(getQualifiedName(givenName));
      }
   }

   public OWLDataProperty getOWLDataProperty(String prefix, String localName)
   {
      return getOWLDataProperty(IRI.create(prefix, localName));
   }

   public OWLDataProperty getOWLDataProperty(IRI iri)
   {
      return owlDataFactory.getOWLDataProperty(iri);
   }

   public OWLAnnotationProperty getOWLAnnotationProperty(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return getOWLAnnotationProperty(IRI.create(givenName));
      } else {
         return getOWLAnnotationProperty(getQualifiedName(givenName));
      }
   }

   public OWLAnnotationProperty getOWLAnnotationProperty(String prefix, String localName)
   {
      return getOWLAnnotationProperty(IRI.create(prefix, localName));
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
      return owlDataFactory.getOWLLiteral(value + "", OWL2Datatype.XSD_INT);
   }

   public OWLAnnotationValue getOWLAnnotationValue(boolean value)
   {
      return owlDataFactory.getOWLLiteral(value);
   }

   public OWLDatatype getOWLDatatype(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return owlDataFactory.getOWLDatatype(IRI.create(givenName));
      } else {
         return owlDataFactory.getOWLDatatype(getQualifiedName(givenName));
      }
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

   public OWLLiteral getOWLLiteralDecimal(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_DECIMAL);
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

   public OWLSubObjectPropertyOfAxiom getOWLSubObjectPropertyOfAxiom(OWLObjectPropertyExpression child,
         OWLObjectPropertyExpression parent)
   {
      return owlDataFactory.getOWLSubObjectPropertyOfAxiom(child, parent);
   }

   public OWLSubDataPropertyOfAxiom getOWLSubDataPropertyOfAxiom(OWLDataPropertyExpression child,
         OWLDataPropertyExpression parent)
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

   public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(OWLAnnotationProperty ap, OWLEntity entity,
         OWLLiteral value)
   {
      return getOWLAnnotationAssertionAxiom(ap, entity.getIRI(), value);
   }

   public OWLAnnotationAssertionAxiom getOWLAnnotationAssertionAxiom(OWLAnnotationProperty ap, OWLAnnotationSubject as,
         OWLAnnotationValue value)
   {
      return owlDataFactory.getOWLAnnotationAssertionAxiom(ap, as, value);
   }

   public OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLClassExpression ce, OWLNamedIndividual ind)
   {
      return owlDataFactory.getOWLClassAssertionAxiom(ce, ind);
   }

   public OWLObjectPropertyAssertionAxiom getOWLObjectPropertyAssertionAxiom(OWLObjectProperty op, OWLIndividual source,
         OWLIndividual target)
   {
      return owlDataFactory.getOWLObjectPropertyAssertionAxiom(op, source, target);
   }

   public OWLDataPropertyAssertionAxiom getOWLDataPropertyAssertionAxiom(OWLDataProperty dp, OWLIndividual source,
         OWLLiteral target)
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

   public boolean isOWLEntity(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return isOWLEntity(IRI.create(givenName));
      } else {
         return isOWLEntity(getQualifiedName(givenName));
      }
   }

   public boolean isOWLClass(IRI iri)
   {
      return this.ontology.containsClassInSignature(iri);
   }

   public boolean isOWLClass(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return isOWLClass(IRI.create(givenName));
      } else {
         return isOWLClass(getQualifiedName(givenName));
      }
   }

   public boolean isOWLNamedIndividual(IRI iri)
   {
      return this.ontology.containsIndividualInSignature(iri);
   }

   public boolean isOWLNamedIndividual(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return isOWLNamedIndividual(IRI.create(givenName));
      } else {
         return isOWLNamedIndividual(getQualifiedName(givenName));
      }
   }

   public boolean isOWLObjectProperty(IRI iri)
   {
      return this.ontology.containsObjectPropertyInSignature(iri);
   }

   public boolean isOWLObjectProperty(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return isOWLObjectProperty(IRI.create(givenName));
      } else {
         return isOWLObjectProperty(getQualifiedName(givenName));
      }
   }

   public boolean isOWLDataProperty(IRI iri)
   {
      return this.ontology.containsDataPropertyInSignature(iri);
   }

   public boolean isOWLDataProperty(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return isOWLDataProperty(IRI.create(givenName));
      } else {
         return isOWLDataProperty(getQualifiedName(givenName));
      }
   }

   public boolean isOWLAnnotationProperty(IRI iri)
   {
      return this.ontology.containsAnnotationPropertyInSignature(iri);
   }

   public boolean isOWLAnnotationProperty(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return isOWLAnnotationProperty(IRI.create(givenName));
      } else {
         return isOWLAnnotationProperty(getQualifiedName(givenName));
      }
   }

   public boolean isOWLDatatype(IRI iri)
   {
      return this.ontology.containsDatatypeInSignature(iri);
   }

   public boolean isOWLDatatype(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return isOWLDatatype(IRI.create(givenName));
      } else {
         return isOWLDatatype(getQualifiedName(givenName));
      }
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

   public Set<OWLEntity> getOWLEntities(String givenName)
   {
      if (NameUtil.isValidIriString(givenName)) {
         return ontology.getEntitiesInSignature(IRI.create(givenName));
      } else {
         return ontology.getEntitiesInSignature(getQualifiedName(givenName));
      }
   }

   public OWLEntity getOWLEntities(String givenName, int entityType)
   {
      Set<OWLEntity> entities = getOWLEntities(givenName);
      for (OWLEntity entity : entities) {
         switch (entityType) {
            case OWL_CLASS:
               if (entity.isOWLClass()) { 
                  return entity.asOWLClass();
               }
               break;
            case OWL_OBJECT_PROPERTY:
               if (entity.isOWLObjectProperty()) {
                  return entity.asOWLObjectProperty();
               }
               break;
            case OWL_DATA_PROPERTY:
               if (entity.isOWLClass()) {
                  return entity.asOWLDataProperty();
               }
               break;
            case OWL_ANNOTATION_PROPERTY:
               if (entity.isOWLAnnotationProperty()) {
                  return entity.asOWLAnnotationProperty();
               }
               break;
            case OWL_NAMED_INDIVIDUAL:
               if (entity.isOWLNamedIndividual()) {
                  return entity.asOWLNamedIndividual();
               }
               break;
         }
      }
      return null;
   }

   public OWLEntity getOWLEntityWithRDFSLabel(String labelText)
   {
      return labelToEntityMapper.getEntityInLabel(labelText);
   }

   public OWLEntity getOWLEntityWithRDFSLabelAndAtLeastOneLanguage(String labelText)
   {
      return labelToEntityMapper.getEntityInLabel(labelText);
   }

   public OWLEntity getOWLEntityWithRDFSLabelAndLanguage(String labelText, String language)
   {
      return labelToEntityMapper.getEntityInLabel(labelText, language);
   }

   public String getPrefixForPrefixLabel(String prefixLabel) throws RendererException
   {
      IRI iri = prefixManager.getIRI(prefixLabel);
      if (iri != null) {
         return iri.toString();
      }
      throw new RendererException("Prefix for prefix label '" + prefixLabel + "' cannot be found!");
   }
}

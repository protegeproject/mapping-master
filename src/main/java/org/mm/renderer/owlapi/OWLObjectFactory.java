package org.mm.renderer.owlapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.mm.core.OWLEntityResolver;
import org.mm.core.OWLOntologySource;
import org.mm.exceptions.EntityCreationException;
import org.mm.exceptions.EntityNotFoundException;
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
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

public class OWLObjectFactory implements MappingMasterParserConstants
{
   private final OWLEntityResolver entityResolver;

   private final OWLDataFactory owlDataFactory;

   private final LabelToEntityMapper labelToEntityMapper;

   private Map<String, String> prefixMap = new HashMap<>();

   private OWLObjectFactory(OWLOntology ontology, OWLEntityResolver entityResolver)
   {
      this.entityResolver = entityResolver;
      owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
      labelToEntityMapper = new LabelToEntityMapper(ontology);
      OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
      if (format.isPrefixOWLOntologyFormat()) {
         prefixMap = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
      }
   }

   public static OWLObjectFactory newInstance(OWLOntologySource ontologySource)
   {
      return new OWLObjectFactory(ontologySource.getOWLOntology(), ontologySource.getEntityResolver());
   }

   /*
    * Handles IRI string resolutions and IRI creation
    */

   public IRI createIri(String inputString)
   {
      if (inputString.startsWith("<")) {
         inputString = inputString.substring(1, inputString.length() - 1);
      }
      return IRI.create(inputString);
   }

   private boolean isValidUriConstruct(String inputName) {
      return NameUtil.isValidUriConstruct(inputName);
   }

   public String getPrefix(String prefixLabel) throws RendererException
   {
      String iriString = prefixMap.get(prefixLabel);
      if (iriString != null) {
         return iriString;
      }
      throw new RendererException("No prefix found for '" + prefixLabel);
   }

   /*
    * Handles the creation of OWL entities, i.e., class, property and individual.
    */

   public OWLEntity createOWLEntity(String inputName, int type) throws RendererException
   {
      switch (type) {
         case OWL_CLASS:
            return createOWLClass(inputName);
         case OWL_NAMED_INDIVIDUAL:
            return createOWLNamedIndividual(inputName);
         case OWL_OBJECT_PROPERTY:
            return createOWLObjectProperty(inputName);
         case OWL_DATA_PROPERTY:
            return createOWLDataProperty(inputName);
         case OWL_ANNOTATION_PROPERTY:
            return createOWLAnnotationProperty(inputName);
      }
      throw new RendererException("Failed to create entity: " + inputName);
   }

   public OWLClass createOWLClass(String inputName) throws RendererException
   {
      try {
         if (isValidUriConstruct(inputName)) {
            return owlDataFactory.getOWLClass(IRI.create(inputName));
         } else {
            return entityResolver.create(inputName, OWLClass.class);
         }
      } catch (EntityCreationException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLNamedIndividual createOWLNamedIndividual(String inputName) throws RendererException
   {
      try {
         if (isValidUriConstruct(inputName)) {
            return owlDataFactory.getOWLNamedIndividual(IRI.create(inputName));
         } else {
            return entityResolver.create(inputName, OWLNamedIndividual.class);
         }
      } catch (EntityCreationException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLObjectProperty createOWLObjectProperty(String inputName) throws RendererException
   {
      try {
         if (isValidUriConstruct(inputName)) {
            return owlDataFactory.getOWLObjectProperty(IRI.create(inputName));
         } else {
            return entityResolver.create(inputName, OWLObjectProperty.class);
         }
      } catch (EntityCreationException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLDataProperty createOWLDataProperty(String inputName) throws RendererException
   {
      try {
         if (isValidUriConstruct(inputName)) {
            return owlDataFactory.getOWLDataProperty(IRI.create(inputName));
         } else {
            return entityResolver.create(inputName, OWLDataProperty.class);
         }
      } catch (EntityCreationException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLAnnotationProperty createOWLAnnotationProperty(String inputName) throws RendererException
   {
      try {
         if (isValidUriConstruct(inputName)) {
            return owlDataFactory.getOWLAnnotationProperty(IRI.create(inputName));
         } else {
            return entityResolver.create(inputName, OWLAnnotationProperty.class);
         }
      } catch (EntityCreationException e) {
         throw new RendererException(e.getMessage());
      }
   }

   /*
    *  Handles the creation of OWL typed literals
    */

   public OWLLiteral createTypedLiteral(String value, int type) throws RendererException
   {
      switch (type) {
         case XSD_STRING:
            return createOWLLiteralString(value);
         case XSD_BOOLEAN:
            return createOWLLiteralBoolean(value);
         case XSD_DOUBLE:
            return createOWLLiteralDouble(value);
         case XSD_FLOAT:
            return createOWLLiteralFloat(value);
         case XSD_LONG:
            return createOWLLiteralLong(value);
         case XSD_INTEGER:
            return createOWLLiteralInteger(value);
         case XSD_SHORT:
            return createOWLLiteralShort(value);
         case XSD_BYTE:
            return createOWLLiteralByte(value);
         case XSD_DECIMAL:
            return createOWLLiteralDecimal(value);
         case XSD_DATETIME:
            return createOWLLiteralDateTime(value);
         case XSD_DATE:
            return createOWLLiteralDate(value);
         case XSD_TIME:
            return createOWLLiteralTime(value);
         case XSD_DURATION:
            return createOWLLiteralDuration(value);
      }
      throw new RendererException("Unknown datatype");
   }

   public OWLLiteral createPlainLiteral(String value, Optional<String> lang)
   {
      if (lang.isPresent()) {
         return owlDataFactory.getOWLLiteral(value, lang.get());
      } else {
         return owlDataFactory.getOWLLiteral(value, "");
      }
   }

   public OWLLiteral createOWLLiteralString(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_STRING);
   }

   public OWLLiteral createOWLLiteralBoolean(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_BOOLEAN);
   }

   public OWLLiteral createOWLLiteralDouble(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_DOUBLE);
   }

   public OWLLiteral createOWLLiteralFloat(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_FLOAT);
   }

   public OWLLiteral createOWLLiteralLong(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_LONG);
   }

   public OWLLiteral createOWLLiteralInteger(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_INTEGER);
   }

   public OWLLiteral createOWLLiteralShort(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_SHORT);
   }

   public OWLLiteral createOWLLiteralByte(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_BYTE);
   }

   public OWLLiteral createOWLLiteralDecimal(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_DECIMAL);
   }

   public OWLLiteral createOWLLiteralDateTime(String value)
   {
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_DATE_TIME);
   }

   public OWLLiteral createOWLLiteralDate(String value)
   {
      return owlDataFactory.getOWLLiteral(value, owlDataFactory.getOWLDatatype(XSDVocabulary.DATE.getIRI()));
   }

   public OWLLiteral createOWLLiteralTime(String value)
   {
      return owlDataFactory.getOWLLiteral(value, owlDataFactory.getOWLDatatype(XSDVocabulary.TIME.getIRI()));
   }

   public OWLLiteral createOWLLiteralDuration(String value)
   {
      return owlDataFactory.getOWLLiteral(value, owlDataFactory.getOWLDatatype(XSDVocabulary.DURATION.getIRI()));
   }

   public OWLDatatype createOWLDatatype(String typeName) throws RendererException
   {
      try {
         return entityResolver.create(typeName, OWLDatatype.class);
      } catch (EntityCreationException e) {
         throw new RendererException(e.getMessage());
      }
   }

   /*
    * Handlers the creation of OWL complex expressions
    */

   public OWLDeclarationAxiom createOWLDeclarationAxiom(OWLEntity entity)
   {
      return owlDataFactory.getOWLDeclarationAxiom(entity);
   }

   public OWLObjectComplementOf createOWLObjectComplementOf(OWLClassExpression ce)
   {
      return owlDataFactory.getOWLObjectComplementOf(ce);
   }

   public OWLObjectIntersectionOf createOWLObjectIntersectionOf(Set<OWLClassExpression> ces)
   {
      return owlDataFactory.getOWLObjectIntersectionOf(ces);
   }

   public OWLObjectUnionOf createOWLObjectUnionOf(Set<OWLClassExpression> ces)
   {
      return owlDataFactory.getOWLObjectUnionOf(ces);
   }

   public OWLObjectOneOf createOWLObjectOneOf(Set<OWLNamedIndividual> inds)
   {
      return owlDataFactory.getOWLObjectOneOf(inds);
   }

   public OWLSubClassOfAxiom createOWLSubClassOfAxiom(OWLClassExpression child, OWLClassExpression parent)
   {
      return owlDataFactory.getOWLSubClassOfAxiom(child, parent);
   }

   public OWLSubObjectPropertyOfAxiom createOWLSubObjectPropertyOfAxiom(OWLObjectPropertyExpression child,
         OWLObjectPropertyExpression parent)
   {
      return owlDataFactory.getOWLSubObjectPropertyOfAxiom(child, parent);
   }

   public OWLSubDataPropertyOfAxiom createOWLSubDataPropertyOfAxiom(OWLDataPropertyExpression child,
         OWLDataPropertyExpression parent)
   {
      return owlDataFactory.getOWLSubDataPropertyOfAxiom(child, parent);
   }

   public OWLEquivalentClassesAxiom createOWLEquivalentClassesAxiom(Set<OWLClassExpression> ces)
   {
      return owlDataFactory.getOWLEquivalentClassesAxiom(ces);
   }

   public OWLEquivalentClassesAxiom createOWLEquivalentClassesAxiom(OWLClassExpression ce1, OWLClassExpression ce2)
   {
      return owlDataFactory.getOWLEquivalentClassesAxiom(ce1, ce2);
   }

   public OWLAnnotationAssertionAxiom createWLAnnotationAssertionAxiom(OWLAnnotationProperty ap, OWLEntity entity,
         OWLLiteral value)
   {
      return createOWLAnnotationAssertionAxiom(ap, entity.getIRI(), value);
   }

   public OWLAnnotationAssertionAxiom createOWLAnnotationAssertionAxiom(OWLAnnotationProperty ap, OWLAnnotationSubject as,
         OWLAnnotationValue value)
   {
      return owlDataFactory.getOWLAnnotationAssertionAxiom(ap, as, value);
   }

   public OWLClassAssertionAxiom createOWLClassAssertionAxiom(OWLClassExpression ce, OWLNamedIndividual ind)
   {
      return owlDataFactory.getOWLClassAssertionAxiom(ce, ind);
   }

   public OWLObjectPropertyAssertionAxiom createOWLObjectPropertyAssertionAxiom(OWLObjectProperty op, OWLIndividual source,
         OWLIndividual target)
   {
      return owlDataFactory.getOWLObjectPropertyAssertionAxiom(op, source, target);
   }

   public OWLDataPropertyAssertionAxiom createOWLDataPropertyAssertionAxiom(OWLDataProperty dp, OWLIndividual source,
         OWLLiteral target)
   {
      return owlDataFactory.getOWLDataPropertyAssertionAxiom(dp, source, target);
   }

   public OWLSameIndividualAxiom createOWLSameIndividualAxiom(OWLIndividual ind1, OWLIndividual ind2)
   {
      return owlDataFactory.getOWLSameIndividualAxiom(ind1, ind2);
   }

   public OWLDifferentIndividualsAxiom createOWLDifferentIndividualsAxiom(OWLIndividual ind1, OWLIndividual ind2)
   {
      return owlDataFactory.getOWLDifferentIndividualsAxiom(ind1, ind2);
   }

   public OWLObjectAllValuesFrom createOWLObjectAllValuesFrom(OWLObjectProperty op, OWLClassExpression ce)
   {
      return owlDataFactory.getOWLObjectAllValuesFrom(op, ce);
   }

   public OWLObjectSomeValuesFrom createOWLObjectSomeValuesFrom(OWLObjectProperty op, OWLClassExpression ce)
   {
      return owlDataFactory.getOWLObjectSomeValuesFrom(op, ce);
   }

   public OWLDataAllValuesFrom createOWLDataAllValuesFrom(OWLDataProperty dp, OWLDatatype dt)
   {
      return owlDataFactory.getOWLDataAllValuesFrom(dp, dt);
   }

   public OWLDataSomeValuesFrom createOWLDataSomeValuesFrom(OWLDataProperty dp, OWLDatatype dt)
   {
      return owlDataFactory.getOWLDataSomeValuesFrom(dp, dt);
   }

   public OWLObjectExactCardinality createOWLObjectExactCardinality(int cardinality, OWLObjectProperty op)
   {
      return owlDataFactory.getOWLObjectExactCardinality(cardinality, op);
   }

   public OWLDataExactCardinality createOWLDataExactCardinality(int cardinality, OWLDataProperty dp)
   {
      return owlDataFactory.getOWLDataExactCardinality(cardinality, dp);
   }

   public OWLObjectMaxCardinality createOWLObjectMaxCardinality(int cardinality, OWLObjectProperty op)
   {
      return owlDataFactory.getOWLObjectMaxCardinality(cardinality, op);
   }

   public OWLDataMaxCardinality createOWLDataMaxCardinality(int cardinality, OWLDataProperty dp)
   {
      return owlDataFactory.getOWLDataMaxCardinality(cardinality, dp);
   }

   public OWLObjectMinCardinality createOWLObjectMinCardinality(int cardinality, OWLObjectProperty op)
   {
      return owlDataFactory.getOWLObjectMinCardinality(cardinality, op);
   }

   public OWLDataMinCardinality createOWLDataMinCardinality(int cardinality, OWLDataProperty dp)
   {
      return owlDataFactory.getOWLDataMinCardinality(cardinality, dp);
   }

   public OWLObjectHasValue createOWLObjectHasValue(OWLObjectProperty op, OWLNamedIndividual ind)
   {
      return owlDataFactory.getOWLObjectHasValue(op, ind);
   }

   public OWLDataHasValue createOWLDataHasValue(OWLDataProperty dp, OWLLiteral lit)
   {
      return owlDataFactory.getOWLDataHasValue(dp, lit);
   }

   public OWLAxiom createLabelAnnotationAxiom(OWLEntity entity, String label, Optional<String> languageTag)
   {
      OWLLiteral value;
      if (languageTag.isPresent()) {
         value = owlDataFactory.getOWLLiteral(label, languageTag.get());
      } else {
         value = owlDataFactory.getOWLLiteral(label);
      }
      OWLAnnotation labelAnno = owlDataFactory.getOWLAnnotation(owlDataFactory.getRDFSLabel(), value);
      return owlDataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), labelAnno);
   }

   /*
    * Handles the retrieval of OWL entities from the input ontology given its name. All these methods below will
    * throw an exception if the input entity name doesn't exist in the ontology.
    */

   public OWLClass getAndCheckOWLClass(String inputName) throws RendererException
   {
      try {
         return entityResolver.resolve(inputName, OWLClass.class);
      } catch (EntityNotFoundException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLObjectProperty getAndCheckOWLObjectProperty(String inputName) throws RendererException
   {
      try {
         return entityResolver.resolve(inputName, OWLObjectProperty.class);
      } catch (EntityNotFoundException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLDataProperty getAndCheckOWLDataProperty(String inputName) throws RendererException
   {
      try {
         return entityResolver.resolve(inputName, OWLDataProperty.class);
      } catch (EntityNotFoundException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLAnnotationProperty getAndCheckOWLAnnotationProperty(String inputName) throws RendererException
   {
      try {
         return entityResolver.resolve(inputName, OWLAnnotationProperty.class);
      } catch (EntityNotFoundException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLNamedIndividual getAndCheckOWLNamedIndividual(String inputName) throws RendererException
   {
      try {
         return entityResolver.resolve(inputName, OWLNamedIndividual.class);
      } catch (EntityNotFoundException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public OWLProperty getAndCheckOWLProperty(String inputName) throws RendererException
   {
      try {
         return entityResolver.resolve(inputName, OWLProperty.class);
      } catch (EntityNotFoundException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public Optional<OWLEntity> getOWLEntity(String inputName, int entityType) throws RendererException
   {
      OWLEntity foundEntity = null;
      try {
         switch (entityType) {
            case OWL_CLASS :
               foundEntity = entityResolver.resolve(inputName, OWLClass.class);
               break;
            case OWL_OBJECT_PROPERTY :
               foundEntity = entityResolver.resolve(inputName, OWLObjectProperty.class);
               break;
            case OWL_DATA_PROPERTY :
               foundEntity = entityResolver.resolve(inputName, OWLDataProperty.class);
               break;
            case OWL_ANNOTATION_PROPERTY :
               foundEntity = entityResolver.resolve(inputName, OWLAnnotationProperty.class);
               break;
            case OWL_NAMED_INDIVIDUAL :
               foundEntity = entityResolver.resolve(inputName, OWLNamedIndividual.class);
               break;
         }
      } catch (EntityNotFoundException e) {
         ; // Do nothing
      }
      return Optional.ofNullable(foundEntity);
   }

   public Optional<OWLEntity> getOWLEntity(String inputName)
   {
      OWLEntity foundEntity = null;
      try {
         foundEntity = entityResolver.resolve(inputName, OWLEntity.class);
      }
      catch (EntityNotFoundException e) {
         ; // Do nothing
      }
      return Optional.ofNullable(foundEntity);
   }

   /*
    * Handles the retrieval of OWL entities from the input ontology given its label name (or display name).
    * The retrieval uses a simple mapping created at initial where each entity's display name is mapped to its
    * OWLEntity object. All these methods below will throw an exception if the input label name doesn't exist in
    * the map.
    */

   public Optional<OWLEntity> getOWLEntityFromDisplayName(String displayName, Optional<String> languageTag) throws RendererException
   {
      return labelToEntityMapper.getEntityInLabel(displayName, languageTag);
   }
}

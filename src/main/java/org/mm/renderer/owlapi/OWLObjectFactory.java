package org.mm.renderer.owlapi;

import java.util.Map;
import java.util.Optional;
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

public class OWLObjectFactory implements MappingMasterParserConstants
{
   private final OWLOntology ontology;

   private final OWLDataFactory owlDataFactory;

   private final PrefixManager prefixManager;

   private final LabelToEntityMapper labelToEntityMapper;

   public OWLObjectFactory(OWLOntology ontology)
   {
      this.ontology = ontology;

      prefixManager = setupPrefixManager(ontology);
      labelToEntityMapper = new LabelToEntityMapper(ontology);

      owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
   }

   private PrefixManager setupPrefixManager(OWLOntology ontology) {
      PrefixManager prefixManager = new DefaultPrefixManager();
      
      /*
       * Assemble the prefix manager for the given ontology
       */
      OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
      if (format.isPrefixOWLOntologyFormat()) {
         Map<String, String> prefixMap = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
         for (String prefixName : prefixMap.keySet()) {
            prefixManager.setPrefix(prefixName, prefixMap.get(prefixName));
         }
      }
      /*
       * Make sure the default prefix is set
       */
      if (prefixManager.getDefaultPrefix() == null) {
         com.google.common.base.Optional<IRI> ontologyIRI = ontology.getOntologyID().getOntologyIRI();
         if (ontologyIRI.isPresent()) {
            String iri = ontologyIRI.get().toString();
            if (!iri.endsWith("/") && !iri.endsWith("#")) {
               iri += "/";
            }
            prefixManager.setDefaultPrefix(iri);
         }
      }
      return prefixManager;
   }

   /*
    * Handles IRI string resolutions and IRI creation
    */

   public String getDefaultPrefix()
   {
      return prefixManager.getDefaultPrefix();
   }

   public IRI createIri(String inputName) throws RendererException
   {
      try {
         if (NameUtil.isValidUriConstruct(inputName)) {
            /*
             * Handles if the input name is already in a valid URI construct, e.g., http://example.org/someName
             */
            return IRI.create(inputName);
         }
         else {
            /*
             * There are two cases where the input name falls into this handler:
             * - the input name has "<...>" enclosed, e.g., <http://example.org/someName>, or
             * - the input name is an abbreviated IRI, e.g., abc:someName, :someName, someName
             */
            return prefixManager.getIRI(inputName);
         }
      } catch (RuntimeException e) {
         throw new RendererException(e.getMessage());
      }
   }

   public String getPrefix(String prefixLabel) throws RendererException
   {
      IRI iri = prefixManager.getIRI(prefixLabel);
      if (iri != null) {
         return iri.toString();
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
      return createOWLClass(createIri(inputName));
   }

   public OWLClass createOWLClass(IRI iri)
   {
      return owlDataFactory.getOWLClass(iri);
   }

   public OWLNamedIndividual createOWLNamedIndividual(String inputName) throws RendererException
   {
      return createOWLNamedIndividual(createIri(inputName));
   }

   public OWLNamedIndividual createOWLNamedIndividual(IRI iri)
   {
      return owlDataFactory.getOWLNamedIndividual(iri);
   }

   public OWLObjectProperty createOWLObjectProperty(String inputName) throws RendererException
   {
      return createOWLObjectProperty(createIri(inputName));
   }

   public OWLObjectProperty createOWLObjectProperty(IRI iri)
   {
      return owlDataFactory.getOWLObjectProperty(iri);
   }

   public OWLDataProperty createOWLDataProperty(String inputName) throws RendererException
   {
      return createOWLDataProperty(createIri(inputName));
   }

   public OWLDataProperty createOWLDataProperty(IRI iri)
   {
      return owlDataFactory.getOWLDataProperty(iri);
   }

   public OWLAnnotationProperty createOWLAnnotationProperty(String inputName) throws RendererException
   {
      return createOWLAnnotationProperty(createIri(inputName));
   }

   public OWLAnnotationProperty createOWLAnnotationProperty(IRI iri)
   {
      return owlDataFactory.getOWLAnnotationProperty(iri);
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
         case XSD_INT:
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
      return owlDataFactory.getOWLDatatype(createIri(typeName));
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

   public OWLClass getAndCheckOWLClass(String inputName) throws RendererException {
      Optional<OWLEntity> foundEntity = getOWLEntity(inputName, OWL_CLASS);
      if (!foundEntity.isPresent()) {
         throwEntityNotFoundException(inputName, "class");
      }
      return foundEntity.get().asOWLClass();
   }

   public OWLObjectProperty getAndCheckOWLObjectProperty(String inputName) throws RendererException {
      Optional<OWLEntity> foundEntity = getOWLEntity(inputName, OWL_OBJECT_PROPERTY);
      if (!foundEntity.isPresent()) {
         throwEntityNotFoundException(inputName, "object property");
      }
      return foundEntity.get().asOWLObjectProperty();
   }

   public OWLDataProperty getAndCheckOWLDataProperty(String inputName) throws RendererException {
      Optional<OWLEntity> foundEntity = getOWLEntity(inputName, OWL_DATA_PROPERTY);
      if (!foundEntity.isPresent()) {
         throwEntityNotFoundException(inputName, "data property");
      }
      return foundEntity.get().asOWLDataProperty();
   }

   public OWLAnnotationProperty getAndCheckOWLAnnotationProperty(String inputName) throws RendererException {
      Optional<OWLEntity> foundEntity = getOWLEntity(inputName, OWL_ANNOTATION_PROPERTY);
      if (!foundEntity.isPresent()) {
         throwEntityNotFoundException(inputName, "annotation property");
      }
      return foundEntity.get().asOWLAnnotationProperty();
   }

   public OWLNamedIndividual getAndCheckOWLNamedIndividual(String inputName) throws RendererException {
      Optional<OWLEntity> foundEntity = getOWLEntity(inputName, OWL_NAMED_INDIVIDUAL);
      if (!foundEntity.isPresent()) {
         throwEntityNotFoundException(inputName, "named individual");
      }
      return foundEntity.get().asOWLNamedIndividual();
   }

   public OWLProperty getAndCheckOWLProperty(String inputName) throws RendererException {
      /*
       * Try to find first if the given input name is a data property
       */
      try {
         return getAndCheckOWLObjectProperty(inputName);
      } catch (RendererException e) {
         // NO-OP
      }
      /*
       * If not, try to check if it is an object property
       */
      try {
         return getAndCheckOWLDataProperty(inputName);
      } catch (RendererException e) {
         // NO-OP
      }
      /*
       * Perhaps, it is an annotation property...
       */
      try {
         return getAndCheckOWLAnnotationProperty(inputName);
      } catch (RendererException e) {
         // NO-OP
      }
      /*
       * OK, give up!
       */
      throwEntityNotFoundException(inputName, "data/object/annotation property");
      return null;
   }

   public Optional<OWLEntity> getOWLEntity(String inputName, int entityType) throws RendererException
   {
      OWLEntity foundEntity = null;
      Set<OWLEntity> entities = getOWLEntities(inputName);
      for (OWLEntity entity : entities) {
         switch (entityType) {
            case OWL_CLASS:
               if (entity.isOWLClass()) { foundEntity = entity; }
               break;
            case OWL_OBJECT_PROPERTY:
               if (entity.isOWLObjectProperty()) { foundEntity = entity; }
               break;
            case OWL_DATA_PROPERTY:
               if (entity.isOWLDataProperty()) { foundEntity = entity; }
               break;
            case OWL_ANNOTATION_PROPERTY:
               if (entity.isOWLAnnotationProperty()) { foundEntity = entity; }
               break;
            case OWL_NAMED_INDIVIDUAL:
               if (entity.isOWLNamedIndividual()) { foundEntity = entity; }
               break;
         }
         if (foundEntity != null) { break; }
      }
      return Optional.ofNullable(foundEntity);
   }

   public Optional<OWLEntity> getOWLEntity(String inputName) throws RendererException
   {
      /*
       * Assuming to return the first entity found in the list.
       */
      return getOWLEntities(inputName).stream().findFirst();
   }

   private Set<OWLEntity> getOWLEntities(String inputName) throws RendererException
   {
      return ontology.getEntitiesInSignature(createIri(inputName));
   }

   private void throwEntityNotFoundException(String inputName, String entityType) throws RendererException
   {
      String msg = String.format("The expected %s (%s) does not exist in the ontology", entityType, inputName);
      throw new RendererException(msg);
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

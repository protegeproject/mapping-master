package org.mm.renderer.owlapi;

import java.util.ArrayList;
import java.util.List;
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
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

public class OWLAPIObjectFactory implements MappingMasterParserConstants
{
   private final OWLOntology ontology;

   private final OWLDataFactory owlDataFactory;

   private final LabelToEntityMapper labelToEntityMapper;

   private final DefaultPrefixManager prefixManager = new DefaultPrefixManager();

   public OWLAPIObjectFactory(OWLOntology ontology)
   {
      this.ontology = ontology;

      labelToEntityMapper = new LabelToEntityMapper(ontology);

      owlDataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

      // Assemble the prefix manager for the given ontology
      OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
      if (format.isPrefixOWLOntologyFormat()) {
         Map<String, String> prefixMap = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
         for (String prefixName : prefixMap.keySet()) {
            prefixManager.setPrefix(prefixName, prefixMap.get(prefixName));
         }
      }

      // Make sure the default prefix is set
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
   }

   /*
    * Handles IRI string resolutions and IRI creation
    */

   public String getDefaultPrefix()
   {
      return prefixManager.getDefaultPrefix();
   }

   public IRI getIri(String inputName) throws RendererException
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

   /*
    * Handles the creation of OWL entities, i.e., class, property and individual
    */

   public OWLClass getOWLClass(String inputName) throws RendererException
   {
      return getOWLClass(getIri(inputName));
   }

   public OWLClass getOWLClass(IRI iri)
   {
      return owlDataFactory.getOWLClass(iri);
   }

   public OWLNamedIndividual getOWLNamedIndividual(String inputName) throws RendererException
   {
      return getOWLNamedIndividual(getIri(inputName));
   }

   public OWLNamedIndividual getOWLNamedIndividual(IRI iri)
   {
      return owlDataFactory.getOWLNamedIndividual(iri);
   }

   public OWLObjectProperty getOWLObjectProperty(String inputName) throws RendererException
   {
      return getOWLObjectProperty(getIri(inputName));
   }

   public OWLObjectProperty getOWLObjectProperty(IRI iri)
   {
      return owlDataFactory.getOWLObjectProperty(iri);
   }

   public OWLDataProperty getOWLDataProperty(String inputName) throws RendererException
   {
      return getOWLDataProperty(getIri(inputName));
   }

   public OWLDataProperty getOWLDataProperty(IRI iri)
   {
      return owlDataFactory.getOWLDataProperty(iri);
   }

   public OWLAnnotationProperty getOWLAnnotationProperty(String inputName) throws RendererException
   {
      return getOWLAnnotationProperty(getIri(inputName));
   }

   public OWLAnnotationProperty getOWLAnnotationProperty(IRI iri)
   {
      return owlDataFactory.getOWLAnnotationProperty(iri);
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
      List<RendererException> exceptions = new ArrayList<>();
      /*
       * Try to find first if the given input name is a data property
       */
      try {
         return getAndCheckOWLObjectProperty(inputName);
      } catch (RendererException e) {
         exceptions.add(e);
      }
      /*
       * If not, try to check if it is an object property
       */
      try {
         return getAndCheckOWLDataProperty(inputName);
      } catch (RendererException e) {
         exceptions.add(e);
      }
      /*
       * Perhaps, it is an annotation property...
       */
      try {
         return getAndCheckOWLAnnotationProperty(inputName);
      } catch (RendererException e) {
         exceptions.add(e);
      }
      /*
       * OK, give up!
       */
      throwPropertyNotFoundException(exceptions);
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

   private Set<OWLEntity> getOWLEntities(String inputName) throws RendererException
   {
      return ontology.getEntitiesInSignature(getIri(inputName));
   }

   private void throwEntityNotFoundException(String inputName, String entityType) throws RendererException
   {
      String msg = String.format("The expected %s (%s) does not exist in the ontology", entityType, inputName);
      throw new RendererException(msg);
   }

   private void throwPropertyNotFoundException(List<RendererException> exceptions) throws RendererException
   {
      StringBuilder sb = new StringBuilder();
      boolean needNewline = false;
      for (RendererException e : exceptions) {
         if (needNewline) {
            sb.append("\n");
         }
         sb.append(e.getMessage());
         needNewline = true;
      }
      throw new RendererException(sb.toString());
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

   /*
    * Handles the creation of typed literal values
    */

   public OWLAnnotationValue getOWLAnnotationValue(String value, boolean isLiteral) throws RendererException
   {
      if (isLiteral) {
         return owlDataFactory.getOWLLiteral(value);
      } else {
         return getIri(value);
      }
   }

   public OWLAnnotationValue getOWLAnnotationValue(String value, String lang) throws RendererException
   {
      return owlDataFactory.getOWLLiteral(value, lang);
   }

   public OWLAnnotationValue getOWLAnnotationValue(float value)
   {
      return owlDataFactory.getOWLLiteral(value);
   }

   public OWLAnnotationValue getOWLAnnotationValue(double value)
   {
      return owlDataFactory.getOWLLiteral(value);
   }

   public OWLAnnotationValue getOWLAnnotationValue(int value)
   {
      return owlDataFactory.getOWLLiteral(value);
   }

   public OWLAnnotationValue getOWLAnnotationValue(boolean value)
   {
      return owlDataFactory.getOWLLiteral(value);
   }

   public OWLDatatype getOWLDatatype(String inputName) throws RendererException
   {
      return owlDataFactory.getOWLDatatype(getIri(inputName));
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
      return owlDataFactory.getOWLLiteral(value, OWL2Datatype.XSD_INTEGER);
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

   /*
    * Handlers the creation of OWL complex expressions
    */

   public OWLDeclarationAxiom getOWLDeclarationAxiom(OWLEntity entity)
   {
      return owlDataFactory.getOWLDeclarationAxiom(entity);
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

   public OWLAxiom getLabelAnnotationAxiom(OWLEntity entity, String label, Optional<String> languageTag)
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

   public String getPrefixForPrefixLabel(String prefixLabel) throws RendererException
   {
      IRI iri = prefixManager.getIRI(prefixLabel);
      if (iri != null) {
         return iri.toString();
      }
      throw new RendererException("Prefix for prefix label '" + prefixLabel + "' cannot be found!");
   }
}

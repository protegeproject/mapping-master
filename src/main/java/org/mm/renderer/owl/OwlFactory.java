package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import org.mm.renderer.internal.AnnotationPropertyIri;
import org.mm.renderer.internal.AnnotationPropertyName;
import org.mm.renderer.internal.ClassIri;
import org.mm.renderer.internal.ClassName;
import org.mm.renderer.internal.DataPropertyIri;
import org.mm.renderer.internal.DataPropertyName;
import org.mm.renderer.internal.IndividualIri;
import org.mm.renderer.internal.IndividualName;
import org.mm.renderer.internal.IriValue;
import org.mm.renderer.internal.LiteralValue;
import org.mm.renderer.internal.ObjectPropertyIri;
import org.mm.renderer.internal.ObjectPropertyName;
import org.mm.renderer.internal.PlainLiteralValue;
import org.mm.renderer.internal.PrefixedValue;
import org.mm.renderer.internal.PropertyIri;
import org.mm.renderer.internal.PropertyName;
import org.mm.renderer.internal.UntypedIri;
import org.mm.renderer.internal.UntypedPrefixedName;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
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
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OwlFactory {

   private final OwlEntityResolver entityResolver;

   public OwlFactory(@Nonnull OwlEntityResolver entityResolver) {
      this.entityResolver = checkNotNull(entityResolver);
   }

   /*
    * Methods to create the OWL Class
    */

   public OWLClass getOWLClass(@Nonnull ClassName className) {
      return (className.isFromWorkbook()) ? createOWLClass(className) : fetchOWLClass(className);
   }

   public OWLClass getOWLClass(@Nonnull UntypedPrefixedName prefixedName) {
      return (prefixedName.isFromWorkbook()) ? createOWLClass(prefixedName) : fetchOWLClass(prefixedName);
   }

   private OWLClass createOWLClass(PrefixedValue entityName) {
      return createOWLClass(entityName.getString());
   }

   private OWLClass fetchOWLClass(PrefixedValue entityName) {
      return fetchOWLClass(entityName.getString());
   }

   public OWLClass getOWLClass(@Nonnull ClassIri classIri) {
      return classIri.isFromWorkbook() ? createOWLClass(classIri) : fetchOWLClass(classIri);
   }

   public OWLClass getOWLClass(@Nonnull UntypedIri iri) {
      return iri.isFromWorkbook() ? createOWLClass(iri) : fetchOWLClass(iri);
   }

   private OWLClass createOWLClass(IriValue entityIri) {
      String entityName = encloseWithBrackets(entityIri.getString());
      return createOWLClass(entityName);
   }

   private OWLClass fetchOWLClass(IriValue entityIri) {
      String entityName = encloseWithBrackets(entityIri.getString());
      return fetchOWLClass(entityName);
   }

   private static String encloseWithBrackets(String iriString) {
      if (!iriString.startsWith("<") && !iriString.endsWith(">")) {
         iriString = String.format("<%s>", iriString);
      }
      return iriString;
   }

   public OWLClass createOWLClass(String className) {
      return entityResolver.createUnchecked(className, OWLClass.class);
   }

   public OWLClass fetchOWLClass(String className) {
      return entityResolver.resolveUnchecked(className, OWLClass.class);
   }

   /*
    * Methods to create the OWL Property
    */

   public OWLProperty getOWLProperty(@Nonnull PropertyName propertyName) {
      return (propertyName.isFromWorkbook()) ? createOWLProperty(propertyName) : fetchOWLProperty(propertyName);
   }

   public OWLProperty getOWLProperty(@Nonnull UntypedPrefixedName propertyName) {
      String entityName = propertyName.getString();
      if (entityResolver.hasType(entityName, OWLDataProperty.class)) {
         return fetchOWLDataProperty(propertyName);
      } else if (entityResolver.hasType(entityName, OWLObjectProperty.class)) {
         return fetchOWLObjectProperty(propertyName);
      } else if (entityResolver.hasType(entityName, OWLAnnotationProperty.class)) {
         return fetchOWLAnnotationProperty(propertyName);
      }
      throw new RuntimeException(
            String.format("The expected entity name '%s' does not exist in the ontology",
                  entityName));
   }

   private OWLProperty createOWLProperty(@Nonnull PropertyName propertyName) {
      if (propertyName.isDataProperty()) {
         return createOWLDataProperty((DataPropertyName) propertyName);
      } else if (propertyName.isObjectProperty()) {
         return createOWLObjectProperty((ObjectPropertyName) propertyName);
      } else if (propertyName.isAnnotationProperty()) {
         return createOWLAnnotationProperty((AnnotationPropertyName) propertyName);
      }
      throw new RuntimeException(
            String.format("Programming error: Creating OWL property of type %s is not yet implemented",
                  propertyName.getClass()));
   }

   private OWLProperty fetchOWLProperty(@Nonnull PropertyName propertyName) {
      if (propertyName.isDataProperty()) {
         return fetchOWLDataProperty((DataPropertyName) propertyName);
      } else if (propertyName.isObjectProperty()) {
         return fetchOWLObjectProperty((ObjectPropertyName) propertyName);
      } else if (propertyName.isAnnotationProperty()) {
         return fetchOWLAnnotationProperty((AnnotationPropertyName) propertyName);
      }
      throw new RuntimeException(
            String.format("Programming error: Fetching OWL property of type %s is not yet implemented",
                  propertyName.getClass()));
   }

   public OWLProperty getOWLProperty(@Nonnull PropertyIri propertyIri) {
      return (propertyIri.isFromWorkbook()) ? createOWLProperty(propertyIri) : fetchOWLProperty(propertyIri);
   }

   public OWLProperty getOWLProperty(@Nonnull UntypedIri entityIri) {
      String entityName = encloseWithBrackets(entityIri.getString());
      if (entityResolver.hasType(entityName, OWLDataProperty.class)) {
         return fetchOWLDataProperty(entityIri);
      } else if (entityResolver.hasType(entityName, OWLObjectProperty.class)) {
         return fetchOWLObjectProperty(entityIri);
      } else if (entityResolver.hasType(entityName, OWLAnnotationProperty.class)) {
         return fetchOWLAnnotationProperty(entityIri);
      }
      throw new RuntimeException(
            String.format("The expected entity name '%s' does not exist in the ontology",
                  entityName));
   }

   private OWLProperty createOWLProperty(@Nonnull PropertyIri propertyIri) {
      if (propertyIri.isDataProperty()) {
         return createOWLDataProperty((DataPropertyIri) propertyIri);
      } else if (propertyIri.isObjectProperty()) {
         return createOWLObjectProperty((ObjectPropertyIri) propertyIri);
      } else if (propertyIri.isAnnotationProperty()) {
         return createOWLAnnotationProperty((AnnotationPropertyIri) propertyIri);
      }
      throw new RuntimeException(
            String.format("Programming error: Creating OWL property of type %s is not yet implemented",
                  propertyIri.getClass()));
   }

   private OWLProperty fetchOWLProperty(@Nonnull PropertyIri propertyIri) {
      if (propertyIri.isDataProperty()) {
         return fetchOWLDataProperty((DataPropertyIri) propertyIri);
      } else if (propertyIri.isObjectProperty()) {
         return fetchOWLObjectProperty((ObjectPropertyIri) propertyIri);
      } else if (propertyIri.isAnnotationProperty()) {
         return fetchOWLAnnotationProperty((AnnotationPropertyIri) propertyIri);
      }
      throw new RuntimeException(
            String.format("Programming error: Fetching OWL property of type %s is not yet implemented",
                  propertyIri.getClass()));
   }

   /*
    * Methods to create the OWL Data Property
    */

   public OWLDataProperty getOWLDataProperty(@Nonnull DataPropertyName dataPropertyName) {
      return (dataPropertyName.isFromWorkbook()) ? createOWLDataProperty(dataPropertyName) : fetchOWLDataProperty(dataPropertyName);
   }

   public OWLDataProperty getOWLDataProperty(@Nonnull UntypedPrefixedName prefixedName) {
      return (prefixedName.isFromWorkbook()) ? createOWLDataProperty(prefixedName) : fetchOWLDataProperty(prefixedName);
   }

   private OWLDataProperty createOWLDataProperty(@Nonnull PrefixedValue propertyName) {
      return createOWLDataProperty(propertyName.getString());
   }

   private OWLDataProperty fetchOWLDataProperty(@Nonnull PrefixedValue  propertyName) {
      return fetchOWLDataProperty(propertyName.getString());
   }

   public OWLDataProperty getOWLDataProperty(@Nonnull DataPropertyIri dataPropertyIri) {
      return (dataPropertyIri.isFromWorkbook()) ? createOWLDataProperty(dataPropertyIri) : fetchOWLDataProperty(dataPropertyIri);
   }

   public OWLDataProperty getOWLDataProperty(@Nonnull UntypedIri iri) {
      return (iri.isFromWorkbook()) ? createOWLDataProperty(iri) : fetchOWLDataProperty(iri);
   }

   private OWLDataProperty createOWLDataProperty(@Nonnull IriValue entityIri) {
      String propertyName = encloseWithBrackets(entityIri.getString());
      return createOWLDataProperty(propertyName);
   }

   private OWLDataProperty fetchOWLDataProperty(@Nonnull IriValue entityIri) {
      String propertyName = encloseWithBrackets(entityIri.getString());
      return fetchOWLDataProperty(propertyName);
   }

   public OWLDataProperty createOWLDataProperty(@Nonnull String propertyName) {
      return entityResolver.createUnchecked(propertyName, OWLDataProperty.class);
   }

   public OWLDataProperty fetchOWLDataProperty(@Nonnull String propertyName) {
      return entityResolver.resolveUnchecked(propertyName, OWLDataProperty.class);
   }

   /*
    * Methods to create the OWL Object Property
    */

   public OWLObjectProperty getOWLObjectProperty(@Nonnull ObjectPropertyName objectPropertyName) {
      return (objectPropertyName.isFromWorkbook()) ? createOWLObjectProperty(objectPropertyName) : fetchOWLObjectProperty(objectPropertyName);
   }

   public OWLObjectProperty getOWLObjectProperty(@Nonnull UntypedPrefixedName prefixedName) {
      return (prefixedName.isFromWorkbook()) ? createOWLObjectProperty(prefixedName) : fetchOWLObjectProperty(prefixedName);
   }

   private OWLObjectProperty createOWLObjectProperty(@Nonnull PrefixedValue propertyName) {
      return createOWLObjectProperty(propertyName.getString());
   }

   private OWLObjectProperty fetchOWLObjectProperty(@Nonnull PrefixedValue propertyName) {
      return fetchOWLObjectProperty(propertyName.getString());
   }

   public OWLObjectProperty getOWLObjectProperty(@Nonnull ObjectPropertyIri objectPropertyIri) {
      return (objectPropertyIri.isFromWorkbook()) ? createOWLObjectProperty(objectPropertyIri) : fetchOWLObjectProperty(objectPropertyIri);
   }

   public OWLObjectProperty getOWLObjectProperty(@Nonnull UntypedIri iri) {
      return (iri.isFromWorkbook()) ? createOWLObjectProperty(iri) : fetchOWLObjectProperty(iri);
   }

   private OWLObjectProperty createOWLObjectProperty(IriValue entityIri) {
      String propertyName = encloseWithBrackets(entityIri.getString());
      return createOWLObjectProperty(propertyName);
   }

   private OWLObjectProperty fetchOWLObjectProperty(IriValue entityIri) {
      String propertyName = encloseWithBrackets(entityIri.getString());
      return fetchOWLObjectProperty(propertyName);
   }

   public OWLObjectProperty createOWLObjectProperty(@Nonnull String propertyName) {
      return entityResolver.createUnchecked(propertyName, OWLObjectProperty.class);
   }

   public OWLObjectProperty fetchOWLObjectProperty(@Nonnull String propertyName) {
      return entityResolver.resolveUnchecked(propertyName, OWLObjectProperty.class);
   }

   /*
    * Methods to create the OWL Annotation Property
    */

   public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull AnnotationPropertyName annotationPropertyName) {
      return (annotationPropertyName.isFromWorkbook()) ? createOWLAnnotationProperty(annotationPropertyName) : fetchOWLAnnotationProperty(annotationPropertyName);
   }

   public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull UntypedPrefixedName prefixedName) {
      return (prefixedName.isFromWorkbook()) ? createOWLAnnotationProperty(prefixedName) : fetchOWLAnnotationProperty(prefixedName);
   }

   private OWLAnnotationProperty createOWLAnnotationProperty(@Nonnull PrefixedValue propertyName) {
      return createOWLAnnotationProperty(propertyName.getString());
   }

   private OWLAnnotationProperty fetchOWLAnnotationProperty(@Nonnull PrefixedValue propertyName) {
      return fetchOWLAnnotationProperty(propertyName.getString());
   }

   public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull AnnotationPropertyIri annotationPropertyIri) {
      return (annotationPropertyIri.isFromWorkbook()) ? createOWLAnnotationProperty(annotationPropertyIri) : fetchOWLAnnotationProperty(annotationPropertyIri);
   }

   public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull UntypedIri iri) {
      return (iri.isFromWorkbook()) ? createOWLAnnotationProperty(iri) : fetchOWLAnnotationProperty(iri);
   }

   private OWLAnnotationProperty createOWLAnnotationProperty(@Nonnull IriValue entityIri) {
      String propertyName = encloseWithBrackets(entityIri.getString());
      return createOWLAnnotationProperty(propertyName);
   }

   private OWLAnnotationProperty fetchOWLAnnotationProperty(@Nonnull IriValue entityIri) {
      String propertyName = encloseWithBrackets(entityIri.getString());
      return fetchOWLAnnotationProperty(propertyName);
   }

   public OWLAnnotationProperty createOWLAnnotationProperty(@Nonnull String propertyName) {
      return entityResolver.createUnchecked(propertyName, OWLAnnotationProperty.class);
   }

   public OWLAnnotationProperty fetchOWLAnnotationProperty(@Nonnull String propertyName) {
      return entityResolver.resolveUnchecked(propertyName, OWLAnnotationProperty.class);
   }

   /*
    * Methods to create the OWL Annotation Value
    */

   public OWLAnnotationValue getOWLAnnotationValue(@Nonnull ClassIri classIri) {
      return IRI.create(classIri.getString());
   }

   public OWLAnnotationValue getOWLAnnotationValue(@Nonnull UntypedIri iri) {
      return IRI.create(iri.getString());
   }

   public OWLAnnotationValue getOWLAnnotationValue(@Nonnull LiteralValue literalValue) {
      return getOWLTypedLiteral(literalValue);
   }

   public OWLAnnotationValue getOWLAnnotationValue(@Nonnull PlainLiteralValue plainLiteralValue) {
      return getOWLPlainLiteral(plainLiteralValue);
   }

   public OWLLiteral getOWLTypedLiteral(@Nonnull LiteralValue literal) {
      final String lexicalString = literal.getString();
      final String datatype = literal.getDatatype();
      return createOWLLiteral(lexicalString, createOWLDatatype(datatype));
   }

   public OWLDatatype createOWLDatatype(@Nonnull String datatype) {
      return entityResolver.resolveUnchecked(datatype, OWLDatatype.class);
   }

   public OWLLiteral getOWLPlainLiteral(@Nonnull PlainLiteralValue literal) {
      final String lexicalString = literal.getString();
      final Optional<String> language = literal.getLanguage();
      if (language.isPresent()) {
         return createOWLLiteral(lexicalString, language.get());
      } else {
         return createOWLLiteral(lexicalString, OWL2Datatype.RDF_PLAIN_LITERAL);
      }
   }

   /*
    * Methods to create the OWL Individual
    */

   public OWLNamedIndividual getOWLNamedIndividual(@Nonnull IndividualName individualName) {
      return (individualName.isFromWorkbook()) ? createOWLNamedIndividual(individualName) : fetchOWLNamedIndividual(individualName);
   }

   public OWLNamedIndividual getOWLNamedIndividual(@Nonnull UntypedPrefixedName prefixedName) {
      return (prefixedName.isFromWorkbook()) ? createOWLNamedIndividual(prefixedName) : fetchOWLNamedIndividual(prefixedName);
   }

   private OWLNamedIndividual createOWLNamedIndividual(@Nonnull PrefixedValue entityName) {
      return entityResolver.createUnchecked(entityName.getString(), OWLNamedIndividual.class);
   }

   private OWLNamedIndividual fetchOWLNamedIndividual(@Nonnull PrefixedValue entityName) {
      return entityResolver.resolveUnchecked(entityName.getString(), OWLNamedIndividual.class);
   }

   public OWLNamedIndividual getOWLNamedIndividual(@Nonnull IndividualIri individualIri) {
      return (individualIri.isFromWorkbook()) ? createOWLNamedIndividual(individualIri) : fetchOWLNamedIndividual(individualIri);
   }

   public OWLNamedIndividual getOWLNamedIndividual(@Nonnull UntypedIri iri) {
      return (iri.isFromWorkbook()) ? createOWLNamedIndividual(iri) : fetchOWLNamedIndividual(iri);
   }

   private OWLNamedIndividual createOWLNamedIndividual(@Nonnull IriValue entityIri) {
      String individualName = encloseWithBrackets(entityIri.getString());
      return entityResolver.createUnchecked(individualName, OWLNamedIndividual.class);
   }

   private OWLNamedIndividual fetchOWLNamedIndividual(@Nonnull IriValue entityIri) {
      String individualName = encloseWithBrackets(entityIri.getString());
      return entityResolver.resolveUnchecked(individualName, OWLNamedIndividual.class);
   }

   /*
    * Public methods to create a various number of OWL expression and axioms
    */

   private final OWLDataFactory owlDataFactory = OWLManager.getOWLDataFactory();

   public OWLLiteral createOWLLiteral(
         @Nonnull String value,
         @Nonnull OWLDatatype datatype) {
      return owlDataFactory.getOWLLiteral(value, datatype);
   }

   public OWLLiteral createOWLLiteral(
         @Nonnull String value,
         @Nonnull OWL2Datatype datatype) {
      return owlDataFactory.getOWLLiteral(value, datatype);
   }

   public OWLLiteral createOWLLiteral(
         @Nonnull String value,
         @Nonnull String language) {
      return owlDataFactory.getOWLLiteral(value, language);
   }

   public OWLAnnotation createOWLAnnotation(
         @Nonnull OWLAnnotationProperty property,
         @Nonnull OWLAnnotationValue value) {
      return owlDataFactory.getOWLAnnotation(property, value);
   }

   public OWLDeclarationAxiom createOWLDeclarationAxiom(@Nonnull OWLEntity entity) {
      return owlDataFactory.getOWLDeclarationAxiom(entity);
   }

   public OWLObjectComplementOf createOWLObjectComplementOf(@Nonnull OWLClassExpression ce) {
      return owlDataFactory.getOWLObjectComplementOf(ce);
   }

   public OWLObjectIntersectionOf createOWLObjectIntersectionOf(@Nonnull Set<OWLClassExpression> ces) {
      return owlDataFactory.getOWLObjectIntersectionOf(ces);
   }

   public OWLObjectUnionOf createOWLObjectUnionOf(@Nonnull Set<OWLClassExpression> ces) {
      return owlDataFactory.getOWLObjectUnionOf(ces);
   }

   public OWLObjectOneOf createOWLObjectOneOf(@Nonnull Set<OWLNamedIndividual> inds) {
      return owlDataFactory.getOWLObjectOneOf(inds);
   }

   public OWLSubClassOfAxiom createOWLSubClassOfAxiom(
         @Nonnull OWLClassExpression child,
         @Nonnull OWLClassExpression parent) {
      return owlDataFactory.getOWLSubClassOfAxiom(child, parent);
   }

   public OWLSubObjectPropertyOfAxiom createOWLSubObjectPropertyOfAxiom(
         @Nonnull OWLObjectPropertyExpression child,
         @Nonnull OWLObjectPropertyExpression parent) {
      return owlDataFactory.getOWLSubObjectPropertyOfAxiom(child, parent);
   }

   public OWLSubDataPropertyOfAxiom createOWLSubDataPropertyOfAxiom(
         @Nonnull OWLDataPropertyExpression child,
         @Nonnull OWLDataPropertyExpression parent) {
      return owlDataFactory.getOWLSubDataPropertyOfAxiom(child, parent);
   }

   public OWLEquivalentClassesAxiom createOWLEquivalentClassesAxiom(@Nonnull Set<OWLClassExpression> ces) {
      return owlDataFactory.getOWLEquivalentClassesAxiom(ces);
   }

   public OWLEquivalentClassesAxiom createOWLEquivalentClassesAxiom(
         @Nonnull OWLClassExpression ce1,
         @Nonnull OWLClassExpression ce2) {
      return owlDataFactory.getOWLEquivalentClassesAxiom(ce1, ce2);
   }

   public OWLAnnotationAssertionAxiom createOWLAnnotationAssertionAxiom(
         @Nonnull OWLEntity entity,
         @Nonnull OWLAnnotation annotation) {
      return owlDataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), annotation);
   }

   public OWLAnnotationAssertionAxiom createOWLAnnotationAssertionAxiom(
         @Nonnull OWLAnnotationSubject subject,
         @Nonnull OWLAnnotation annotation) {
      return owlDataFactory.getOWLAnnotationAssertionAxiom(subject, annotation);
   }

   public OWLClassAssertionAxiom createOWLClassAssertionAxiom(
         @Nonnull OWLClassExpression ce,
         @Nonnull OWLNamedIndividual ind) {
      return owlDataFactory.getOWLClassAssertionAxiom(ce, ind);
   }

   public OWLObjectPropertyAssertionAxiom createOWLObjectPropertyAssertionAxiom(
         @Nonnull OWLObjectProperty op,
         @Nonnull OWLIndividual source,
         @Nonnull OWLIndividual target) {
      return owlDataFactory.getOWLObjectPropertyAssertionAxiom(op, source, target);
   }

   public OWLDataPropertyAssertionAxiom createOWLDataPropertyAssertionAxiom(
         @Nonnull OWLDataProperty dp,
         @Nonnull OWLIndividual source,
         @Nonnull OWLLiteral target) {
      return owlDataFactory.getOWLDataPropertyAssertionAxiom(dp, source, target);
   }

   public OWLSameIndividualAxiom createOWLSameIndividualAxiom(
         @Nonnull OWLIndividual ind1,
         @Nonnull OWLIndividual ind2) {
      return owlDataFactory.getOWLSameIndividualAxiom(ind1, ind2);
   }

   public OWLDifferentIndividualsAxiom createOWLDifferentIndividualsAxiom(
         @Nonnull OWLIndividual ind1,
         @Nonnull OWLIndividual ind2) {
      return owlDataFactory.getOWLDifferentIndividualsAxiom(ind1, ind2);
   }

   public OWLObjectAllValuesFrom createOWLObjectAllValuesFrom(
         @Nonnull OWLObjectProperty op,
         @Nonnull OWLClassExpression ce) {
      return owlDataFactory.getOWLObjectAllValuesFrom(op, ce);
   }

   public OWLObjectSomeValuesFrom createOWLObjectSomeValuesFrom(
         @Nonnull OWLObjectProperty op,
         @Nonnull OWLClassExpression ce) {
      return owlDataFactory.getOWLObjectSomeValuesFrom(op, ce);
   }

   public OWLDataAllValuesFrom createOWLDataAllValuesFrom(
         @Nonnull OWLDataProperty dp,
         @Nonnull OWLDatatype dt) {
      return owlDataFactory.getOWLDataAllValuesFrom(dp, dt);
   }

   public OWLDataSomeValuesFrom createOWLDataSomeValuesFrom(
         @Nonnull OWLDataProperty dp,
         @Nonnull OWLDatatype dt) {
      return owlDataFactory.getOWLDataSomeValuesFrom(dp, dt);
   }

   public OWLObjectExactCardinality createOWLObjectExactCardinality(
         int cardinality,
         @Nonnull OWLObjectProperty op) {
      return owlDataFactory.getOWLObjectExactCardinality(cardinality, op);
   }

   public OWLObjectExactCardinality createOWLObjectExactCardinality(
         int cardinality,
         @Nonnull OWLObjectProperty property,
         @Nonnull OWLClassExpression classExpression) {
      return owlDataFactory.getOWLObjectExactCardinality(cardinality, property, classExpression);
   }

   public OWLDataExactCardinality createOWLDataExactCardinality(
         int cardinality,
         @Nonnull OWLDataProperty property,
         @Nonnull OWLDatatype datatype) {
      return owlDataFactory.getOWLDataExactCardinality(cardinality, property, datatype);
   }

   public OWLObjectMaxCardinality createOWLObjectMaxCardinality(
         int cardinality,
         @Nonnull OWLObjectProperty op) {
      return owlDataFactory.getOWLObjectMaxCardinality(cardinality, op);
   }

   public OWLObjectMaxCardinality createOWLObjectMaxCardinality(
         int cardinality,
         @Nonnull OWLObjectProperty property,
         @Nonnull OWLClassExpression classExpression) {
      return owlDataFactory.getOWLObjectMaxCardinality(cardinality, property, classExpression);
   }

   public OWLDataMaxCardinality createOWLDataMaxCardinality(
         int cardinality,
         @Nonnull OWLDataProperty property,
         @Nonnull OWLDatatype datatype) {
      return owlDataFactory.getOWLDataMaxCardinality(cardinality, property, datatype);
   }

   public OWLObjectMinCardinality createOWLObjectMinCardinality(
         int cardinality,
         @Nonnull OWLObjectProperty op) {
      return owlDataFactory.getOWLObjectMinCardinality(cardinality, op);
   }

   public OWLObjectMinCardinality createOWLObjectMinCardinality(
         int cardinality,
         @Nonnull OWLObjectProperty property,
         @Nonnull OWLClassExpression classExpression) {
      return owlDataFactory.getOWLObjectMinCardinality(cardinality, property, classExpression);
   }

   public OWLDataMinCardinality createOWLDataMinCardinality(
         int cardinality,
         @Nonnull OWLDataProperty property,
         @Nonnull OWLDatatype datatype) {
      return owlDataFactory.getOWLDataMinCardinality(cardinality, property, datatype);
   }

   public OWLObjectHasValue createOWLObjectHasValue(
         @Nonnull OWLObjectProperty op,
         @Nonnull OWLNamedIndividual ind) {
      return owlDataFactory.getOWLObjectHasValue(op, ind);
   }

   public OWLDataHasValue createOWLDataHasValue(
         @Nonnull OWLDataProperty dp,
         @Nonnull OWLLiteral lit) {
      return owlDataFactory.getOWLDataHasValue(dp, lit);
   }
}

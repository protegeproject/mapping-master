package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.mm.renderer.internal.EmptyValue;
import org.mm.renderer.internal.PrefixedValue;
import org.mm.renderer.internal.IriValue;
import org.mm.renderer.internal.LiteralValue;
import org.mm.renderer.internal.PlainLiteralValue;
import org.mm.renderer.internal.PropertyName;
import org.mm.renderer.internal.ReferencedEntityName;
import org.mm.renderer.internal.Value;
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

   private final OWLDataFactory owlDataFactory = OWLManager.getOWLDataFactory();

   public OwlFactory(@Nonnull OwlEntityResolver entityResolver) {
      this.entityResolver = checkNotNull(entityResolver);
   }

   @Nullable
   public OWLClass getOWLClass(@Nonnull Value value) {
      if (value instanceof EmptyValue) {
         return null;
      } else if (value instanceof PrefixedValue) {
         return fetchOWLClass((PrefixedValue) value);
      } else if (value instanceof ReferencedEntityName) {
         return createOWLClass((ReferencedEntityName) value);
      } else if (value instanceof IriValue) {
         return createOWLClass(((IriValue) value));
      }
      throw new RuntimeException("Programming error: Creating OWL class using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLClass fetchOWLClass(PrefixedValue entityName) {
      return entityResolver.resolveUnchecked(entityName.getString(), OWLClass.class);
   }

   private OWLClass createOWLClass(ReferencedEntityName referencedValue) {
      return entityResolver.createUnchecked(referencedValue.getString(), OWLClass.class);
   }

   private OWLClass createOWLClass(IriValue iriValue) {
      return owlDataFactory.getOWLClass(IRI.create(iriValue.getString()));
   }

   @Nullable
   public OWLProperty getOWLProperty(@Nonnull Value value) {
      if (value instanceof EmptyValue) {
         return null;
      } else if (value instanceof PrefixedValue) {
         return fetchOWLProperty(((PrefixedValue) value));
      } else if (value instanceof ReferencedEntityName) {
         return createOWLProperty((ReferencedEntityName) value);
      }
      throw new RuntimeException("Programming error: Creating OWL property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLProperty fetchOWLProperty(PrefixedValue entityName) {
      return entityResolver.resolveUnchecked(entityName.getString(), OWLProperty.class);
   }

   private OWLProperty createOWLProperty(ReferencedEntityName referencedValue) {
      if (referencedValue instanceof PropertyName) {
         PropertyName propertyName = (PropertyName) referencedValue;
         if (propertyName.isDataProperty()) {
            return createOWLDataProperty(propertyName);
         } else if (propertyName.isObjectProperty()) {
            return createOWLObjectProperty(propertyName);
         } else if (propertyName.isAnnotationProperty()) {
            return createOWLAnnotationProperty(propertyName);
         }
      }
      throw new RuntimeException("Programming error: Unknown property type");
   }

   @Nullable
   public OWLDataProperty getOWLDataProperty(@Nonnull Value value) {
      if (value instanceof EmptyValue) {
         return null;
      } else if (value instanceof PrefixedValue) {
         return fetchOWLDataProperty((PrefixedValue) value);
      } else if (value instanceof ReferencedEntityName) {
         return createOWLDataProperty((ReferencedEntityName) value);
      } else if (value instanceof IriValue) {
         return createOWLDataProperty((IriValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL data property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLDataProperty fetchOWLDataProperty(PrefixedValue entityName) {
      return entityResolver.resolveUnchecked(entityName.getString(), OWLDataProperty.class);
   }

   private OWLDataProperty createOWLDataProperty(ReferencedEntityName referencedValue) {
      return entityResolver.createUnchecked(referencedValue.getString(), OWLDataProperty.class);
   }

   private OWLDataProperty createOWLDataProperty(IriValue iriValue) {
      return owlDataFactory.getOWLDataProperty(IRI.create(iriValue.getString()));
   }

   @Nullable
   public OWLObjectProperty getOWLObjectProperty(@Nonnull Value value) {
      if (value instanceof EmptyValue) {
         return null;
      } else if (value instanceof PrefixedValue) {
         return fetchOWLObjectProperty((PrefixedValue) value);
      } else if (value instanceof ReferencedEntityName) {
         return createOWLObjectProperty((ReferencedEntityName) value);
      } else if (value instanceof IriValue) {
         return createOWLObjectProperty((IriValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL data property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLObjectProperty fetchOWLObjectProperty(PrefixedValue entityName) {
      return entityResolver.resolveUnchecked(entityName.getString(), OWLObjectProperty.class);
   }

   private OWLObjectProperty createOWLObjectProperty(ReferencedEntityName referencedValue) {
      return entityResolver.createUnchecked(referencedValue.getString(), OWLObjectProperty.class);
   }

   private OWLObjectProperty createOWLObjectProperty(IriValue iriValue) {
      return owlDataFactory.getOWLObjectProperty(IRI.create(iriValue.getString()));
   }

   @Nullable
   public OWLAnnotationProperty getOWLAnnotationProperty(@Nonnull Value value) {
      if (value instanceof EmptyValue) {
         return null;
      } else if (value instanceof PrefixedValue) {
         return fetchOWLAnnotationProperty((PrefixedValue) value);
      } else if (value instanceof ReferencedEntityName) {
         return createOWLAnnotationProperty((ReferencedEntityName) value);
      } else if (value instanceof IriValue) {
         return createOWLAnnotationProperty((IriValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL data property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLAnnotationProperty fetchOWLAnnotationProperty(PrefixedValue entityName) {
      return entityResolver.resolveUnchecked(entityName.getString(), OWLAnnotationProperty.class);
   }

   private OWLAnnotationProperty createOWLAnnotationProperty(ReferencedEntityName referencedValue) {
      return entityResolver.createUnchecked(referencedValue.getString(), OWLAnnotationProperty.class);
   }

   private OWLAnnotationProperty createOWLAnnotationProperty(IriValue iriValue) {
      return owlDataFactory.getOWLAnnotationProperty(IRI.create(iriValue.getString()));
   }

   @Nullable
   public OWLNamedIndividual getOWLNamedIndividual(@Nonnull Value value) {
      if (value instanceof EmptyValue) {
         return null;
      } else if (value instanceof PrefixedValue) {
         return fetchOWLNamedIndividual((PrefixedValue) value);
      } else if (value instanceof ReferencedEntityName) {
         return createOWLNamedIndividual((ReferencedEntityName) value);
      } else if (value instanceof IriValue) {
         return createOWLNamedIndividual((IriValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL data property using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLNamedIndividual fetchOWLNamedIndividual(PrefixedValue entityName) {
      return entityResolver.resolveUnchecked(entityName.getString(), OWLNamedIndividual.class);
   }

   private OWLNamedIndividual createOWLNamedIndividual(ReferencedEntityName referencedValue) {
      return entityResolver.createUnchecked(referencedValue.getString(), OWLNamedIndividual.class);
   }

   private OWLNamedIndividual createOWLNamedIndividual(IriValue iriValue) {
      return owlDataFactory.getOWLNamedIndividual(IRI.create(iriValue.getString()));
   }

   @Nullable
   public OWLAnnotationValue getOWLAnnotationValue(@Nonnull Value value) {
      if (value instanceof EmptyValue) {
         return null;
      } else if (value instanceof IriValue) {
         return getIri((IriValue) value);
      } else if (value instanceof LiteralValue) {
         return getOWLTypedLiteral((LiteralValue) value);
      } else if (value instanceof PlainLiteralValue) {
         return getOWLPlainLiteral((PlainLiteralValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL annotation using "
            + value.getClass() + " is not yet implemented");
   }

   private OWLAnnotationValue getIri(IriValue value) {
      return IRI.create(value.getString());
   }

   @Nullable
   public OWLLiteral getOWLLiteral(@Nonnull Value value) {
      if (value instanceof EmptyValue) {
         return null;
      } else if (value instanceof LiteralValue) {
         return getOWLTypedLiteral((LiteralValue) value);
      } else if (value instanceof PlainLiteralValue) {
         return getOWLPlainLiteral((PlainLiteralValue) value);
      }
      throw new RuntimeException("Programming error: Creating OWL literal using "
            + value.getClass() + " is not yet implemented");
   }

   public OWLLiteral getOWLTypedLiteral(@Nonnull LiteralValue literal) {
      final String lexicalString = literal.getString();
      final String datatype = literal.getDatatype();
      return owlDataFactory.getOWLLiteral(lexicalString, getOWLDatatype(datatype));
   }

   private OWLDatatype getOWLDatatype(@Nonnull String datatype) {
      return entityResolver.resolveUnchecked(datatype, OWLDatatype.class);
   }

   public OWLLiteral getOWLPlainLiteral(@Nonnull PlainLiteralValue literal) {
      final String lexicalString = literal.getString();
      final Optional<String> language = literal.getLanguage();
      if (language.isPresent()) {
         return owlDataFactory.getOWLLiteral(lexicalString, language.get());
      } else {
         return owlDataFactory.getOWLLiteral(lexicalString, OWL2Datatype.RDF_PLAIN_LITERAL);
      }
   }

   /*
    * Public methods to create a various number of OWL expression and axioms
    */

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

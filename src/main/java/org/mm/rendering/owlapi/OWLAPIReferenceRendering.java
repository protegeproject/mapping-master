package org.mm.rendering.owlapi;

import org.mm.core.ReferenceType;
import org.mm.rendering.ReferenceRendering;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Optional;
import java.util.Set;

public class OWLAPIReferenceRendering extends OWLAPIRendering implements ReferenceRendering
{
  private final Optional<OWLLiteral> literal;
  private final Optional<OWLEntity> entity;
  private final ReferenceType referenceType;
  private final String rawRendering;

  public OWLAPIReferenceRendering(OWLLiteral literal, ReferenceType referenceType)
  {
    this.literal = Optional.of(literal);
    this.entity = Optional.empty();
    this.rawRendering = literal.getLiteral();
    this.referenceType = referenceType;
  }

  public OWLAPIReferenceRendering(OWLEntity entity, ReferenceType referenceType)
  {
    this.entity = Optional.of(entity);
    this.literal = Optional.empty();
    this.rawRendering = entity.getIRI().toString();
    this.referenceType = referenceType;
  }

  public OWLAPIReferenceRendering(OWLEntity entity, Set<OWLAxiom> axioms, ReferenceType referenceType)
  {
    super(axioms);
    this.entity = Optional.of(entity);
    this.literal = Optional.empty();
    this.rawRendering = entity.getIRI().toString();
    this.referenceType = referenceType;
  }

  public Optional<OWLEntity> getOWLEntity() { return this.entity; }

  public Optional<OWLLiteral> getOWLLiteral() { return this.literal; }

  @Override public ReferenceType getReferenceType() { return this.referenceType; }

  @Override public String getRawValue() { return this.rawRendering; }

  @Override public boolean isOWLLiteral() { return this.literal.isPresent(); }

  @Override public boolean isOWLEntity() { return this.entity.isPresent(); }

  @Override public boolean isOWLClass() { return this.entity.isPresent() && this.entity.get().isOWLClass(); }

  @Override public boolean isOWLNamedIndividual()
  {
    return this.entity.isPresent() && this.entity.get().isOWLNamedIndividual();
  }

  @Override public boolean isOWLObjectProperty()
  {
    return this.entity.isPresent() && this.entity.get().isOWLObjectProperty();
  }

  @Override public boolean isOWLDataProperty()
  {
    return this.entity.isPresent() && this.entity.get().isOWLDataProperty();
  }

  @Override public boolean isOWLAnnotationProperty()
  {
    return this.entity.isPresent() && this.entity.get().isOWLAnnotationProperty();
  }

  @Override public boolean isOWLDatatype() { return this.entity.isPresent() && this.entity.get().isOWLDatatype(); }
}


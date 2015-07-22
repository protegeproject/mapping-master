package org.mm.renderer.owlapi;

import org.mm.renderer.ReferenceRendering;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Optional;
import java.util.Set;

public class OWLAPIReferenceRendering extends OWLAPIRendering implements ReferenceRendering
{
  private final Optional<OWLLiteral> literal;
  private final Optional<OWLEntity> entity;
  private final String rawRendering;

  public OWLAPIReferenceRendering(OWLLiteral literal)
  {
    this.literal = Optional.of(literal);
    this.entity = Optional.empty();
    this.rawRendering = literal.getLiteral();
  }

  public OWLAPIReferenceRendering(OWLEntity entity)
  {
    this.entity = Optional.of(entity);
    this.literal = Optional.empty();
    this.rawRendering = entity.getIRI().toString();
  }

  public OWLAPIReferenceRendering(OWLEntity entity, Set<OWLAxiom> axioms)
  {
    super(axioms);
    this.entity = Optional.of(entity);
    this.literal = Optional.empty();
    this.rawRendering = entity.getIRI().toString();
  }

  public Optional<OWLEntity> getOWLEntity() { return entity; }

  public Optional<OWLLiteral> getOWLLiteral() { return literal; }

  @Override public String getRawValue() { return this.rawRendering; }

  @Override public boolean isOWLLiteral() { return this.literal.isPresent(); }

  @Override public boolean isOWLEntity() { return this.entity.isPresent(); }

  @Override public boolean isOWLClass() { return this.entity.isPresent() && this.entity.get().isOWLClass(); }

  @Override public boolean isOWLNamedIndividual() { return this.entity.isPresent() && this.entity.get().isOWLNamedIndividual(); }

  @Override public boolean isOWLObjectProperty() { return this.entity.isPresent() && this.entity.get().isOWLObjectProperty(); }

  @Override public boolean isOWLDataProperty() { return this.entity.isPresent() && this.entity.get().isOWLDataProperty(); }

  @Override public boolean isOWLAnnotationProperty()
  {
    return this.entity.isPresent() && this.entity.get().isOWLAnnotationProperty();
  }

  @Override public boolean isOWLDatatype() { return this.entity.isPresent() && this.entity.get().isOWLDatatype(); }
}


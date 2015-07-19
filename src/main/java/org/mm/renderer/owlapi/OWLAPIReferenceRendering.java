package org.mm.renderer.owlapi;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Optional;

public class OWLAPIReferenceRendering extends OWLAPIRendering
{
  private Optional<OWLLiteral> literal = Optional.empty();
  private Optional<OWLEntity> entity = Optional.empty();

  public void setOWLLiteral(OWLLiteral literal)
  {
    this.literal = Optional.of(literal);
    this.entity = Optional.empty();
  }

  public void setOWLEntity(OWLEntity entity)
  {
    this.entity = Optional.of(entity);
    this.literal = Optional.empty();
  }

  public Optional<OWLEntity> getOWLEntity() { return entity; }

  public Optional<OWLLiteral> getOWLLiteral() { return literal; }

  public boolean isOWLLiteral() { return this.literal.isPresent(); }

  public boolean isOWLEntity() { return this.entity.isPresent(); }

  public boolean isOWLClass() { return this.entity.isPresent() && this.entity.get().isOWLClass(); }

  public boolean isOWLNamedIndividual() { return this.entity.isPresent() && this.entity.get().isOWLNamedIndividual(); }

  public boolean isOWLObjectProperty() { return this.entity.isPresent() && this.entity.get().isOWLObjectProperty(); }

  public boolean isOWLDataProperty() { return this.entity.isPresent() && this.entity.get().isOWLDataProperty(); }

  public boolean isOWLAnnotationProperty() { return this.entity.isPresent() && this.entity.get().isOWLAnnotationProperty(); }

  public boolean isOWLDatatype() { return this.entity.isPresent() && this.entity.get().isOWLDatatype(); }
}


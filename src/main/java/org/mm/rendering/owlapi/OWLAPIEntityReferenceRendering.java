package org.mm.rendering.owlapi;

import java.util.Set;

import org.mm.core.ReferenceType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

public class OWLAPIEntityReferenceRendering extends OWLAPIReferenceRendering
{
   private final OWLEntity entity;

   private final String rawRendering;

   public OWLAPIEntityReferenceRendering(OWLEntity entity, ReferenceType referenceType)
   {
      super(referenceType);
      this.entity = entity;
      this.rawRendering = entity.getIRI().toString();
   }

   public OWLAPIEntityReferenceRendering(OWLEntity entity, Set<OWLAxiom> axioms, ReferenceType referenceType)
   {
      super(axioms, referenceType);
      this.entity = entity;
      this.rawRendering = entity.getIRI().toString();
   }

   public OWLEntity getOWLEntity() {
      return entity;
   }

   @Override
   public String getRawValue()
   {
      return rawRendering;
   }

   @Override
   public boolean isOWLLiteral()
   {
      return false;
   }

   @Override
   public boolean isOWLEntity()
   {
      return true;
   }

   @Override
   public boolean isOWLClass()
   {
      return entity.isOWLClass();
   }

   @Override
   public boolean isOWLNamedIndividual()
   {
      return entity.isOWLNamedIndividual();
   }

   @Override
   public boolean isOWLObjectProperty()
   {
      return entity.isOWLObjectProperty();
   }

   @Override
   public boolean isOWLDataProperty()
   {
      return entity.isOWLDataProperty();
   }

   @Override
   public boolean isOWLAnnotationProperty()
   {
      return entity.isOWLAnnotationProperty();
   }

   @Override
   public boolean isOWLDatatype()
   {
      return entity.isOWLDatatype();
   }
}

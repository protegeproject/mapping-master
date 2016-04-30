package org.mm.core;

import org.semanticweb.owlapi.model.OWLOntology;

public interface OWLOntologySource
{
   OWLOntology getOWLOntology();

   OWLEntityResolver getEntityResolver();
}

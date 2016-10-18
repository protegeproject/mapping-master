package org.mm.core;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface OWLOntologySource {

   OWLOntology getOWLOntology();

   OWLEntityResolver getEntityResolver();
}

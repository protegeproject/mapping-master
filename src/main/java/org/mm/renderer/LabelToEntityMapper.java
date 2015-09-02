package org.mm.renderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;

public class LabelToEntityMapper
{
	Map<String, OWLEntity> mapper = new HashMap<String, OWLEntity>();
	
	public LabelToEntityMapper(OWLOntology ontology)
	{
		/*
		 * A very simple implementation of label-to-entity mapper. It can't handle label duplicates and
		 * very case sensitive.
		 */
		for (OWLEntity entity : ontology.getSignature()) {
			if (entity instanceof OWLAnnotationProperty || entity instanceof OWLDatatype) { // we skip those
				continue;
			}
			Set<OWLAnnotationAssertionAxiom> axioms = ontology.getAnnotationAssertionAxioms(entity.getIRI());
			for (OWLAnnotationAssertionAxiom axiom : axioms) {
				OWLAnnotationValue value = axiom.getValue();
				if (value instanceof IRI) {
					String label = value.asIRI().get().toString();
					mapper.put(label, entity); // put the IRI string
				} else if (value instanceof OWLLiteral) {
					OWLLiteral literal = value.asLiteral().get();
					String label = literal.getLiteral();
					mapper.put(label, entity); // put the Literal string
					if (literal.hasLang()) {
						String labelWithLanguage = label + "@" + literal.getLang();
						mapper.put(labelWithLanguage, entity); // additionally, if the Literal has a language tag we add this too.
					}
				}
			}
		}
	}

	public OWLEntity getEntityInLabel(String label)
	{
		return mapper.get(label);
	}

	public OWLEntity getEntityInLabel(String label, String language)
	{
		return mapper.get(label + "@" + language);
	}
}

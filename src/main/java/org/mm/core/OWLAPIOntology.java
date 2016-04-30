package org.mm.core;

import java.util.Map;

import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class OWLAPIOntology implements OWLOntologySource
{
   private OWLOntology ontology;

   public OWLAPIOntology(OWLOntology ontology)
   {
      this.ontology = ontology;
   }

   @Override
   public OWLOntology getOWLOntology()
   {
      return ontology;
   }

   @Override
   public OWLEntityResolver getEntityResolver()
   {
      return new OWLAPIEntityResolver(ontology, buildPrefixManager());
   }

   private PrefixManager buildPrefixManager()
   {
      PrefixManager prefixManager = new DefaultPrefixManager();
      OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
      if (format.isPrefixOWLOntologyFormat()) {
         Map<String, String> prefixMap = format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap();
         for (String prefixName : prefixMap.keySet()) {
            prefixManager.setPrefix(prefixName, prefixMap.get(prefixName));
         }
      }
      return prefixManager;
   }
}

package org.mm.app;

import org.mm.core.TransformationRuleSet;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.OWLOntology;

public class MMApplication
{
   private MMApplicationModel applicationModel;

   public MMApplication(OWLOntology ontology, SpreadSheetDataSource dataSource, TransformationRuleSet ruleSet)
   {
      applicationModel = new MMApplicationModel(ontology, dataSource, ruleSet);
   }

   public MMApplicationModel getApplicationModel()
   {
      return applicationModel;
   }
}

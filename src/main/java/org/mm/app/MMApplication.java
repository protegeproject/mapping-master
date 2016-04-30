package org.mm.app;

import org.mm.core.OWLOntologySource;
import org.mm.core.TransformationRuleSet;
import org.mm.ss.SpreadSheetDataSource;

public class MMApplication
{
   private MMApplicationModel applicationModel;

   public MMApplication(OWLOntologySource ontologySource, SpreadSheetDataSource dataSource, TransformationRuleSet ruleSet)
   {
      applicationModel = new MMApplicationModel(ontologySource, dataSource, ruleSet);
   }

   public MMApplicationModel getApplicationModel()
   {
      return applicationModel;
   }
}

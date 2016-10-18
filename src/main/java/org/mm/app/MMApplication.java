package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.mm.core.OWLOntologySource;
import org.mm.core.TransformationRuleSet;
import org.mm.ss.SpreadSheetDataSource;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplication {

   private final OWLOntologySource ontology;
   private final SpreadSheetDataSource dataSource;
   private final TransformationRuleSet ruleSet;

   public MMApplication(@Nonnull OWLOntologySource ontology,
         @Nonnull SpreadSheetDataSource dataSource, @Nonnull TransformationRuleSet ruleSet) {
      this.ontology = checkNotNull(ontology);
      this.dataSource = checkNotNull(dataSource);
      this.ruleSet = checkNotNull(ruleSet);
   }

   @Nonnull
   public MMApplicationModel getApplicationModel() { // TODO: Rename to MappingMasterEngine?
      return new MMApplicationModel(ontology, dataSource, ruleSet);
   }
}

package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import javax.annotation.Nonnull;

import org.mm.core.OWLOntologySource;
import org.mm.core.TransformationRuleSet;
import org.mm.core.TransformationRuleSetFactory;
import org.mm.workbook.WorkbookLoader;
import org.mm.workbook.Workbook;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplication {

   private final OWLOntologySource ontologySource;
   private final Workbook workbook;
   private final TransformationRuleSet ruleSet;

   /* package */ MMApplication(@Nonnull OWLOntologySource ontologySource,
         @Nonnull Workbook workbook,
         @Nonnull TransformationRuleSet ruleSet) {
      this.ontologySource = checkNotNull(ontologySource);
      this.workbook = checkNotNull(workbook);
      this.ruleSet = checkNotNull(ruleSet);
   }

   @Nonnull
   public static MMApplication create(@Nonnull OWLOntologySource ontologySource,
         @Nonnull File workbookFile, @Nonnull File ruleFile) throws Exception {
      checkNotNull(ontologySource);
      checkNotNull(workbookFile);
      checkNotNull(ruleFile);
      return new MMApplication(ontologySource,
            WorkbookLoader.loadWorkbook(workbookFile),
            TransformationRuleSetFactory.loadTransformationRulesFromDocument(ruleFile));
   }

   @Nonnull
   public static MMApplication create(@Nonnull OWLOntologySource ontologySource,
         @Nonnull File workbookFile) throws Exception {
      checkNotNull(ontologySource);
      checkNotNull(workbookFile);
      return new MMApplication(ontologySource,
            WorkbookLoader.loadWorkbook(workbookFile),
            TransformationRuleSetFactory.createEmptyTransformationRuleSet());
   }

   @Nonnull
   public MMApplicationModel getApplicationModel() { // TODO: Rename to MappingMasterEngine?
      return new MMApplicationModel(ontologySource, workbook, ruleSet);
   }
}

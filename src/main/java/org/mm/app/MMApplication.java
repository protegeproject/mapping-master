package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.poi.ss.usermodel.Workbook;
import org.mm.transformationrule.TransformationRuleSet;
import org.mm.transformationrule.TransformationRuleSetManager;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplication {

   private final OWLOntology ontology;
   private final Workbook workbook;
   private final TransformationRuleSet ruleSet;

   /* package */ MMApplication(@Nonnull OWLOntology ontology,
         @Nonnull Workbook workbook,
         @Nonnull TransformationRuleSet ruleSet) {
      this.ontology = checkNotNull(ontology);
      this.workbook = checkNotNull(workbook);
      this.ruleSet = checkNotNull(ruleSet);
   }

   @Nonnull
   public static MMApplication create(@Nonnull OWLOntology ontology,
         @Nonnull File workbookFile, @Nonnull File ruleFile) throws Exception {
      checkNotNull(ontology);
      checkNotNull(workbookFile);
      checkNotNull(ruleFile);
      return new MMApplication(ontology,
            WorkbookLoader.loadWorkbook(workbookFile),
            TransformationRuleSetManager.loadTransformationRulesFromDocument(ruleFile));
   }

   @Nonnull
   public static MMApplication create(@Nonnull OWLOntology ontology,
         @Nonnull File workbookFile) throws Exception {
      checkNotNull(ontology);
      checkNotNull(workbookFile);
      return new MMApplication(ontology,
            WorkbookLoader.loadWorkbook(workbookFile),
            TransformationRuleSetManager.createEmptyTransformationRuleSet());
   }

   @Nonnull
   public MMApplicationModel getApplicationModel() {
      return new MMApplicationModel(ontology, workbook, ruleSet);
   }
}

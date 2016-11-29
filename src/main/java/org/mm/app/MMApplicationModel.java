package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.apache.poi.ss.usermodel.Workbook;
import org.mm.transformationrule.TransformationRuleSet;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplicationModel implements ApplicationModel {

   private final OWLOntology ontology;
   private final Workbook workbook;

   private TransformationRuleSet ruleSet;

   public MMApplicationModel(@Nonnull OWLOntology ontology,
         @Nonnull Workbook workbook,
         @Nonnull TransformationRuleSet ruleSet) {
      this.ontology = checkNotNull(ontology);
      this.workbook = checkNotNull(workbook);
      this.ruleSet = checkNotNull(ruleSet);
   }

   @Override
   public OWLOntology getOntology() {
      return ontology;
   }

   @Override
   public Workbook getWorkbook() {
      return workbook;
   }

   @Override
   public TransformationRuleSet getTransformationRules() {
      return ruleSet;
   }

   public void setTransformationRules(@Nonnull TransformationRuleSet ruleSet) {
      this.ruleSet = ruleSet;
   }
}

package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.mm.core.OWLOntologySource;
import org.mm.renderer.Renderer;
import org.mm.renderer.owlapi.OWLRenderer;
import org.mm.renderer.text.TextRenderer;
import org.mm.transformationrule.TransformationRuleSet;
import org.mm.workbook.Workbook;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplicationModel implements ApplicationModel {

   private final OWLOntologySource ontologySource;
   private final Workbook workbook;
   private final TransformationRuleSet ruleSet;

   public MMApplicationModel(@Nonnull OWLOntologySource ontologySource,
         @Nonnull Workbook workbook,
         @Nonnull TransformationRuleSet ruleSet) {
      this.ontologySource = checkNotNull(ontologySource);
      this.workbook = checkNotNull(workbook);
      this.ruleSet = checkNotNull(ruleSet);
   }

   @Override
   public Workbook getWorkbook() {
      return workbook;
   }

   @Override
   public Renderer getTransformationRenderer() {
      return new OWLRenderer(ontologySource, workbook);
   }

   @Override
   public MMTransformationRuleModel getTransformationRuleModel() {
      return new MMTransformationRuleModel(ruleSet);
   }

   public TextRenderer getLogRenderer() {
      TextRenderer renderer = new TextRenderer(workbook);
      renderer.setComment(true);
      return renderer;
   }
}

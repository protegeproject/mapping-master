package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.mm.core.OWLOntologySource;
import org.mm.core.TransformationRuleSet;
import org.mm.renderer.Renderer;
import org.mm.renderer.owlapi.OWLRenderer;
import org.mm.renderer.text.TextRenderer;
import org.mm.workbook.SpreadSheetDataSource;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplicationModel implements ApplicationModel {

   private final OWLOntologySource ontologySource;
   private final SpreadSheetDataSource dataSource;
   private final TransformationRuleSet ruleSet;

   public MMApplicationModel(@Nonnull OWLOntologySource ontologySource,
         @Nonnull SpreadSheetDataSource dataSource,
         @Nonnull TransformationRuleSet ruleSet) {
      this.ontologySource = checkNotNull(ontologySource);
      this.dataSource = checkNotNull(dataSource);
      this.ruleSet = checkNotNull(ruleSet);
   }

   @Override
   public SpreadSheetDataSource getWorkbook() {
      return dataSource;
   }

   @Override
   public Renderer getTransformationRenderer() {
      return new OWLRenderer(ontologySource, dataSource);
   }

   @Override
   public MMTransformationRuleModel getTransformationRuleModel() {
      return new MMTransformationRuleModel(ruleSet);
   }

   public TextRenderer getLogRenderer() {
      TextRenderer renderer = new TextRenderer(dataSource);
      renderer.setComment(true);
      return renderer;
   }
}

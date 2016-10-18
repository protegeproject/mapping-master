package org.mm.app;

import org.mm.core.OWLOntologySource;
import org.mm.core.TransformationRuleSet;
import org.mm.renderer.Renderer;
import org.mm.renderer.owlapi.OWLRenderer;
import org.mm.renderer.text.TextRenderer;
import org.mm.ss.SpreadSheetDataSource;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplicationModel implements ApplicationModel {

   private final OWLOntologySource ontologySource;
   private final SpreadSheetDataSource dataSource;

   private final Renderer applicationRenderer;
   private final MMDataSourceModel dataSourceModel;
   private final MMTransformationRuleModel expressionMappingsModel;

   public MMApplicationModel(OWLOntologySource ontologySource, SpreadSheetDataSource dataSource,
         TransformationRuleSet ruleSet) {
      this.ontologySource = ontologySource;
      this.dataSource = dataSource;

      applicationRenderer = new OWLRenderer(ontologySource, dataSource);
      dataSourceModel = new MMDataSourceModel(dataSource);
      expressionMappingsModel = new MMTransformationRuleModel(ruleSet);
   }

   @Override
   public MMDataSourceModel getDataSourceModel() {
      return dataSourceModel;
   }

   @Override
   public MMTransformationRuleModel getTransformationRuleModel() {
      return expressionMappingsModel;
   }

   @Override
   public Renderer getDefaultRenderer() {
      return applicationRenderer;
   }

   public TextRenderer getLogRenderer() {
      TextRenderer renderer = new TextRenderer(dataSource);
      renderer.setComment(true);
      return renderer;
   }
}

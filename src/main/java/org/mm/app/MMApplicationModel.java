package org.mm.app;

import org.mm.core.OWLOntologySource;
import org.mm.core.TransformationRuleSet;
import org.mm.renderer.Renderer;
import org.mm.renderer.owlapi.OWLRenderer;
import org.mm.renderer.text.TextRenderer;
import org.mm.ss.SpreadSheetDataSource;

public class MMApplicationModel implements ApplicationModel
{
   private OWLOntologySource ontologySource;
   private SpreadSheetDataSource dataSource;

   private MMDataSourceModel dataSourceModel;
   private MMTransformationRuleModel expressionMappingsModel;

   public MMApplicationModel(OWLOntologySource ontologySource, SpreadSheetDataSource dataSource, TransformationRuleSet ruleSet)
   {
      this.ontologySource = ontologySource;
      this.dataSource = dataSource;

      dataSourceModel = new MMDataSourceModel(dataSource);
      expressionMappingsModel = new MMTransformationRuleModel(ruleSet);
   }

   @Override
   public MMDataSourceModel getDataSourceModel()
   {
      return dataSourceModel;
   }

   @Override
   public MMTransformationRuleModel getTransformationRuleModel()
   {
      return expressionMappingsModel;
   }

   @Override
   public Renderer getDefaultRenderer()
   {
      return getOWLAPIRenderer();
   }

   public OWLRenderer getOWLAPIRenderer()
   {
      return new OWLRenderer(ontologySource, dataSource);
   }

   public TextRenderer getTextRenderer()
   {
      return new TextRenderer(dataSource);
   }

   public TextRenderer getLogRenderer()
   {
      TextRenderer renderer = new TextRenderer(dataSource);
      renderer.setComment(true);
      return renderer;
   }
}

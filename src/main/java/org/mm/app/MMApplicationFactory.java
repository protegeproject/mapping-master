package org.mm.app;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Workbook;
import org.mm.core.OWLAPIOntology;
import org.mm.core.OWLOntologySource;
import org.mm.core.OWLOntologySourceHook;
import org.mm.core.TransformationRuleSet;
import org.mm.core.TransformationRuleSetFactory;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class MMApplicationFactory
{
   private Properties properties;

   public MMApplicationFactory()
   {
      properties = new Properties();
   }

   protected MMApplicationFactory addProperty(String propertyName, String value)
   {
      properties.setProperty(propertyName, value);
      return this;
   }

   public String getWorkbookFileLocation()
   {
      return properties.getProperty(Environment.WORKBOOK_SOURCE);
   }

   public void setWorkbookFileLocation(String path)
   {
      properties.setProperty(Environment.WORKBOOK_SOURCE, path);
   }

   public String getOntologyFileLocation()
   {
      return properties.getProperty(Environment.ONTOLOGY_SOURCE);
   }

   public void setOntologyFileLocation(String path)
   {
      properties.setProperty(Environment.ONTOLOGY_SOURCE, path);
   }

   public String getRuleFileLocation()
   {
      return properties.getProperty(Environment.TRANSFORMATION_RULES_SOURCE);
   }

   public void setRuleFileLocation(String path)
   {
      properties.setProperty(Environment.TRANSFORMATION_RULES_SOURCE, path);
   }

   public Properties getProperties()
   {
      return properties;
   }

   public MMApplication createApplication() throws Exception
   {
      Properties copy = new Properties();
      copy.putAll(properties);
      validate (copy, null);
      Resources resources = buildResources(copy);
      return new MMApplication(resources.getOWLOntologySource(),
            resources.getSpreadSheetDataSource(),
            resources.getTransformationRuleSet());
   }

   public MMApplication createApplication(OWLOntologySourceHook ontologySourceHook) throws Exception
   {
      Properties copy = new Properties();
      copy.putAll(properties);
      validate(copy, ontologySourceHook);
      Resources resources = buildResources(copy, ontologySourceHook);
      return new MMApplication(resources.getOWLOntologySource(),
            resources.getSpreadSheetDataSource(),
            resources.getTransformationRuleSet());
   }

   private Resources buildResources(Properties properties) throws Exception
   {
      Resources resources = new Resources();

      String ontologySourceLocation = properties.getProperty(Environment.ONTOLOGY_SOURCE);
      OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
      OWLOntology ontology = owlManager.loadOntologyFromOntologyDocument(new FileInputStream(ontologySourceLocation));
      OWLOntologySource ontologySource = new OWLAPIOntology(ontology);
      resources.setOWLOntologySource(ontologySource);

      String workbookSourceLocation = properties.getProperty(Environment.WORKBOOK_SOURCE);
      Workbook workbook = SpreadsheetFactory.loadWorkbookFromDocument(workbookSourceLocation);
      SpreadSheetDataSource datasource = new SpreadSheetDataSource(workbook);
      resources.setSpreadSheetDataSource(datasource);

      String ruleSourceLocation = properties.getProperty(Environment.TRANSFORMATION_RULES_SOURCE);
      TransformationRuleSet rules = TransformationRuleSetFactory.createEmptyTransformationRuleSet();
      if (ruleSourceLocation != null) {
         rules = TransformationRuleSetFactory.loadTransformationRulesFromDocument(ruleSourceLocation);
      }
      resources.setTransformationRuleSet(rules);

      return resources;
   }

   private Resources buildResources(Properties properties, OWLOntologySourceHook ontologySourceHook) throws Exception
   {
      Resources resources = new Resources();

      resources.setOWLOntologySource(ontologySourceHook);

      String workbookSourceLocation = properties.getProperty(Environment.WORKBOOK_SOURCE);
      Workbook workbook = SpreadsheetFactory.loadWorkbookFromDocument(workbookSourceLocation);
      SpreadSheetDataSource datasource = new SpreadSheetDataSource(workbook);
      resources.setSpreadSheetDataSource(datasource);

      String ruleSourceLocation = properties.getProperty(Environment.TRANSFORMATION_RULES_SOURCE);
      TransformationRuleSet ruleSet = TransformationRuleSetFactory.createEmptyTransformationRuleSet();
      if (ruleSourceLocation != null) {
         ruleSet = TransformationRuleSetFactory.loadTransformationRulesFromDocument(ruleSourceLocation);
      }
      resources.setTransformationRuleSet(ruleSet);

      return resources;
   }

   private void validate(Properties properties, OWLOntologySource ontologySource)
   {
      if (ontologySource == null) {
         if (properties.getProperty(Environment.ONTOLOGY_SOURCE) == null) {
            throw new ApplicationStartupException("Missing ontology source parameter");
         }
      }
      if (properties.getProperty(Environment.WORKBOOK_SOURCE) == null) {
         throw new ApplicationStartupException("Missing workbook source parameter");
      }
   }

   class Resources
   {
      private SpreadSheetDataSource spreadsheet;
      private OWLOntologySource ontology;
      private TransformationRuleSet ruleSet;

      public SpreadSheetDataSource getSpreadSheetDataSource()
      {
         return spreadsheet;
      }

      public void setSpreadSheetDataSource(SpreadSheetDataSource spreadsheet)
      {
         this.spreadsheet = spreadsheet;
      }

      public OWLOntologySource getOWLOntologySource()
      {
         return ontology;
      }

      public void setOWLOntologySource(OWLOntologySource ontology)
      {
         this.ontology = ontology;
      }

      public TransformationRuleSet getTransformationRuleSet()
      {
         return ruleSet;
      }

      public void setTransformationRuleSet(TransformationRuleSet ruleSet)
      {
         this.ruleSet = ruleSet;
      }
   }
}

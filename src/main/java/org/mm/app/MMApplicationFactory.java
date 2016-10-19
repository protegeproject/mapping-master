package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileInputStream;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.mm.core.OWLAPIOntology;
import org.mm.core.OWLOntologySource;
import org.mm.core.TransformationRuleSet;
import org.mm.core.TransformationRuleSetFactory;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class MMApplicationFactory {

   private static final String DEFAULT_VALUE = "";

   private final Properties properties = new Properties();

   public MMApplicationFactory() {
      // NO-OP
   }

   protected MMApplicationFactory addProperty(@Nonnull String propertyName, @Nonnull String value) {
      checkNotNull(propertyName);
      checkNotNull(value);
      properties.setProperty(propertyName, value);
      return this;
   }

   @Nonnull
   public String getWorkbookFileLocation() {
      return properties.getProperty(Environment.WORKBOOK_SOURCE, DEFAULT_VALUE);
   }

   public void setWorkbookFileLocation(@Nonnull String path) {
      checkNotNull(path);
      properties.setProperty(Environment.WORKBOOK_SOURCE, path);
   }

   @Nonnull
   public String getOntologyFileLocation() {
      return properties.getProperty(Environment.ONTOLOGY_SOURCE, DEFAULT_VALUE);
   }

   public void setOntologyFileLocation(@Nonnull String path) {
      checkNotNull(path);
      properties.setProperty(Environment.ONTOLOGY_SOURCE, path);
   }

   @Nonnull
   public String getRuleFileLocation() {
      return properties.getProperty(Environment.TRANSFORMATION_RULES_SOURCE, DEFAULT_VALUE);
   }

   public void setRuleFileLocation(@Nonnull String path) {
      checkNotNull(path);
      properties.setProperty(Environment.TRANSFORMATION_RULES_SOURCE, path);
   }

   @Nonnull
   public Properties getProperties() {
      return properties;
   }

   @Nonnull
   public MMApplication createApplication() throws Exception {
      Properties copy = new Properties();
      copy.putAll(properties);
      validate(copy);
      Resources resources = buildResources(copy);
      return new MMApplication(resources.getOWLOntologySource(),
            resources.getSpreadSheetDataSource(),
            resources.getTransformationRuleSet());
   }

   private Resources buildResources(Properties properties) throws Exception {
      Resources resources = new Resources();

      String ontologySourceLocation = properties.getProperty(Environment.ONTOLOGY_SOURCE);
      OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(
            new FileInputStream(ontologySourceLocation));
      OWLOntologySource ontologySource = new OWLAPIOntology(ontology);
      resources.setOWLOntologySource(ontologySource);

      String workbookSourceLocation = properties.getProperty(Environment.WORKBOOK_SOURCE);
      SpreadSheetDataSource datasource = SpreadsheetFactory.loadWorkbookFromDocument(workbookSourceLocation);
      resources.setSpreadSheetDataSource(datasource);

      String ruleSourceLocation = properties.getProperty(Environment.TRANSFORMATION_RULES_SOURCE);
      TransformationRuleSet rules = TransformationRuleSetFactory.loadTransformationRulesFromDocument(ruleSourceLocation);
      resources.setTransformationRuleSet(rules);
      return resources;
   }

   private void validate(Properties properties) {
      if (properties.getProperty(Environment.ONTOLOGY_SOURCE) == null) {
         throw new ApplicationStartupException("Missing ontology source parameter");
      }
      if (properties.getProperty(Environment.WORKBOOK_SOURCE) == null) {
         throw new ApplicationStartupException("Missing workbook source parameter");
      }
   }

   class Resources {

      private SpreadSheetDataSource spreadsheet;
      private OWLOntologySource ontology;
      private TransformationRuleSet ruleSet;

      public SpreadSheetDataSource getSpreadSheetDataSource() {
         return spreadsheet;
      }

      public void setSpreadSheetDataSource(SpreadSheetDataSource spreadsheet) {
         this.spreadsheet = spreadsheet;
      }

      public OWLOntologySource getOWLOntologySource() {
         return ontology;
      }

      public void setOWLOntologySource(OWLOntologySource ontology) {
         this.ontology = ontology;
      }

      public TransformationRuleSet getTransformationRuleSet() {
         return ruleSet;
      }

      public void setTransformationRuleSet(TransformationRuleSet ruleSet) {
         this.ruleSet = ruleSet;
      }
   }
}

package org.mm.app;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileInputStream;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.mm.core.OWLAPIOntology;
import org.mm.core.OWLOntologySource;
import org.mm.transformationrule.TransformationRuleSet;
import org.mm.transformationrule.TransformationRuleSetManager;
import org.mm.workbook.WorkbookLoader;
import org.mm.workbook.Workbook;
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
      return new MMApplication(resources.getOntology(),
            resources.getWorkbook(),
            resources.getTransformationRules());
   }

   private Resources buildResources(Properties properties) throws Exception {

      String ontologySourceLocation = properties.getProperty(Environment.ONTOLOGY_SOURCE);
      OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(
            new FileInputStream(ontologySourceLocation));
      OWLOntologySource ontologySource = new OWLAPIOntology(ontology);

      String workbookLocation = properties.getProperty(Environment.WORKBOOK_SOURCE);
      Workbook workbook = WorkbookLoader.loadWorkbook(new FileInputStream(workbookLocation));

      String ruleLocation = properties.getProperty(Environment.TRANSFORMATION_RULES_SOURCE);
      TransformationRuleSet ruleSet = TransformationRuleSetManager.loadTransformationRulesFromDocument(
            new FileInputStream(ruleLocation));

      return new Resources(ontologySource, workbook, ruleSet);
   }

   private void validate(Properties properties) {
      if (properties.getProperty(Environment.ONTOLOGY_SOURCE) == null) {
         throw new ApplicationStartupException("Missing ontology source parameter");
      }
      if (properties.getProperty(Environment.WORKBOOK_SOURCE) == null) {
         throw new ApplicationStartupException("Missing workbook source parameter");
      }
   }

   private class Resources {

      private final OWLOntologySource ontology;
      private final Workbook workbook;
      private final TransformationRuleSet ruleSet;

      public Resources(@Nonnull OWLOntologySource ontology, @Nonnull Workbook workbook,
            @Nonnull TransformationRuleSet ruleSet) {
         this.ontology = checkNotNull(ontology);
         this.workbook = checkNotNull(workbook);
         this.ruleSet = checkNotNull(ruleSet);
      }

      @Nonnull
      public OWLOntologySource getOntology() {
         return ontology;
      }

      @Nonnull
      public Workbook getWorkbook() {
         return workbook;
      }

      @Nonnull
      public TransformationRuleSet getTransformationRules() {
         return ruleSet;
      }
   }
}

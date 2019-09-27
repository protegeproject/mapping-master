package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import java.util.Set;
import javax.annotation.Nonnull;
import org.mm.renderer.AbstractRendererTest;
import org.mm.renderer.RenderingContext;
import org.mm.renderer.Sheet;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class AbstractOwlRendererTest extends AbstractRendererTest {

   private static final String ONTOLOGY_ID = "http://protege.stanford.edu/mapping-master/test/";

   private OWLOntologyManager ontologyManager;
   private OWLDataFactory dataFactory;
   private OWLOntology ontology;

   protected void createEmptyOWLOntology() {
      try {
         ontologyManager = OWLManager.createOWLOntologyManager();
         dataFactory = ontologyManager.getOWLDataFactory();
         ontology = ontologyManager.createOntology(IRI.create(ONTOLOGY_ID));
         declareDefaultOwl2Datatypes(ontology);
         declareDefaultOwl2Annotations(ontology);
      } catch(OWLOntologyCreationException e) {
         throw new RuntimeException(e);
      }
   }

   private void declareDefaultOwl2Datatypes(OWLOntology ontology) {
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.RDFS_LITERAL));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.RDF_PLAINLITERAL));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_STRING));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_BOOLEAN));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_DOUBLE));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_FLOAT));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_LONG));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_INTEGER));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_SHORT));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_BYTE));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_DECIMAL));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_DATETIME));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_TIME));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_DATE));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.XSD_DURATION));
   }

   private void declareDefaultOwl2Annotations(OWLOntology ontology) {
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.OWL_VERSION_INFO));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.RDFS_COMMENT));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.RDFS_LABEL));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.RDFS_SEE_ALSO));
      ontologyManager.addAxiom(ontology, Declaration(Vocabulary.RDFS_IS_DEFINED_BY));
   }

   protected OWLOntology getOntology() {
      return ontology;
   }

   protected Set<OWLAxiom> evaluate(@Nonnull String ruleString) {
      checkNotNull(ruleString);
      OwlRenderer owlRenderer = new OwlRenderer();
      return owlRenderer.render(ruleString, getWorkbook(), getDefaultRenderingContext(), getEntityResolver());
   }

   private RenderingContext getDefaultRenderingContext() {
      final Sheet sheet = getWorkbook().getSheet(0);
      return new RenderingContext(
            sheet.getSheetName(),
            sheet.getStartColumnIndex(),
            sheet.getEndColumnIndex(),
            sheet.getStartRowIndex(),
            sheet.getEndRowIndex());
   }

   private OwlEntityResolver getEntityResolver() {
      return new OwlEntityResolverImpl(ontology);
   }

   protected void setPrefix(String prefixName, String prefix) {
      OWLDocumentFormat format = ontologyManager.getOntologyFormat(ontology);
      PrefixManager prefixManager = (PrefixManager) format.asPrefixOWLOntologyFormat();
      prefixManager.setPrefix(prefixName, prefix);
   }

   protected void declareEntity(OWLClass cls) {
      OWLDeclarationAxiom classDeclarationxiom = dataFactory.getOWLDeclarationAxiom(cls);
      ontologyManager.addAxiom(ontology, classDeclarationxiom);
   }

   protected void declareEntity(OWLNamedIndividual individual) {
      OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(individual);
      ontologyManager.addAxiom(ontology, axiom);
   }

   protected void declareEntity(OWLObjectProperty property) {
      OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(property);
      ontologyManager.addAxiom(ontology, axiom);
   }

   protected void declareEntity(OWLDataProperty property) {
      OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(property);
      ontologyManager.addAxiom(ontology, axiom);
   }

   protected void declareEntity(OWLAnnotationProperty property) {
      OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(property);
      ontologyManager.addAxiom(ontology, axiom);
   }

   protected void declareEntity(OWLDatatype datatype) {
      OWLDataFactory dataFactory = ontologyManager.getOWLDataFactory();
      OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(datatype);
      ontologyManager.addAxiom(ontology, axiom);
   }
}

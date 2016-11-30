package org.mm.renderer.owl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.poi.ss.usermodel.Sheet;
import org.mm.renderer.AbstractRendererTest;
import org.mm.renderer.RenderingContext;
import org.mm.renderer.internal.ReferenceResolver;
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
public abstract class OwlRendererTest extends AbstractRendererTest {

   private static final String ONTOLOGY_ID = "http://protege.stanford.edu/mapping-master/test/";

   private OWLOntologyManager ontologyManager;
   private OWLDataFactory dataFactory;
   private OWLOntology ontology;

   protected void createEmptyOWLOntology() {
      try {
         ontologyManager = OWLManager.createOWLOntologyManager();
         dataFactory = ontologyManager.getOWLDataFactory();
         ontology = ontologyManager.createOntology(IRI.create(ONTOLOGY_ID));
      } catch(OWLOntologyCreationException e) {
         throw new RuntimeException(e);
      }
   }

   protected OWLOntology getOntology() {
      return ontology;
   }

   protected Set<OWLAxiom> evaluate(@Nonnull String ruleString) {
      checkNotNull(ruleString);
      final OwlEntityResolver entityResolver = new OwlEntityResolverImpl(ontology);
      OwlRenderer owlRenderer = new OwlRenderer(
            new ReferenceResolver(getWorkbook()),
            new OwlFactory(entityResolver));
      return owlRenderer.render(ruleString, getDefaultRenderingContext());
   }

   private RenderingContext getDefaultRenderingContext() {
      final Sheet sheet = getWorkbook().getSheetAt(0);
      return new RenderingContext(
            sheet.getSheetName(),
            sheet.getRow(0).getFirstCellNum(),
            sheet.getRow(0).getLastCellNum(),
            sheet.getFirstRowNum(),
            sheet.getLastRowNum());
   }

   protected void setPrefix(OWLOntology ontology, String prefixName, String prefix) {
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

package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.vocab.Namespaces;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class CellReferenceTest extends AbstractOwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldRenderNameValue_InClassDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderIriValue_InClassDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(IRI)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderNameValue_InIndividualDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "fred");
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.FRED)));
   }

   @Test
   public void shouldRenderIriValue_InIndividualDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/fred");
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1(IRI)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.FRED)));
   }

   @Test
   public void shouldRenderNameValue_InSubClassDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "Student");
      createCell("Sheet1", 2, 1, "Person");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1 SubClassOf: @B1");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(
            Declaration(Vocabulary.STUDENT),
            SubClassOf(Vocabulary.STUDENT, Vocabulary.PERSON)));
   }

   @Test
   public void shouldRenderIriValue_InSubClassDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/Student");
      createCell("Sheet1", 2, 1, "http://protege.stanford.edu/mapping-master/test/Person");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(IRI) SubClassOf: @B1(IRI)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(
            Declaration(Vocabulary.STUDENT),
            SubClassOf(Vocabulary.STUDENT, Vocabulary.PERSON)));
   }

   @Test
   public void shouldRenderNameValue_InClassExpression() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) some @C1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectSomeValuesFrom(Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }

   @Test
   public void shouldRenderIriValue_InClassExpression() {
      // Arrange
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/Car");
      createCell("Sheet1", 2, 1, "http://protege.stanford.edu/mapping-master/test/hasEngine");
      createCell("Sheet1", 3, 1, "http://protege.stanford.edu/mapping-master/test/Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:entityIRI) SubClassOf: @B1(ObjectProperty mm:entityIRI) some @C1(mm:entityIRI)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectSomeValuesFrom(Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }

   @Test
   public void shouldRenderNameValue_InPropertyAssertions() {
      // Arrange
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "hasAge");
      createCell("Sheet1", 3, 1, "25");
      createCell("Sheet1", 4, 1, "hasParent");
      createCell("Sheet1", 5, 1, "bob");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 "
            + "Facts: @B1 @C1(xsd:integer), @D1(ObjectProperty) @E1");
      // Assert
      assertThat(results, hasSize(3));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            DataPropertyAssertion(Vocabulary.HAS_AGE, Vocabulary.FRED, Literal("25", Vocabulary.XSD_INTEGER)),
            ObjectPropertyAssertion(Vocabulary.HAS_PARENT, Vocabulary.FRED, Vocabulary.BOB)));
   }

   @Test
   public void shouldRenderIriValue_InPropertyAssertions() {
      // Arrange
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/fred");
      createCell("Sheet1", 2, 1, "http://protege.stanford.edu/mapping-master/test/hasAge");
      createCell("Sheet1", 3, 1, "25");
      createCell("Sheet1", 4, 1, "http://protege.stanford.edu/mapping-master/test/hasParent");
      createCell("Sheet1", 5, 1, "http://protege.stanford.edu/mapping-master/test/bob");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1(IRI) "
            + "Facts: @B1(mm:entityIRI) @C1(xsd:integer), @D1(ObjectProperty mm:entityIRI) @E1(IRI)");
      // Assert
      assertThat(results, hasSize(3));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            DataPropertyAssertion(Vocabulary.HAS_AGE, Vocabulary.FRED, Literal("25", Vocabulary.XSD_INTEGER)),
            ObjectPropertyAssertion(Vocabulary.HAS_PARENT, Vocabulary.FRED, Vocabulary.BOB)));
   }

   @Test
   public void shouldRenderNameValue_InPropertyAnnotation() {
      // Arrange
      setPrefix("foaf", Namespaces.FOAF.toString());
      String text = "https://upload.wikimedia.org/wikipedia/en/a/ad/Fred_Flintstone.png";
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "foaf:depiction");
      createCell("Sheet1", 3, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Annotations: @B1 @C1(IRI)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            AnnotationAssertion(Vocabulary.FOAF_DEPICTION, Vocabulary.FRED.getIRI(),
                  IRI(text))));
   }

   @Test
   public void shouldRenderIriValue_InPropertyAnnotation() {
      // Arrange
      String text = "https://upload.wikimedia.org/wikipedia/en/a/ad/Fred_Flintstone.png";
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/fred");
      createCell("Sheet1", 2, 1, "http://xmlns.com/foaf/0.1/depiction");
      createCell("Sheet1", 3, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1(IRI) Annotations: @B1(IRI) @C1(IRI)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            AnnotationAssertion(Vocabulary.FOAF_DEPICTION, Vocabulary.FRED.getIRI(),
                  IRI(text))));
   }
}

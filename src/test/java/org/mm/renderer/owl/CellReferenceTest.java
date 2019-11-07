package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

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
   public void shouldRenderNameLabel_InClassDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderIriLabel_InClassDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(IRI)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderNameLabel_InIndividualDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "fred");
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.FRED)));
   }

   @Test
   public void shouldRenderIriLabel_InIndividualDeclaration() {
      // Arrange
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/fred");
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1(IRI)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.FRED)));
   }

   @Test
   public void shouldRenderNameLabel_InSubClassDeclaration() {
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
   public void shouldRenderIriLabel_InSubClassDeclaration() {
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
   public void shouldRenderNameLabel_InClassExpression() {
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
   public void shouldRenderIriLabel_InClassExpression() {
      // Arrange
      createCell("Sheet1", 1, 1, "http://protege.stanford.edu/mapping-master/test/Car");
      createCell("Sheet1", 2, 1, "http://protege.stanford.edu/mapping-master/test/hasEngine");
      createCell("Sheet1", 3, 1, "http://protege.stanford.edu/mapping-master/test/Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(IRI) SubClassOf: @B1(ObjectProperty IRI) some @C1(IRI)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectSomeValuesFrom(Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }
}

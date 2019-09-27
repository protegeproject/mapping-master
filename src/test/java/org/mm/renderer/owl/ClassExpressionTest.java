package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ClassExpressionTest extends AbstractOwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldRenderObjectExactCardinality() {
      // Arrange
      declareEntity(Vocabulary.HAS_ENGINE);
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasEngine exactly 1 @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectExactCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderDataExactCardinality() {
      // Arrange
      declareEntity(Vocabulary.HAS_ENGINE);
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasEngine exactly 1 xsd:integer");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectExactCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderObjectMinCardinality() {
      // Arrange
      declareEntity(Vocabulary.HAS_ENGINE);
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasEngine min 1 @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectMinCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderObjectMaxCardinality() {
      // Arrange
      declareEntity(Vocabulary.HAS_ENGINE);
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasEngine max 1 @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectMaxCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderObjectHasValue() {
      // Arrange
      declareEntity(Vocabulary.HAS_ENGINE);
      createCell("Sheet1", 1, 1, "BMW");
      createCell("Sheet1", 2, 1, "bmw-motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasEngine value @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.BMW),
            SubClassOf(ObjectHasValue(Vocabulary.HAS_ENGINE, Vocabulary.BMW_MOTOR), Vocabulary.BMW)));
   }

   @Test
   public void shouldRenderObjectSomeValues() {
      // Arrange
      declareEntity(Vocabulary.HAS_ENGINE);
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasEngine some @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectSomeValuesFrom(Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderObjectAllValues() {
      // Arrange
      declareEntity(Vocabulary.HAS_ENGINE);
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasEngine only @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectAllValuesFrom(Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }
}

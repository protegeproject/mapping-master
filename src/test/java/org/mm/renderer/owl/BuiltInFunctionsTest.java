package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class BuiltInFunctionsTest extends AbstractOwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldTransformTextToLowerCase() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:toLowerCase)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.CAR_LOWERCASE)));
   }

   @Test
   public void shouldTransformTextToUpperCase() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:toUpperCase)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.CAR_UPPERCASE)));
   }

   @Test
   public void shouldReverseLetterByLetter() {
      // Arrange
      createCell("Sheet1", 1, 1, "raC");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:reverse)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldTrimWhitespaces() {
      // Arrange
      createCell("Sheet1", 1, 1, "   Car   ");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:trim)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   public void shouldAppendText_Prefix() {
      // Arrange
      createCell("Sheet1", 1, 1, "Barbara's Puffins Honey");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:printf(\"%s-RiceCereal-10.5ozBox\"))");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.BARBARA_PUFFINS_CAMELCASE)));
   }

   @Test
   public void shouldAppendText_Suffix() {
      // Arrange
      createCell("Sheet1", 1, 1, "10.5oz Box");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:printf(\"Barbara'sPuffinsHoney-RiceCereal-%s\"))");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.BARBARA_PUFFINS_CAMELCASE)));
   }

   @Test
   public void shouldAppendText_Infix() {
      // Arrange
      createCell("Sheet1", 1, 1, "Rice Cereal");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:printf(\"Barbara'sPuffinsHoney-%s-10.5ozBox\"))");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.BARBARA_PUFFINS_CAMELCASE)));
   }

   @Test
   public void shouldApplyDecimalFormat() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, "23000.2");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasValue @B1(mm:decimalFormat(\"###,###.00\") xsd:decimal)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.P1),
            DataPropertyAssertion(Vocabulary.HAS_VALUE, Vocabulary.P1,
                  Literal("23,000.20", Vocabulary.XSD_DECIMAL))));
   }

   @Test
   public void shouldReplaceSubstring() {
      // Arrange
      declareEntity(Vocabulary.HAS_NAME);
      String text = "Barbara's Puffins Honey - Rice Cereal - 10.5 oz Box";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasName @B1(mm:replace(\"bar\", \"\"))");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.P1),
            DataPropertyAssertion(Vocabulary.HAS_NAME, Vocabulary.P1,
                  Literal("Bara's Puffins Honey - Rice Cereal - 10.5 oz Box", Vocabulary.XSD_STRING))));
   }

   @Test
   public void shouldReplaceFirstSubstring() {
      // Arrange
      declareEntity(Vocabulary.HAS_NAME);
      String text = "Barbara's Puffins Honey - Rice Cereal - 10.5 oz Box";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasName @B1(mm:replaceFirst(\"(?i)bar\", \"\"))");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.P1),
            DataPropertyAssertion(Vocabulary.HAS_NAME, Vocabulary.P1,
                  Literal("bara's Puffins Honey - Rice Cereal - 10.5 oz Box", Vocabulary.XSD_STRING))));
   }

   @Test
   public void shouldReplaceAllSubstring() {
      // Arrange
      declareEntity(Vocabulary.HAS_NAME);
      String text = "Barbara's Puffins Honey - Rice Cereal - 10.5 oz Box";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasName @B1(mm:replaceAll(\"(?i)bar\", \"\"))");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.P1),
            DataPropertyAssertion(Vocabulary.HAS_NAME, Vocabulary.P1,
                  Literal("a's Puffins Honey - Rice Cereal - 10.5 oz Box", Vocabulary.XSD_STRING))));
   }

   @Test
   public void shouldCaptureSubstring() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String text = "Barbara's Puffins Honey - Rice Cereal - 10.5 oz Box";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasValue @B1(mm:capturing(\"([0-9]+.[0-9]+)\") xsd:decimal)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.P1),
            DataPropertyAssertion(Vocabulary.HAS_VALUE, Vocabulary.P1,
                  Literal("10.5", Vocabulary.XSD_DECIMAL))));
   }

   @Test
   public void shouldCaptureSubstring_UseSyntacticSugar() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String text = "Barbara's Puffins Honey - Rice Cereal - 10.5 oz Box";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasValue @B1([\"([0-9]+.[0-9]+)\"] xsd:decimal)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.P1),
            DataPropertyAssertion(Vocabulary.HAS_VALUE, Vocabulary.P1,
                  Literal("10.5", Vocabulary.XSD_DECIMAL))));
   }
}

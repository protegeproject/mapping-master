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
public class DatatypeDirectiveTest extends AbstractOwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldApplyDatatypeString() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "text";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:string)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_STRING))));
   }

   @Test
   public void shouldApplyDatatypeDecimal() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "100.0";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:decimal)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_DECIMAL))));
   }

   @Test
   public void shouldApplyDatatypeByte() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "100";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:byte)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_BYTE))));
   }

   @Test
   public void shouldApplyDatatypeShort() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "100";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:short)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_SHORT))));
   }

   @Test
   public void shouldApplyDatatypeInteger() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "100";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:integer)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_INTEGER))));
   }

   @Test
   public void shouldApplyDatatypeLong() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "100";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:long)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_LONG))));
   }

   @Test
   public void shouldApplyDatatypeFloat() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "3.14";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:float)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_FLOAT))));
   }

   @Test
   public void shouldApplyDatatypeDouble() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "3.14";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:double)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_DOUBLE))));
   }

   @Test
   public void shouldApplyDatatypeBoolean() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "true";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:boolean)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_BOOLEAN))));
   }

   @Test
   public void shouldApplyDatatypeTime() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "03:14:15";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:time)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_TIME))));
   }

   @Test
   public void shouldApplyDatatypeDate() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "2017-01-19";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:date)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_DATE))));
   }

   @Test
   public void shouldApplyDatatypeDateTime() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "2017-01-19T03:14:15";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:dateTime)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_DATETIME))));
   }

   @Test
   public void shouldApplyDatatypeDuration() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "P5Y2M10D";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xsd:duration)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.P1), DataPropertyAssertion(
            Vocabulary.HAS_VALUE, Vocabulary.P1, Literal(value, Vocabulary.XSD_DURATION))));
   }

   @Test
   public void shouldApplyDatatypePlainLiteral() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "text";
      createCell("Sheet1", 1, 1, "p1");
      createCell("Sheet1", 2, 1, value);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(rdf:plainLiteral)");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result,
            containsInAnyOrder(Declaration(Vocabulary.P1),
                  DataPropertyAssertion(Vocabulary.HAS_VALUE, Vocabulary.P1,
                        Literal(value, Vocabulary.RDF_PLAINLITERAL))));
   }
}

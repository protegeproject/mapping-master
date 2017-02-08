package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.NamedIndividual;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceDirectivesTest extends OwlRendererTest {

   private static final String EX_PREFIX = "http://example.org/prefix#";

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyExcelWorkbook();
      createEmptyOWLOntology();
      setPrefix("ex", EX_PREFIX);
   }

   @Test
   public void shouldAppendUserPrefix_InClassDeclaration() {
      // Arrange
      String text = "Person";
      addCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\")");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Class(IRI(EX_PREFIX + text)))));
   }

   @Test
   public void shouldAppendUserPrefix_InIndividualDeclaration() {
      // Arrange
      String text = "wilma";
      addCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1(mm:Prefix=\"ex\")");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(NamedIndividual(IRI(EX_PREFIX + text)))));
   }

   @Test
   public void shouldAppendUserNamespace_InClassDeclaration() {
      // Arrange
      String text = "Person";
      addCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Namespace=\"http://example.org/prefix#\")");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Class(IRI(EX_PREFIX + text)))));
   }

   @Test
   public void shouldAppendUserNamespace_InIndividualDeclaration() {
      // Arrange
      String text = "wilma";
      addCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate(
            "Individual: @A1(mm:Namespace=\"http://example.org/prefix#\")");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(NamedIndividual(IRI(EX_PREFIX + text)))));
   }

   @Test
   public void shouldAppendLanguage_InAnnotationAssertion() {
      // Arrange
      String label1 = "Auto";
      String label2 = "Mobil";
      addCell("Sheet1", 1, 1, "Car");
      addCell("Sheet1", 2, 1, label1);
      addCell("Sheet1", 3, 1, label2);
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1 "
            + "Annotations: rdfs:label @B1(xml:lang=\"de\"), rdfs:label @C1(xml:lang=\"id\")");
      // Assert
      assertThat(result, hasSize(3));
      assertThat(result,
            containsInAnyOrder(Declaration(Vocabulary.CAR),
                  AnnotationAssertion(Vocabulary.RDFS_LABEL, Vocabulary.CAR.getIRI(),
                        Literal(label1, "de")),
            AnnotationAssertion(Vocabulary.RDFS_LABEL, Vocabulary.CAR.getIRI(),
                  Literal(label2, "id"))));
   }

   @Test
   public void shouldApplyDatatypeString() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String value = "text";
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, value);
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

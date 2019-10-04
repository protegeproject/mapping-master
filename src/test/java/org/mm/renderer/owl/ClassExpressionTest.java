package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataAllValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataHasValue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataMaxCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataMinCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectAllValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectHasValue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectMaxCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectMinCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;
import java.util.Set;
import org.junit.Before;
import org.junit.Ignore;
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
   public void shouldRenderObjectExactCardinality_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) exactly @C1 @D1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectExactCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderDataExactCardinality() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasAlias exactly 1 xsd:string");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataExactCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING), Vocabulary.PERSON)));
   }

   @Test
   public void shouldRenderDataExactCardinality_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "hasAlias");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "xsd:string");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1 exactly @C1 @D1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataExactCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING), Vocabulary.PERSON)));
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
   public void shouldRenderObjectMinCardinality_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) min @C1 @D1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectMinCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderDatatMinCardinality() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasAlias min 1 xsd:string");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataMinCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING), Vocabulary.PERSON)));
   }

   @Test
   public void shouldRenderDatatMinCardinality_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "hasAlias");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "xsd:string");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1 min @C1 @D1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataMinCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING), Vocabulary.PERSON)));
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
   public void shouldRenderObjectMaxCardinality_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) max @C1 @D1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectMaxCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderDatatMaxCardinality() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasAlias max 1 xsd:string");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataMaxCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING), Vocabulary.PERSON)));
   }

   @Test
   public void shouldRenderDataMaxCardinality_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "hasAlias");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "xsd:string");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1 max @C1 @D1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataMaxCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING), Vocabulary.PERSON)));
   }

   @Test
   public void shouldRenderObjectHasValue() {
      // Arrange
      declareEntity(Vocabulary.HAS_ENGINE);
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "bmw-motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasEngine value @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectHasValue(Vocabulary.HAS_ENGINE, Vocabulary.BMW_MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderObjectHasValue_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "bmw-motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) value @C1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(ObjectHasValue(Vocabulary.HAS_ENGINE, Vocabulary.BMW_MOTOR), Vocabulary.CAR)));
   }

   @Test
   public void shouldRenderDataHasValue() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "Nick");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasAlias value @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataHasValue(Vocabulary.HAS_ALIAS, Vocabulary.NICK), Vocabulary.PERSON)));
   }

   @Test
   public void shouldRenderDataHasValue_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "hasAlias");
      createCell("Sheet1", 3, 1, "Nick");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1 value @C1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataHasValue(Vocabulary.HAS_ALIAS, Vocabulary.NICK), Vocabulary.PERSON)));
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
   public void shouldRenderDataSomeValues() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "Nick");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasAlias some xsd:string");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataSomeValuesFrom(Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING), Vocabulary.PERSON)));
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

   @Test
   public void shouldRenderDataAllValues() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "Nick");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasAlias only xsd:string");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(DataAllValuesFrom(Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING), Vocabulary.PERSON)));
   }
}

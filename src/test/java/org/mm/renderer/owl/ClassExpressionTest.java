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
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectAllValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectHasValue;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectIntersectionOf;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectMaxCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectMinCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectSomeValuesFrom;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectUnionOf;
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
            SubClassOf(Vocabulary.CAR, ObjectExactCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
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
            SubClassOf(Vocabulary.CAR, ObjectExactCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }

   @Test
   public void shouldRenderObjectExactCardinality_UsingClassExpression() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "useFuel");
      createCell("Sheet1", 4, 2, "Gasoline");
      createCell("Sheet1", 4, 3, "Diesel");
      createCell("Sheet1", 5, 1, "useElectricity");
      createCell("Sheet1", 5, 2, "Battery");
      createCell("Sheet1", 6, 1, "numberOfCylinder");
      createCell("Sheet1", 6, 2, "6");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) exactly @C1 "
            + "((((@D1(ObjectProperty) some (@D2 or @D3) or @E1(ObjectProperty) some @E2)) and @F1 value @F2(xsd:integer)))");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectExactCardinality(1, Vocabulary.HAS_ENGINE, 
                  ObjectIntersectionOf(ObjectUnionOf(
                        ObjectSomeValuesFrom(Vocabulary.USE_FUEL, ObjectUnionOf(Vocabulary.GASOLINE, Vocabulary.DIESEL)),
                        ObjectSomeValuesFrom(Vocabulary.USE_ELECTRICITY, Vocabulary.BATTERY)),
                  DataHasValue(Vocabulary.NUMBER_OF_CYLINDER, Literal(6)))))));
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
            SubClassOf(Vocabulary.PERSON, DataExactCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
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
            SubClassOf(Vocabulary.PERSON, DataExactCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
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
            SubClassOf(Vocabulary.CAR, ObjectMinCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
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
            SubClassOf(Vocabulary.CAR, ObjectMinCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }

   @Test
   public void shouldRenderObjectMinCardinality_UsingClassExpression() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "useFuel");
      createCell("Sheet1", 4, 2, "Gasoline");
      createCell("Sheet1", 4, 3, "Diesel");
      createCell("Sheet1", 5, 1, "useElectricity");
      createCell("Sheet1", 5, 2, "Battery");
      createCell("Sheet1", 6, 1, "numberOfCylinder");
      createCell("Sheet1", 6, 2, "6");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) min @C1 "
            + "((((@D1(ObjectProperty) some (@D2 or @D3) or @E1(ObjectProperty) some @E2)) and @F1 value @F2(xsd:integer)))");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectMinCardinality(1, Vocabulary.HAS_ENGINE, 
                  ObjectIntersectionOf(ObjectUnionOf(
                        ObjectSomeValuesFrom(Vocabulary.USE_FUEL, ObjectUnionOf(Vocabulary.GASOLINE, Vocabulary.DIESEL)),
                        ObjectSomeValuesFrom(Vocabulary.USE_ELECTRICITY, Vocabulary.BATTERY)),
                  DataHasValue(Vocabulary.NUMBER_OF_CYLINDER, Literal(6)))))));
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
            SubClassOf(Vocabulary.PERSON, DataMinCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
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
            SubClassOf(Vocabulary.PERSON, DataMinCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
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
            SubClassOf(Vocabulary.CAR, ObjectMaxCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
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
            SubClassOf(Vocabulary.CAR, ObjectMaxCardinality(1, Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }

   @Test
   public void shouldRenderObjectMaxCardinality_UsingClassExpression() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "useFuel");
      createCell("Sheet1", 4, 2, "Gasoline");
      createCell("Sheet1", 4, 3, "Diesel");
      createCell("Sheet1", 5, 1, "useElectricity");
      createCell("Sheet1", 5, 2, "Battery");
      createCell("Sheet1", 6, 1, "numberOfCylinder");
      createCell("Sheet1", 6, 2, "6");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) max @C1 "
            + "((((@D1(ObjectProperty) some (@D2 or @D3) or @E1(ObjectProperty) some @E2)) and @F1 value @F2(xsd:integer)))");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectMaxCardinality(1, Vocabulary.HAS_ENGINE, 
                  ObjectIntersectionOf(ObjectUnionOf(
                        ObjectSomeValuesFrom(Vocabulary.USE_FUEL, ObjectUnionOf(Vocabulary.GASOLINE, Vocabulary.DIESEL)),
                        ObjectSomeValuesFrom(Vocabulary.USE_ELECTRICITY, Vocabulary.BATTERY)),
                  DataHasValue(Vocabulary.NUMBER_OF_CYLINDER, Literal(6)))))));
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
            SubClassOf(Vocabulary.PERSON, DataMaxCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
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
            SubClassOf(Vocabulary.PERSON, DataMaxCardinality(1, Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
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
            SubClassOf(Vocabulary.CAR, ObjectHasValue(Vocabulary.HAS_ENGINE, Vocabulary.BMW_MOTOR))));
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
            SubClassOf(Vocabulary.CAR, ObjectHasValue(Vocabulary.HAS_ENGINE, Vocabulary.BMW_MOTOR))));
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
            SubClassOf(Vocabulary.PERSON, DataHasValue(Vocabulary.HAS_ALIAS, Vocabulary.NICK))));
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
            SubClassOf(Vocabulary.PERSON, DataHasValue(Vocabulary.HAS_ALIAS, Vocabulary.NICK))));
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
            SubClassOf(Vocabulary.CAR, ObjectSomeValuesFrom(Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }

   @Test
   public void shouldRenderObjectSomeValues_UsingReferences() {
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
   public void shouldRenderObjectSomeValues_UsingClassExpression() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "useFuel");
      createCell("Sheet1", 4, 2, "Gasoline");
      createCell("Sheet1", 4, 3, "Diesel");
      createCell("Sheet1", 5, 1, "useElectricity");
      createCell("Sheet1", 5, 2, "Battery");
      createCell("Sheet1", 6, 1, "numberOfCylinder");
      createCell("Sheet1", 6, 2, "6");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) some "
            + "((((@D1(ObjectProperty) some (@D2 or @D3) or @E1(ObjectProperty) some @E2)) and @F1 value @F2(xsd:integer)))");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectSomeValuesFrom(Vocabulary.HAS_ENGINE, 
                  ObjectIntersectionOf(ObjectUnionOf(
                        ObjectSomeValuesFrom(Vocabulary.USE_FUEL, ObjectUnionOf(Vocabulary.GASOLINE, Vocabulary.DIESEL)),
                        ObjectSomeValuesFrom(Vocabulary.USE_ELECTRICITY, Vocabulary.BATTERY)),
                  DataHasValue(Vocabulary.NUMBER_OF_CYLINDER, Literal(6)))))));
   }

   @Test
   public void shouldRenderDataSomeValues() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasAlias some xsd:string");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(Vocabulary.PERSON, DataSomeValuesFrom(Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
   }

   @Test
   public void shouldRenderDataSomeValues_UsingReferences() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "hasAlias");
      createCell("Sheet1", 3, 1, "xsd:string");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1 some @C1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(Vocabulary.PERSON, DataSomeValuesFrom(Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
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
            SubClassOf(Vocabulary.CAR, ObjectAllValuesFrom(Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }

   @Test
   public void shouldRenderObjectAllValues_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "Motor");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) only @C1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectAllValuesFrom(Vocabulary.HAS_ENGINE, Vocabulary.MOTOR))));
   }

   @Test
   public void shouldRenderObjectAllValues_UsingClassExpression() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      createCell("Sheet1", 2, 1, "hasEngine");
      createCell("Sheet1", 3, 1, "1");
      createCell("Sheet1", 4, 1, "useFuel");
      createCell("Sheet1", 4, 2, "Gasoline");
      createCell("Sheet1", 4, 3, "Diesel");
      createCell("Sheet1", 5, 1, "useElectricity");
      createCell("Sheet1", 5, 2, "Battery");
      createCell("Sheet1", 6, 1, "numberOfCylinder");
      createCell("Sheet1", 6, 2, "6");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1(ObjectProperty) only "
            + "((((@D1(ObjectProperty) some (@D2 or @D3) or @E1(ObjectProperty) some @E2)) and @F1 value @F2(xsd:integer)))");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.CAR),
            SubClassOf(Vocabulary.CAR, ObjectAllValuesFrom(Vocabulary.HAS_ENGINE, 
                  ObjectIntersectionOf(ObjectUnionOf(
                        ObjectSomeValuesFrom(Vocabulary.USE_FUEL, ObjectUnionOf(Vocabulary.GASOLINE, Vocabulary.DIESEL)),
                        ObjectSomeValuesFrom(Vocabulary.USE_ELECTRICITY, Vocabulary.BATTERY)),
                  DataHasValue(Vocabulary.NUMBER_OF_CYLINDER, Literal(6)))))));
   }

   @Test
   public void shouldRenderDataAllValues() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: hasAlias only xsd:string");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(Vocabulary.PERSON, DataAllValuesFrom(Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
   }

   @Test
   public void shouldRenderDataAllValues_UsingReferences() {
      // Arrange
      declareEntity(Vocabulary.HAS_ALIAS);
      createCell("Sheet1", 1, 1, "Person");
      createCell("Sheet1", 2, 1, "hasAlias");
      createCell("Sheet1", 3, 1, "xsd:string");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1 SubClassOf: @B1 only @C1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(Vocabulary.PERSON, DataAllValuesFrom(Vocabulary.HAS_ALIAS, Vocabulary.XSD_STRING))));
   }
}

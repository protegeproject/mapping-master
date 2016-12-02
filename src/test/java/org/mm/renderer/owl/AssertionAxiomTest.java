package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ClassAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectPropertyAssertion;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class AssertionAxiomTest extends OwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyExcelWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shoudRenderClassAssertion() {
      // Arrange
      declareEntity(Vocabulary.PERSON);
      addCell("Sheet1", 1, 1, "fred");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Types: Person");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ClassAssertion(Vocabulary.PERSON, Vocabulary.FRED)));
   }

   @Test
   public void shoudRenderClassAssertion_Multiple() {
      // Arrange
      declareEntity(Vocabulary.PERSON);
      declareEntity(Vocabulary.STUDENT);
      declareEntity(Vocabulary.CAR_OWNER);
      addCell("Sheet1", 1, 1, "fred");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Types: Person, Student, CarOwner");
      // Assert
      assertThat(results, hasSize(4));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ClassAssertion(Vocabulary.PERSON, Vocabulary.FRED),
            ClassAssertion(Vocabulary.STUDENT, Vocabulary.FRED),
            ClassAssertion(Vocabulary.CAR_OWNER, Vocabulary.FRED)));
   }

   @Test
   public void shoudRenderDataPropertyAssertion() {
      // Arrange
      declareEntity(Vocabulary.HAS_AGE);
      addCell("Sheet1", 1, 1, "fred");
      addCell("Sheet1", 2, 1, "25");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasAge @B1(xsd:integer)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            DataPropertyAssertion(Vocabulary.HAS_AGE, Vocabulary.FRED,
                  Literal("25", Vocabulary.XSD_INTEGER))));
   }

   @Test
   public void shoudRenderObjectPropertyAssertion() {
      // Arrange
      declareEntity(Vocabulary.HAS_PARENT);
      addCell("Sheet1", 1, 1, "fred");
      addCell("Sheet1", 2, 1, "bob");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasParent @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ObjectPropertyAssertion(Vocabulary.HAS_PARENT, Vocabulary.FRED, Vocabulary.BOB)));
   }

   @Test
   public void shoudRenderDataPropertyAssertion_ColumnHeaderAsProperty() {
      // Arrange
      addCell("Sheet1", 1, 2, "fred");
      addCell("Sheet1", 2, 1, "hasAge");
      addCell("Sheet1", 2, 2, "25");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A2 Facts: @B1 @B2(xsd:integer)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            DataPropertyAssertion(Vocabulary.HAS_AGE, Vocabulary.FRED,
                  Literal("25", Vocabulary.XSD_INTEGER))));
   }

   @Test
   public void shoudRenderObjectPropertyAssertion_ColumnHeaderAsProperty() {
      // Arrange
      addCell("Sheet1", 1, 2, "fred");
      addCell("Sheet1", 2, 1, "hasParent");
      addCell("Sheet1", 2, 2, "bob");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A2 Facts: @B1(ObjectProperty) @B2");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ObjectPropertyAssertion(Vocabulary.HAS_PARENT, Vocabulary.FRED, Vocabulary.BOB)));
   }
}
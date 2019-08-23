package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;

import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class AbsentEntityDirectiveTest extends AbstractOwlRendererTest {

   @Rule
   public final ExpectedException thrown = ExpectedException.none();

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldUseDefaultResponse() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   @Ignore("Unsupported by the API")
   public void shouldIgnoreIfEntityIsAbsent() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:ignoreIfEntityAbsent)");
      // Assert
      assertThat(result, hasSize(0));
   }

   @Test
   public void shouldCreateIfEntityIsAbsent() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:createIfEntityAbsent)");
      // Assert
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.CAR)));
   }

   @Test
   @Ignore("Unsupported by the API")
   public void shouldWarnIfEntityIsAbsent() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:warningIfEntityAbsent)");
      // Assert
      assertThat(result, hasSize(0));
   }

   @Test
   @Ignore("Unsupported by the API")
   public void shouldFailIfEntityIsAbsent() {
      // Arrange
      createCell("Sheet1", 1, 1, "Car");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:errorIfEntityAbsent)");
      // Assert
      assertThat(result, hasSize(0));
   }
}

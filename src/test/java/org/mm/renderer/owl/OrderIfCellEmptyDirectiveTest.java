package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mm.renderer.exception.EmptyCellException;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class OrderIfCellEmptyDirectiveTest extends OwlRendererTest {

   @Rule
   public final ExpectedException thrown = ExpectedException.none();

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyExcelWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldUseDefaultOrder() {
      // Arrange
      addCell("Sheet1", 1, 1, "");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1");
      // Assert
      assertThat(result, hasSize(0));
   }

   @Test
   public void shouldIgnoreIfCellIsEmpty() {
      // Arrange
      addCell("Sheet1", 1, 1, "");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:ignoreIfCellEmpty)");
      // Assert
      assertThat(result, hasSize(0));
   }

   @Test
   public void shouldCreateIfCellIsEmpty() {
      // Arrange
      addCell("Sheet1", 1, 1, "");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:createIfCellEmpty)");
      // Assert
      assertThat(result, hasSize(1));
   }

   @Test
   public void shouldWarnIfCellIsEmpty() {
      // Arrange
      addCell("Sheet1", 1, 1, "");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:warningIfCellEmpty)");
      // Assert
      assertThat(result, hasSize(0));
   }

   @Test
   public void shouldFailIfCellIsEmpty() {
      thrown.expect(EmptyCellException.class);
      // Arrange
      addCell("Sheet1", 1, 1, "");
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:errorIfCellEmpty)");
      // Assert
      assertThat(result, hasSize(0));
   }
}

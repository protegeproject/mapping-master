package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
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
public class IriEncodingDirectiveTest extends OwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldUseDefaultEncoding() throws Exception {
      // Arrange
      String text = "Barbara's Puffins Honey-Rice Cereal - 10.5oz box";
      createCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.BARBARA_PUFFINS_CAMELCASE)));
   }

   @Test
   public void shouldUseCamelCaseEncoding() throws Exception {
      // Arrange
      String text = "Barbara's Puffins Honey-Rice Cereal - 10.5oz box";
      createCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:camelCaseEncode)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.BARBARA_PUFFINS_CAMELCASE)));
   }

   @Test
   public void shouldUseSnakeCaseEncoding() throws Exception {
      // Arrange
      String text = "Barbara's Puffins Honey-Rice Cereal - 10.5oz box";
      createCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:snakeCaseEncode)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.BARBARA_PUFFINS_SNAKECASE)));
   }

   @Test
   public void shouldUseUuidEncoding() throws Exception {
      // Arrange
      String text = "Barbara's Puffins Honey-Rice Cereal - 10.5oz box";
      createCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:uuidEncode)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.BARBARA_PUFFINS_UUID)));
   }

   @Test
   public void shouldUseHashEncoding() throws Exception {
      // Arrange
      String text = "Barbara's Puffins Honey-Rice Cereal - 10.5oz box";
      createCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:hashEncode)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.BARBARA_PUFFINS_MD5)));
   }
}

package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;

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
public class EmptyCellDirectiveTest extends AbstractOwlRendererTest {

   @Rule
   public final ExpectedException thrown = ExpectedException.none();

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldIgnoreIfCellIsEmpty_DefaultSetting() {
      Set<OWLAxiom> result = evaluate("Class: @A1");
      assertThat(result, hasSize(0));
   }

   @Test
   public void shouldIgnoreIfCellIsEmpty() {
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:ignoreIfCellEmpty)");
      assertThat(result, hasSize(0));
   }

   @Test
   public void shouldCreateIfCellIsEmpty() {
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:createIfCellEmpty)");
      assertThat(result, hasSize(1));
      assertThat(result, containsInAnyOrder(Declaration(Vocabulary.SHEET1_A1_UUID)));
   }

   @Test
   public void shouldWarnIfCellIsEmpty() {
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:warningIfCellEmpty)");
      assertThat(result, hasSize(0));
   }

   @Test
   public void shouldFailIfCellIsEmpty() {
      thrown.expect(EmptyCellException.class);
      Set<OWLAxiom> result = evaluate("Class: @A1(mm:errorIfCellEmpty)");
      assertThat(result, hasSize(0));
   }
}

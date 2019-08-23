package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
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
public class PrefixDirectiveTest extends AbstractOwlRendererTest {

   private static final String EX_PREFIX = "http://example.org/prefix#";

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
      setPrefix("ex", EX_PREFIX);
   }

   @Test
   public void shouldAppendUserPrefix_InClassDeclaration() {
      // Arrange
      String text = "Person";
      createCell("Sheet1", 1, 1, text);
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
      createCell("Sheet1", 1, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1(mm:Prefix=\"ex\")");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(NamedIndividual(IRI(EX_PREFIX + text)))));
   }
}

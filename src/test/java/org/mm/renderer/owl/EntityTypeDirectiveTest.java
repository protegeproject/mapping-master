package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataExactCardinality;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
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
public class EntityTypeDirectiveTest extends AbstractOwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldSupportDatatypeType() {
      // Arrange
      declareEntity(Vocabulary.PERSON);
      declareEntity(Vocabulary.HAS_NAME);
      createCell("Sheet1", 1, 1, "xsd:string");
      // Act
      Set<OWLAxiom> results = evaluate("Class: Person SubClassOf: hasName exactly 1 @A1(Datatype)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(
            Declaration(Vocabulary.PERSON),
            SubClassOf(Vocabulary.PERSON, DataExactCardinality(1, Vocabulary.HAS_NAME, Vocabulary.XSD_STRING))));
   }
}

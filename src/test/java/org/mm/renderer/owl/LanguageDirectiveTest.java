package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class LanguageDirectiveTest extends OwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyExcelWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldAppendLanguage_InAnnotationAssertion() {
      // Arrange
      String label1 = "Auto";
      addCell("Sheet1", 1, 1, "Car");
      addCell("Sheet1", 2, 1, label1);
      // Act
      Set<OWLAxiom> result = evaluate("Class: @A1 Annotations: rdfs:label @B1(xml:lang=\"de\")");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result,
            containsInAnyOrder(Declaration(Vocabulary.CAR),
                  AnnotationAssertion(Vocabulary.RDFS_LABEL, Vocabulary.CAR.getIRI(),
                        Literal(label1, "de"))));
   }

   @Test
   public void shouldAppendLanguage_InDataPropertyAssertion() {
      // Arrange
      declareEntity(Vocabulary.HAS_VALUE);
      String label1 = "Auto";
      addCell("Sheet1", 1, 1, "p1");
      addCell("Sheet1", 2, 1, label1);
      // Act
      Set<OWLAxiom> result = evaluate("Individual: @A1 Facts: hasValue @B1(xml:lang=\"de\")");
      // Assert
      assertThat(result, hasSize(2));
      assertThat(result,
            containsInAnyOrder(Declaration(Vocabulary.P1),
                  DataPropertyAssertion(Vocabulary.HAS_VALUE, Vocabulary.P1,
                        Literal(label1, "de"))));
   }

   @Test
   public void shouldAppendLanguage_MultipleEntries() {
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
}

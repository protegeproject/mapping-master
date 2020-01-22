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

public class PrefixFunctionsTest extends AbstractOwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldAddPrefix() {
      // Arrange
      setPrefix("ex", Vocabulary.EX_NAMESPACE);
      createCell("Sheet1", 1, 1, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\")");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.EX_CAR_OWNER)));
   }

   @Test
   public void shouldUppercaseEntityName() {
      // Arrange
      setPrefix("ex", Vocabulary.EX_NAMESPACE);
      createCell("Sheet1", 1, 1, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\" mm:toUpperCase)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.EX_CAR_OWNER_UPPERCASE)));
   }

   @Test
   public void shouldLowercaseEntityName() {
      // Arrange
      setPrefix("ex", Vocabulary.EX_NAMESPACE);
      createCell("Sheet1", 1, 1, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\" mm:toLowerCase)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.EX_CAR_OWNER_LOWERCASE)));
   }

   @Test
   public void shouldReverseEntityName() {
      // Arrange
      setPrefix("ex", Vocabulary.EX_NAMESPACE);
      createCell("Sheet1", 1, 1, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\" mm:reverse)");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.EX_CAR_OWNER_REVERSE)));
   }

   @Test
   public void shouldReplaceEntityName() {
      // Arrange
      setPrefix("ex", Vocabulary.EX_NAMESPACE);
      createCell("Sheet1", 1, 1, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\" mm:replace(\"Car\", \"Bar\"))");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.EX_CAR_OWNER_REPLACE)));
   }

   @Test
   public void shouldReplaceFirstEntityName() {
      // Arrange
      setPrefix("ex", Vocabulary.EX_NAMESPACE);
      createCell("Sheet1", 1, 1, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\" mm:replaceFirst(\"r\", \"l\"))");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.EX_CAR_OWNER_REPLACE_FIRST)));
   }

   @Test
   public void shouldFormatPrintEntityName() {
      // Arrange
      setPrefix("ex", Vocabulary.EX_NAMESPACE);
      createCell("Sheet1", 1, 1, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\" mm:printf(\"New%s\"))");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.EX_CAR_OWNER_PRINTF)));
   }

   @Test
   public void shouldCaptureEntityName() {
      // Arrange
      setPrefix("ex", Vocabulary.EX_NAMESPACE);
      createCell("Sheet1", 1, 1, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Class: @A1(mm:Prefix=\"ex\" mm:capturing(\"(^.{0,3})\"))");
      // Assert
      assertThat(results, hasSize(1));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.EX_CAR_OWNER_CAPTURE)));
   }
}

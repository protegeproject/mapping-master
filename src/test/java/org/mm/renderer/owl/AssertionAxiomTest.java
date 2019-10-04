package org.mm.renderer.owl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ClassAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.DataPropertyAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Declaration;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectPropertyAssertion;
import java.util.Set;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.vocab.Namespaces;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class AssertionAxiomTest extends AbstractOwlRendererTest {

   @Before
   public void setUp() throws OWLOntologyCreationException {
      createEmptyWorkbook();
      createEmptyOWLOntology();
   }

   @Test
   public void shouldRenderClassAssertion() {
      // Arrange
      declareEntity(Vocabulary.PERSON);
      createCell("Sheet1", 1, 1, "fred");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Types: Person");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ClassAssertion(Vocabulary.PERSON, Vocabulary.FRED)));
   }

   @Test
   public void shouldRenderClassAssertion_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "Person");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Types: @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ClassAssertion(Vocabulary.PERSON, Vocabulary.FRED)));
   }

   @Test
   public void shouldRenderClassAssertion_MultipleTypes() {
      // Arrange
      declareEntity(Vocabulary.PERSON);
      declareEntity(Vocabulary.STUDENT);
      declareEntity(Vocabulary.CAR_OWNER);
      createCell("Sheet1", 1, 1, "fred");
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
   public void shouldRenderClassAssertion_MultipleTypes_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "Person");
      createCell("Sheet1", 2, 2, "Student");
      createCell("Sheet1", 2, 3, "CarOwner");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Types: @B1, @B2, @B3");
      // Assert
      assertThat(results, hasSize(4));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ClassAssertion(Vocabulary.PERSON, Vocabulary.FRED),
            ClassAssertion(Vocabulary.STUDENT, Vocabulary.FRED),
            ClassAssertion(Vocabulary.CAR_OWNER, Vocabulary.FRED)));
   }

   @Test
   public void shouldRenderDataPropertyAssertion() {
      // Arrange
      declareEntity(Vocabulary.HAS_AGE);
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "25");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasAge @B1(xsd:integer)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            DataPropertyAssertion(Vocabulary.HAS_AGE, Vocabulary.FRED,
                  Literal("25", Vocabulary.XSD_INTEGER))));
   }

   @Test
   public void shouldRenderDataPropertyAssertion_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "hasAge");
      createCell("Sheet1", 3, 1, "25");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: @B1 @C1(xsd:integer)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            DataPropertyAssertion(Vocabulary.HAS_AGE, Vocabulary.FRED,
                  Literal("25", Vocabulary.XSD_INTEGER))));
   }

   @Test
   public void shouldRenderObjectPropertyAssertion() {
      // Arrange
      declareEntity(Vocabulary.HAS_PARENT);
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "bob");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: hasParent @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ObjectPropertyAssertion(Vocabulary.HAS_PARENT, Vocabulary.FRED, Vocabulary.BOB)));
   }

   @Test
   public void shouldRenderObjectPropertyAssertion_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "hasParent");
      createCell("Sheet1", 3, 1, "bob");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Facts: @B1(ObjectProperty) @C1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            ObjectPropertyAssertion(Vocabulary.HAS_PARENT, Vocabulary.FRED, Vocabulary.BOB)));
   }

   @Test
   public void shouldRenderPropertyAssertion_MultipleProperties() {
      // Arrange
      declareEntity(Vocabulary.HAS_AGE);
      declareEntity(Vocabulary.HAS_PARENT);
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "25");
      createCell("Sheet1", 3, 1, "bob");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 "
            + "Facts: hasAge @B1(xsd:integer), hasParent @C1");
      // Assert
      assertThat(results, hasSize(3));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            DataPropertyAssertion(Vocabulary.HAS_AGE, Vocabulary.FRED, Literal("25", Vocabulary.XSD_INTEGER)),
            ObjectPropertyAssertion(Vocabulary.HAS_PARENT, Vocabulary.FRED, Vocabulary.BOB)));
   }

   @Test
   public void shouldRenderPropertyAssertion_MultipleProperties_UsingReferences() {
      // Arrange
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "hasAge");
      createCell("Sheet1", 3, 1, "25");
      createCell("Sheet1", 4, 1, "hasParent");
      createCell("Sheet1", 5, 1, "bob");
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 "
            + "Facts: @B1 @C1(xsd:integer), @D1(ObjectProperty) @E1");
      // Assert
      assertThat(results, hasSize(3));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            DataPropertyAssertion(Vocabulary.HAS_AGE, Vocabulary.FRED, Literal("25", Vocabulary.XSD_INTEGER)),
            ObjectPropertyAssertion(Vocabulary.HAS_PARENT, Vocabulary.FRED, Vocabulary.BOB)));
   }

   @Test
   public void shouldRenderAnnotationPropertyAssertion() {
      // Arrange
      declareEntity(Vocabulary.COMMENT);
      String text = "Fred works for Stanford";
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Annotations: comment @B1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            AnnotationAssertion(Vocabulary.COMMENT, Vocabulary.FRED.getIRI(),
                  Literal(text, Vocabulary.XSD_STRING))));
   }

   @Test
   public void shouldRenderAnnotationPropertyAssertion_UsingReferences() {
      // Arrange
      String text = "Fred works for Stanford";
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "comment");
      createCell("Sheet1", 3, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Annotations: @B1 @C1");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            AnnotationAssertion(Vocabulary.COMMENT, Vocabulary.FRED.getIRI(),
                  Literal(text, Vocabulary.XSD_STRING))));
   }

   @Test
   public void shouldRenderAnnotationPropertyAssertion_IriValue() {
      // Arrange
      setPrefix("foaf", Namespaces.FOAF.toString());
      declareEntity(Vocabulary.FOAF_DEPICTION);
      String text = "https://upload.wikimedia.org/wikipedia/en/a/ad/Fred_Flintstone.png";
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Annotations: foaf:depiction @B1(IRI)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            AnnotationAssertion(Vocabulary.FOAF_DEPICTION, Vocabulary.FRED.getIRI(),
                  IRI(text))));
   }

   @Test
   public void shouldRenderAnnotationPropertyAssertion_IriValue_UsingReferences() {
      // Arrange
      setPrefix("foaf", Namespaces.FOAF.toString());
      String text = "https://upload.wikimedia.org/wikipedia/en/a/ad/Fred_Flintstone.png";
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "foaf:depiction");
      createCell("Sheet1", 3, 1, text);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 Annotations: @B1 @C1(IRI)");
      // Assert
      assertThat(results, hasSize(2));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            AnnotationAssertion(Vocabulary.FOAF_DEPICTION, Vocabulary.FRED.getIRI(),
                  IRI(text))));
   }

   @Test
   public void shoudRenderAnnotationPropertyAssertion_MultipleAnnotations() {
      // Arrange
      String label = "Alfred";
      String vCard = "http://example.org/people/fred.json";
      String comment = "Fred works for Stanford";
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, label);
      createCell("Sheet1", 3, 1, vCard);
      createCell("Sheet1", 4, 1, comment);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 "
            + "Annotations: rdfs:label @B1, rdfs:seeAlso @C1(IRI), rdfs:comment @D1");
      // Assert
      assertThat(results, hasSize(4));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            AnnotationAssertion(Vocabulary.RDFS_LABEL, Vocabulary.FRED.getIRI(),
                  Literal(label, Vocabulary.XSD_STRING)),
            AnnotationAssertion(Vocabulary.RDFS_SEE_ALSO, Vocabulary.FRED.getIRI(),
                  IRI(vCard)),
            AnnotationAssertion(Vocabulary.RDFS_COMMENT, Vocabulary.FRED.getIRI(),
                  Literal(comment, Vocabulary.XSD_STRING))));
   }

   @Test
   public void shoudRenderAnnotationPropertyAssertion_MultipleAnnotations_UsingReferences() {
      // Arrange
      String label = "Alfred";
      String vCard = "http://example.org/people/fred.json";
      String comment = "Fred works for Stanford";
      createCell("Sheet1", 1, 1, "fred");
      createCell("Sheet1", 2, 1, "rdfs:label");
      createCell("Sheet1", 2, 2, label);
      createCell("Sheet1", 3, 1, "rdfs:seeAlso");
      createCell("Sheet1", 3, 2, vCard);
      createCell("Sheet1", 4, 1, "rdfs:comment");
      createCell("Sheet1", 4, 2, comment);
      // Act
      Set<OWLAxiom> results = evaluate("Individual: @A1 "
            + "Annotations: @B1 @B2, @C1 @C2(IRI), @D1 @D2");
      // Assert
      assertThat(results, hasSize(4));
      assertThat(results, containsInAnyOrder(Declaration(Vocabulary.FRED),
            AnnotationAssertion(Vocabulary.RDFS_LABEL, Vocabulary.FRED.getIRI(),
                  Literal(label, Vocabulary.XSD_STRING)),
            AnnotationAssertion(Vocabulary.RDFS_SEE_ALSO, Vocabulary.FRED.getIRI(),
                  IRI(vCard)),
            AnnotationAssertion(Vocabulary.RDFS_COMMENT, Vocabulary.FRED.getIRI(),
                  Literal(comment, Vocabulary.XSD_STRING))));
   }
}

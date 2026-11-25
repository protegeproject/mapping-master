package org.mm;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.mm.core.OWLAPIOntology;
import org.mm.core.settings.ReferenceSettings;
import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.node.ExpressionNode;
import org.mm.renderer.owlapi.OWLRenderer;
import org.mm.rendering.owlapi.OWLRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetFactory;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.Assert.*;

public class OWLApi5TransformationTest {
    private static final String BASE = "https://example.org/cellfie#";

    @Test public void importsXlsxAndRoundTripsOntology() throws Exception {
        transformWorkbook(new XSSFWorkbook(), ".xlsx");
    }

    @Test public void importsLegacyXlsAndRoundTripsOntology() throws Exception {
        transformWorkbook(new HSSFWorkbook(), ".xls");
    }

    private void transformWorkbook(Workbook workbook, String extension) throws Exception {
        Path source = Files.createTempFile("mapping-master-", extension);
        Path result = Files.createTempFile("mapping-master-", ".ttl");
        try {
            try (Workbook input = workbook; OutputStream out = Files.newOutputStream(source)) {
                input.createSheet("Classes").createRow(0).createCell(0).setCellValue("Child");
                input.getSheetAt(0).getRow(0).createCell(1).setCellValue("Enfant français 中文");
                input.write(out);
            }
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology ontology = manager.createOntology(IRI.create(BASE));
            TurtleDocumentFormat format = new TurtleDocumentFormat();
            format.setDefaultPrefix(BASE);
            format.setPrefix("ex:", BASE);
            manager.setOntologyFormat(ontology, format);
            OWLDataFactory df = manager.getOWLDataFactory();
            OWLClass parent = df.getOWLClass(IRI.create(BASE + "Parent"));
            manager.addAxiom(ontology, df.getOWLDeclarationAxiom(parent));

            try (Workbook loaded = SpreadsheetFactory.loadWorkbookFromDocument(source.toString())) {
                SpreadSheetDataSource data = new SpreadSheetDataSource(loaded);
                data.setCurrentLocation(new SpreadsheetLocation("Classes", 1, 1));
                OWLRenderer renderer = new OWLRenderer(new OWLAPIOntology(ontology), data);
                Set<OWLAxiom> preview = render(renderer,
                        "Class: @A* SubClassOf: ex:Parent Annotations: rdfs:label @B*");
                OWLClass child = df.getOWLClass(IRI.create(BASE + "Child"));
                assertTrue(preview.contains(df.getOWLDeclarationAxiom(child)));
                assertTrue(preview.contains(df.getOWLSubClassOfAxiom(child, parent)));
                assertTrue(preview.contains(df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(),
                        child.getIRI(), df.getOWLLiteral("Enfant français 中文"))));
                // Rendering a preview must not apply the generated axioms.
                assertFalse(ontology.containsAxiom(df.getOWLSubClassOfAxiom(child, parent)));
                manager.addAxioms(ontology, preview);
                assertTrue(ontology.containsAxiom(df.getOWLSubClassOfAxiom(child, parent)));
                manager.removeAxioms(ontology, preview);
                assertFalse(ontology.containsAxiom(df.getOWLSubClassOfAxiom(child, parent)));
                manager.addAxioms(ontology, preview);
                manager.saveOntology(ontology, format, IRI.create(result.toUri()));
                OWLOntology reopened = OWLManager.createOWLOntologyManager()
                        .loadOntologyFromOntologyDocument(result.toFile());
                assertEquals(ontology.getAxioms(), reopened.getAxioms());
            }
        } finally {
            Files.deleteIfExists(source);
            Files.deleteIfExists(result);
        }
    }

    @Test(expected = ParseException.class)
    public void rejectsInvalidRule() throws Exception {
        parse("Class: (");
    }

    private Set<OWLAxiom> render(OWLRenderer renderer, String rule) throws Exception {
        OWLRendering rendering = renderer.render(new ExpressionNode(parse(rule)).getMMExpressionNode())
                .orElseThrow(() -> new AssertionError("No rendering generated"));
        return rendering.getOWLAxioms();
    }

    private ASTExpression parse(String rule) throws ParseException {
        return (ASTExpression) new MappingMasterParser(
                new ByteArrayInputStream(rule.getBytes(StandardCharsets.UTF_8)),
                new ReferenceSettings(), -1).expression();
    }
}

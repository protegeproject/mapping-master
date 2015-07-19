package org.mm.test;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.SimpleNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.renderer.owlapi.OWLAPIClassExpressionRenderer;
import org.mm.renderer.owlapi.OWLAPICoreRenderer;
import org.mm.renderer.owlapi.OWLAPIEntityRenderer;
import org.mm.renderer.owlapi.OWLAPILiteralRenderer;
import org.mm.renderer.owlapi.OWLAPIRendering;
import org.mm.renderer.owlapi.XXXReferenceRenderer;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class OWLAPIRendererTest
{
	public static void main(String args[])
	{

		try {
			String expressionText = "Class: @A3";
			MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expressionText.getBytes()));
			OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = ontologyManager.createOntology();
			File file = File.createTempFile("temp", "xlsx");
			WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
			WritableSheet sheet = writableWorkbook.createSheet("s1", 0);
			Label label0 = new Label(0, 0, "Derw");
			sheet.addCell(label0);
			writableWorkbook.write();
			writableWorkbook.close();

			Workbook workbook = Workbook.getWorkbook(file);

			SpreadSheetDataSource dataSource = new SpreadSheetDataSource(workbook);

			OWLAPIEntityRenderer entityRenderer = new OWLAPIEntityRenderer();
			OWLAPILiteralRenderer literalRenderer = new OWLAPILiteralRenderer(ontology.getOWLOntologyManager().getOWLDataFactory());
			XXXReferenceRenderer referenceRenderer = new XXXReferenceRenderer(ontology, dataSource, entityRenderer);
			OWLAPIClassExpressionRenderer classExpressionRenderer = new OWLAPIClassExpressionRenderer(ontology,
				entityRenderer, referenceRenderer, literalRenderer);
			OWLAPICoreRenderer coreRenderer = new OWLAPICoreRenderer(ontology, dataSource, entityRenderer,
					classExpressionRenderer, referenceRenderer);

			SimpleNode simpleNode = parser.expression();
			ExpressionNode expressionNode = new ExpressionNode((ASTExpression)simpleNode);

			simpleNode.dump(" ");

			if (expressionNode.hasMMExpression()) {
				System.err.println("expressionNode.toString()      : " + expressionNode.toString());
				Optional<OWLAPIRendering> rendering = coreRenderer.renderMMExpression(expressionNode.getMMExpressionNode());
				if (rendering.isPresent()) {
					Set<OWLAxiom> axioms = rendering.get().getOWLAxioms();
				}
			}

		} catch (MappingMasterException | ParseException | OWLOntologyCreationException | IOException | jxl.read.biff.BiffException | WriteException e) {
			System.err.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void Usage()
	{
		System.err.println("Usage: OWLAPIRendererTest <Expression>");
		System.exit(-1);
	}
}
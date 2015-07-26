package org.mm.test;

import jxl.Workbook;
import jxl.read.biff.BiffException;
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
import org.mm.parser.node.MMExpressionNode;
import org.mm.parser.node.OWLNamedIndividualNode;
import org.mm.renderer.owlapi.OWLAPICoreRenderer;
import org.mm.renderer.text.TextRenderer;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.rendering.text.TextRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class IntegrationTestBase
{
	protected static final String SHEET1 = "Sheet1";
	protected static final String SHEET2 = "Sheet2";
	protected static final String SHEET3 = "Sheet3";
	protected static final String DEFAULT_SHEET = SHEET1;
	protected static final Set<Label> EMPTY_CELL_SET = Collections.emptySet();
	protected static final SpreadsheetLocation DEFAULT_CURRENT_LOCATION = new SpreadsheetLocation(SHEET1, 1, 1);
	protected static final String DEFAULT_NAMESPACE = ":";

	protected final DefaultPrefixManager prefixManager;

	protected IntegrationTestBase()
	{
		this.prefixManager = new DefaultPrefixManager();
		prefixManager.setDefaultPrefix(DEFAULT_NAMESPACE);
	}

	protected OWLOntology createOWLOntology() throws OWLOntologyCreationException
	{
		return OWLManager.createOWLOntologyManager().createOntology();
	}

	/**
	 * Create a single sheet workbook. Not clear how to create an in-memory-only {@link Workbook} in JXL so
	 * we create a {@link WritableWorkbook} (which is not a subclass of {@link Workbook}) save it to a temporary file and
	 * then create a {@link Workbook} from it.
	 */
	protected Workbook createWorkbook(String sheetName, Set<Label> cells)
			throws IOException, WriteException, BiffException
	{
		File file = File.createTempFile("temp", "xlsx");
		WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);
		WritableSheet sheet = writableWorkbook.createSheet(sheetName, 0);

		for (Label cell : cells)
			sheet.addCell(cell);

		writableWorkbook.write();
		writableWorkbook.close();

		return Workbook.getWorkbook(file);
	}

	/**
	 * @param data Map of sheet names to cells
	 * @return A workbook
	 * @throws IOException
	 * @throws WriteException
	 * @throws BiffException
	 */
	protected Workbook createWorkbook(Map<String, Set<Label>> data) throws IOException, WriteException, BiffException
	{
		File file = File.createTempFile("temp", "xlsx");
		WritableWorkbook writableWorkbook = Workbook.createWorkbook(file);

		int sheetIndex = 0;
		for (String sheetName : data.keySet()) {
			Set<Label> cells = data.get(sheetName);
			WritableSheet sheet = writableWorkbook.createSheet(sheetName, sheetIndex++);

			for (Label cell : cells)
				sheet.addCell(cell);
		}

		writableWorkbook.write();
		writableWorkbook.close();

		return Workbook.getWorkbook(file);
	}

	protected SpreadSheetDataSource createSpreadsheetDataSource(String sheetName, Set<Label> cells)
			throws BiffException, IOException, WriteException, MappingMasterException
	{
		Workbook workbook = createWorkbook(sheetName, cells);
		return new SpreadSheetDataSource(workbook);
	}

	protected MMExpressionNode parseExpression(String expression) throws ParseException
	{
		MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expression.getBytes()));
		SimpleNode simpleNode = parser.expression();
		ExpressionNode expressionNode = new ExpressionNode((ASTExpression)simpleNode);

		return expressionNode.getMMExpressionNode();
	}

	protected Optional<? extends TextRendering> createTextRendering(String expression)
			throws WriteException, BiffException, MappingMasterException, IOException, ParseException
	{
		return createTextRendering(DEFAULT_SHEET, this.EMPTY_CELL_SET, expression);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, Set<Label> cells,
			SpreadsheetLocation currentLocation, String expression)
			throws WriteException, BiffException, MappingMasterException, IOException, ParseException
	{
		SpreadSheetDataSource dataSource = createSpreadsheetDataSource(sheetName, cells);

		dataSource.setCurrentLocation(currentLocation);

		TextRenderer renderer = new TextRenderer(dataSource);
		MMExpressionNode mmExpressionNode = parseExpression(expression);

		return renderer.renderMMExpression(mmExpressionNode);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, String expression)
			throws WriteException, BiffException, MappingMasterException, IOException, ParseException
	{
		return createTextRendering(sheetName, EMPTY_CELL_SET, expression);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, Set<Label> cells, String expression)
			throws WriteException, BiffException, MappingMasterException, IOException, ParseException
	{
		return createTextRendering(sheetName, cells, this.DEFAULT_CURRENT_LOCATION, expression);
	}

	protected Optional<? extends OWLAPIRendering> createOWLAPIRendering(OWLOntology ontology, String expression)
			throws WriteException, BiffException, MappingMasterException, IOException, ParseException
	{
		return createOWLAPIRendering(ontology, DEFAULT_SHEET, EMPTY_CELL_SET, this.DEFAULT_CURRENT_LOCATION, expression);
	}

	protected Optional<? extends OWLAPIRendering> createOWLAPIRendering(OWLOntology ontology, String sheetName,
			Set<Label> cells, String expression)
			throws WriteException, BiffException, MappingMasterException, IOException, ParseException
	{
		return createOWLAPIRendering(ontology, sheetName, cells, this.DEFAULT_CURRENT_LOCATION, expression);
	}

	protected Optional<? extends OWLAPIRendering> createOWLAPIRendering(OWLOntology ontology, String sheetName,
			Set<Label> cells, SpreadsheetLocation currentLocation, String expression)
			throws WriteException, BiffException, MappingMasterException, IOException, ParseException
	{
		SpreadSheetDataSource dataSource = createSpreadsheetDataSource(sheetName, cells);

		dataSource.setCurrentLocation(currentLocation);

		OWLAPICoreRenderer renderer = new OWLAPICoreRenderer(ontology, dataSource);
		MMExpressionNode mmExpressionNode = parseExpression(expression);

		return renderer.renderMMExpression(mmExpressionNode);
	}

	/**
	 * @param content      Content of the cell
	 * @param columnNumber 1-based column number
	 * @param rowNumber    1-based row number
	 * @return A cell
	 */
	protected Label createCell(String content, int columnNumber, int rowNumber)
	{
		return new Label(columnNumber - 1, rowNumber - 1, content); // JXL is 0-based
	}

	protected Set<Label> createCells(Label... cells)
	{
		Set<Label> cellSet = new HashSet<>();
		Collections.addAll(cellSet, cells);
		return cellSet;
	}

	protected void declareOWLClass(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLClass(iri);

		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLNamedIndividual(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLNamedIndividual(iri);

		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLObjectProperty(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLObjectProperty(iri);

		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLDataProperty(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLDataProperty(iri);

		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLAnnotationProperty(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLAnnotationProperty(iri);

		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLDatatype(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLDatatype(iri);

		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLClasses(OWLOntology ontology, String... shortNames)
	{
		for (String shortName : shortNames)
			declareOWLClass(ontology, shortName);
	}

	protected void declareOWLNamedIndividuals(OWLOntology ontology, String... shortNames)
	{
		for (String shortName : shortNames)
			declareOWLNamedIndividual(ontology, shortName);
	}

	protected void declareOWLObjectProperties(OWLOntology ontology, String... shortNames)
	{
		for (String shortName : shortNames)
			declareOWLObjectProperty(ontology, shortName);
	}

	protected void declareOWLDataProperties(OWLOntology ontology, String... shortNames)
	{
		for (String shortName : shortNames)
			declareOWLDataProperty(ontology, shortName);
	}

	protected void declareOWLAnnotationProperties(OWLOntology ontology, String... shortNames)
	{
		for (String shortName : shortNames)
			declareOWLAnnotationProperty(ontology, shortName);
	}

	protected void declareOWLDatatypes(OWLOntology ontology, String... shortNames)
	{
		for (String shortName : shortNames)
			declareOWLDatatype(ontology, shortName);
	}

	protected void addOWLAxioms(OWLOntology ontology, OWLAxiom... axioms)
	{
		OWLOntologyManager ontologyManager = ontology.getOWLOntologyManager();
		for (OWLAxiom axiom : axioms)
			ontologyManager.addAxiom(ontology, axiom);
	}
}

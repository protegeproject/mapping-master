package org.mm.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mm.core.settings.ReferenceSettings;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.ASTExpression;
import org.mm.parser.MappingMasterParser;
import org.mm.parser.ParseException;
import org.mm.parser.SimpleNode;
import org.mm.parser.node.ExpressionNode;
import org.mm.parser.node.MMExpressionNode;
import org.mm.renderer.owlapi.OWLAPIRenderer;
import org.mm.renderer.text.TextRenderer;
import org.mm.rendering.owlapi.OWLAPIRendering;
import org.mm.rendering.text.TextRendering;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetLocation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class IntegrationTestBase
{
	protected static final String SHEET1 = "Sheet1";
	protected static final String SHEET2 = "Sheet2";
	protected static final String SHEET3 = "Sheet3";
	protected static final String DEFAULT_SHEET = SHEET1;
	protected static final Set<Label> EMPTY_CELL_SET = Collections.emptySet();
	protected static final SpreadsheetLocation DEFAULT_CURRENT_LOCATION = new SpreadsheetLocation(SHEET1, 1, 1);
	protected static final String DEFAULT_PREFIX = ":";

	protected final DefaultPrefixManager prefixManager;

	protected IntegrationTestBase()
	{
		this.prefixManager = new DefaultPrefixManager();
		prefixManager.setDefaultPrefix(DEFAULT_PREFIX);
	}

	protected OWLOntology createOWLOntology() throws OWLOntologyCreationException
	{
		return OWLManager.createOWLOntologyManager().createOntology();
	}

	protected Workbook createWorkbook(String sheetName, Set<Label> cells) throws IOException
	{
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);
		
		Map<Integer, Row> buffer = new HashMap<Integer, Row>();
		for (Label cell : cells) {
			int rownum = cell.getRowIndex();
			Row row = buffer.get(rownum);
			if (row == null) {
				row = sheet.createRow(rownum);
				buffer.put(rownum, row);
			}
			row.createCell(cell.columnIndex).setCellValue(cell.getContent());
		}
		return workbook;
	}

	protected SpreadSheetDataSource createSpreadsheetDataSource(String sheetName, Set<Label> cells)
			throws IOException, MappingMasterException
	{
		Workbook workbook = createWorkbook(sheetName, cells);
		return new SpreadSheetDataSource(workbook);
	}

	protected MMExpressionNode parseExpression(String expression, ReferenceSettings settings) throws ParseException
	{
		MappingMasterParser parser = new MappingMasterParser(new ByteArrayInputStream(expression.getBytes()), settings, -1);
		SimpleNode simpleNode = parser.expression();
		ExpressionNode expressionNode = new ExpressionNode((ASTExpression)simpleNode);

		return expressionNode.getMMExpressionNode();
	}

	protected Optional<? extends TextRendering> createTextRendering(String expression, ReferenceSettings settings)
			throws MappingMasterException, IOException, ParseException
	{
		return createTextRendering(DEFAULT_SHEET, EMPTY_CELL_SET, expression, settings);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, Set<Label> cells,
			SpreadsheetLocation currentLocation, String expression, ReferenceSettings settings)
			throws MappingMasterException, IOException, ParseException
	{
		SpreadSheetDataSource dataSource = createSpreadsheetDataSource(sheetName, cells);

		dataSource.setCurrentLocation(currentLocation);

		TextRenderer renderer = new TextRenderer(dataSource);
		MMExpressionNode mmExpressionNode = parseExpression(expression, settings);

		return renderer.renderMMExpression(mmExpressionNode);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, String expression,
			ReferenceSettings settings) throws MappingMasterException, IOException, ParseException
	{
		return createTextRendering(sheetName, EMPTY_CELL_SET, expression, settings);
	}

	protected Optional<? extends TextRendering> createTextRendering(String sheetName, Set<Label> cells, String expression,
			ReferenceSettings settings) throws MappingMasterException, IOException, ParseException
	{
		return createTextRendering(sheetName, cells, DEFAULT_CURRENT_LOCATION, expression, settings);
	}

	protected Optional<? extends OWLAPIRendering> createOWLAPIRendering(OWLOntology ontology, String expression,
			ReferenceSettings settings) throws MappingMasterException, IOException, ParseException
	{
		return createOWLAPIRendering(ontology, DEFAULT_SHEET, EMPTY_CELL_SET, DEFAULT_CURRENT_LOCATION, expression, settings);
	}

	protected Optional<? extends OWLAPIRendering> createOWLAPIRendering(OWLOntology ontology, String sheetName,
			Set<Label> cells, String expression, ReferenceSettings settings)
			throws MappingMasterException, IOException, ParseException
	{
		return createOWLAPIRendering(ontology, sheetName, cells, DEFAULT_CURRENT_LOCATION, expression, settings);
	}

	protected Optional<? extends OWLAPIRendering> createOWLAPIRendering(OWLOntology ontology, String sheetName,
			Set<Label> cells, SpreadsheetLocation currentLocation, String expression, ReferenceSettings settings)
			throws MappingMasterException, IOException, ParseException
	{
		SpreadSheetDataSource dataSource = createSpreadsheetDataSource(sheetName, cells);

		dataSource.setCurrentLocation(currentLocation);

		OWLAPIRenderer renderer = new OWLAPIRenderer(ontology, dataSource);
		MMExpressionNode mmExpressionNode = parseExpression(expression, settings);

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
		return new Label(content, columnNumber-1, rowNumber-1); // POI is 0-based
	}

	protected Set<Label> createCells(Label... cells)
	{
		Set<Label> cellSet = new HashSet<>();
		Collections.addAll(cellSet, cells);
		return cellSet;
	}

	protected void createRDFSLabelAnnotationAxiom(OWLOntology ontology, String subject, String annotation)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI subjectIri = IRI.create(subject);
		OWLAnnotationProperty rdfsLabel = dataFactory.getRDFSLabel();
		OWLAnnotationValue value = dataFactory.getOWLLiteral(annotation);
		OWLAnnotationAssertionAxiom annotationAxiom = dataFactory.getOWLAnnotationAssertionAxiom(rdfsLabel, subjectIri, value);
		ontology.getOWLOntologyManager().addAxiom(ontology, annotationAxiom);
	}

	protected void declareOWLClass(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI classIri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLClass(classIri);
		
		OWLDeclarationAxiom classDeclarationxiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, classDeclarationxiom);
	}

	protected void declareOWLClass(OWLOntology ontology, String shortName, String labelAnnotation)
	{
		declareOWLClass(ontology, shortName);
		createRDFSLabelAnnotationAxiom(ontology, shortName, labelAnnotation);
	}

	protected void declareOWLNamedIndividual(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLNamedIndividual(iri);
		
		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLNamedIndividual(OWLOntology ontology, String shortName, String labelAnnotation)
	{
		declareOWLNamedIndividual(ontology, shortName);
		createRDFSLabelAnnotationAxiom(ontology, shortName, labelAnnotation);
	}

	protected void declareOWLObjectProperty(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLObjectProperty(iri);

		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLObjectProperty(OWLOntology ontology, String shortName, String labelAnnotation)
	{
		declareOWLObjectProperty(ontology, shortName);
		createRDFSLabelAnnotationAxiom(ontology, shortName, labelAnnotation);
	}

	protected void declareOWLDataProperty(OWLOntology ontology, String shortName)
	{
		OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
		IRI iri = IRI.create(shortName);
		OWLEntity entity = dataFactory.getOWLDataProperty(iri);

		OWLDeclarationAxiom axiom = dataFactory.getOWLDeclarationAxiom(entity);
		ontology.getOWLOntologyManager().addAxiom(ontology, axiom);
	}

	protected void declareOWLDataProperty(OWLOntology ontology, String shortName, String labelAnnotation)
	{
		declareOWLDataProperty(ontology, shortName);
		createRDFSLabelAnnotationAxiom(ontology, shortName, labelAnnotation);
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

	public class Label
	{
		private String content;
		private int columnIndex;
		private int rowIndex;
		
		public Label(String content, int columnIndex, int rowIndex)
		{
			this.content = content;
			this.columnIndex = columnIndex;
			this.rowIndex = rowIndex;
		}
		
		public String getContent()
		{
			return content;
		}
		
		public int getColumnIndex()
		{
			return columnIndex;
		}
		
		public int getRowIndex()
		{
			return rowIndex;
		}
	}
}

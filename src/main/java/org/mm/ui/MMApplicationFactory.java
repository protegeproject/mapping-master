package org.mm.ui;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Workbook;
import org.mm.core.MappingExpressionSet;
import org.mm.core.MappingExpressionSetFactory;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ss.SpreadsheetFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class MMApplicationFactory
{
	private Properties properties;

	public MMApplicationFactory()
	{
		properties = new Properties();
	}

	protected MMApplicationFactory addProperty(String propertyName, String value)
	{
		properties.setProperty(propertyName, value);
		return this;
	}

	public String getWorkbookLocation()
	{
		return properties.getProperty(Environment.WORKBOOK_SOURCE);
	}

	public void setWorkbookLocation(String path)
	{
		properties.setProperty(Environment.WORKBOOK_SOURCE, path);
	}

	public String getOntologyLocation()
	{
		return properties.getProperty(Environment.ONTOLOGY_SOURCE);
	}

	public void setOntologyLocation(String path)
	{
		properties.setProperty(Environment.ONTOLOGY_SOURCE, path);
	}

	public String getMappingLocation()
	{
		return properties.getProperty(Environment.MAPPING_SOURCE);
	}

	public void setMappingLocation(String path)
	{
		properties.setProperty(Environment.MAPPING_SOURCE, path);
	}

	public Properties getProperties()
	{
		return properties;
	}

	public MMApplication createApplication() throws Exception
	{
		Properties copy = new Properties();
		copy.putAll(properties);
		Resources resources = buildResources(copy);
		return new MMApplication(
				resources.getOntology(),
				resources.getSpreadSheetDataSource(),
				resources.getMappingExpressionSet());
	}

	private Resources buildResources(Properties properties) throws Exception
	{
		Resources resources = new Resources();
		
		String workbookLocation = properties.getProperty(Environment.WORKBOOK_SOURCE);
		Workbook workbook = SpreadsheetFactory.createEmptyWorkbook();
		if (workbookLocation != null) {
			workbook = SpreadsheetFactory.loadWorkbookFromDocument(workbookLocation);
		}
		SpreadSheetDataSource datasource = new SpreadSheetDataSource(workbook);
		resources.setSpreadSheetDataSource(datasource);
		
		String ontologyLocation = properties.getProperty(Environment.ONTOLOGY_SOURCE);
		OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = owlManager.createOntology();
		if (ontologyLocation != null) {
			ontology = owlManager.loadOntologyFromOntologyDocument(new FileInputStream(ontologyLocation));
		}
		resources.setOWLOntology(ontology);
		
		String mappingLocation = properties.getProperty(Environment.MAPPING_SOURCE);
		MappingExpressionSet mappings = MappingExpressionSetFactory.createEmptyMappingExpressionSet();
		if (mappingLocation != null) {
			mappings = MappingExpressionSetFactory.loadMapppingExpressionSetFromDocument(mappingLocation);
		}
		resources.setMappingExpressionSet(mappings);
		
		return resources;
	}

	class Resources
	{
		private SpreadSheetDataSource spreadsheet;
		private OWLOntology ontology;
		private MappingExpressionSet mappings;
		
		public SpreadSheetDataSource getSpreadSheetDataSource()
		{
			return spreadsheet;
		}
		
		public void setSpreadSheetDataSource(SpreadSheetDataSource spreadsheet)
		{
			this.spreadsheet = spreadsheet;
		}
		
		public OWLOntology getOntology()
		{
			return ontology;
		}
		
		public void setOWLOntology(OWLOntology ontology)
		{
			this.ontology = ontology;
		}
		
		public MappingExpressionSet getMappingExpressionSet()
		{
			return mappings;
		}
		
		public void setMappingExpressionSet(MappingExpressionSet mappings)
		{
			this.mappings = mappings;
		}
	}
}

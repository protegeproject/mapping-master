package org.mm.ui.model;

import org.mm.core.MappingExpressionSet;
import org.mm.core.MappingExpressionPersistenceLayer;
import org.mm.renderer.Renderer;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.OWLOntology;

public class ApplicationModel implements MMModel
{
	private DataSourceModel dataSourceModel;
	private MappingExpressionModel expressionMappingsModel;
	private MappingExpressionPersistenceLayer mappingExpressionsPersistenceLayer;

	private Renderer renderer;

	public ApplicationModel(OWLOntology ontology, SpreadSheetDataSource dataSource, MappingExpressionSet mappings)
	{
		dataSourceModel = new DataSourceModel(dataSource);
		expressionMappingsModel = new MappingExpressionModel(mappings);
	}

	public DataSourceModel getDataSourceModel()
	{
		return dataSourceModel;
	}

	public MappingExpressionModel getMappingExpressionsModel()
	{
		return expressionMappingsModel;
	}

	public MappingExpressionPersistenceLayer getMappingExpressionsPersistenceLayer()
	{
		return mappingExpressionsPersistenceLayer;
	}

	public Renderer getRenderer()
	{
		return renderer;
	}

	public void registerRenderer(Renderer renderer)
	{
		this.renderer = renderer;
	}
}

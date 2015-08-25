package org.mm.ui.model;

import org.mm.core.MappingExpressionSet;
import org.mm.core.MappingExpressionsPersistenceLayer;
import org.mm.renderer.Renderer;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.OWLOntology;

public class MMApplicationModel implements MMModel
{
	private DataSourceModel dataSourceModel;
	private MappingsExpressionsModel expressionMappingsModel;
	private MappingExpressionsPersistenceLayer mappingExpressionsPersistenceLayer;

	private Renderer renderer;

	public MMApplicationModel(OWLOntology ontology, SpreadSheetDataSource dataSource, MappingExpressionSet mappings)
	{
		dataSourceModel = new DataSourceModel(dataSource);
		expressionMappingsModel = new MappingsExpressionsModel(mappings);
	}

	public DataSourceModel getDataSourceModel()
	{
		return dataSourceModel;
	}

	public MappingsExpressionsModel getMappingExpressionsModel()
	{
		return expressionMappingsModel;
	}

	public MappingExpressionsPersistenceLayer getMappingExpressionsPersistenceLayer()
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

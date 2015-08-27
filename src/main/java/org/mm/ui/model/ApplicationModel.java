package org.mm.ui.model;

import java.util.HashMap;
import java.util.Map;

import org.mm.core.MappingExpressionSet;
import org.mm.renderer.Renderer;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ui.Environment;
import org.semanticweb.owlapi.model.OWLOntology;

public class ApplicationModel implements MMModel
{
	private DataSourceModel dataSourceModel;
	private MappingExpressionModel expressionMappingsModel;

	private Map<String, Renderer> renderers = new HashMap<String, Renderer>();

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

	public Renderer getDefaultRenderer()
	{
		return getRenderer(Environment.TEXT_RENDERER);
	}

	public Renderer getRenderer(String label)
	{
		return renderers.get(label);
	}

	public void registerRenderer(String label, Renderer renderer)
	{
		renderers.put(label, renderer);
	}
}

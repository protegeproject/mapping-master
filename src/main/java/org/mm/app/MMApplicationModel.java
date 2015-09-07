package org.mm.app;

import org.mm.core.MappingExpressionSet;
import org.mm.renderer.Renderer;
import org.mm.renderer.owlapi.OWLAPIRenderer;
import org.mm.renderer.text.TextRenderer;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.OWLOntology;

public class MMApplicationModel implements ApplicationModel
{
	private OWLOntology ontology;
	private SpreadSheetDataSource dataSource;

	private MMDataSourceModel dataSourceModel;
	private MMMappingExpressionModel expressionMappingsModel;

	public MMApplicationModel(OWLOntology ontology, SpreadSheetDataSource dataSource, MappingExpressionSet mappings)
	{
		this.ontology = ontology;
		this.dataSource = dataSource;
		
		dataSourceModel = new MMDataSourceModel(dataSource);
		expressionMappingsModel = new MMMappingExpressionModel(mappings);
	}

	@Override
	public MMDataSourceModel getDataSourceModel()
	{
		return dataSourceModel;
	}

	@Override
	public MMMappingExpressionModel getMappingExpressionsModel()
	{
		return expressionMappingsModel;
	}

	@Override
	public Renderer getDefaultRenderer()
	{
		return getOWLAPIRenderer();
	}

	public OWLAPIRenderer getOWLAPIRenderer()
	{
		return new OWLAPIRenderer(ontology, dataSource);
	}

	public TextRenderer getTextRenderer()
	{
		return new TextRenderer(dataSource);
	}
}

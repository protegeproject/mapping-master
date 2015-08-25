package org.mm.ui;

import org.mm.core.MappingExpressionSet;
import org.mm.renderer.owlapi.OWLAPIRenderer;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ui.model.MMApplicationModel;
import org.semanticweb.owlapi.model.OWLOntology;

public class MMApplication
{
	private MMApplicationModel applicationModel;

	public MMApplication(OWLOntology ontology, SpreadSheetDataSource dataSource, MappingExpressionSet mappings)
	{
		applicationModel = new MMApplicationModel(ontology, dataSource, mappings);
		applicationModel.registerRenderer(new OWLAPIRenderer(ontology, dataSource));
	}

	public MMApplicationModel getApplicationModel()
	{
		return applicationModel;
	}
}

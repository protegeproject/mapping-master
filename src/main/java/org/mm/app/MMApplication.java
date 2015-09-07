package org.mm.app;

import org.mm.core.MappingExpressionSet;
import org.mm.ss.SpreadSheetDataSource;
import org.semanticweb.owlapi.model.OWLOntology;

public class MMApplication
{
	private MMApplicationModel applicationModel;

	public MMApplication(OWLOntology ontology, SpreadSheetDataSource dataSource, MappingExpressionSet mappings)
	{
		applicationModel = new MMApplicationModel(ontology, dataSource, mappings);
	}

	public MMApplicationModel getApplicationModel()
	{
		return applicationModel;
	}
}

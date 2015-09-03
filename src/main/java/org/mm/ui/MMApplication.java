package org.mm.ui;

import org.mm.core.MappingExpressionSet;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ui.model.ApplicationModel;
import org.semanticweb.owlapi.model.OWLOntology;

public class MMApplication
{
	private ApplicationModel applicationModel;

	public MMApplication(OWLOntology ontology, SpreadSheetDataSource dataSource, MappingExpressionSet mappings)
	{
		applicationModel = new ApplicationModel(ontology, dataSource, mappings);
	}

	public ApplicationModel getApplicationModel()
	{
		return applicationModel;
	}
}

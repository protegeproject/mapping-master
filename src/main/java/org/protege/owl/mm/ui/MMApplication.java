package org.protege.owl.mm.ui;

import org.protege.owl.mm.MappingExpressionsPersistenceLayer;
import org.protege.owl.mm.ui.model.MMApplicationModel;
import org.protege.owl.mm.ui.view.MMApplicationView;

public class MMApplication
{
  private final MMApplicationView applicationView;
  private final MMApplicationModel applicationModel;

  public MMApplication(MMApplicationView applicationView, MMApplicationModel applicationModel)
  {
    this.applicationView = applicationView;
    this.applicationModel = applicationModel;
  }

  public MMApplicationView getApplicationView() { return applicationView; }

  public MMApplicationModel getApplicationModel() { return applicationModel; }

  public MappingExpressionsPersistenceLayer getMappingExpressionsPersistenceLayer()
  {
    return getApplicationModel().getMappingExpressionsPersistenceLayer();
  }
} 

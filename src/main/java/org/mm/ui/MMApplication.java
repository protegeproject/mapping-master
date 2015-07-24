package org.mm.ui;

import org.mm.ui.model.MMApplicationModel;
import org.mm.core.MappingExpressionsPersistenceLayer;
import org.mm.ui.view.MMApplicationView;

public class MMApplication
{
  private final MMApplicationView applicationView;
  private final MMApplicationModel applicationModel;

  public MMApplication(MMApplicationView applicationView, MMApplicationModel applicationModel)
  {
    this.applicationView = applicationView;
    this.applicationModel = applicationModel;
  }

  public MMApplicationView getApplicationView() { return this.applicationView; }

  public MMApplicationModel getApplicationModel() { return this.applicationModel; }

  public MappingExpressionsPersistenceLayer getMappingExpressionsPersistenceLayer()
  {
    return getApplicationModel().getMappingExpressionsPersistenceLayer();
  }
} 

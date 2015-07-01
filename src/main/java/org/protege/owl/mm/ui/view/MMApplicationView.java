package org.protege.owl.mm.ui.view;

import org.protege.owl.mm.ui.MMApplication;
import org.protege.owl.mm.ui.action.SaveMappingsAction;
import org.protege.owl.mm.ui.dialog.MMApplicationDialogManager;
import org.protege.owl.mm.ui.model.MMApplicationModel;

import javax.swing.*;

public class MMApplicationView extends JSplitPane implements MMView
{
  private final MMApplication application;
  private final MMApplicationDialogManager applicationDialogManager;
  private final DataSourceView dataSourceView;
  private final MappingsControlView mappingsControlView;
  private final MappingExpressionsView mappingsExpressionsView;
  private final JTabbedPane mappingsPane;

  public MMApplicationView(MMApplicationModel applicationModel, MMApplicationDialogManager applicationDialogManager)
  {
    applicationModel.setApplicationView(this);

    this.applicationDialogManager = applicationDialogManager;

    application = new MMApplication(this, applicationModel);

    applicationModel.setSaveMappingsAction(new SaveMappingsAction(application));

    this.applicationDialogManager.initialize(application);

    setOrientation(JSplitPane.VERTICAL_SPLIT);

    setResizeWeight(0.5);

    dataSourceView = new DataSourceView(application);
    setTopComponent(dataSourceView);

    mappingsPane = new JTabbedPane();
    setBottomComponent(mappingsPane);

    mappingsControlView = new MappingsControlView(application);
    mappingsPane.addTab("Mappings Control", null, mappingsControlView, "Mappings Control Tab");

    mappingsExpressionsView = new MappingExpressionsView(application);
    mappingsPane.addTab("Expressions", null, mappingsExpressionsView, "Expressions Tab");
  }

  @Override public void update()
  {
    dataSourceView.update();
    mappingsControlView.update();
    mappingsExpressionsView.update();
  }

  public MMApplication getApplication()
  {
    return application;
  }

  public DataSourceView getDataSourceView()
  {
    return dataSourceView;
  }

  public MappingsControlView getMappingsControlView()
  {
    return mappingsControlView;
  }

  public MMApplicationDialogManager getApplicationDialogManager()
  {
    return applicationDialogManager;
  }
}

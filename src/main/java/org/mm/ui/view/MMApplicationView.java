package org.mm.ui.view;

import org.mm.ui.MMApplication;
import org.mm.ui.action.SaveMappingsAction;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.MMApplicationModel;

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

    this.application = new MMApplication(this, applicationModel);

    applicationModel.setSaveMappingsAction(new SaveMappingsAction(this.application));

    this.applicationDialogManager.initialize(this.application);

    setOrientation(JSplitPane.VERTICAL_SPLIT);

    setResizeWeight(0.5);

    this.dataSourceView = new DataSourceView(this.application);
    setTopComponent(this.dataSourceView);

    this.mappingsPane = new JTabbedPane();
    setBottomComponent(this.mappingsPane);

    this.mappingsControlView = new MappingsControlView(this.application);
    this.mappingsPane.addTab("Mappings Control", null, this.mappingsControlView, "Mappings Control Tab");

    this.mappingsExpressionsView = new MappingExpressionsView(this.application);
    this.mappingsPane.addTab("Expressions", null, this.mappingsExpressionsView, "Expressions Tab");
  }

  @Override public void update()
  {
    this.dataSourceView.update();
    this.mappingsControlView.update();
    this.mappingsExpressionsView.update();
  }

  public MMApplication getApplication()
  {
    return this.application;
  }

  public DataSourceView getDataSourceView()
  {
    return this.dataSourceView;
  }

  public MappingsControlView getMappingsControlView()
  {
    return this.mappingsControlView;
  }

  public MMApplicationDialogManager getApplicationDialogManager()
  {
    return this.applicationDialogManager;
  }
}

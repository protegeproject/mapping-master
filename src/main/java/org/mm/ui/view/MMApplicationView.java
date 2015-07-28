package org.mm.ui.view;

import org.mm.ui.MMApplication;
import org.mm.ui.action.SaveMappingsAction;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.MMApplicationModel;

import javax.swing.*;

/**
 * This is the main Mapping Master user interface. It contains a view of a spreadsheet and a control area to edit and
 * execute Mapping Master expressions.
 */
public class MMApplicationView extends JSplitPane implements MMView
{
  private static final String MAPPINGS_CONTROL_VIEW_TITLE = "MappingsControl";
  private static final String MAPPINGS_CONTROL_VIEW_DESCRIPTION = "MappingsControl";
  private static final String EXPRESSIONS_VIEW_TITLE = "Expressions";
  private static final String EXPRESSIONS_VIEW_DESCRIPTION = "Expressions Tab";

  private final MMApplication application;
  private final MMApplicationDialogManager applicationDialogManager;
  private final DataSourceView dataSourceView;
  private final MappingsControlView mappingsControlView;
  private final MappingExpressionsView mappingsExpressionsView;
  private final JTabbedPane mappingsPane;

  public MMApplicationView(MMApplicationModel applicationModel, MMApplicationDialogManager applicationDialogManager)
  {
    this.applicationDialogManager = applicationDialogManager;

    this.application = new MMApplication(this, applicationModel);

    this.applicationDialogManager.initialize(this.application);

    setOrientation(JSplitPane.VERTICAL_SPLIT);

    setResizeWeight(0.5);

    this.dataSourceView = new DataSourceView(this.application);
    setTopComponent(this.dataSourceView);

    this.mappingsPane = new JTabbedPane();
    setBottomComponent(this.mappingsPane);

    this.mappingsControlView = new MappingsControlView(this.application);
    this.mappingsPane
      .addTab(MAPPINGS_CONTROL_VIEW_TITLE, null, this.mappingsControlView, MAPPINGS_CONTROL_VIEW_DESCRIPTION);

    this.mappingsExpressionsView = new MappingExpressionsView(this.application);
    this.mappingsPane.addTab(EXPRESSIONS_VIEW_TITLE, null, this.mappingsExpressionsView, EXPRESSIONS_VIEW_DESCRIPTION);

    applicationModel.setApplicationView(this);
    applicationModel.setSaveMappingsAction(new SaveMappingsAction(this.application));
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

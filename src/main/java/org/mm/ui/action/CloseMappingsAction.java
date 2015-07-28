package org.mm.ui.action;

import org.mm.ui.MMApplication;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.MMApplicationModel;
import org.mm.ui.model.MappingsExpressionsModel;
import org.mm.ui.view.MMApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseMappingsAction implements ActionListener
{
  private static final String CLOSE_MAPPINGS_TITLE = "Close Mappings";
  private static final String CLOSE_MAPPINGS_MESSAGE = "Close Mappings";

  private final MMApplication application;

  public CloseMappingsAction(MMApplication application) { this.application = application; }

  public void actionPerformed(ActionEvent e) { closeMappings(this.application); }

  public void closeMappings(MMApplication application)
  {
    MappingsExpressionsModel mappingExpressionsModel = application.getApplicationModel().getMappingExpressionsModel();
    MMApplicationView applicationView = application.getApplicationView();
    MMApplicationModel applicationModel = application.getApplicationModel();

    if (mappingExpressionsModel.hasMappingExpressions() && applicationModel.areMappingsModified() &&
      getApplicationDialogManager().showConfirmDialog(applicationView, CLOSE_MAPPINGS_TITLE, CLOSE_MAPPINGS_MESSAGE)) {
      close(application);
    } else
      close(application);
  }

  private static void close(MMApplication application)
  {
    MappingsExpressionsModel mappingsExpressionsModel = application.getApplicationModel().getMappingExpressionsModel();
    MMApplicationModel applicationModel = application.getApplicationModel();

    mappingsExpressionsModel.clearMappingExpressions();
    applicationModel.clearMappingFileName();
    applicationModel.clearModifiedStatus();
  }

  private MMApplicationView getApplicationView() { return this.application.getApplicationView(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }
}

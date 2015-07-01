package org.protege.owl.mm.ui.action;

import org.protege.owl.mm.ui.MMApplication;
import org.protege.owl.mm.ui.dialog.MMApplicationDialogManager;
import org.protege.owl.mm.ui.model.MMApplicationModel;
import org.protege.owl.mm.ui.model.MappingsExpressionsModel;
import org.protege.owl.mm.ui.view.MMApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CloseMappingsAction implements ActionListener
{
  private final MMApplication application;

  public CloseMappingsAction(MMApplication application) { this.application = application; }

  public void actionPerformed(ActionEvent e) { closeMappings(application); }

  public void closeMappings(MMApplication application)
  {
    MappingsExpressionsModel mappingExpressionsModel = application.getApplicationModel().getMappingExpressionsModel();
    MMApplicationView applicationView = application.getApplicationView();
    MMApplicationModel applicationModel = application.getApplicationModel();

    if (mappingExpressionsModel.hasMappingExpressions() && applicationModel.areMappingsModified() &&
      getApplicationDialogManager()
        .showConfirmDialog(applicationView, "Close Mappings", "Do you really want to close the mappings?")) {
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

  private MMApplicationView getApplicationView() { return application.getApplicationView(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }
}

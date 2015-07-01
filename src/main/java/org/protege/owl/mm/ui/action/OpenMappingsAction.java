package org.protege.owl.mm.ui.action;

import org.protege.owl.mm.MappingExpression;
import org.protege.owl.mm.exceptions.MappingMasterException;
import org.protege.owl.mm.ui.MMApplication;
import org.protege.owl.mm.ui.dialog.MMApplicationDialogManager;
import org.protege.owl.mm.ui.model.MMApplicationModel;
import org.protege.owl.mm.ui.model.MappingsExpressionsModel;
import org.protege.owl.mm.ui.view.MMApplicationView;
import org.protege.owl.mm.ui.view.MappingsControlView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

public class OpenMappingsAction implements ActionListener
{
  private final MMApplication application;

  public OpenMappingsAction(MMApplication application) { this.application = application; }

  public void actionPerformed(ActionEvent e) { openMappings(); }

  public void openMappings()
  {
    MappingsExpressionsModel mappingExpressionsModel = application.getApplicationModel().getMappingExpressionsModel();
    MMApplicationView applicationView = application.getApplicationView();
    MMApplicationModel applicationModel = application.getApplicationModel();
    MappingsControlView mappingsControlView = applicationView.getMappingsControlView();

    JFileChooser fileChooser = getApplicationDialogManager().createFileChooser("Open Mapping Ontology", "owl");

    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      String fileName = file.getAbsolutePath();
      Set<MappingExpression> mappingExpressions = null;

      mappingsControlView.statusWindowAppend("Opening mappings file '" + fileName + "'...\n");

      try {
        mappingExpressions = application.getMappingExpressionsPersistenceLayer().getMappingExpressions(fileName);

        mappingsControlView.statusWindowAppend("Mapping file successfully opened.\n");
      } catch (MappingMasterException ex) {
        getApplicationDialogManager().showErrorMessageDialog(applicationView, ex.getMessage());
        mappingsControlView.statusWindowAppend("Error opening mapping file: " + ex.getMessage() + "\n");
      }

      if (mappingExpressions != null) {
        mappingsControlView
          .statusWindowAppend("Found " + mappingExpressions.size() + " mapping expressions(s) in mapping file.\n");
        mappingExpressionsModel.setMappingExpression(mappingExpressions);
        applicationModel.setMappingFileName(fileName);
      } else
        mappingsControlView.statusWindowAppend("No mappings defined in mapping file.\n");

      applicationModel.clearModifiedStatus();
    }
  }

  private MMApplicationView getApplicationView() { return application.getApplicationView(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }
}

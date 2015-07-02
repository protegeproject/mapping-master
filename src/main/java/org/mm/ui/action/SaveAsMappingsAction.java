package org.mm.ui.action;

import org.mm.core.MappingExpression;
import org.mm.exceptions.MappingMasterException;
import org.mm.ui.MMApplication;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.model.MMApplicationModel;
import org.mm.ui.model.MappingsExpressionsModel;
import org.mm.ui.view.MMApplicationView;
import org.mm.ui.view.MappingsControlView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

public class SaveAsMappingsAction implements ActionListener
{
  private final MMApplication application;

  public SaveAsMappingsAction(MMApplication application) { this.application = application; }

  public void actionPerformed(ActionEvent e) { saveMappings(); }

  public void saveMappings()
  {
    MappingsExpressionsModel mappingExpressionsModel = application.getApplicationModel().getMappingExpressionsModel();
    MMApplicationView applicationView = application.getApplicationView();
    MMApplicationModel applicationModel = application.getApplicationModel();
    MappingsControlView mappingsControlView = applicationView.getMappingsControlView();

    JFileChooser fileChooser = getApplicationDialogManager()
      .createSaveFileChooser("Save Mapping Ontology", "owl", true);

    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      File file = fileChooser.getSelectedFile();
      String fileName = file.getAbsolutePath();
      Set<MappingExpression> mappingExpressions = mappingExpressionsModel.getMappingExpressions();

      if (!fileName.endsWith(".owl"))
        fileName = fileName.concat(".owl");

      mappingsControlView.statusWindowAppend("Saving mappings ontology '" + fileName + "'...\n");

      try {
        application.getMappingExpressionsPersistenceLayer().putMappingExpressions(mappingExpressions, fileName);

        applicationModel.setMappingFileName(fileName);
        applicationModel.clearModifiedStatus();

        mappingsControlView.statusWindowAppend("Mapping file successfully saved.\n");
      } catch (MappingMasterException ex) {
        getApplicationDialogManager().showErrorMessageDialog(applicationView, ex.getMessage());
        mappingsControlView.statusWindowAppend("Error saving mapping file: " + ex.getMessage());
      }
    }
  }

  private MMApplicationView getApplicationView() { return application.getApplicationView(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }

}

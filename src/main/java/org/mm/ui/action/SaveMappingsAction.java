package org.mm.ui.action;

import org.mm.exceptions.MappingMasterException;
import org.mm.ui.model.MMApplicationModel;
import org.mm.ui.model.MappingsExpressionsModel;
import org.mm.core.MappingExpression;
import org.mm.ui.MMApplication;
import org.mm.ui.dialog.MMApplicationDialogManager;
import org.mm.ui.view.MMApplicationView;
import org.mm.ui.view.MappingsControlView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class SaveMappingsAction implements ActionListener
{
  private final MMApplication application;

  public SaveMappingsAction(MMApplication application)
  {
    this.application = application;
  }

  public void actionPerformed(ActionEvent e) { saveMappings(); }

  public void saveMappings()
  {
    if (getApplicationModel().hasMappingFile()) {
      MappingsExpressionsModel mappingsExpressionsModel = getApplicationModel().getMappingExpressionsModel();
      MappingsControlView mappingsControlView = getApplicationView().getMappingsControlView();
      String fileName = getApplicationModel().getMappingFileName();
      Set<MappingExpression> mappingExpressions = mappingsExpressionsModel.getMappingExpressions();

      mappingsControlView.statusWindowAppend("Saving mappings ontology '" + fileName + "'...\n");

      try {
        getApplicationModel().getMappingExpressionsPersistenceLayer()
          .putMappingExpressions(mappingExpressions, fileName);

        getApplicationModel().setMappingFileName(fileName);
        getApplicationModel().clearModifiedStatus();

        mappingsControlView.statusWindowAppend("Mapping file successfully saved.\n");
      } catch (MappingMasterException ex) {
        getApplicationDialogManager().showErrorMessageDialog(getApplicationView(), ex.getMessage());
        mappingsControlView.statusWindowAppend("Error saving mapping file: " + ex.getMessage());
      }
    }
  }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }

  private MMApplicationView getApplicationView() { return application.getApplicationView(); }

  private MMApplicationModel getApplicationModel() { return application.getApplicationModel(); }
} 

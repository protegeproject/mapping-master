package org.protege.owl.mm.ui.action;

import org.protege.owl.mm.MappingExpression;
import org.protege.owl.mm.exceptions.MappingMasterException;
import org.protege.owl.mm.ss.SpreadSheetExpressionMapper;
import org.protege.owl.mm.ui.MMApplication;
import org.protege.owl.mm.ui.dialog.MMApplicationDialogManager;
import org.protege.owl.mm.ui.model.DataSourceModel;
import org.protege.owl.mm.ui.model.MMApplicationModel;
import org.protege.owl.mm.ui.model.MappingsExpressionsModel;
import org.protege.owl.mm.ui.view.MMApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class MapExpressionsAction implements ActionListener
{
  private final MMApplication application;

  public MapExpressionsAction(MMApplication application) { this.application = application; }

  public void actionPerformed(ActionEvent e)
  {
    if (!getMappingExpressionsModel().hasMappingExpressions())
      getApplicationDialogManager().showMessageDialog(getApplicationView(), "No mappings defined!");
    else if (!getDataSourceModel().hasDataSource())
      getApplicationDialogManager().showMessageDialog(getApplicationView(), "No data source loaded!");
    else {
      try {
        Set<MappingExpression> mappingExpressions = getMappingExpressionsModel().getMappingExpressions(true);
        SpreadSheetExpressionMapper.map(getApplicationModel().getRenderer(), mappingExpressions);

        getApplicationDialogManager().showMessageDialog(getApplicationView(), "Mappings performed successfully.");
      } catch (MappingMasterException ex) {
        ex.printStackTrace();
        getApplicationDialogManager().showErrorMessageDialog(getApplicationView(), "Mapping error: " + ex.getMessage());
      }
    }
  }

  private MappingsExpressionsModel getMappingExpressionsModel()
  {
    return application.getApplicationModel().getMappingExpressionsModel();
  }

  private DataSourceModel getDataSourceModel() { return application.getApplicationModel().getDataSourceModel(); }

  private MMApplicationView getApplicationView() { return application.getApplicationView(); }

  private MMApplicationModel getApplicationModel() { return application.getApplicationModel(); }

  private MMApplicationDialogManager getApplicationDialogManager()
  {
    return getApplicationView().getApplicationDialogManager();
  }
}

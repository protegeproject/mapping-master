package org.mm.ui.model;

import org.mm.renderer.owlapi.OWLAPIRenderer;
import org.mm.core.MappingExpressionsPersistenceLayer;
import org.mm.renderer.MappingConfigurationOptionsManager;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ui.action.SaveMappingsAction;
import org.mm.ui.view.MMApplicationView;
import org.mm.ui.view.MappingsControlView;

public class MMApplicationModel implements MMModel
{
  private final DataSourceModel dataSourceModel;
  private final MappingsExpressionsModel expressionMappingsModel;
  private final OWLAPIRenderer renderer;
  private final MappingConfigurationOptionsManager optionsManager;
  private final MappingExpressionsPersistenceLayer mappingExpressionsPersistenceLayer;

  private MMApplicationView applicationView;
  private String mappingFileName = null;
  private SaveMappingsAction saveMappingsAction;

  public MMApplicationModel(SpreadSheetDataSource dataSource, OWLAPIRenderer renderer,
    MappingExpressionsPersistenceLayer mappingExpressionsPersistenceLayer)
  {
    dataSourceModel = new DataSourceModel(dataSource);
    expressionMappingsModel = new MappingsExpressionsModel();
    this.renderer = renderer;
    optionsManager = new MappingConfigurationOptionsManager(renderer);
    this.mappingExpressionsPersistenceLayer = mappingExpressionsPersistenceLayer;
  }

  public DataSourceModel getDataSourceModel() { return dataSourceModel; }

  public MappingsExpressionsModel getMappingExpressionsModel() { return expressionMappingsModel; }

  public OWLAPIRenderer getRenderer() { return renderer; }

  public MappingConfigurationOptionsManager getMappingConfigurationOptionsManager() { return optionsManager; }

  public MappingExpressionsPersistenceLayer getMappingExpressionsPersistenceLayer() { return mappingExpressionsPersistenceLayer; }

  public void setApplicationView(MMApplicationView view) { this.applicationView = view; }

  public MMApplicationView getApplicationView() { return applicationView; }

  public void setSaveMappingsAction(SaveMappingsAction saveMappingsAction)
  { this.saveMappingsAction = saveMappingsAction; }

  public void saveMappings()
  {
    if (saveMappingsAction != null)
      saveMappingsAction.saveMappings();
  }

  public void dataSourceUpdated()
  {
    renderer.setDataSource(getDataSourceModel().getDataSource());
  }

  public void setMappingFileName(String mappingFileName)
  {
    this.mappingFileName = mappingFileName;
    updateView();
  }

  public void clearMappingFileName()
  {
    mappingFileName = null;
    getMappingsControlView().update();
  }

  public boolean hasMappingFile() { return mappingFileName != null; }

  public String getMappingFileName() { return mappingFileName; }

  public boolean areMappingsModified() { return expressionMappingsModel.hasBeenModified(); }

  public void clearModifiedStatus() { expressionMappingsModel.clearModifiedStatus(); }

  private void updateView()
  {
    if (applicationView != null)
      applicationView.update();
  }

  private MappingsControlView getMappingsControlView()
  {
    return ((MMApplicationView)applicationView).getMappingsControlView();
  }
}

package org.mm.ui.model;

import org.mm.core.MappingExpressionsPersistenceLayer;
import org.mm.renderer.CoreRenderer;
import org.mm.renderer.ReferenceRendererOptionsManager;
import org.mm.ss.SpreadSheetDataSource;
import org.mm.ui.action.SaveMappingsAction;
import org.mm.ui.view.MMApplicationView;
import org.mm.ui.view.MappingsControlView;

public class MMApplicationModel implements MMModel
{
  private final DataSourceModel dataSourceModel;
  private final MappingsExpressionsModel expressionMappingsModel;
  private final CoreRenderer coreRenderer;
  private final ReferenceRendererOptionsManager optionsManager;
  private final MappingExpressionsPersistenceLayer mappingExpressionsPersistenceLayer;

  private MMApplicationView applicationView;
  private String mappingFileName;
  private SaveMappingsAction saveMappingsAction;

  public MMApplicationModel(SpreadSheetDataSource dataSource, CoreRenderer coreRenderer,
    MappingExpressionsPersistenceLayer mappingExpressionsPersistenceLayer)
  {
    this.dataSourceModel = new DataSourceModel(dataSource);
    this.expressionMappingsModel = new MappingsExpressionsModel();
    this.coreRenderer = coreRenderer;
    this.optionsManager = new ReferenceRendererOptionsManager(coreRenderer.getReferenceRenderer());
    this.mappingExpressionsPersistenceLayer = mappingExpressionsPersistenceLayer;
  }

  public DataSourceModel getDataSourceModel() { return dataSourceModel; }

  public MappingsExpressionsModel getMappingExpressionsModel() { return expressionMappingsModel; }

  public CoreRenderer getCoreRenderer() { return coreRenderer; }

  public ReferenceRendererOptionsManager getMappingConfigurationOptionsManager() { return optionsManager; }

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
    coreRenderer.setDataSource(getDataSourceModel().getDataSource());
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
    return applicationView.getMappingsControlView();
  }
}

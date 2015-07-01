package org.protege.owl.mm.ui.model;

import org.protege.owl.mm.ss.SpreadSheetDataSource;
import org.protege.owl.mm.ui.view.MMView;

import java.util.List;

public class DataSourceModel implements MMModel
{
  private SpreadSheetDataSource dataSource = null;
  private MMView view = null;
  private String fileName = null;

  public DataSourceModel(SpreadSheetDataSource dataSource) { this.dataSource = dataSource; }

  public void setView(MMView view) { this.view = view; }

  public void setDataSource(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
    updateView();
  }

  public boolean hasDataSource() { return dataSource != null; }

  public void clearDataSource()
  {
    dataSource = null;
    updateView();
  }

  public SpreadSheetDataSource getDataSource() { return dataSource; }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
    updateView();
  }

  public void clearFileName()
  {
    fileName = null;
    updateView();
  }

  public boolean hasFileName() { return fileName != null; }

  public String getFileName() { return fileName; }

  public List<String> getSubSourceNames() { return dataSource.getSubSourceNames(); }

  private void updateView()
  {
    if (view != null)
      view.update();
  }
} 

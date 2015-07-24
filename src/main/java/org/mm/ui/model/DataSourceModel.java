package org.mm.ui.model;

import org.mm.ss.SpreadSheetDataSource;
import org.mm.ui.view.MMView;

import java.util.List;

public class DataSourceModel implements MMModel
{
  private SpreadSheetDataSource dataSource;
  private MMView view;
  private String fileName;

  public DataSourceModel(SpreadSheetDataSource dataSource) { this.dataSource = dataSource; }

  public void setView(MMView view) { this.view = view; }

  public void setDataSource(SpreadSheetDataSource dataSource)
  {
    this.dataSource = dataSource;
    updateView();
  }

  public boolean hasDataSource() { return this.dataSource != null; }

  public void clearDataSource()
  {
    this.dataSource = null;
    updateView();
  }

  public SpreadSheetDataSource getDataSource() { return this.dataSource; }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
    updateView();
  }

  public void clearFileName()
  {
    this.fileName = null;
    updateView();
  }

  public boolean hasFileName() { return this.fileName != null; }

  public String getFileName() { return this.fileName; }

  public List<String> getSubSourceNames() { return this.dataSource.getSubSourceNames(); }

  private void updateView()
  {
    if (this.view != null)
      this.view.update();
  }
} 

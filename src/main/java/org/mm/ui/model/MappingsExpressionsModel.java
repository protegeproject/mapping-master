package org.mm.ui.model;

import org.mm.core.MappingExpression;
import org.mm.ui.view.MMView;

import javax.swing.table.AbstractTableModel;
import java.util.HashSet;
import java.util.Set;

public class MappingsExpressionsModel extends AbstractTableModel implements MMModel
{
  private static final int ACTIVE_COLUMN = 0;
  private static final int COMMENT_COLUMN = 1;
  private static final int EXPRESSION_COLUMN = 2;
  private static final int SOURCE_SHEET_NAME_COLUMN = 3;
  private static final int START_COLUMN_COLUMN = 4;
  private static final int FINISH_COLUMN_COLUMN = 5;
  private static final int START_ROW_COLUMN = 6;
  private static final int FINISH_ROW_COLUMN = 7;
  private static final int NUMBER_OF_COLUMNS = 8;

  private MMView view;
  private boolean isModified;

  private Set<MappingExpression> mappingExpressions;

  public MappingsExpressionsModel() { this.mappingExpressions = new HashSet<>(); }

  public Set<MappingExpression> getMappingExpressions() { return this.mappingExpressions; }

  public Set<MappingExpression> getMappingExpressions(boolean isActiveFlag)
  {
    Set<MappingExpression> res = new HashSet<>();

    for (MappingExpression expr : this.mappingExpressions) {
      if (expr.isActive() == isActiveFlag) {
        res.add(expr);
      }
    }
    return res;
  }

  public boolean hasMappingExpressions() { return !this.mappingExpressions.isEmpty(); }

  public boolean hasMappingExpressions(MappingExpression mappingExpression)
  {
    return this.mappingExpressions.contains(mappingExpression);
  }

  public void setMappingExpression(Set<MappingExpression> mappingExpressions)
  {
    this.mappingExpressions = new HashSet<>(mappingExpressions);
    updateView();
  }

  public void addMappingExpression(MappingExpression mappingExpression)
  {
    if (!this.mappingExpressions.contains(mappingExpression))
      this.mappingExpressions.add(mappingExpression);

    this.isModified = true;
    updateView();
  }

  public void removeMappingExpression(MappingExpression mappingExpression)
  {
    if (this.mappingExpressions.contains(mappingExpression))
      this.mappingExpressions.remove(mappingExpression);
    this.isModified = true;
    updateView();
  }

  public void clearMappingExpressions()
  {
    this.mappingExpressions = new HashSet<>();
    updateView();
    this.isModified = false;
  }

  public void setView(MMView view) { this.view = view; }

  public boolean hasBeenModified() { return this.isModified; }

  public void clearModifiedStatus() { this.isModified = false; }

  public int getRowCount() { return this.mappingExpressions.size(); }

  public int getColumnCount() { return NUMBER_OF_COLUMNS; }

  @Override public String getColumnName(int column)
  {
    if (column == COMMENT_COLUMN)
      return "Comment";
    else if (column == EXPRESSION_COLUMN)
      return "MM DSL expression";
    else if (column == SOURCE_SHEET_NAME_COLUMN)
      return "Sheet name";
    else if (column == START_COLUMN_COLUMN)
      return "Start column";
    else if (column == FINISH_COLUMN_COLUMN)
      return "Finish column";
    else if (column == START_ROW_COLUMN)
      return "Start row";
    else if (column == FINISH_ROW_COLUMN)
      return "Finish row";
    else if (column == ACTIVE_COLUMN)
      return "";
    else
      return null;
  }

  public Object getValueAt(int row, int column)
  {
    Object result = null;

    if (row < 0 || row >= getRowCount() || column < 0 || column >= getColumnCount())
      result = "OUT OF BOUNDS";
    else {
      if (column == EXPRESSION_COLUMN)
        result = ((MappingExpression)this.mappingExpressions.toArray()[row]).getExpression();
      else if (column == COMMENT_COLUMN)
        result = ((MappingExpression)this.mappingExpressions.toArray()[row]).getComment();
      else if (column == SOURCE_SHEET_NAME_COLUMN)
        result = ((MappingExpression)this.mappingExpressions.toArray()[row]).getSourceSheetName();
      else if (column == START_COLUMN_COLUMN)
        result = ((MappingExpression)this.mappingExpressions.toArray()[row]).getStartColumn();
      else if (column == FINISH_COLUMN_COLUMN)
        result = ((MappingExpression)this.mappingExpressions.toArray()[row]).getFinishColumn();
      else if (column == START_ROW_COLUMN)
        result = ((MappingExpression)this.mappingExpressions.toArray()[row]).getStartRow();
      else if (column == FINISH_ROW_COLUMN)
        result = ((MappingExpression)this.mappingExpressions.toArray()[row]).getFinishRow();
      else if (column == ACTIVE_COLUMN)
        result = ((MappingExpression)this.mappingExpressions.toArray()[row]).isActive();
    }
    return result;
  }

  @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return columnIndex == ACTIVE_COLUMN; }

  @Override public Class<?> getColumnClass(int columnIndex)
  {
    if (columnIndex == ACTIVE_COLUMN) {
      return Boolean.class;
    } else {
      return super.getColumnClass(columnIndex);
    }
  }

  @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    if (columnIndex == ACTIVE_COLUMN) {
      ((MappingExpression)this.mappingExpressions.toArray()[rowIndex]).setActive((Boolean)aValue);
    } else {
      super.setValueAt(aValue, rowIndex, columnIndex);
    }
  }

  private void updateView()
  {
    if (this.view != null)
      this.view.update();
  }
} 

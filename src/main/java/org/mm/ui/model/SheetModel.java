package org.mm.ui.model;

import jxl.Sheet;
import org.mm.ss.SpreadSheetUtil;

import javax.swing.table.AbstractTableModel;

public class SheetModel extends AbstractTableModel
{
  private final Sheet sheet;

  public SheetModel(Sheet sheet) { this.sheet = sheet; }

  public int getRowCount() { return this.sheet.getRows(); }

  public int getColumnCount() { return this.sheet.getColumns() + 1; }

  public String getColumnName(int column)
  {
    if (isValidColumn(column)) {
      if (column == 0)
        return "";
      else
        return SpreadSheetUtil.columnNumber2Name(column);
    } else
      return null;
  }

  public Object getValueAt(int row, int column)
  {
    Object value;

    if (isValidLocation(row, column)) {
      if (column == 0)
        return Integer.toString(row + 1);
      else {
        value = this.sheet.getCell(column - 1, row).getContents();
        return value == null ? "" : value;
      }
    } else
      return "";
  }

  private boolean isValidLocation(int row, int column) { return isValidRow(row) && isValidColumn(column); }

  private boolean isValidRow(int row) { return row >= 0 && row < getRowCount(); }

  private boolean isValidColumn(int column) { return column >= 0 && column < getColumnCount(); }
}


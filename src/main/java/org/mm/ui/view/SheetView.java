package org.mm.ui.view;

import jxl.Sheet;
import org.mm.ui.model.SheetModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class SheetView extends JPanel implements MMView
{
  private final Sheet sheet;
  private final SheetModel sheetModel;
  private final JTable sheetTable;

  public SheetView(Sheet sheet)
  {
    this.sheet = sheet;
    this.sheetModel = new SheetModel(sheet);

    setLayout(new BorderLayout());

    this.sheetTable = new JTable(this.sheetModel);
    setFirstColumnWidth(30);
    JScrollPane scrollPane = new JScrollPane(this.sheetTable);
    JViewport viewport = scrollPane.getViewport();
    viewport.setBackground(this.sheetTable.getBackground());

    add(BorderLayout.CENTER, scrollPane);
  }

  public String getSheetName() { return this.sheet.getName(); }

  private void setFirstColumnWidth(int newWidth)
  {
    TableColumn column1 = this.sheetTable.getColumnModel().getColumn(0);
    column1.setPreferredWidth(newWidth);
    column1.setMaxWidth(2 * newWidth);
  }

  public void update() { validate(); }

  public void validate()
  {
    this.sheetModel.fireTableDataChanged();
    super.validate();
  }
}

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

    this.sheetTable = new JTable(sheetModel);
    setFirstColumnWidth(30);
    JScrollPane scrollPane = new JScrollPane(sheetTable);
    JViewport viewport = scrollPane.getViewport();
    viewport.setBackground(sheetTable.getBackground());

    add(BorderLayout.CENTER, scrollPane);
  }

  public String getSheetName() { return sheet.getName(); }

  private void setFirstColumnWidth(int newWidth)
  {
    TableColumn column1 = sheetTable.getColumnModel().getColumn(0);
    column1.setPreferredWidth(newWidth);
    column1.setMaxWidth(2 * newWidth);
  }

  public void update() { validate(); }

  public void validate()
  {
    sheetModel.fireTableDataChanged();
    super.validate();
  }
}

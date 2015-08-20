package org.mm.ui.model;

import javax.swing.table.AbstractTableModel;

import org.apache.poi.ss.usermodel.Sheet;
import org.mm.ss.SpreadSheetUtil;

public class SheetModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;

	private final Sheet sheet;

	public SheetModel(Sheet sheet)
	{
		this.sheet = sheet;
	}

	public int getRowCount()
	{
		return sheet.getLastRowNum() + 1;
	}

	public int getColumnCount()
	{
		int maxCount = 0;
		for (int i = 0; i < getRowCount(); i++) {
			int currentCount = sheet.getRow(i).getLastCellNum();
			if (currentCount > maxCount) {
				maxCount = currentCount;
			}
		}
		return maxCount;
	}

	public String getColumnName(int column)
	{
		if (column == 0) {
			return "";
		}
		else {
			return SpreadSheetUtil.columnNumber2Name(column);
		}
	}

	public Object getValueAt(int row, int column)
	{
		return sheet.getRow(row).getCell(column).getStringCellValue();
	}
}


package org.protege.owl.mm.ss;

import jxl.Sheet;

public class SpreadsheetLocation
{
	// There is an equals() method defined on this class.
	private final Sheet sheet;
	private int columnNumber, rowNumber;

	public SpreadsheetLocation(Sheet sheet, int columnNumber, int rowNumber)
	{
		this.sheet = sheet;
		this.columnNumber = columnNumber;
		this.rowNumber = rowNumber;
	}

	public Sheet getSheet()
	{
		return sheet;
	}

	public int getColumnNumber()
	{
		return columnNumber;
	}

	public String getColumnName()
	{
		return SpreadSheetUtil.columnNumber2Name(columnNumber);
	}

	public int getRowNumber()
	{
		return rowNumber;
	}

	public void setColumnNumber(int columnNumber)
	{
		this.rowNumber = columnNumber;
	}

	public void setRowNumber(int rowNumber)
	{
		this.rowNumber = rowNumber;
	}

	public void incrementColumnNumber()
	{
		columnNumber++;
	}

	public void incrementRowNumber()
	{
		rowNumber++;
	}

	public String getSheetName()
	{
		return sheet == null ? "" : sheet.getName();
	}

	public String getCellLocation()
	{
		return getColumnName() + rowNumber;
	}

	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if ((obj == null) || (obj.getClass() != this.getClass()))
			return false;
		SpreadsheetLocation l = (SpreadsheetLocation)obj;
		return (getSheetName() == l.getSheetName() && getColumnNumber() == l.getColumnNumber() && getRowNumber() == l.getRowNumber());
	}

	public int hashCode()
	{
		int hash = 12;

		hash = hash + getSheetName().hashCode();
		hash = hash + columnNumber;
		hash = hash + rowNumber;
		return hash;
	}

	public String getFullyQualifiedLocation()
	{
		return "'" + getSheetName() + "'!" + getCellLocation();
	}

	public String toString()
	{
		return getFullyQualifiedLocation();
	}
}

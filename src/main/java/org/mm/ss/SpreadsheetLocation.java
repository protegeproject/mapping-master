
package org.mm.ss;

import jxl.Sheet;

public class SpreadsheetLocation
{
	// There is an equals() method defined on this class.
	private final String sheetName;
	private final int columnNumber, rowNumber;

	public SpreadsheetLocation(String sheetName, int columnNumber, int rowNumber)
	{
		this.sheetName = sheetName;
		this.columnNumber = columnNumber;
		this.rowNumber = rowNumber;
	}

	public String getSheetName()
	{
		return this.sheetName;
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
		return (this.sheetName == l.sheetName && getColumnNumber() == l.getColumnNumber() && getRowNumber() == l.getRowNumber());
	}

	public int hashCode()
	{
		int hash = 12;

		hash = hash + sheetName.hashCode();
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

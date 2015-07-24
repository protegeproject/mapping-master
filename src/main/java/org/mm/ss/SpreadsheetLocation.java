package org.mm.ss;

public class SpreadsheetLocation
{
  // There is an equals() method defined on this class.
  private final String sheetName;
  private final int columnNumber, rowNumber;

  /**
   *
   * @param sheetName The name of the sheet
   * @param columnNumber 1-based column number
   * @param rowNumber 1-based row number
   */
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
    return this.columnNumber;
  }

  public String getColumnName()
  {
    return SpreadSheetUtil.columnNumber2Name(this.columnNumber);
  }

  public int getRowNumber()
  {
    return this.rowNumber;
  }

  public String getCellLocation()
  {
    return getColumnName() + this.rowNumber;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    SpreadsheetLocation that = (SpreadsheetLocation)o;

    if (this.columnNumber != that.columnNumber)
      return false;
    if (this.rowNumber != that.rowNumber)
      return false;
    return !(this.sheetName != null ? !this.sheetName.equals(that.sheetName) : that.sheetName != null);

  }

  @Override public int hashCode()
  {
    int result = this.sheetName != null ? this.sheetName.hashCode() : 0;
    result = 31 * result + this.columnNumber;
    result = 31 * result + this.rowNumber;
    return result;
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

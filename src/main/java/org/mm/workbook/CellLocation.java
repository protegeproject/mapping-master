package org.mm.workbook;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Represents the cell location (e.g., A1, D22, etc) in a spreadsheet.
 *
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class CellLocation {

   private final String sheetName;
   private final int columnNumber, rowNumber;

   /**
    * Constructs a cell location, or a cell coordinate in a spreadsheet
    *
    * @param sheetName
    *           The name of the sheet
    * @param columnNumber
    *           The physical column number (start from 1)
    * @param rowNumber
    *           The physical row number (start from 1)
    */
   public CellLocation(@Nonnull String sheetName, int columnNumber, int rowNumber) {
      this.sheetName = checkNotNull(sheetName);
      this.columnNumber = columnNumber;
      this.rowNumber = rowNumber;
   }

   @Nonnull
   public String getSheetName() {
      return sheetName;
   }

   /**
    * Get the logical column number (0-based)
    *
    * @return The column number, starts from 0.
    */
   public int getColumnIndex() {
      return columnNumber - 1;
   }

   /**
    * Get the physical column number as usually presented in a spreadsheet
    * application (1-based)
    *
    * @return The column number, starts from 1.
    */
   public int getPhysicalColumnNumber() {
      return columnNumber;
   }

   public String getColumnName() {
      return WorkbookUtils.columnNumber2Name(getPhysicalColumnNumber());
   }

   /**
    * Get the logical row number (0-based)
    *
    * @return The row number, starts from 0.
    */
   public int getRowIndex() {
      return rowNumber - 1;
   }

   /**
    * Get the physical row number as usually presented in a spreadsheet
    * application (1-based).
    *
    * @return The row number, starts from 1.
    */
   public int getPhysicalRowNumber() {
      return rowNumber;
   }

   /**
    * Get the physical cell location (1-based)
    *
    * @return The cell location in format [COLUMN_NAME][ROW_NUMBER], e.g., A1,
    *         B3, H24
    */
   public String getCellLocation() {
      return getColumnName() + getPhysicalRowNumber();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
     }
     if (obj == this) {
         return true;
     }
     if (!(obj instanceof CellLocation)) {
         return false;
     }
     CellLocation other = (CellLocation) obj;
     return this.sheetName.equals(other.sheetName)
           && this.columnNumber == other.columnNumber
           && this.rowNumber == other.rowNumber;

   }

   @Override
   public int hashCode() {
      return Objects.hashCode(sheetName) + columnNumber + rowNumber;
   }

   public String getFullyQualifiedLocation() {
      return "'" + getSheetName() + "'!" + getCellLocation();
   }

   @Override
   public String toString() {
      return getFullyQualifiedLocation();
   }
}

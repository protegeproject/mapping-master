package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class CellAddress {

   private final String sheetName;
   private final int columnNumber;
   private final int rowNumber;

   public CellAddress(@Nonnull String sheetName, int columnNumber, int rowNumber) {
      this.sheetName = checkNotNull(sheetName);
      this.columnNumber = columnNumber;
      this.rowNumber = rowNumber;
   }

   public String getSheetName() {
      return sheetName;
   }

   public int getColumnNumber() {
      return columnNumber; // 1-based indexing
   }

   public int getColumnIndex() {
      return columnNumber - 1; // 0-based indexing
   }

   public int getRowNumber() {
      return rowNumber; // 1-based indexing
   }

   public int getRowIndex() {
      return rowNumber - 1; // 0-based indexing
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
     }
     if (obj == this) {
         return true;
     }
     if (!(obj instanceof CellAddress)) {
         return false;
     }
     CellAddress other = (CellAddress) obj;
     return this.sheetName.equals(other.sheetName)
           && this.columnNumber == other.columnNumber
           && this.rowNumber == other.rowNumber;

   }

   @Override
   public int hashCode() {
      return Objects.hashCode(sheetName) + columnNumber + rowNumber;
   }

   @Override
   public String toString() {
      return format("%s!%s%s", getSheetName(), CellUtils.toColumnLabel(columnNumber), rowNumber);
   }
}
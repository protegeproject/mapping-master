package org.mm.workbook;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the spreadsheet in a workbook.
 *
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class SheetImpl implements Sheet {

   private static final Logger logger = LoggerFactory.getLogger(SheetImpl.class);

   private final org.apache.poi.ss.usermodel.Sheet sheet;

   public SheetImpl(@Nonnull org.apache.poi.ss.usermodel.Sheet sheet) {
      this.sheet = checkNotNull(sheet);
   }

   @Override
   @Nonnull
   public String getName() {
      return sheet.getSheetName();
   }

   /**
    * Gets the last row index. The returned index is in 0-based order, i.e,
    * the first index starts at 0.
    */
   @Override
   public int getLastRowIndex() {
      return sheet.getLastRowNum();
   }

   /**
    * Gets the last column index at the given row. The returned index is in
    * 0-based order, i.e., the first index starts at 0.
    * 
    * @param whichRow
    *           The row where to get the last column index (0-based order)
    */
   @Override
   public int getLastColumnIndexAt(int whichRow) {
      return sheet.getRow(whichRow).getLastCellNum() - 1;
   }

   /**
    * Gets the cell value given the row and column indexes.
    * 
    * @param row
    *           The row index (0-based order)
    * @param column
    *           The column index (0-based order)
    */
   @Override
   @Nonnull
   public Object getCellValue(int row, int column) {
      try {
         Cell cell = sheet.getRow(row).getCell(column);
         switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING :
               return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC :
               // Check if the numeric is double or integer
               if (isInteger(cell.getNumericCellValue())) {
                  return (int) cell.getNumericCellValue();
               } else {
                  return cell.getNumericCellValue();
               }
            case Cell.CELL_TYPE_BOOLEAN :
               return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_FORMULA :
               return cell.getNumericCellValue();
            default :
               return "";
         }
      } catch (NullPointerException e) {
         logger.warn("Looking for a cell beyond the maximum row and column range");
         return "";
      }
   }

   private boolean isInteger(double number) {
      return (number == Math.floor(number) && !Double.isInfinite(number));
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(sheet);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (this == obj) {
         return true;
      }
      if (!(obj instanceof SheetImpl)) {
         return false;
      }
      SheetImpl other = (SheetImpl) obj;
      return this.sheet.equals(other.sheet);
   }

   @Override
   public String toString() {
      return sheet.toString();
   }
}

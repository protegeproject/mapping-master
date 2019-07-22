package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class Sheet {

   private final org.apache.poi.ss.usermodel.Sheet poiSheet;

   public Sheet(@Nonnull org.apache.poi.ss.usermodel.Sheet poiSheet) {
      this.poiSheet = checkNotNull(poiSheet);
   }

   public String getSheetName() {
      return poiSheet.getSheetName();
   }

   public int getStartColumnIndex() {
      return 0;
   }

   public int getEndColumnIndex() {
      int endColumnIndex = 0;
      for (int rowIndex = 0; rowIndex <= getEndRowIndex(); rowIndex++) {
         int columnIndex = poiSheet.getRow(rowIndex).getLastCellNum()-1;
         if (columnIndex > endColumnIndex) {
            endColumnIndex = columnIndex;
         }
      }
      return endColumnIndex;
   }

   public int getStartRowIndex() {
      return 0;
   }

   public int getEndRowIndex() {
      return poiSheet.getLastRowNum() - 1;
   }

   public void addValueToCell(int row, int column, String value) {
      Row poiRow = poiSheet.getRow(row);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row);
      }
      poiRow.createCell(column).setCellValue(value);
   }

   public String getValueFromCell(int row, int column) {
      final Cell cell = poiSheet.getRow(row).getCell(column);
      return getCellValue(cell);
   }
   
   private String getCellValue(Cell cell) {
      Object cellValueObject = null;
      try {
         int cellType = cell.getCellType();
         if (cellType == Cell.CELL_TYPE_STRING) {
            cellValueObject = cell.getStringCellValue();
         } else if (cellType == Cell.CELL_TYPE_NUMERIC) {
            // Check if the numeric is double or integer
            if (isInteger(cell.getNumericCellValue())) {
               cellValueObject = (int) cell.getNumericCellValue();
            } else {
               cellValueObject = cell.getNumericCellValue();
            }
         } else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
            cellValueObject = cell.getBooleanCellValue();
         } else if (cellType == Cell.CELL_TYPE_FORMULA) {
            cellValueObject = cell.getNumericCellValue();
         }
      } catch (NullPointerException e) {
         cellValueObject = null;
      }
      return (cellValueObject == null) ? "" : String.valueOf(cellValueObject);
   }

   private boolean isInteger(double number) {
      return (number == Math.floor(number) && !Double.isInfinite(number));
   }
}

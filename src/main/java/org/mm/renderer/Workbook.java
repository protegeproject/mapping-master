package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.mm.renderer.internal.CellAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class Workbook {

   private static final Logger logger = LoggerFactory.getLogger(Workbook.class);

   private final org.apache.poi.ss.usermodel.Workbook workbook;

   public Workbook(@Nonnull org.apache.poi.ss.usermodel.Workbook workbook) {
      this.workbook = checkNotNull(workbook);
   }

   public Sheet getSheet(String sheetName) {
      return workbook.getSheet(sheetName);
   }

   public String getCellValue(CellAddress cellAddress) {
      final Sheet sheet = workbook.getSheet(cellAddress.getSheetName());
      return getCellValue(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
   }

   public String getCellValue(Sheet sheet, int columnIndex, int rowIndex) {
      final Cell cell = sheet.getRow(rowIndex).getCell(columnIndex);
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
         } else {
            cellValueObject = "";
         }
      } catch (NullPointerException e) {
         logger.warn("Looking for a cell beyond the maximum row and column range");
      }
      return (cellValueObject == null) ? "" : String.valueOf(cellValueObject);
   }

   private boolean isInteger(double number) {
      return (number == Math.floor(number) && !Double.isInfinite(number));
   }
}

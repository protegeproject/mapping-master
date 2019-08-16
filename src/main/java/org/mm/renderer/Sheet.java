package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
         Row poiRow = poiSheet.getRow(rowIndex);
         if (poiRow != null) {
            int currentEndColumnIndex = poiRow.getLastCellNum() - 1;
            if (currentEndColumnIndex > endColumnIndex) {
               endColumnIndex = currentEndColumnIndex;
            }
         }
      }
      return endColumnIndex;
   }

   public int getStartRowIndex() {
      return 0;
   }

   public int getEndRowIndex() {
      return poiSheet.getLastRowNum();
   }

   public void addValueToCell(int column, int row, String value) {
      Row poiRow = poiSheet.getRow(row);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row);
      }
      poiRow.createCell(column).setCellValue(value);
   }

   public Optional<String> getValueFromCell(int column, int row) {
      final Row poiRow = poiSheet.getRow(row);
      Optional<String> value = Optional.empty();
      if (poiRow != null) {
         final Cell cell = poiRow.getCell(column);
         if (cell != null) {
            final String stringValue = getCellValue(cell);
            value = Optional.of(stringValue);
         }
      }
      return value;
   }

   @Nullable
   private String getCellValue(Cell cell) {
      int cellType = cell.getCellType();
      switch (cellType) {
         case CELL_TYPE_BLANK: return null;
         case CELL_TYPE_NUMERIC:
            if (isInteger(cell.getNumericCellValue())) {
               return String.valueOf((int) cell.getNumericCellValue());
            } else {
               return String.valueOf(cell.getNumericCellValue());
            }
         case CELL_TYPE_STRING: return cell.getStringCellValue();
         case CELL_TYPE_FORMULA: return String.valueOf(cell.getStringCellValue());
         case CELL_TYPE_BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
         case CELL_TYPE_ERROR: return null;
         default: return null;
      }
   }

   private boolean isInteger(double number) {
      return (number == Math.floor(number) && !Double.isInfinite(number));
   }
}

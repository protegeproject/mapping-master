package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
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

   public int getStartColumnNumber() { // 1-based index
      return 1;
   }

   public int getEndColumnNumber() { // 1-based index
      int endColumn = 1;
      for (int row = getStartRowNumber(); row <= getEndRowNumber(); row++) {
         Row poiRow = poiSheet.getRow(row-1);
         if (poiRow != null) {
            int currentEndColumn = poiRow.getLastCellNum();
            if (currentEndColumn > endColumn) {
               endColumn = currentEndColumn;
            }
         }
      }
      return endColumn;
   }

   public int getStartRowNumber() { // 1-based index
      return 1;
   }

   public int getEndRowNumber() { // 1-based index
      return poiSheet.getLastRowNum() + 1;
   }

   public void addValueToCell(int column, int row, boolean value) { // 1-based index
      Row poiRow = poiSheet.getRow(row-1);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row-1);
      }
      poiRow.createCell(column-1).setCellValue(value);
   }

   public void addValueToCell(int column, int row, double value) { // 1-based index
      Row poiRow = poiSheet.getRow(row-1);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row-1);
      }
      poiRow.createCell(column-1).setCellValue(value);
   }

   public void addValueToCell(int column, int row, String value) { // 1-based index
      Row poiRow = poiSheet.getRow(row-1);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row-1);
      }
      poiRow.createCell(column-1).setCellValue(value);
   }

   public void addValueToCell(int column, int row, Date value) { // 1-based index
      Row poiRow = poiSheet.getRow(row-1);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row-1);
      }
      Cell cell = poiRow.createCell(column-1);
      cell.setCellValue(value);
      setValueAsDate(cell);
   }

   private void setValueAsDate(Cell cell) {
      CellStyle cellStyle = poiSheet.getWorkbook().createCellStyle();
      CreationHelper createHelper = poiSheet.getWorkbook().getCreationHelper();
      cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-ddThh:mm:ss"));
      cell.setCellStyle(cellStyle);
   }

   public Optional<String> getValueFromCell(int column, int row) { // 1-based index
      final Row poiRow = poiSheet.getRow(row-1);
      Optional<String> value = Optional.empty();
      if (poiRow != null) {
         final Cell cell = poiRow.getCell(column-1);
         if (cell != null) {
            final String stringValue = getCellValue(cell);
            value = Optional.ofNullable(stringValue);
         }
      }
      return value;
   }

   @Nullable
   private String getCellValue(Cell cell) {
      CellType cellType = cell.getCellType();
      switch (cellType) {
         case BLANK: return null;
         case NUMERIC:
            double value = cell.getNumericCellValue();
            if (DateUtil.isCellDateFormatted(cell)) {
               String pattern = "yyyy-MM-dd'T'hh:mm:ss";  // ISO 8601
               return new SimpleDateFormat(pattern).format(cell.getDateCellValue());
            } else if (isInteger(value)) {
               return String.valueOf((int) value);
            } else {
               return String.valueOf(value);
            }
         case STRING: return cell.getStringCellValue();
         case FORMULA: return String.valueOf(cell.getStringCellValue());
         case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
         case ERROR: return null;
         default: return null;
      }
   }

   private boolean isInteger(double number) {
      return (number == Math.floor(number) && !Double.isInfinite(number));
   }

   @Override
   public String toString() {
      return getSheetName();
   }
}

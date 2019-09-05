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

   public void addValueToCell(int column, int row, boolean value) {
      Row poiRow = poiSheet.getRow(row);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row);
      }
      poiRow.createCell(column).setCellValue(value);
   }

   public void addValueToCell(int column, int row, double value) {
      Row poiRow = poiSheet.getRow(row);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row);
      }
      poiRow.createCell(column).setCellValue(value);
   }

   public void addValueToCell(int column, int row, String value) {
      Row poiRow = poiSheet.getRow(row);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row);
      }
      poiRow.createCell(column).setCellValue(value);
   }

   public void addValueToCell(int column, int row, Date value) {
      Row poiRow = poiSheet.getRow(row);
      if (poiRow == null) {
         poiRow = poiSheet.createRow(row);
      }
      Cell cell = poiRow.createCell(column);
      cell.setCellValue(value);
      setValueAsDate(cell);
   }

   private void setValueAsDate(Cell cell) {
      CellStyle cellStyle = poiSheet.getWorkbook().createCellStyle();
      CreationHelper createHelper = poiSheet.getWorkbook().getCreationHelper();
      cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-ddThh:mm:ss"));
      cell.setCellStyle(cellStyle);
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
}

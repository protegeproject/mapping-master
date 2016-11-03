package org.mm.workbook;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mm.exceptions.MappingMasterException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class SpreadSheetUtil {

   public static String columnRow2Name(int columnIndex, int rowIndex) // 1-based
   {
      return columnNumber2Name(columnIndex) + rowIndex;
   }

   public static String columnNumber2Name(int columnIndex) // 1-based
   {
      String col = "";
      while (columnIndex > 0) {
         columnIndex--;
         col = (char) (columnIndex % 26 + 65) + col;
         columnIndex = columnIndex / 26;
      }
      return col;
   }

   public static int columnName2Number(String columnName) throws MappingMasterException {
      int columnNumber = 0;
      for (int i = 0; i < columnName.length(); i++) {
         columnNumber *= 26;
         try {
            char c = columnName.charAt(i);
            if (!isAlpha(c)) {
               throw new MappingMasterException("Invalid column name: '" + columnName + "'");
            }
            columnNumber += Integer.parseInt(String.valueOf(c), 36) - 9;
         } catch (NumberFormatException e) {
            throw new MappingMasterException("Invalid column name: '" + columnName + "'");
         }
      }
      return columnNumber; // 0-indexed
   }

   public static int rowLabel2Number(String rowLabel) throws MappingMasterException {
      try {
         return Integer.parseInt(rowLabel);
      } catch (NumberFormatException e) {
         throw new MappingMasterException("Invalid row label: '" + rowLabel + "'");
      }
   }

   public static Sheet getSheet(Workbook workbook, String sheetName) throws MappingMasterException {
      return workbook.getSheet(sheetName);
   }

   private static boolean isAlpha(char c) {
      return c >= 'A' && c <= 'Z';
   }
}

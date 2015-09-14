package org.mm.ss;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mm.core.MappingExpression;
import org.mm.exceptions.MappingMasterException;

public class SpreadSheetUtil
{
   static public void checkColumnSpecification(String columnSpecification) throws MappingMasterException
   {
      if (columnSpecification.isEmpty()) throw new MappingMasterException("empty column specification");

      if (!columnSpecification.equals(MappingExpression.EndWildcard)) {
         for (int i = 0; i < columnSpecification.length(); i++) {
            char c = columnSpecification.charAt(i);
            if (!isAlpha(c)) throw new MappingMasterException("invalid column specification " + columnSpecification);
         }
      }
   }

   static public void checkRowSpecification(String rowSpecification) throws MappingMasterException
   {
      if (rowSpecification.isEmpty()) throw new MappingMasterException("empty row specification");

      if (!rowSpecification.equals(MappingExpression.EndWildcard)) {
         for (int i = 0; i < rowSpecification.length(); i++) {
            char c = rowSpecification.charAt(i);
            if (!isNumeric(c)) throw new MappingMasterException("invalid row specification " + rowSpecification);
         }
      }
   }

   static public int columnName2Number(String columnName) throws MappingMasterException
   {
      int pos = 0;

      checkColumnSpecification(columnName);

      for (int i = 0; i < columnName.length(); i++) {
         pos *= 26;
         try {
            pos += Integer.parseInt(columnName.substring(i, i + 1), 36) - 9;
         } catch (NumberFormatException e) {
            throw new MappingMasterException("invalid column name " + columnName);
         }
      }
      return pos;
   }

   // TODO Check column and row
   static public String columnRow2Name(int column, int row)
   {
      return columnNumber2Name(column) + row;
   }

   static public String columnNumber2Name(int pos) // 1-based
   {
      String col = "";
      while (pos > 0) {
         pos--;
         col = (char) (pos % 26 + 65) + col;
         pos = pos / 26;
      }
      return col;
   }

   static public int row2Number(String row) throws MappingMasterException
   {
      if (row.isEmpty()) throw new MappingMasterException("empty row number");

      try {
         return Integer.parseInt(row);
      } catch (NumberFormatException e) {
         throw new MappingMasterException(row + " is not a valid row number");
      }
   }

   public static int getColumnNumber(Sheet sheet, String columnSpecification) throws MappingMasterException
   {
      checkColumnSpecification(columnSpecification);
      int columnNumber = columnName2Number(columnSpecification);
      return columnNumber; // 0-indexed
   }

   public static int getRowNumber(Sheet sheet, String rowSpecification) throws MappingMasterException
   {
      checkRowSpecification(rowSpecification);
      int rowNumber = Integer.parseInt(rowSpecification);
      return rowNumber; // 0-indexed
   }

   public static Sheet getSheet(Workbook workbook, String sheetName) throws MappingMasterException
   {
      Sheet sheet = workbook.getSheet(sheetName);

      if (sheet == null) throw new MappingMasterException("invalid sheet name " + sheetName);

      return sheet;
   }

   private static boolean isAlpha(char c)
   {
      return c >= 'A' && c <= 'Z';
   }

   private static boolean isNumeric(char c)
   {
      return c >= '0' && c <= '9';
   }
}

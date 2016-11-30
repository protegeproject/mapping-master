package org.mm.renderer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class AbstractRendererTest {

   private Workbook workbook;

   protected void createEmptyExcelWorkbook() {
      workbook = new XSSFWorkbook();
   }

   protected Workbook getWorkbook() {
      return workbook;
   }

   protected void addCell(String sheetName, int columnNumber, int rowNumber, String content) {
      Sheet sheet = workbook.getSheet(sheetName);
      if (sheet == null) {
         sheet = workbook.createSheet(sheetName);
      }
      Row row = sheet.getRow(toZeroBasedIndexing(rowNumber));
      if (row == null) {
         row = sheet.createRow(toZeroBasedIndexing(rowNumber));
      }
      row.createCell(toZeroBasedIndexing(columnNumber)).setCellValue(content);
   }

   private static int toZeroBasedIndexing(int indexNumber) {
      return indexNumber - 1;
   }
}

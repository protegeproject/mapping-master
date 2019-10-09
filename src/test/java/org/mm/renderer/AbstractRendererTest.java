package org.mm.renderer;

import java.util.Date;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class AbstractRendererTest {

   private Workbook workbook;

   protected void createEmptyWorkbook() {
      workbook = Workbook.createEmptyWorkbook();
      workbook.createSheet("Sheet1");
   }

   protected Workbook getWorkbook() {
      return workbook;
   }

   protected void createCell(String sheetName, int columnNumber, int rowNumber, boolean value) { // 1-based index
      Sheet sheet = workbook.getSheet(sheetName);
      if (sheet == null) {
         sheet = workbook.createSheet(sheetName);
      }
      sheet.addValueToCell(columnNumber, rowNumber, value);
   }

   protected void createCell(String sheetName, int columnNumber, int rowNumber, double value) { // 1-based index
      Sheet sheet = workbook.getSheet(sheetName);
      if (sheet == null) {
         sheet = workbook.createSheet(sheetName);
      }
      sheet.addValueToCell(columnNumber, rowNumber, value);
   }

   protected void createCell(String sheetName, int columnNumber, int rowNumber, String value) { // 1-based index
      Sheet sheet = workbook.getSheet(sheetName);
      if (sheet == null) {
         sheet = workbook.createSheet(sheetName);
      }
      sheet.addValueToCell(columnNumber, rowNumber, value);
   }

   protected void createCell(String sheetName, int columnNumber, int rowNumber, Date value) { // 1-based index
      Sheet sheet = workbook.getSheet(sheetName);
      if (sheet == null) {
         sheet = workbook.createSheet(sheetName);
      }
      sheet.addValueToCell(columnNumber, rowNumber, value);
   }
}

package org.mm.renderer;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class AbstractRendererTest {

   private Workbook workbook;

   protected void createEmptyWorkbook() {
      workbook = Workbook.createEmptyWorkbook();
   }

   protected Workbook getWorkbook() {
      return workbook;
   }

   protected void createCell(String sheetName, int columnNumber, int rowNumber, String value) {
      Sheet sheet = workbook.getSheet(sheetName);
      if (sheet == null) {
         sheet = workbook.createSheet(sheetName);
      }
      sheet.addValueToCell(columnNumber-1, columnNumber-1, value);
   }
}

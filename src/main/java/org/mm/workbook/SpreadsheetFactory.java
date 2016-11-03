package org.mm.workbook;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class SpreadsheetFactory {

   public static SpreadSheetDataSource createEmptyWorkbook() {
      Workbook emptyWorkbook = new XSSFWorkbook(); // an empty xlsx workbook
      return new SpreadSheetDataSource(emptyWorkbook);
   }

   public static SpreadSheetDataSource loadWorkbookFromDocument(String path) throws Exception {
      Workbook workbook = WorkbookFactory.create(new FileInputStream(path));
      return new SpreadSheetDataSource(workbook);
   }

   public static SpreadSheetDataSource loadWorkbookFromDocument(File file) throws Exception {
      Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
      return new SpreadSheetDataSource(workbook);
   }
}

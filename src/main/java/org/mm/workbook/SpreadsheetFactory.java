package org.mm.workbook;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class SpreadsheetFactory {

   public static Workbook createEmptyWorkbook() {
      org.apache.poi.ss.usermodel.Workbook emptyWorkbook = new XSSFWorkbook(); // an empty xlsx workbook
      return new WorkbookImpl(emptyWorkbook);
   }

   public static Workbook loadWorkbookFromDocument(String path) throws Exception {
      org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(new FileInputStream(path));
      return new WorkbookImpl(workbook);
   }

   public static Workbook loadWorkbookFromDocument(File file) throws Exception {
      org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
      return new WorkbookImpl(workbook);
   }
}

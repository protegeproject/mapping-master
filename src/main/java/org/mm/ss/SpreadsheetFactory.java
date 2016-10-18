package org.mm.ss;

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

   public static Workbook createEmptyWorkbook() {
      return new XSSFWorkbook(); // an empty xlsx workbook
   }

   public static Workbook loadWorkbookFromDocument(String path) throws Exception {
      return WorkbookFactory.create(new FileInputStream(path));
   }

   public static Workbook loadWorkbookFromDocument(File file) throws Exception {
      return WorkbookFactory.create(new FileInputStream(file));
   }
}

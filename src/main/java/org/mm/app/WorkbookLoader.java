package org.mm.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class WorkbookLoader {

   public static Workbook loadWorkbook(File file) throws Exception {
      return WorkbookFactory.create(new FileInputStream(file));
   }

   public static Workbook loadWorkbook(InputStream inputStream) throws Exception {
      return WorkbookFactory.create(inputStream);
   }
}

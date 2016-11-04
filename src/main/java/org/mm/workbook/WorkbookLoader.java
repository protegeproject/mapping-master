package org.mm.workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class WorkbookLoader {

   public static Workbook loadWorkbook(File file) throws Exception {
      return new WorkbookImpl(WorkbookFactory.create(new FileInputStream(file)));
   }

   public static Workbook loadWorkbook(InputStream inputStream) throws Exception {
      return new WorkbookImpl(WorkbookFactory.create(inputStream));
   }
}

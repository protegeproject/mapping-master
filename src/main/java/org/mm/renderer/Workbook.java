package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mm.renderer.internal.CellAddress;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class Workbook {

   private final org.apache.poi.ss.usermodel.Workbook workbook;

   public Workbook(@Nonnull org.apache.poi.ss.usermodel.Workbook workbook) {
      this.workbook = checkNotNull(workbook);
   }

   public static Workbook createEmptyWorkbook() {
      return new Workbook(new XSSFWorkbook());
   }
   
   public Sheet createSheet(String sheetName) {
      return new Sheet(workbook.createSheet(sheetName));
   }

   @Nullable
   public Sheet getSheet(String sheetName) {
      org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(sheetName);
      return (sheet != null) ? new Sheet(sheet) : null;
   }

   public Sheet getSheet(int sheetIndex) {
      return new Sheet(workbook.getSheetAt(sheetIndex));
   }

   public List<Sheet> getSheets() {
      List<Sheet> sheets = Lists.newArrayList();
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
         sheets.add(getSheet(i));
      }
      return sheets;
   }

   public List<String> getSheetNames() {
      List<String> names = Lists.newArrayList();
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
         names.add(workbook.getSheetName(i));
      }
      return names;
   }

   public Optional<String> getCellValue(CellAddress cellAddress) {
      final Sheet sheet = getSheet(cellAddress.getSheetName());
      return sheet.getValueFromCell(cellAddress.getColumnNumber(), cellAddress.getRowNumber());
   }
}

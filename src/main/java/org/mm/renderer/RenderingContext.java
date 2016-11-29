package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class RenderingContext {

   private final String sheetName;
   private final int startColumn;
   private final int endColumn;
   private final int startRow;
   private final int endRow;

   public RenderingContext(@Nonnull String sheetName, int startColumn, int endColumn, int startRow,
         int endRow) {
      this.sheetName = checkNotNull(sheetName);
      this.startColumn = startColumn;
      this.endColumn = endColumn;
      this.startRow = startRow;
      this.endRow = endRow;
   }

   public String getSheetName() {
      return sheetName;
   }

   public int getStartColumn() {
      return startColumn;
   }

   public int getEndColumn() {
      return endColumn;
   }

   public int getStartRow() {
      return startRow;
   }

   public int getEndRow() {
      return endRow;
   }
}

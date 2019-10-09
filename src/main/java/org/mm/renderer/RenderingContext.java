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

   // All are in 1-based index (i.e., column A = 1, row 1 = 1)
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

   public int getStartColumn() { // 1-based index
      return startColumn;
   }

   public int getEndColumn() { // 1-based index
      return endColumn;
   }

   public int getStartRow() { // 1-based index
      return startRow;
   }

   public int getEndRow() { // 1-based index
      return endRow;
   }

   public Iterator getIterator() {
      return new Iterator(startColumn, startRow);
   }

   public class Iterator {

      private int column;
      private int row;

      public Iterator(int column, int row) {
         this.column = column;
         this.row = row - 1;
      }

      public CellCursor getCursor() {
         return new CellCursor(sheetName, column, row);
      }

      public boolean next() {
         row = row + 1;
         if (row <= endRow) {
            return true;
         } else {
            column = column + 1;
            row = startRow;
            if (column <= endColumn) {
               return true;
            } else {
               return false;
            }
         }
      }
   }
}

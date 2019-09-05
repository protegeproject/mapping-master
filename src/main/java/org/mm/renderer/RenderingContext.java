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

   public Iterator getIterator() {
      return new Iterator(startColumn-1, startRow);
   }

   public class Iterator {

      private int column;
      private int row;

      public Iterator(int column, int row) {
         this.column = column;
         this.row = row;
      }

      public CellCursor getCursor() {
         return new CellCursor(sheetName, column, row);
      }

      public boolean next() {
         boolean hasNext = true;
         if (moveToNextColumn()) {
            column = startColumn-1;
            if (moveToNextRow()) {
               hasNext = false;
            }
         }
         return hasNext;
      }

      private boolean moveToNextColumn() {
         column++;
         return column > getEndColumn();
      }

      private boolean moveToNextRow() {
         row++;
         return row > getEndRow();
      }
   }
}

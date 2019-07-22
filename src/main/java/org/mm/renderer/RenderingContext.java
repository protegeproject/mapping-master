package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class RenderingContext {

   private final String sheetName;
   private final int fromColumn;
   private final int toColumn;
   private final int fromRow;
   private final int toRow;

   public RenderingContext(@Nonnull String sheetName, int fromColumn, int toColumn, int fromRow,
         int toRow) {
      this.sheetName = checkNotNull(sheetName);
      this.fromColumn = fromColumn;
      this.toColumn = toColumn;
      this.fromRow = fromRow;
      this.toRow = toRow;
   }

   public String getSheetName() {
      return sheetName;
   }

   public int getStartColumn() {
      return fromColumn;
   }

   public int getEndColumn() {
      return toColumn;
   }

   public int getStartRow() {
      return fromRow;
   }

   public int getEndRow() {
      return toRow;
   }

   public Iterator getIterator() {
      return new Iterator(fromColumn-1, fromRow);
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
            column = fromColumn-1;
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

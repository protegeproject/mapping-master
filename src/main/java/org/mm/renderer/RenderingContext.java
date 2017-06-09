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

   private Iterator iterator = new Iterator();

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

   public int getCurrentColumn() {
      return iterator.getColumn();
   }
   
   public int getCurrentRow() {
      return iterator.getRow();
   }

   public boolean hasNextCell() {
      return iterator.hasNextCell();
   }

   private class Iterator {

      private int currentColumn = startColumn;
      private int currentRow = startRow;

      private int getColumn() {
         return currentColumn;
      }

      private int getRow() {
         return currentRow;
      }

      private void setColumn(int column) {
         currentColumn = column;
      }

      private void setRow(int row) {
         currentRow = row;
      }

      public boolean hasNextCell() {
         boolean hasNext = true;
         
         int lastColumn = getColumn();
         int lastRow = getRow();
         
         int nextRow = lastRow++;
         if (isRowOutOfBoundary(nextRow)) {
            int nextColumn = lastColumn++;
            if (isColumnOutOfBoundary(nextColumn)) {
               hasNext = false;
            }
            nextRow = startRow;
         }
         if (hasNext) {
            setColumn(lastColumn);
            setRow(lastRow);
         }
         return hasNext;
      }

      private boolean isColumnOutOfBoundary(int nextColumn) {
         return nextColumn > endColumn;
      }

      private boolean isRowOutOfBoundary(int nextRow) {
         return nextRow > endRow;
      }
   }
}

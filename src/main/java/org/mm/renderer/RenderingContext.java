package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import org.mm.renderer.internal.CellUtils;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class RenderingContext {

   private static final String ANY_WILDCARD = "+";

   private final Sheet sheet;
   private final String startColumn;
   private final String endColumn;
   private final String startRow;
   private final String endRow;

   public RenderingContext(@Nonnull Sheet sheet, @Nonnull String startColumn, @Nonnull String endColumn,
         @Nonnull String startRow, @Nonnull String endRow) {
      this.sheet = checkNotNull(sheet);
      this.startColumn = checkNotNull(startColumn);
      this.endColumn = checkNotNull(endColumn);
      this.startRow = checkNotNull(startRow);
      this.endRow = checkNotNull(endRow);
   }

   public String getSheetName() {
      return sheet.getSheetName();
   }

   public String getStartColumn() {
      return startColumn;
   }

   public String getEndColumn() {
      return endColumn;
   }

   public String getStartRow() {
      return startRow;
   }

   public String getEndRow() {
      return endRow;
   }

   public int getStartColumnIndex() {
      return CellUtils.toColumnIndex(startColumn);
   }

   public int getEndColumnIndex() {
      return (ANY_WILDCARD.equals(endColumn)) ? sheet.getEndColumnIndex() : CellUtils.toColumnIndex(endColumn);
   }

   public int getStartRowIndex() {
      return CellUtils.toRowIndex(startRow);
   }

   public int getEndRowIndex() {
      return (ANY_WILDCARD.equals(endRow)) ? sheet.getEndRowIndex() : CellUtils.toRowIndex(endRow);
   }

   public Iterator getIterator() {
      return new Iterator(getStartColumnIndex()-1, getStartRowIndex());
   }

   public class Iterator {

      private int column;
      private int row;

      public Iterator(int column, int row) {
         this.column = column;
         this.row = row;
      }

      public CellCursor getCursor() {
         return new CellCursor(getSheetName(), column, row);
      }

      public boolean next() {
         boolean hasNext = true;
         if (moveToNextColumn()) {
            column = getStartColumnIndex()-1;
            if (moveToNextRow()) {
               hasNext = false;
            }
         }
         return hasNext;
      }

      private boolean moveToNextColumn() {
         column++;
         return column > getEndColumnIndex();
      }

      private boolean moveToNextRow() {
         row++;
         return row > getEndRowIndex();
      }
   }
}

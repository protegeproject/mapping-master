package org.mm.renderer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class CellCursor {

   private final String sheetName;
   private final int column;
   private final int row;

   public CellCursor(@Nonnull String sheetName, int column, int row) {
      this.sheetName = checkNotNull(sheetName);
      this.column = column;
      this.row = row;
   }

   public static CellCursor getDefaultCursor() {
      return new CellCursor("Sheet1", 0, 0);
   }

   /**
    * Returns the sheet name.
    * 
    * @return The sheet name
    */
   public String getSheetName() {
      return sheetName;
   }

   /**
    * Returns the column position (0-based ordering).
    * 
    * @return A number representing the column index position.
    */
   public int getColumn() {
      return column;
   }

   /**
    * Returns the row position (0-based ordering).
    * 
    * @return A number representing the column index position.
    */
   public int getRow() {
      return row;
   }
}

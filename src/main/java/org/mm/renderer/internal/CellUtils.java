package org.mm.renderer.internal;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class CellUtils {

   /**
    * Returns the logical column (not physical) 0-based ordering. For example, the column label "D"
    * has an index number of 3.
    * 
    * @param columnLabel Column label string (e.g., "A", "B", "AA", etc.)
    * 
    * @return A number representing the column index.
    */
   public static int toColumnIndex(String columnLabel) {
      return toColumnNumber(columnLabel) - 1;
   }

   /**
    * Returns the physical column 1-based ordering. For example, the column label "D" has a physical
    * number of 4.
    * 
    * @param columnLabel Column label string (e.g., "A", "B", "AA", etc.)
    * 
    * @return A number representing the column number.
    */
   public static int toColumnNumber(String columnLabel) {
      int index = 0;
      for (int i = 0; i < columnLabel.length(); i++) {
         index *= 26;
         char c = columnLabel.charAt(i);
         index += Integer.parseInt(String.valueOf(c), 36) - 9;
      }
      return index;
   }

   /**
    * Returns the logical row (not physical) 0-based ordering. For example, the row label "2" has an
    * index number of 1.
    * 
    * @param rowLabel Row label string (e.g., "1", "2", "99", etc.)
    * 
    * @return A number representing the row index.
    */
   public static int toRowIndex(String rowLabel) {
      return toRowNumber(rowLabel) - 1;
   }

   /**
    * Returns the physical row 1-based ordering. For example, the row label "2" has a physical
    * number of 2.
    * 
    * @param rowLabel Row label string (e.g., "1", "2", "99", etc.)
    * 
    * @return A number representing the row number.
    */
   public static int toRowNumber(String rowLabel) {
      return Integer.parseInt(rowLabel);
   }
}

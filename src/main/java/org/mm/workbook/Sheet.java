package org.mm.workbook;

/**
 * Represents the spreadsheet in a workbook.
 *
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public interface Sheet {

   /**
    * Returns the sheet name
    */
   String getName();

   /**
    * Returns the last row index
    */
   int getLastRowIndex();

   /**
    * Returns the last column index at the given row.
    */
   int getLastColumnIndexAt(int whichRow);

   /**
    * Returns the cell value given the cell coordinate (row, column)
    */
   Object getCellValue(int row, int column);
}

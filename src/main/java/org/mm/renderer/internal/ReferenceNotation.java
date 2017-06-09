package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceNotation implements Argument {

   public static final String INDEX_WILDCARD = "*";

   public static final String DEFAULT_SHEET_NAME = "Sheet1";
   public static final int DEFAULT_COLUMN_NUMBER = 1;
   public static final int DEFAULT_ROW_NUMBER = 1;

   private final Optional<String> sheetName;
   private final String columnNotation;
   private final String rowNotation;

   private String defaultSheetName = DEFAULT_SHEET_NAME;
   private int defaultColumnNumber = DEFAULT_COLUMN_NUMBER;
   private int defaultRowNumber = DEFAULT_ROW_NUMBER;

   public ReferenceNotation(@Nonnull Optional<String> sheetName, @Nonnull String columnNotation,
         @Nonnull String rowNotation) {
      this.sheetName = checkNotNull(sheetName);
      this.columnNotation = checkNotNull(columnNotation);
      this.rowNotation = checkNotNull(rowNotation);
   }

   public String getSheetName() {
      return sheetName.orElse("");
   }

   public String getColumnName() {
      return columnNotation;
   }

   public String getRowNumber() {
      return rowNotation;
   }

   public ReferenceNotation setContext(@Nonnull String sheetName, int columnNumber, int rowNumber) {
      defaultSheetName = checkNotNull(sheetName);
      defaultColumnNumber = columnNumber;
      defaultRowNumber = rowNumber;
      return this;
   }

   public CellAddress toCellAddress() {
      return new CellAddress(
            getSheetNameOrDefault(),
            getColumnNumberOrDefault(),
            getRowNumberOrDefault());
   }

   private String getSheetNameOrDefault() {
      return sheetName.orElse(defaultSheetName);
   }

   private int getColumnNumberOrDefault() {
      return !isWildcard(columnNotation) ? columnName2Number(columnNotation) : defaultColumnNumber;
   }

   private int getRowNumberOrDefault() {
      return !isWildcard(rowNotation) ? Integer.parseInt(rowNotation) : defaultRowNumber;
   }

   private int columnName2Number(String columnName) {
      int columnNumber = 0;
      for (int i = 0; i < columnName.length(); i++) {
         columnNumber *= 26;
         char c = columnName.charAt(i);
         columnNumber += Integer.parseInt(String.valueOf(c), 36) - 9;
      }
      return columnNumber;
   }

   private boolean isWildcard(String notation) {
      return notation.equals(INDEX_WILDCARD);
   }
}

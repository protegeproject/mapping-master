package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceNotation {

   public static final String INDEX_WILDCARD = "*";

   private final Optional<String> specifiedSheetName;
   private final String cellNotation;

   public ReferenceNotation(@Nonnull Optional<String> specifiedSheetName, @Nonnull String cellNotation) {
      this.specifiedSheetName = checkNotNull(specifiedSheetName);
      this.cellNotation = checkNotNull(cellNotation);
   }

   public Optional<String> getSheetName() {
      return specifiedSheetName;
   }

   public String getCellNotation() {
      return cellNotation;
   }

   public CellAddress apply(@Nonnull String sheetName, int column, int row) {
      NotationSolver notationSolver = new NotationSolver().apply(sheetName, column, row);
      return new CellAddress(
            notationSolver.getSheetName(),
            notationSolver.getColumn(),
            notationSolver.getRow());
   }

   private class NotationSolver {

      private final Pattern cellNotationPattern = Pattern.compile("([A-Z]+|\\*)(\\d+|\\*)");

      private String sheetName;
      private int column;
      private int row;

      private NotationSolver apply(@Nonnull String sheetName, int column, int row) {
         this.sheetName = checkNotNull(sheetName);
         this.column = column;
         this.row = row;
         return this;
      }

      private String getSheetName() {
         return specifiedSheetName.orElse(sheetName);
      }

      private int getColumn() {
         Matcher matcher = cellNotationPattern.matcher(cellNotation);
         if (matcher.matches()) {
            String columnSymbol = matcher.group(1);
            if (columnSymbol.equals(INDEX_WILDCARD)) {
               return column;
            } else {
               return columnName2Number(columnSymbol);
            }
         }
         throw new RuntimeException("Unable to read the cell reference notation: " + cellNotation);
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

      private int getRow() {
         Matcher matcher = cellNotationPattern.matcher(cellNotation);
         if (matcher.matches()) {
            String rowSymbol = matcher.group(2);
            if (rowSymbol.equals(INDEX_WILDCARD)) {
               return row;
            } else {
               return Integer.parseInt(rowSymbol);
            }
         }
         throw new RuntimeException("Unable to read the cell reference notation: " + cellNotation);
      }
   }
}

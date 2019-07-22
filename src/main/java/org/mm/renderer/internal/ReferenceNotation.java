package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceNotation implements Argument {

   private final String cellReference;

   private static final Pattern CELL_REF_PATTERN =
         Pattern.compile("([A-Za-z]+|\\*)([0-9]+|\\*)");

   public ReferenceNotation(@Nonnull String cellReference) {
      this.cellReference = checkNotNull(cellReference);
   }

   public ColumnReference getColumnReference() {
      Matcher m = checkPatternValid(cellReference);
      return new ColumnReference(m.group(1));
   }

   public RowReference getRowReference() {
      Matcher m = checkPatternValid(cellReference);
      return new RowReference(m.group(2));
   }

   private static Matcher checkPatternValid(String s) {
      Matcher cellRefPatternMatcher = CELL_REF_PATTERN.matcher(s);
      if (!cellRefPatternMatcher.matches()) {
         throw new RuntimeException("Cell reference notation is invalid");
      }
      return cellRefPatternMatcher;
   }

   public class ColumnReference {

      public static final String INDEX_WILDCARD = "*";

      private final String columnReference;

      public ColumnReference(@Nonnull String columnReference) {
         this.columnReference = checkNotNull(columnReference);
      }

      public String getString() {
         return columnReference;
      }

      public boolean isWildcard() {
         return columnReference.equals(INDEX_WILDCARD);
      }

      @Override
      public String toString() {
         return columnReference;
      }
   }

   public class RowReference {

      public static final String INDEX_WILDCARD = "*";

      private final String rowReference;

      public RowReference(@Nonnull String rowReference) {
         this.rowReference = checkNotNull(rowReference);
      }

      public String getString() {
         return rowReference;
      }

      public boolean isWildcard() {
         return rowReference.equals(INDEX_WILDCARD);
      }

      @Override
      public String toString() {
         return rowReference;
      }
   }
}

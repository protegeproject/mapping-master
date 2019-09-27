package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class LiteralValue implements Value, Argument {

   private final String value;
   private final String datatype;
   private final boolean isFromWorkbook;

   public LiteralValue(@Nonnull String value, @Nonnull String datatype, boolean isFromWorkbook) {
      this.value = checkNotNull(value);
      this.datatype = checkNotNull(datatype);
      this.isFromWorkbook = isFromWorkbook;
   }

   public static LiteralValue create(@Nonnull Object value, @Nonnull String datatype) {
      return new LiteralValue(String.valueOf(value), datatype, false);
   }

   @Override
   public String getString() {
      return value;
   }

   @Override
   public LiteralValue update(String newValue) {
      return new LiteralValue(newValue, datatype, isFromWorkbook);
   }

   @Override
   public boolean isFromWorkbook() {
      return isFromWorkbook;
   }

   public String getDatatype() {
      return datatype;
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof LiteralValue)) {
         return false;
      }
      LiteralValue other = (LiteralValue) o;
      return Objects.equal(value, other.getString())
            && Objects.equal(datatype, other.getDatatype())
            && Objects.equal(isFromWorkbook, other.isFromWorkbook());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(value, datatype, isFromWorkbook);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(value)
            .addValue(datatype)
            .addValue(isFromWorkbook)
            .toString();
   }
}

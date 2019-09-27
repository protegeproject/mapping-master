package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public abstract class IriValue implements Value {

   private final String value;
   private final boolean isFromWorkbook;

   public IriValue(@Nonnull String value, boolean isFromWorkbook) {
      this.value = checkNotNull(value);
      this.isFromWorkbook = isFromWorkbook;
   }

   @Override
   public String getString() {
      return value;
   }

   @Override
   public boolean isFromWorkbook() {
      return isFromWorkbook;
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof IriValue)) {
         return false;
      }
      IriValue other = (IriValue) o;
      return Objects.equal(value, other.getString())
            && Objects.equal(isFromWorkbook, other.isFromWorkbook());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(value, isFromWorkbook);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(value)
            .addValue(isFromWorkbook)
            .toString();
   }
}

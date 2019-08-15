package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class IriValue implements Value {

   private final String iriString;

   public IriValue(@Nonnull String iriString) {
      this.iriString = checkNotNull(iriString);
   }

   @Override
   public String getString() {
      return iriString;
   }

   @Override
   public IriValue update(String newIriString) {
      return new IriValue(newIriString);
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
      return Objects.equal(iriString, other.getString());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(iriString);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(iriString)
            .toString();
   }
}

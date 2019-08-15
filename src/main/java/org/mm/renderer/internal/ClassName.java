package org.mm.renderer.internal;

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ClassName extends ReferencedValue  {

   public ClassName(@Nonnull String prefixedName) {
      super(prefixedName);
   }

   @Override
   public ClassName update(String newPrefixedName) {
      return new ClassName(newPrefixedName);
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof ClassName)) {
         return false;
      }
      ClassName other = (ClassName) o;
      return Objects.equal(this.getString(), other.getString());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.getString());
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(this.getString())
            .toString();
   }
}

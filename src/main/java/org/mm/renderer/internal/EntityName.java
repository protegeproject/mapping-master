package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class EntityName implements Value {

   private final String prefixedName;

   public EntityName(@Nonnull String prefixedName) {
      this.prefixedName = checkNotNull(prefixedName);
   }

   @Override
   public String getString() {
      return prefixedName;
   }

   @Override
   public EntityName update(String newPrefixedName) {
      return new EntityName(newPrefixedName);
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof EntityName)) {
         return false;
      }
      EntityName other = (EntityName) o;
      return Objects.equal(prefixedName, other.getString());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(prefixedName);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(prefixedName)
            .toString();
   }
}

package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class BuiltInFunction {

   private final int functionType;
   private final List<Argument> arguments;

   public BuiltInFunction(int functionType, @Nonnull List<Argument> arguments) {
      this.functionType = functionType;
      this.arguments = checkNotNull(arguments);
   }

   public int getFunctionType() {
      return functionType;
   }

   public List<Argument> getArguments() {
      return arguments;
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof BuiltInFunction)) {
         return false;
      }
      BuiltInFunction other = (BuiltInFunction) o;
      return functionType == other.getFunctionType()
            && Objects.equal(arguments, other.getArguments());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(functionType, arguments);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(functionType)
            .addValue(arguments)
            .toString();
   }
}

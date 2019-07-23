package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Optional;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class PlainLiteralValue implements Value {

   private final String value;
   private final String language;

   private PlainLiteralValue(@Nonnull String value, @Nonnull String language) {
      this.value = checkNotNull(value);
      this.language = checkNotNull(language);
   }

   public static PlainLiteralValue createPlainLiteral(String value, String language) {
      return new PlainLiteralValue(value, language);
   }

   public static PlainLiteralValue createPlainLiteral(String value) {
      return new PlainLiteralValue(value, "");
   }

   @Override
   public String getString() {
      return value;
   }

   public Optional<String> getLanguage() {
      return language.isEmpty() ? Optional.empty() : Optional.of(language);
   }

   public String getDatatype() {
      return "rdf:PlainLiteral";
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof PlainLiteralValue)) {
         return false;
      }
      PlainLiteralValue other = (PlainLiteralValue) o;
      return Objects.equal(value, other.getString())
            && Objects.equal(language, other.getLanguage());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(value, language);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(value)
            .addValue(language)
            .toString();
   }
}

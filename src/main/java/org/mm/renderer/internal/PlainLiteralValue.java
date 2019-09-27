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
   private final boolean isFromWorkbook;

   public PlainLiteralValue(@Nonnull String value, @Nonnull String language, boolean isFromWorkbook) {
      this.value = checkNotNull(value);
      this.language = checkNotNull(language);
      this.isFromWorkbook = isFromWorkbook;
   }

   public static PlainLiteralValue create(String value, String language) {
      return new PlainLiteralValue(value, language, false);
   }

   public static PlainLiteralValue create(String value) {
      return new PlainLiteralValue(value, "", false);
   }

   @Override
   public String getString() {
      return value;
   }

   @Override
   public PlainLiteralValue update(String newValue) {
      return new PlainLiteralValue(newValue, language, isFromWorkbook);
   }

   @Override
   public boolean isFromWorkbook() {
      return isFromWorkbook;
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
            && Objects.equal(language, other.getLanguage())
            && Objects.equal(isFromWorkbook, other.isFromWorkbook());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(value, language, isFromWorkbook);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(value)
            .addValue(language)
            .addValue(isFromWorkbook)
            .toString();
   }
}

package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class UntypedValue implements Value {

   private final String value;
   private final String datatype;
   private final String language;
   private final boolean isFromWorkbook;

   public UntypedValue(@Nonnull String value, String datatype, String language, boolean isFromWorkbook) {
      this.value = checkNotNull(value);
      this.datatype = datatype;
      this.language = language;
      this.isFromWorkbook = isFromWorkbook;
   }

   @Override
   public String getString() {
      return value;
   }

   public String getDatatype() {
      return datatype;
   }

   public String getLanguage() {
      return language;
   }

   @Override
   public Value update(String newValue) {
      return new UntypedValue(newValue, datatype, language, isFromWorkbook);
   }

   @Override
   public boolean isFromWorkbook() {
      return isFromWorkbook;
   }

   public ClassIri asClassIri() {
      return new ClassIri(value, isFromWorkbook);
   }

   public DataPropertyIri asDataPropertyIri() {
      return new DataPropertyIri(value, isFromWorkbook);
   }

   public ObjectPropertyIri asObjectPropertyIri() {
      return new ObjectPropertyIri(value, isFromWorkbook);
   }

   public AnnotationPropertyIri asAnnotationPropertyIri() {
      return new AnnotationPropertyIri(value, isFromWorkbook);
   }

   public IndividualIri asIndividualIri() {
      return new IndividualIri(value, isFromWorkbook);
   }

   public ClassName asClassName() {
      return new ClassName(value, isFromWorkbook);
   }

   public DataPropertyName asDataPropertyName() {
      return new DataPropertyName(value, isFromWorkbook);
   }

   public ObjectPropertyName asObjectPropertyName() {
      return new ObjectPropertyName(value, isFromWorkbook);
   }

   public AnnotationPropertyName asAnnotationPropertyName() {
      return new AnnotationPropertyName(value, isFromWorkbook);
   }

   public IndividualName asIndividualName() {
      return new IndividualName(value, isFromWorkbook);
   }

   public LiteralValue asLiteralValue() {
      return new LiteralValue(value, datatype, isFromWorkbook);
   }

   public PlainLiteralValue asPlainLiteralValue() {
      return new PlainLiteralValue(value, language, isFromWorkbook);
   }

   @Override
   public boolean equals(Object o) {
      if (o == null) {
         return false;
      }
      if (this == o) {
         return true;
      }
      if (!(o instanceof UntypedValue)) {
         return false;
      }
      UntypedValue other = (UntypedValue) o;
      return Objects.equal(value, other.getString())
            && Objects.equal(datatype, other.getDatatype())
            && Objects.equal(language, other.getLanguage())
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
            .addValue(datatype)
            .addValue(language)
            .addValue(isFromWorkbook)
            .toString();
   }
}

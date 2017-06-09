package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class LiteralValue implements Value<String>, Argument {

   private final String literalValue;
   private final String datatype;

   private LiteralValue(@Nonnull String literalValue, @Nonnull String datatype) {
      this.literalValue = checkNotNull(literalValue);
      this.datatype = checkNotNull(datatype);
   }

   public static LiteralValue createLiteral(String literalValue, String datatype) {
      return new LiteralValue(literalValue, datatype);
   }

   public static LiteralValue createLiteral(String stringValue) {
      return new LiteralValue(String.valueOf(stringValue), Datatype.XSD_STRING);
   }

   public static LiteralValue createLiteral(int integerValue) {
      return new LiteralValue(String.valueOf(integerValue), Datatype.XSD_INTEGER);
   }

   public static LiteralValue createLiteral(float floatValue) {
      return new LiteralValue(String.valueOf(floatValue), Datatype.XSD_FLOAT);
   }

   public static LiteralValue createLiteral(boolean booleanValue) {
      return new LiteralValue(String.valueOf(booleanValue), Datatype.XSD_BOOLEAN);
   }

   public String getLexicalString() {
      return literalValue;
   }

   public String getDatatype() {
      return datatype;
   }

   @Override
   public String getActualObject() {
      return getLexicalString();
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
      return Objects.equal(literalValue, other.getLexicalString())
            && Objects.equal(datatype, other.getDatatype());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(literalValue, datatype);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(literalValue)
            .addValue(datatype)
            .toString();
   }

   public static class Datatype {
      public static final String XSD_STRING = "xsd:string";
      public static final String XSD_BOOLEAN = "xsd:boolean";
      public static final String XSD_DOUBLE = "xsd:double";
      public static final String XSD_FLOAT = "xsd:float";
      public static final String XSD_LONG = "xsd:long";
      public static final String XSD_INTEGER = "xsd:integer";
      public static final String XSD_SHORT = "xsd:short";
      public static final String XSD_BYTE = "xsd:byte";
      public static final String XSD_DECIMAL = "xsd:decimal";
      public static final String XSD_TIME = "xsd:time";
      public static final String XSD_DATE = "xsd:date";
      public static final String XSD_DATETIME = "xsd:dateTime";
      public static final String XSD_DURATION = "xsd:duration";
   }
}

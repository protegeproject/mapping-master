package org.mm.directive;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.mm.parser.MappingMasterParserConstants;
import com.google.common.base.MoreObjects;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceDirectives implements MappingMasterParserConstants {

   private final String prefix;
   private final String namespace;
   private final String language;

   private final int entityType;
   private final int valueType;
   private final int propertyType;

   private final int valueDatatype;

   private final int iriEncoding;

   private final int shiftDirection;

   private final String labelValue;
   private final String literalValue;

   private final int orderIfCellEmpty;
   private final int orderIfEntityAbsent;

   public ReferenceDirectives(@Nonnull String prefix, @Nonnull String namespace,
         @Nonnull String language, @Nonnull String labelValue, @Nonnull String literalValue,
         int entityType, int valueType, int propertyType, int valueDatatype, int iriEncoding,
         int shiftDirection, int orderIfCellEmpty, int orderIfEntityAbsent) {
      this.prefix = checkNotNull(prefix);
      this.namespace = checkNotNull(namespace);
      this.language = checkNotNull(language);
      this.labelValue = checkNotNull(labelValue);
      this.literalValue = checkNotNull(literalValue);
      this.entityType = entityType;
      this.valueType = valueType;
      this.propertyType = propertyType;
      this.valueDatatype = valueDatatype;
      this.iriEncoding = iriEncoding;
      this.shiftDirection = shiftDirection;
      this.orderIfCellEmpty = orderIfCellEmpty;
      this.orderIfEntityAbsent = orderIfEntityAbsent;
   }

   public ReferenceDirectives(@Nonnull ReferenceDirectives sourceDirectives) {
      this(sourceDirectives.getPrefix(),
           sourceDirectives.getNamespace(),
           sourceDirectives.getLanguage(),
           sourceDirectives.getLabelValue(),
           sourceDirectives.getLiteralValue(),
           sourceDirectives.getEntityType(),
           sourceDirectives.getValueType(),
           sourceDirectives.getPropertyType(),
           sourceDirectives.getValueDatatype(),
           sourceDirectives.getIriEncoding(),
           sourceDirectives.getShiftDirection(),
           sourceDirectives.getOrderIfCellEmpty(),
           sourceDirectives.getOrderIfEntityAbsent());
   }

   public String getPrefix() {
      return prefix;
   }

   public ReferenceDirectives setPrefix(String newPrefix) {
      return new ReferenceDirectives(newPrefix, namespace, language, labelValue, literalValue,
            entityType, valueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public boolean useUserPrefix() {
      return prefix.isEmpty() ? false : true;
   }

   public String getNamespace() {
      return namespace;
   }

   public ReferenceDirectives setNamespace(String newNamespace) {
      return new ReferenceDirectives(prefix, newNamespace, language, labelValue, literalValue,
            entityType, valueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public boolean useUserNamespace() {
      return namespace.isEmpty() ? false : true;
   }

   public String getLanguage() {
      return language;
   }

   public ReferenceDirectives setLanguage(String newLanguage) {
      return new ReferenceDirectives(prefix, namespace, newLanguage, labelValue, literalValue,
            entityType, valueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public boolean useUserLanguage() {
      return language.isEmpty() ? false : true;
   }

   public String getLabelValue() {
      return labelValue;
   }

   public ReferenceDirectives setLabelValue(String newLabelValue) {
      return new ReferenceDirectives(prefix, namespace, language, newLabelValue, literalValue,
            entityType, valueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public boolean useUserLabelValue() {
      return labelValue.isEmpty() ? false : true;
   }

   public String getLiteralValue() {
      return literalValue;
   }

   public ReferenceDirectives setLiteralValue(String newLiteralValue) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, newLiteralValue,
            entityType, valueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public boolean useUserLiteralValue() {
      return literalValue.isEmpty() ? false : true;
   }

   public int getEntityType() {
      return entityType;
   }

   public ReferenceDirectives setEntityType(int newEntityType) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, literalValue,
            newEntityType, valueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public int getValueType() {
      return valueType;
   }

   public ReferenceDirectives setValueType(int newValueType) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, literalValue,
            entityType, newValueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public int getPropertyType() {
      return propertyType;
   }

   public ReferenceDirectives setPropertyType(int newPropertyType) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, literalValue,
            entityType, valueType, newPropertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public int getValueDatatype() {
      return valueDatatype;
   }

   public ReferenceDirectives setValueDatatype(int newValueDatatype) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, literalValue,
            entityType, valueType, propertyType, newValueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public int getIriEncoding() {
      return iriEncoding;
   }

   public ReferenceDirectives setIriEncoding(int newIriEncoding) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, literalValue,
            entityType, valueType, propertyType, valueDatatype, newIriEncoding, shiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public int getShiftDirection() {
      return shiftDirection;
   }

   public ReferenceDirectives setShiftDirection(int newShiftDirection) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, literalValue,
            entityType, valueType, propertyType, valueDatatype, iriEncoding, newShiftDirection,
            orderIfCellEmpty, orderIfEntityAbsent);
   }

   public int getOrderIfCellEmpty() {
      return orderIfCellEmpty;
   }

   public ReferenceDirectives setOrderIfCellEmpty(int newOrderIfCellEmpty) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, literalValue,
            entityType, valueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            newOrderIfCellEmpty, orderIfEntityAbsent);
   }

   public int getOrderIfEntityAbsent() {
      return orderIfEntityAbsent;
   }

   public ReferenceDirectives setOrderIfEntityAbsent(int newOrderIfEntityAbsent) {
      return new ReferenceDirectives(prefix, namespace, language, labelValue, literalValue,
            entityType, valueType, propertyType, valueDatatype, iriEncoding, shiftDirection,
            orderIfCellEmpty, newOrderIfEntityAbsent);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof ReferenceDirectives)) {
         return false;
      }
      ReferenceDirectives other = (ReferenceDirectives) obj;
      return Objects.equals(prefix, other.getPrefix())
            && Objects.equals(namespace, other.getNamespace())
            && Objects.equals(language, other.getLanguage())
            && Objects.equals(labelValue, other.getLabelValue())
            && Objects.equals(literalValue, other.getLiteralValue())
            && Objects.equals(entityType, other.getEntityType())
            && Objects.equals(valueType, other.getValueType())
            && Objects.equals(propertyType, other.getPropertyType())
            && Objects.equals(valueDatatype, other.getValueDatatype())
            && Objects.equals(iriEncoding, other.getIriEncoding())
            && Objects.equals(shiftDirection, other.getShiftDirection())
            && Objects.equals(orderIfCellEmpty, other.getOrderIfCellEmpty())
            && Objects.equals(orderIfEntityAbsent, other.getOrderIfEntityAbsent());

   }

   @Override
   public int hashCode() {
      return Objects.hash(prefix, namespace, language, labelValue, literalValue, entityType,
            valueType, propertyType, valueDatatype, iriEncoding, shiftDirection, orderIfCellEmpty,
            orderIfEntityAbsent);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
            .addValue(prefix)
            .addValue(namespace)
            .addValue(language)
            .addValue(labelValue)
            .addValue(literalValue)
            .addValue(entityType)
            .addValue(valueType)
            .addValue(propertyType)
            .addValue(valueDatatype)
            .addValue(iriEncoding)
            .addValue(shiftDirection)
            .addValue(orderIfCellEmpty)
            .addValue(orderIfEntityAbsent)
            .toString();
   }
}

package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.renderer.Sheet;
import org.mm.renderer.Workbook;
import org.mm.renderer.exception.EmptyCellException;
import org.mm.renderer.exception.Locatable;
import org.mm.renderer.exception.WarningEmptyCellException;
import org.mm.renderer.internal.LiteralValue.Datatype;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceResolver implements MappingMasterParserConstants {

   private final static String EMPTY_CELL_VALUE_STRING = "";

   private final Workbook workbook;

   public ReferenceResolver(@Nonnull Workbook workbook) {
      this.workbook = checkNotNull(workbook);
   }

   public Value resolve(CellAddress cellAddress, ReferenceDirectives directives) {
      try {
         String cellValueString = EMPTY_CELL_VALUE_STRING;
         Optional<String> cellValue = workbook.getCellValue(cellAddress);
         if (!cellValue.isPresent()) {
            cellValueString = processEmptyCell(cellAddress, directives);
            if (cellValueString.isEmpty()) {
               cellValueString = processCellShifting(cellAddress, directives);
            }
         } else {
            cellValueString = cellValue.get();
         }
         return getValueObject(cellAddress, cellValueString, directives);
      } catch (WarningEmptyCellException | EmptyCellException e) {
         supplyErrorLocation(e, cellAddress);
         throw e;
      }
   }

   @SuppressWarnings("unchecked")
   private void supplyErrorLocation(RuntimeException e, CellAddress cellAddress) {
      if (e instanceof Locatable<?>) {
         ((Locatable<CellAddress>) e).setErrorLocation(cellAddress);
      }
   }

   private String processCellShifting(CellAddress cellAddress, ReferenceDirectives directives) {
      final Sheet sheet = workbook.getSheet(cellAddress.getSheetName());
      int option = directives.getShiftDirection();
      switch (option) {
         case MM_NO_SHIFT: return EMPTY_CELL_VALUE_STRING;
         case MM_SHIFT_UP: return processShiftingCellUp(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
         case MM_SHIFT_LEFT: return processShiftingCellLeft(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
         case MM_SHIFT_DOWN: return processShiftingCellDown(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
         case MM_SHIFT_RIGHT: return processShiftingCellRight(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
         default: return EMPTY_CELL_VALUE_STRING;
      }
   }

   private String processShiftingCellUp(Sheet sheet, int columnIndex, int rowIndex) {
      final int topMostRowIndex = 0;
      int currentRowIndex = rowIndex;
      while (currentRowIndex >= topMostRowIndex) {
         Optional<String> cellValue = sheet.getValueFromCell(columnIndex, currentRowIndex);
         if (cellValue.isPresent()) {
            return cellValue.get();
         }
         currentRowIndex--; // move 1 row up
      }
      return EMPTY_CELL_VALUE_STRING;
   }

   private String processShiftingCellLeft(Sheet sheet, int columnIndex, int rowIndex) {
      final int leftMostColumnIndex = 0;
      int currentColumnIndex = columnIndex;
      while (currentColumnIndex >= leftMostColumnIndex) {
         Optional<String> cellValue = sheet.getValueFromCell(currentColumnIndex, rowIndex);
         if (cellValue.isPresent()) {
            return cellValue.get();
         }
         currentColumnIndex--; // move 1 column left
      }
      return EMPTY_CELL_VALUE_STRING;
   }

   private String processShiftingCellDown(Sheet sheet, int columnIndex, int rowIndex) {
      final int bottomMostRowIndex = sheet.getEndRowIndex();
      int currentRowIndex = rowIndex;
      while (currentRowIndex <= bottomMostRowIndex) {
         Optional<String> cellValue = sheet.getValueFromCell(columnIndex, currentRowIndex);
         if (cellValue.isPresent()) {
            return cellValue.get();
         }
         currentRowIndex++; // move 1 row down
      }
      return EMPTY_CELL_VALUE_STRING;
   }

   private String processShiftingCellRight(Sheet sheet, int columnIndex, int rowIndex) {
      final int rightMostColumn = sheet.getEndColumnIndex();
      int currentColumnIndex = columnIndex;
      while (currentColumnIndex <= rightMostColumn) {
         Optional<String> cellValue = sheet.getValueFromCell(currentColumnIndex, rowIndex);
         if (cellValue.isPresent()) {
            return cellValue.get();
         }
         currentColumnIndex++; // move 1 column right
      }
      return EMPTY_CELL_VALUE_STRING;
   }

   private String processEmptyCell(CellAddress cellAddress, ReferenceDirectives directives) {
      int option = directives.getOrderIfCellEmpty();
      if (option == MM_CREATE_IF_CELL_EMPTY) {
         return getDefaultCellValue(cellAddress);
      } else if (option == MM_IGNORE_IF_CELL_EMPTY) {
         return EMPTY_CELL_VALUE_STRING;
      } else if (option == MM_WARNING_IF_CELL_EMPTY) {
         throw new WarningEmptyCellException();
      } else if (option == MM_ERROR_IF_CELL_EMPTY) {
         throw new EmptyCellException();
      }
      throw new RuntimeException("Programming error: Unknown directive to handle empty cell"
            + " (" + tokenImage[option] + ")");
   }

   private String getDefaultCellValue(CellAddress cellAddress) {
      String cellAddressUuid = NameUtils.toUUID(cellAddress.toString());
      return cellAddressUuid;
   }

   private Value getValueObject(CellAddress cellAddress, String cellValueString, ReferenceDirectives directives) {
      Value value = EmptyValue.create();
      if (!cellValueString.isEmpty()) {
         int option = directives.getReferenceType();
         switch (option) {
            case OWL_CLASS: value = processClassName(cellAddress, cellValueString, directives); break;
            case OWL_DATA_PROPERTY: value = processDataPropertyName(cellAddress, cellValueString, directives); break;
            case OWL_OBJECT_PROPERTY: value = processObjectPropertyName(cellAddress, cellValueString, directives); break;
            case OWL_ANNOTATION_PROPERTY: value = processAnnotationPropertyName(cellAddress, cellValueString, directives); break;
            case OWL_NAMED_INDIVIDUAL: value = processIndividualName(cellAddress, cellValueString, directives); break;
            case OWL_IRI: value = getIriValue(cellValueString); break;
            case MM_ENTITY_IRI: value = getEntityName(cellValueString); break;
            case XSD_DATETIME:
               String dateTimeString = cellValueString;
               value = processLiteral(dateTimeString, directives); break;
            case XSD_DATE:
               String dateString = cellValueString.split("T")[0];
               value = processLiteral(dateString, directives); break;
            case XSD_TIME:
               String timeString = cellValueString.split("T")[1];
               value = processLiteral(timeString, directives); break;
            case XSD_STRING:
            case XSD_DECIMAL:
            case XSD_BYTE:
            case XSD_SHORT:
            case XSD_INTEGER:
            case XSD_LONG:
            case XSD_FLOAT:
            case XSD_DOUBLE:
            case XSD_BOOLEAN:
            case XSD_DURATION:
            case RDF_PLAINLITERAL: value = processLiteral(cellValueString, directives); break;
            default: throw new RuntimeException("Programming error: Unknown directive to handle reference type"
                  + " (" + tokenImage[option] + ")");
         }
      }
      return value;
   }

   private Value processClassName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value className = new ClassName(localName);
      if (directives.useUserPrefix()) {
         className = new ClassName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         className = new IriValue(directives.getNamespace() + localName);
      }
      return className;
   }

   private Value processDataPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value propertyName = new DataPropertyName(localName);
      if (directives.useUserPrefix()) {
         propertyName = new DataPropertyName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         propertyName = new IriValue(directives.getNamespace() + localName);
      }
      return propertyName;
   }

   private Value processObjectPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value propertyName = new ObjectPropertyName(localName);
      if (directives.useUserPrefix()) {
         propertyName = new ObjectPropertyName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         propertyName = new IriValue(directives.getNamespace() + localName);
      }
      return propertyName;
   }

   private Value processAnnotationPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value propertyName = new AnnotationPropertyName(localName);
      if (directives.useUserPrefix()) {
         propertyName = new AnnotationPropertyName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         propertyName = new IriValue(directives.getNamespace() + localName);
      }
      return propertyName;
   }

   private Value processIndividualName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value propertyName = new IndividualName(localName);
      if (directives.useUserPrefix()) {
         propertyName = new IndividualName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         propertyName = new IriValue(directives.getNamespace() + localName);
      }
      return propertyName;
   }

   private String getLocalName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      int option = directives.getIriEncoding();
      if (option == MM_CAMELCASE_ENCODE) {
         if (directives.getReferenceType() == OWL_CLASS) {
            return NameUtils.toUpperCamel(cellValue);
         } else {
            return NameUtils.toLowerCamel(cellValue);
         }
      } else if (option == MM_SNAKECASE_ENCODE) {
         return NameUtils.toSnakeCase(cellValue);
      } else if (option == MM_UUID_ENCODE) {
         String cellValueWithAddress = cellValue.concat(cellAddress.toString());
         return NameUtils.toUUID(cellValueWithAddress);
      } else if (option == MM_HASH_ENCODE) {
         return NameUtils.toMD5(cellValue);
      } else if (option == MM_NO_ENCODE) {
         return cellValue;
      }
      throw new RuntimeException("Programming error: Unknown directive to handle IRI encoding"
            + " (" + tokenImage[option] + ")");
   }

   private IriValue getIriValue(String cellValue) {
      return new IriValue(cellValue);
   }

   private EntityName getEntityName(String cellValue) {
      return new EntityName(cellValue);
   }

   private Value processLiteral(String cellValue, ReferenceDirectives directives) {
      int option = directives.getValueDatatype();
      if (option == XSD_STRING) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_STRING);
      } else if (option == XSD_BOOLEAN) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_BOOLEAN);
      } else if (option == XSD_DOUBLE) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_DOUBLE);
      } else if (option == XSD_FLOAT) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_FLOAT);
      } else if (option == XSD_LONG) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_LONG);
      } else if (option == XSD_INTEGER) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_INTEGER);
      } else if (option == XSD_SHORT) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_SHORT);
      } else if (option == XSD_BYTE) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_BYTE);
      } else if (option == XSD_DECIMAL) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_DECIMAL);
      } else if (option == XSD_TIME) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_TIME);
      } else if (option == XSD_DATE) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_DATE);
      } else if (option == XSD_DATETIME) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_DATETIME);
      } else if (option == XSD_DURATION) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_DURATION);
      } else if (option == RDF_PLAINLITERAL) {
         if (directives.useUserLanguage()) {
            return PlainLiteralValue.createPlainLiteral(cellValue, directives.getLanguage());
         } else {
            return PlainLiteralValue.createPlainLiteral(cellValue);
         }
      }
      throw new RuntimeException("Programming error: Unknown datatype"
            + " (" + tokenImage[option] + ")");
   }
}

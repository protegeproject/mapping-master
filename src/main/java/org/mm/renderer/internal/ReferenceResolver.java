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

   private Value getValueObject(CellAddress cellAddress, String cellValue, ReferenceDirectives directives) {
      Value value = EmptyValue.create();
      if (!cellValue.isEmpty()) {
         int entityType = directives.getEntityType();
         switch (entityType) {
            case OWL_CLASS: value = processClassName(cellAddress, cellValue, directives); break;
            case OWL_DATA_PROPERTY: value = processDataPropertyName(cellAddress, cellValue, directives); break;
            case OWL_OBJECT_PROPERTY: value = processObjectPropertyName(cellAddress, cellValue, directives); break;
            case OWL_ANNOTATION_PROPERTY: value = processAnnotationPropertyName(cellAddress, cellValue, directives); break;
            case OWL_NAMED_INDIVIDUAL: value = processIndividualName(cellAddress, cellValue, directives); break;
            case OWL_IRI: value = getIriValue(cellValue); break;
            case OWL_LITERAL:
               int datatype = directives.getValueDatatype();
               switch (datatype) {
                  case XSD_DATETIME:
                     String dateTimeString = cellValue;
                     value = processLiteral(dateTimeString, directives); break;
                  case XSD_DATE:
                     String dateString = cellValue.split("T")[0];
                     value = processLiteral(dateString, directives); break;
                  case XSD_TIME:
                     String timeString = cellValue.split("T")[1];
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
                  case RDF_PLAINLITERAL: value = processLiteral(cellValue, directives); break;
                  case MM_UNTYPED: value = getUntypedValue(cellValue, directives); break;
                  default: throw new RuntimeException(
                        String.format("Programming error: Unknown directive to handle value datatype (%s)",
                              tokenImage[datatype]));
               }
               break;
            case MM_UNTYPED: value = getUntypedValue(cellValue, directives); break;
            default: throw new RuntimeException(
                  String.format("Programming error: Unknown directive to handle reference entity type (%s)",
                        tokenImage[entityType]));
         }
      }
      return value;
   }

   private Value processClassName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value className = new ClassName(localName, true);
      if (directives.useUserPrefix()) {
         className = new ClassName(directives.getPrefix() + ":" + localName, true);
      } else if (directives.useUserNamespace()) {
         className = new ClassIri(directives.getNamespace() + localName, true);
      }
      return className;
   }

   private Value processDataPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value propertyName = new DataPropertyName(localName, true);
      if (directives.useUserPrefix()) {
         propertyName = new DataPropertyName(directives.getPrefix() + ":" + localName, true);
      } else if (directives.useUserNamespace()) {
         propertyName = new DataPropertyIri(directives.getNamespace() + localName, true);
      }
      return propertyName;
   }

   private Value processObjectPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value propertyName = new ObjectPropertyName(localName, true);
      if (directives.useUserPrefix()) {
         propertyName = new ObjectPropertyName(directives.getPrefix() + ":" + localName, true);
      } else if (directives.useUserNamespace()) {
         propertyName = new ObjectPropertyIri(directives.getNamespace() + localName, true);
      }
      return propertyName;
   }

   private Value processAnnotationPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value propertyName = new AnnotationPropertyName(localName, true);
      if (directives.useUserPrefix()) {
         propertyName = new AnnotationPropertyName(directives.getPrefix() + ":" + localName, true);
      } else if (directives.useUserNamespace()) {
         propertyName = new AnnotationPropertyIri(directives.getNamespace() + localName, true);
      }
      return propertyName;
   }

   private Value processIndividualName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value propertyName = new IndividualName(localName, true);
      if (directives.useUserPrefix()) {
         propertyName = new IndividualName(directives.getPrefix() + ":" + localName, true);
      } else if (directives.useUserNamespace()) {
         propertyName = new IndividualIri(directives.getNamespace() + localName, true);
      }
      return propertyName;
   }

   private String getLocalName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      int option = directives.getIriEncoding();
      if (option == MM_CAMELCASE_ENCODE) {
         if (directives.getEntityType() == OWL_CLASS) {
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

   private UntypedValue getUntypedValue(String cellValue, ReferenceDirectives directives) {
      return new UntypedValue(cellValue, getDatatype(directives.getValueDatatype()), directives.getLanguage(), true);
   }

   private IriValue getIriValue(String cellValue) {
      return new UntypedIri(cellValue, true);
   }

   private Value processLiteral(String cellValue, ReferenceDirectives directives) {
      int option = directives.getValueDatatype();
      return (option == RDF_PLAINLITERAL) ?
            new PlainLiteralValue(cellValue, directives.getLanguage(), true) :
            new LiteralValue(cellValue, getDatatype(option), true);
   }

   private String getDatatype(int datatypeConstant) {
      switch(datatypeConstant) {
         case XSD_STRING: return Datatypes.XSD_STRING;
         case XSD_BOOLEAN: return Datatypes.XSD_BOOLEAN;
         case XSD_DOUBLE: return Datatypes.XSD_DOUBLE;
         case XSD_FLOAT: return Datatypes.XSD_FLOAT;
         case XSD_LONG: return Datatypes.XSD_LONG;
         case XSD_INTEGER: return Datatypes.XSD_INTEGER;
         case XSD_SHORT: return Datatypes.XSD_SHORT;
         case XSD_BYTE: return Datatypes.XSD_BYTE;
         case XSD_DECIMAL: return Datatypes.XSD_DECIMAL;
         case XSD_TIME: return Datatypes.XSD_TIME;
         case XSD_DATE: return Datatypes.XSD_DATE;
         case XSD_DATETIME: return Datatypes.XSD_DATETIME;
         case XSD_DURATION: return Datatypes.XSD_DURATION;
         case RDF_PLAINLITERAL: return Datatypes.RDF_PLAINLITERAL;
         case MM_UNTYPED: return "";
      }
      throw new RuntimeException("Programming error: Unknown datatype"
            + " (" + tokenImage[datatypeConstant] + ")");
   }
}

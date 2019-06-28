package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.renderer.RenderingContext;
import org.mm.renderer.Sheet;
import org.mm.renderer.Workbook;
import org.mm.renderer.exception.EmptyCellException;
import org.mm.renderer.exception.IgnoreEmptyCellException;
import org.mm.renderer.exception.Locatable;
import org.mm.renderer.exception.WarningEmptyCellException;
import org.mm.renderer.internal.LiteralValue.Datatype;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceResolver implements MappingMasterParserConstants {

   private final Workbook workbook;
   private final RenderingContext context;

   public ReferenceResolver(@Nonnull Workbook workbook, @Nonnull RenderingContext context) {
      this.workbook = checkNotNull(workbook);
      this.context = checkNotNull(context);
   }

   public Value<?> resolve(ReferenceNotation referenceNotation, ReferenceDirectives directives) {
      CellAddress cellAddress = toCellAddress(referenceNotation);
      try {
         String cellValue = workbook.getCellValue(cellAddress);
         cellValue = processCellShifting(cellAddress, cellValue, directives);
         cellValue = processEmptyValue(cellAddress, cellValue, directives);
         return processReferenceType(cellAddress, cellValue, directives);
      } catch (RuntimeException e) {
         supplyErrorLocation(e, cellAddress);
         throw e;
      }
   }

   private CellAddress toCellAddress(ReferenceNotation referenceNotation) {
      return referenceNotation.setContext(
            context.getSheetName(),
            context.getCurrentColumn(),
            context.getCurrentRow()).toCellAddress();
   }

   @SuppressWarnings("unchecked")
   private void supplyErrorLocation(RuntimeException e, CellAddress cellAddress) {
      if (e instanceof Locatable<?>) {
         ((Locatable<CellAddress>) e).setErrorLocation(cellAddress);
      }
   }

   private String processCellShifting(CellAddress cellAddress, String cellValue, ReferenceDirectives directives) {
      int option = directives.getShiftDirection();
      if (option == MM_NO_SHIFT) {
         return cellValue;
      } else {
         final Sheet sheet = workbook.getSheet(cellAddress.getSheetName());
         if (option == MM_SHIFT_UP) {
            return processShiftingCellUp(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
         } else if (option == MM_SHIFT_LEFT) {
            return processShiftingCellLeft(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
         } else if (option == MM_SHIFT_DOWN) {
            return processShiftingCellDown(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
         } else if (option == MM_SHIFT_RIGHT) {
            return processShiftingCellRight(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
         }
      }
      throw new RuntimeException("Programming error: Unknown directive to handle shifting cell"
            + " (" + tokenImage[option] + ")");
   }

   private String processShiftingCellUp(Sheet sheet, int columnIndex, int rowIndex) {
      final int topMostRowIndex = 0;
      int currentRowIndex = rowIndex;
      while (currentRowIndex >= topMostRowIndex) {
         String cellValue = sheet.getValueFromCell(currentRowIndex, columnIndex);
         if (!cellValue.isEmpty()) {
            return cellValue;
         }
         currentRowIndex--; // move 1 row up
      }
      return "";
   }

   private String processShiftingCellLeft(Sheet sheet, int columnIndex, int rowIndex) {
      final int leftMostColumnIndex = 0;
      int currentColumnIndex = columnIndex;
      while (currentColumnIndex >= leftMostColumnIndex) {
         String cellValue = sheet.getValueFromCell(rowIndex, currentColumnIndex);
         if (!cellValue.isEmpty()) {
            return cellValue;
         }
         currentColumnIndex--; // move 1 column left
      }
      return "";
   }

   private String processShiftingCellDown(Sheet sheet, int columnIndex, int rowIndex) {
      final int bottomMostRowIndex = sheet.getEndRowIndex();
      int currentRowIndex = rowIndex;
      while (currentRowIndex <= bottomMostRowIndex) {
         String cellValue = sheet.getValueFromCell(currentRowIndex, columnIndex);
         if (!cellValue.isEmpty()) {
            return cellValue;
         }
         currentRowIndex++; // move 1 row down
      }
      return "";
   }

   private String processShiftingCellRight(Sheet sheet, int columnIndex, int rowIndex) {
      final int rightMostColumn = sheet.getStartColumnIndex();
      int currentColumnIndex = columnIndex;
      while (currentColumnIndex <= rightMostColumn) {
         String cellValue = sheet.getValueFromCell(rowIndex, currentColumnIndex);
         if (!cellValue.isEmpty()) {
            return cellValue;
         }
         currentColumnIndex++; // move 1 column right
      }
      return "";
   }

   private String processEmptyValue(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      if (cellValue.isEmpty()) {
         cellValue = applyOrderForEmptyCell(cellAddress, directives);
      }
      return cellValue;
   }

   private String applyOrderForEmptyCell(CellAddress cellAddress, ReferenceDirectives directives) {
      int option = directives.getOrderIfCellEmpty();
      if (option == MM_CREATE_IF_CELL_EMPTY) {
         return getDefaultCellValue(cellAddress);
      } else if (option == MM_IGNORE_IF_CELL_EMPTY) {
         throw new IgnoreEmptyCellException();
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

   private Value<?> processReferenceType(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      int option = directives.getReferenceType();
      if (option == OWL_CLASS) {
         return processClassName(cellAddress, cellValue, directives);
      } else if (option == OWL_DATA_PROPERTY) {
         return processDataPropertyName(cellAddress, cellValue, directives);
      } else if (option == OWL_OBJECT_PROPERTY) {
         return processObjectPropertyName(cellAddress, cellValue, directives);
      } else if (option == OWL_ANNOTATION_PROPERTY) {
         return processAnnotationPropertyName(cellAddress, cellValue, directives);
      } else if (option == OWL_NAMED_INDIVIDUAL) {
         return processIndividualName(cellAddress, cellValue, directives);
      } else if (option == OWL_IRI) {
         return getIriValue(cellValue);
      } else if (option == MM_ENTITY_IRI) {
         return getEntityName(cellValue);
      } else if (option == XSD_STRING
            || option == XSD_DECIMAL
            || option == XSD_BYTE
            || option == XSD_SHORT
            || option == XSD_INTEGER
            || option == XSD_LONG
            || option == XSD_FLOAT
            || option == XSD_DOUBLE
            || option == XSD_BOOLEAN
            || option == XSD_TIME
            || option == XSD_DATETIME
            || option == XSD_DATE
            || option == XSD_DURATION
            || option == RDF_PLAINLITERAL) {
         return processLiteral(cellValue, directives);
      } 
      throw new RuntimeException("Programming error: Unknown directive to handle reference type"
            + " (" + tokenImage[option] + ")");
   }

   private Value<?> processClassName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value<?> className = new ClassName(localName);
      if (directives.useUserPrefix()) {
         className = new ClassName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         className = new IriValue(directives.getNamespace() + localName);
      }
      return className;
   }

   private Value<?> processDataPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value<?> propertyName = new DataPropertyName(localName);
      if (directives.useUserPrefix()) {
         propertyName = new DataPropertyName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         propertyName = new IriValue(directives.getNamespace() + localName);
      }
      return propertyName;
   }

   private Value<?> processObjectPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value<?> propertyName = new ObjectPropertyName(localName);
      if (directives.useUserPrefix()) {
         propertyName = new ObjectPropertyName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         propertyName = new IriValue(directives.getNamespace() + localName);
      }
      return propertyName;
   }

   private Value<?> processAnnotationPropertyName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value<?> propertyName = new AnnotationPropertyName(localName);
      if (directives.useUserPrefix()) {
         propertyName = new AnnotationPropertyName(directives.getPrefix() + ":" + localName);
      } else if (directives.useUserNamespace()) {
         propertyName = new IriValue(directives.getNamespace() + localName);
      }
      return propertyName;
   }

   private Value<?> processIndividualName(CellAddress cellAddress, String cellValue,
         ReferenceDirectives directives) {
      String localName = getLocalName(cellAddress, cellValue, directives);
      Value<?> propertyName = new IndividualName(localName);
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

   private QName getEntityName(String cellValue) {
      return new QName(cellValue);
   }

   private Value<?> processLiteral(String cellValue, ReferenceDirectives directives) {
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

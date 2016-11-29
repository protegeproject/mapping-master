package org.mm.renderer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

import javax.annotation.Nonnull;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mm.directive.ReferenceDirectives;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.renderer.exception.EmptyCellException;
import org.mm.renderer.exception.IgnoreEmptyCellException;
import org.mm.renderer.exception.Locatable;
import org.mm.renderer.exception.WarningEmptyCellException;
import org.mm.renderer.internal.LiteralValue.Datatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class ReferenceResolver implements MappingMasterParserConstants {

   private static final Logger logger = LoggerFactory.getLogger(ReferenceResolver.class);

   private final Workbook workbook;

   public ReferenceResolver(@Nonnull Workbook workbook) {
      this.workbook = checkNotNull(workbook);
   }

   public Value<?> resolve(CellAddress cellAddress, ReferenceDirectives directives) {
      try {
         String cellValue = getCellValue(cellAddress);
         cellValue = processCellShifting(cellValue, cellAddress, directives);
         cellValue = processEmptyValue(cellValue, directives);
         return processReferenceType(cellValue, directives);
      } catch (RuntimeException e) {
         supplyErrorLocation(e, cellAddress);
         throw e;
      }
   }

   private String getCellValue(CellAddress cellAddress) {
      final Sheet sheet = workbook.getSheet(cellAddress.getSheetName());
      return getCellValue(sheet, cellAddress.getColumnIndex(), cellAddress.getRowIndex());
   }

   private String getCellValue(Sheet sheet, int columnIndex, int rowIndex) {
      final Cell cell = sheet.getRow(rowIndex).getCell(columnIndex);
      return getCellValue(cell);
   }

   private String getCellValue(Cell cell) {
      Object cellValueObject = null;
      try {
         int cellType = cell.getCellType();
         if (cellType == Cell.CELL_TYPE_STRING) {
            cellValueObject = cell.getStringCellValue();
         } else if (cellType == Cell.CELL_TYPE_NUMERIC) {
            // Check if the numeric is double or integer
            if (isInteger(cell.getNumericCellValue())) {
               cellValueObject = (int) cell.getNumericCellValue();
            } else {
               cellValueObject = cell.getNumericCellValue();
            }
         } else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
            cellValueObject = cell.getBooleanCellValue();
         } else if (cellType == Cell.CELL_TYPE_FORMULA) {
            cellValueObject = cell.getNumericCellValue();
         } else {
            cellValueObject = "";
         }
      } catch (NullPointerException e) {
         logger.warn("Looking for a cell beyond the maximum row and column range");
      }
      return (cellValueObject == null) ? "" : String.valueOf(cellValueObject);
   }

   private boolean isInteger(double number) {
      return (number == Math.floor(number) && !Double.isInfinite(number));
   }

   @SuppressWarnings("unchecked")
   private void supplyErrorLocation(RuntimeException e, CellAddress cellAddress) {
      if (e instanceof Locatable<?>) {
         ((Locatable<CellAddress>) e).setErrorLocation(cellAddress);
      }
   }

   private String processCellShifting(String cellValue, CellAddress cellAddress,
         ReferenceDirectives directives) {
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
      final int topMostRow = 0;
      int currentRow = rowIndex;
      while (currentRow >= topMostRow) {
         String cellValue = getCellValue(sheet, columnIndex, rowIndex);
         if (!cellValue.isEmpty()) {
            return cellValue;
         }
         currentRow--; // move 1 row up
      }
      return "";
   }

   private String processShiftingCellLeft(Sheet sheet, int columnIndex, int rowIndex) {
      final int leftMostColumn = 1;
      int currentColumn = columnIndex;
      while (currentColumn >= leftMostColumn) {
         String cellValue = getCellValue(sheet, columnIndex, rowIndex);
         if (!cellValue.isEmpty()) {
            return cellValue;
         }
         currentColumn--; // move 1 column left
      }
      return "";
   }

   private String processShiftingCellDown(Sheet sheet, int columnIndex, int rowIndex) {
      final int bottomMostRow = sheet.getLastRowNum();
      int currentRow = rowIndex;
      while (currentRow <= bottomMostRow) {
         String cellValue = getCellValue(sheet, columnIndex, rowIndex);
         if (!cellValue.isEmpty()) {
            return cellValue;
         }
         currentRow++; // move 1 row down
      }
      return "";
   }

   private String processShiftingCellRight(Sheet sheet, int columnIndex, int rowIndex) {
      final int rightMostColumn = sheet.getRow(rowIndex).getLastCellNum();
      int currentColumn = columnIndex;
      while (currentColumn <= rightMostColumn) {
         String cellValue = getCellValue(sheet, columnIndex, rowIndex);
         if (!cellValue.isEmpty()) {
            return cellValue;
         }
         currentColumn++; // move 1 column right
      }
      return "";
   }

   private String processEmptyValue(String cellValue, ReferenceDirectives directives) {
      if (cellValue.isEmpty()) {
         cellValue = applyOrderForEmptyCell(cellValue, directives);
      }
      return cellValue;
   }

   private String applyOrderForEmptyCell(String cellValue, ReferenceDirectives directives) {
      int option = directives.getOrderIfCellEmpty();
      if (option == MM_CREATE_IF_CELL_EMPTY) {
         return "";
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

   private Value<?> processReferenceType(String cellValue, ReferenceDirectives directives) {
      int option = directives.getReferenceType();
      if (option == OWL_CLASS
            || option == OWL_DATA_PROPERTY
            || option == OWL_OBJECT_PROPERTY
            || option == OWL_ANNOTATION_PROPERTY
            || option == OWL_NAMED_INDIVIDUAL) {
         return processEntity(cellValue, directives);
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
      } else if (option == OWL_IRI) {
         return processIri(cellValue);
      } else if (option == MM_ENTITY_IRI) {
         return processEntityName(cellValue);
      }
      throw new RuntimeException("Programming error: Unknown directive to handle reference type"
            + " (" + tokenImage[option] + ")");
   }

   private ReferredEntityName processEntity(String cellValue, ReferenceDirectives directives) {
      cellValue = processEntityName(cellValue, directives);
      cellValue = processEntityPrefix(cellValue, directives);
      return new ReferredEntityName(cellValue);
   }

   private String processEntityName(String cellValue, ReferenceDirectives directives) {
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
         return NameUtils.toUUID();
      } else if (option == MM_HASH_ENCODE) {
         return NameUtils.toMD5(cellValue);
      } else if (option == MM_NO_ENCODE) {
         return cellValue;
      }
      throw new RuntimeException("Programming error: Unknown directive to handle IRI encoding"
            + " (" + tokenImage[option] + ")");
   }

   private String processEntityPrefix(String cellValue, ReferenceDirectives directives) {
      if (directives.useUserPrefix()) {
         return format("%s:%s", directives.getPrefix(), cellValue);
      }
      return cellValue;
   }

   private IriValue processIri(String cellValue) {
      return new IriValue(cellValue);
   }

   private EntityName processEntityName(String cellValue) {
      return new EntityName(cellValue);
   }

   private LiteralValue processLiteral(String cellValue, ReferenceDirectives directives) {
      int option = directives.getPropertyValueDatatype();
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
      } else if (option == XSD_DATETIME) {
         return LiteralValue.createLiteral(cellValue, Datatype.XSD_DATETIME);
      } else if (option == RDF_PLAINLITERAL) {
         return LiteralValue.createLiteral(cellValue, Datatype.RDF_PLAINLITERAL);
      }
      throw new RuntimeException("Programming error: Unknown datatype"
            + " (" + tokenImage[option] + ")");
   }
}

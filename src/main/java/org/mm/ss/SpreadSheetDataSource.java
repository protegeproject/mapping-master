package org.mm.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mm.core.DataSource;
import org.mm.exceptions.MappingMasterException;
import org.mm.parser.MappingMasterParserConstants;
import org.mm.parser.node.ReferenceNode;
import org.mm.parser.node.SourceSpecificationNode;
import org.mm.renderer.InternalRendererException;
import org.mm.renderer.RendererException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu>
 * @author Martin O'Connor <sunid@stanford.edu> <br>
 *         Stanford Center for Biomedical Informatics Research
 */
public class SpreadSheetDataSource implements DataSource, MappingMasterParserConstants {

   private final Workbook workbook;

   private final List<String> sheetNameList = new ArrayList<>();
   private final List<Sheet> sheetList = new ArrayList<>();

   private Optional<SpreadsheetLocation> currentLocation;

   public SpreadSheetDataSource() {
      this(SpreadsheetFactory.createEmptyWorkbook());
   }

   public SpreadSheetDataSource(Workbook workbook) {
      this.workbook = workbook;
      currentLocation = Optional.empty();

      /*
       * Populate the sheets from the workbook
       */
      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
         sheetNameList.add(workbook.getSheetName(i));
         sheetList.add(workbook.getSheetAt(i));
      }
   }

   public void setCurrentLocation(SpreadsheetLocation location) {
      currentLocation = Optional.of(location);
   }

   public Optional<SpreadsheetLocation> getCurrentLocation() {
      return currentLocation;
   }

   public boolean hasCurrentLocation() {
      return currentLocation != null;
   }

   public Workbook getWorkbook() {
      return workbook;
   }

   public boolean hasWorkbook() {
      return workbook != null;
   }

   public List<Sheet> getSheets() {
      return new ArrayList<Sheet>(sheetList);
   }

   public List<String> getSheetNames() {
      return new ArrayList<String>(sheetNameList);
   }

   public String getLocationValue(SpreadsheetLocation location, ReferenceNode referenceNode)
         throws RendererException {
      String locationValue = getLocationValue(location);
      if (referenceNode.getActualShiftDirective() != MM_NO_SHIFT) {
         locationValue = getLocationValueWithShifting(location, referenceNode);
      }
      return locationValue;
   }

   public String getLocationValue(SpreadsheetLocation location) throws RendererException {
      int columnNumber = location.getColumnNumber();
      int rowNumber = location.getRowNumber();

      Sheet sheet = workbook.getSheet(location.getSheetName());
      Row row = sheet.getRow(rowNumber);
      if (row == null) {
         return "";
      }
      Cell cell = row.getCell(columnNumber);
      if (cell == null) {
         return "";
      }
      return getStringValue(cell);
   }

   private String getStringValue(Cell cell) {
      switch (cell.getCellType()) {
         case Cell.CELL_TYPE_BLANK :
            return "";
         case Cell.CELL_TYPE_STRING :
            return cell.getStringCellValue();
         case Cell.CELL_TYPE_NUMERIC :
            // Check if the numeric is an integer or double
            if (isInteger(cell.getNumericCellValue())) {
               return Integer.toString((int) cell.getNumericCellValue());
            } else {
               return Double.toString(cell.getNumericCellValue());
            }
         case Cell.CELL_TYPE_BOOLEAN :
            return Boolean.toString(cell.getBooleanCellValue());
         case Cell.CELL_TYPE_FORMULA :
            return Double.toString(cell.getNumericCellValue());
         default :
            return "";
      }
   }

   private boolean isInteger(double number) {
      return (number == Math.floor(number) && !Double.isInfinite(number));
   }

   public String getLocationValueWithShifting(SpreadsheetLocation location,
         ReferenceNode referenceNode) throws RendererException {
      String sheetName = location.getSheetName();
      Sheet sheet = workbook.getSheet(sheetName);
      String shiftedLocationValue = getLocationValue(location);
      if (shiftedLocationValue == null || shiftedLocationValue.isEmpty()) {
         switch (referenceNode.getActualShiftDirective()) {
            case MM_SHIFT_LEFT :
               int firstColumnNumber = 1;
               for (int currentColumn = location
                     .getPhysicalColumnNumber(); currentColumn >= firstColumnNumber; currentColumn--) {
                  shiftedLocationValue = getLocationValue(new SpreadsheetLocation(sheetName,
                        currentColumn, location.getPhysicalRowNumber()));
                  if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
                     break;
                  }
               }
               return shiftedLocationValue;
            case MM_SHIFT_RIGHT :
               int lastColumnNumber = sheet.getRow(location.getRowNumber()).getLastCellNum();
               for (int currentColumn = location
                     .getPhysicalColumnNumber(); currentColumn <= lastColumnNumber; currentColumn++) {
                  shiftedLocationValue = getLocationValue(new SpreadsheetLocation(sheetName,
                        currentColumn, location.getPhysicalRowNumber()));
                  if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
                     break;
                  }
               }
               return shiftedLocationValue;
            case MM_SHIFT_DOWN :
               int lastRowNumber = sheet.getLastRowNum() + 1;
               for (int currentRow = location
                     .getPhysicalRowNumber(); currentRow <= lastRowNumber; currentRow++) {
                  shiftedLocationValue = getLocationValue(new SpreadsheetLocation(sheetName,
                        location.getPhysicalColumnNumber(), currentRow));
                  if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
                     break;
                  }
               }
               return shiftedLocationValue;
            case MM_SHIFT_UP :
               int firstRowNumber = 1;
               for (int currentRow = location
                     .getPhysicalRowNumber(); currentRow >= firstRowNumber; currentRow--) {
                  shiftedLocationValue = getLocationValue(new SpreadsheetLocation(sheetName,
                        location.getPhysicalColumnNumber(), currentRow));
                  if (shiftedLocationValue != null && !shiftedLocationValue.isEmpty()) {
                     break;
                  }
               }
               return shiftedLocationValue;
            default :
               throw new InternalRendererException(
                     "Unknown shift setting " + referenceNode.getActualShiftDirective());
         }
      } else {
         referenceNode.setShiftedLocation(location);
         return shiftedLocationValue;
      }
   }

   public SpreadsheetLocation resolveLocation(SourceSpecificationNode sourceSpecification)
         throws RendererException {
      Pattern p = Pattern.compile("(\\*|[a-zA-Z]+)(\\*|[0-9]+)");
      Matcher m = p.matcher(sourceSpecification.getLocation());
      Sheet sheet;
      SpreadsheetLocation resolvedLocation;

      if (!currentLocation.isPresent()) {
         throw new RendererException("current location not set");
      }
      if (sourceSpecification.hasSource()) {
         String sheetName = sourceSpecification.getSource();
         if (!hasWorkbook()) {
            throw new RendererException(
                  "Sheet name '" + sheetName + "' specified but there is no active workbook");
         }
         sheet = getWorkbook().getSheet(sheetName);
         if (sheet == null) {
            throw new RendererException("Sheet name '" + sheetName + "' does not exist");
         }
      } else {
         String sheetName = getCurrentLocation().get().getSheetName();
         sheet = getWorkbook().getSheet(sheetName);
         if (sheet == null) {
            throw new RendererException("Sheet name '" + sheetName + "' does not exist");
         }
      }

      if (m.find()) {
         String columnSpecification = m.group(1);
         String rowSpecification = m.group(2);

         if (columnSpecification == null) {
            throw new RendererException(
                  "Missing column specification in location " + sourceSpecification);
         }
         if (rowSpecification == null) {
            throw new RendererException(
                  "Missing row specification in location " + sourceSpecification);
         }
         boolean isColumnWildcard = "*".equals(columnSpecification);
         boolean isRowWildcard = "*".equals(rowSpecification);
         int columnNumber, rowNumber;

         try {
            if (isColumnWildcard) {
               columnNumber = getCurrentLocation().get().getPhysicalColumnNumber();
            } else {
               columnNumber = SpreadSheetUtil.columnName2Number(columnSpecification);
            }
            if (isRowWildcard) {
               rowNumber = getCurrentLocation().get().getPhysicalRowNumber();
            } else {
               rowNumber = SpreadSheetUtil.rowLabel2Number(rowSpecification);
            }
         } catch (MappingMasterException e) {
            throw new RendererException(
                  "Invalid source specification " + sourceSpecification + " - " + e.getMessage());
         }
         resolvedLocation = new SpreadsheetLocation(sheet.getSheetName(), columnNumber, rowNumber);
      } else {
         throw new RendererException("Invalid source specification " + sourceSpecification);
      }
      return resolvedLocation;
   }
}
